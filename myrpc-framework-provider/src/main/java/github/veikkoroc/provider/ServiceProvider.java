package github.veikkoroc.provider;

import github.veikkoroc.remote.entity.RpcServiceProperties;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 14:13
 */
public interface ServiceProvider {
    /**
     * 添加服务到本地
     * @param service              服务对象
     * @param serviceClass         由服务实例对象实现的接口类
     * @param rpcServiceProperties 服务相关属性
     */
    void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties);

    /**
     * 获取本地服务
     *
     * @param rpcServiceProperties 服务相关的属性
     * @return service object
     */
    Object getService(RpcServiceProperties rpcServiceProperties);

    /**
     * 发布服务到 zk
     * @param service              服务对象
     * @param rpcServiceProperties 服务相关属性
     */
    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * 发布服务到 zk
     * @param service 服务对象
     */
    void publishService(Object service);
}
