package github.veikkoroc.discovery;

import java.net.InetSocketAddress;

/**
 * 用于发现 zk 上的服务
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:18
 */
public interface ServiceDiscovery {
    /**
     *通过服务名称查找服务
     * @param RpcServiceName
     * @return  service address
     */
    InetSocketAddress lookupService(String RpcServiceName);
}
