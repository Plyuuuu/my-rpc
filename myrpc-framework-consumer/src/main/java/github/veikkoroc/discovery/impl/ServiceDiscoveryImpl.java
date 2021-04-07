package github.veikkoroc.discovery.impl;

import github.veikkoroc.discovery.ServiceDiscovery;
import github.veikkoroc.loadbalance.LoadBalance;
import github.veikkoroc.loadbalance.impl.LoadBalanceImpl;
import github.veikkoroc.utils.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 发现注册中心的服务
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:20
 */
@Slf4j
public class ServiceDiscoveryImpl implements ServiceDiscovery {
    /**
     * 负载均衡策略
     *
     * */
    private final LoadBalance loadBalance;

    public ServiceDiscoveryImpl() {
        this.loadBalance = new LoadBalanceImpl();
    }

    /**
     * 查询服务根据服务名称
     * @param rpcServiceName
     * @return
     */
    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        log.info("----> 开始查询 zk 上服务名为[{}]的所有服务",rpcServiceName);
        // 获得Zk客户端
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        // serviceUrlList中存放的是子节点的名称，如：192.168.1.103:6668等等
        List<String> childrenNodes = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        log.info("<---- 获取到的服务 childrenNodes:[{}]",childrenNodes);
        // 没有该服务抛出异常
        if (childrenNodes.size()==0){
            throw new RuntimeException("服务未找到:"+rpcServiceName);
        }
        // 如果有服务就执行负载均衡
        String s = loadBalance.selectServiceAddress(childrenNodes);
        log.info("====> 通过负载均衡获取的服务器地址:[{}]",s);
        //获得服务器地址的 ip 和 port
        String[] split = s.split(":");
        String ip = split[0];
        int port = Integer.parseInt(split[1]);
        log.info("====> 获取服务服务提供者的 ip:[{}] port:[{}]",ip,port);
        return new InetSocketAddress(ip,port);
    }
}
