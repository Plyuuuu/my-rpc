package github.veikkoroc.provider.impl;

import github.veikkoroc.Utils.Constants;
import github.veikkoroc.provider.ServiceProvider;
import github.veikkoroc.registry.ServiceRegister;
import github.veikkoroc.registry.impl.ZkServiceRegisterImpl;
import github.veikkoroc.remote.entity.RpcServiceProperties;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 14:14
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    /**
     * 存储服务的容器
     *  key:   eg: github.veikkoroc.service.UserService11.0  ---> 接口名+group+版本
     *  value: 服务实体对象 eg: OrderServiceImpl
     */
    private final Map<String, Object> serviceMap;
    /**
     * 服务名存储容器
     */
    private final Set<String> registeredService;
    /**
     * 服务注册器
     */
    private final ServiceRegister serviceRegister;

    /**
     * 构造方法
     */
    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegister = new ZkServiceRegisterImpl();
    }

    /**
     * 把服务添加到本地
     * @param service              服务对象
     * @param serviceClass         由服务实例对象实现的接口类
     * @param rpcServiceProperties 服务相关属性
     */
    @Override
    public void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        log.info("====> 当前本地的服务 serviceMap:[{}]",serviceMap);
        log.info("----> 把服务:[{}]添加到本地 serviceMap:[{}]",service,serviceMap);
        // rpcServiceName : this.getServiceName()+this.getGroup()+this.getVersion()
        String rpcServiceName = rpcServiceProperties.getRpcServicePropertiesFields();
        // registeredService 是一个Set<String>,服务已经注册就不再添加
        if (registeredService.contains(rpcServiceName)) {
            log.info("<---- 本地已经存在该服务:[{}]",registeredService.toString());
            return;
        }
        // 服务还没有注册,添加到map缓存和set
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, service);
        log.info("<---- 服务添加到本地成功 \n serviceMap:[{}] \n registeredService:[{}]",serviceMap,registeredService);
    }

    /**
     * 获取本地的服务
     * @param rpcServiceProperties 服务相关的属性
     * @return
     */
    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        log.info("----> 开始获取 serviceMap 中的服务:[{}]",rpcServiceProperties);
        Object service = serviceMap.get(rpcServiceProperties.getRpcServicePropertiesFields());
        if (null == service) {
            log.info("<---- 本地没有该服务");
            throw new RuntimeException("没有找到指定的服务");
        }
        log.info("<---- 获取本地服务成功:[{}]",service);
        return service;
    }

    /**
     * 发布服务到 zk
     * @param service              服务对象
     * @param rpcServiceProperties 服务相关属性
     */
    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        String host = null;
        try {
            log.info("----> 开始获取本机 IP");
            host = InetAddress.getLocalHost().getHostAddress();
            log.info("<---- 获取本机 IP 成功:[{}]",host);
        } catch (UnknownHostException e) {
            log.info("<---- 获取本机 IP 失败 error:[{}]",e.getMessage());
            e.printStackTrace();
        }
        // 获取服务对象的接口 interface github.veikkoroc.service.UserService
        Class<?> serviceRelatedInterface = service.getClass().getInterfaces()[0];
        // 返回 Java Language Specification 中所定义的底层类的规范化名称。github.veikkoroc.service.UserService
        String serviceName = serviceRelatedInterface.getCanonicalName();
        // 设置服务的名称，其实就是接口的名称github.veikkoroc.service.UserService
        rpcServiceProperties.setServiceName(serviceName);
        log.info("----> 开始添加服务到本地 ,要注册的服务[{}] 服务的属性[{}]",service,rpcServiceProperties);
        //将服务加入本地缓存中
        this.addService(service, serviceRelatedInterface, rpcServiceProperties);
        log.info("<---- 服务添加到本地成功,本地可用的服务:[{}]",serviceMap);
        //将服务注册到注册中心
        log.info("----> 开始添加服务到 zk ,要注册的服务[{}] 服务的属性[{}]",service,rpcServiceProperties);
        //   github.veikkoroc.service.UserService11.0,   //192.168.1.101:9998
        serviceRegister.registryService(rpcServiceProperties.getRpcServicePropertiesFields(), new InetSocketAddress(host, Constants.PROVIDER_PORT));
        log.info("<---- 服务添加到 zk 成功");
    }

    /**
     * 发布服务
     *      未指定服务属性默认设置版本号和组群为空
     * @param service 服务对象
     */
    @Override
    public void publishService(Object service) {
        this.publishService(service, RpcServiceProperties.builder().group("").version("").build());
    }
}
