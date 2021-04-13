package github.veikkoroc.Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  操作 zk 的工具类
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 14:22
 */
@Slf4j
public class CuratorUtils {
    /**
     * 重试策略的重试时间间隔 1s
     */
    private static final int BASE_SLEEP_TIME = 1000;
    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 3;
    /**
     * zk 上注册服务的根路径
     */
    public static final String ZK_REGISTER_ROOT_PATH = Constants.ZK_REGISTER_ROOT_PATH;

    /**
     * zk 与本地的服务地址映射
     */
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();

    /**
     * 注册路径集合
     */
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    /**
     * zk 客户端
     */
    private static CuratorFramework zkClient;
    /**
     * 默认的 zk 的 Socket
     */
    private static String defaultZookeeperAddress = Constants.DEFAULT_ZOOKEEPER_SOCKET;


    /**
     * 私有化构造器
     */
    private CuratorUtils(){

    }

    /**
     * 创建永久节点
     *
     * @param path 节点路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        log.info("----> 开始在 zk 上创建永久节点");
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("<---- 该节点已经存在:[{}]", path);
            } else {
                // 如: /my-rpc/github.veikkoroc.OrderService/127.0.0.1:9998
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("<---- 在 zk 上创建永久节点成功 path:[{}]",path);
            }
            REGISTERED_PATH_SET.add(path);
            log.info("====> 注册路径集合 REGISTERED_PATH_SET:[{}]",REGISTERED_PATH_SET.toString());
        } catch (Exception e) {
            log.info("<---- 在 zk 上创建永久节点出错:error[{}]",e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 获取节点下的子节点们
     *
     * @param rpcServiceName 服务名 如:github.veikkoroc.OrderServicetest2version1
     * @return 指定的节点下所有子节点
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        log.info("----> 获取指定节点[{}]的所有子节点",rpcServiceName);
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            log.info("<---- 本地获取到该服务名的服务:[{}]",SERVICE_ADDRESS_MAP.get(rpcServiceName));
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result;
        //  " /my-rpc/toRpcServiceName() "
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            //result中存放的是子节点的名称，如: 192.168.3.5:9998 等等
            result = zkClient.getChildren().forPath(servicePath);
            log.info("<---- zk 上获取该服务名的子节点:[{}]",result);
            log.info("----> 把 zk 上获取到的子节点添加到本地");
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            log.info("<---- 添加后的本地服务:[{}]",SERVICE_ADDRESS_MAP);
            //监听 " /my-rpc/toRpcServiceName() "节点的子节点变化
            log.info("====> 监听:[{}]节点的子节点的变化",rpcServiceName);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            log.info("<---- 获取 zk 上子节点出错:[{}]",e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        return result;
    }
    /**
     * 初始化 zk 上的数据
     */
    public static void clearRegistry(CuratorFramework zkClient) {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                log.info("----> 开始清空 zk 上残留数据参数p:[{}]...",p);
                zkClient.delete().forPath(p);
                log.info("<---- 清空 zk 上的数据成功");
            } catch (Exception e) {
                log.info("<---- 清空 zk 上的数据失败 error:[{}]",e.getMessage());
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
        });
        log.info("====> 所有注册在 zk 上的服务已经清除:[{}]", REGISTERED_PATH_SET.toString());

    }

    /**
     * 获取 zkClient
     * @return
     */
    public static CuratorFramework getZkClient() {
        log.info("----> 开始获取 zkClient ");
        // 如果zkClient已启动，则直接return
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            log.info("<---- zkClient 已经存在:[{}]",zkClient);
            return zkClient;
        }
        // 重试策略:重试3次，这将增加重试之间的睡眠时间。
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                // 要连接的服务器（可以是服务器列表）
                .connectString(defaultZookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        log.info("<---- 获取 zkClient 成功:[{}]",zkClient);
        return zkClient;
    }

    /**
     * 注册器监听指定的 node
     *
     * @param rpcServiceName 服务名例如:github.veikkoroc.OrderServicetest2version2
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        log.info("====> 开始监听指定的节点:[{}]",servicePath);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
