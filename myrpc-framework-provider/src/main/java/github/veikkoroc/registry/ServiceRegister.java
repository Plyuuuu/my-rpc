package github.veikkoroc.registry;

import java.net.InetSocketAddress;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 14:19
 */
public interface ServiceRegister {
    /**
     * 注册服务，通过服务名，服务地址
     * @param rpcServiceName service name
     * @param inetSocketAddress service address
     */
    void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}

