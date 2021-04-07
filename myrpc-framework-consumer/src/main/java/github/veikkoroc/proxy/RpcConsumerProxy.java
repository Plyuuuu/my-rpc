package github.veikkoroc.proxy;

import github.veikkoroc.remote.entity.RpcRequest;
import github.veikkoroc.remote.entity.RpcResponse;
import github.veikkoroc.remote.entity.RpcServiceProperties;
import github.veikkoroc.remote.transport.netty.consumer.ConsumerTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:54
 */
@Slf4j
public class RpcConsumerProxy implements InvocationHandler {
    /**
     * 用于向服务器发送请求。
     */
    private final ConsumerTransport consumerTransport;
    /**
     * 服务属性
     */
    private final RpcServiceProperties rpcServiceProperties;

    public RpcConsumerProxy(ConsumerTransport consumerTransport, RpcServiceProperties rpcServiceProperties) {
        this.consumerTransport = consumerTransport;
        // 没有设置群和版本默认为空
        if (rpcServiceProperties.getGroup() == null) {
            rpcServiceProperties.setGroup("");
        }
        if (rpcServiceProperties.getVersion() == null) {
            rpcServiceProperties.setVersion("");
        }
        this.rpcServiceProperties = rpcServiceProperties;
    }
    public RpcConsumerProxy(ConsumerTransport consumerTransport) {
        this.consumerTransport = consumerTransport;
        this.rpcServiceProperties = RpcServiceProperties.builder().group("").version("").build();
    }
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("====> 调用方法:[{}]",method.getName());
        log.info("====> 传入参数:[{}]",args.toString());
        log.info("====> 接口名字:[{}]",method.getDeclaringClass().getName());
        log.info("====> 参数类型:[{}]",method.getParameterTypes());
        log.info("====> 请求的ID:[{}]", UUID.randomUUID().toString());
        log.info("====> 组:[{}]",rpcServiceProperties.getGroup());
        log.info("====> 版本:[{}]",rpcServiceProperties.getVersion());
        log.info("----> 开始构建 RpcRequest...");
        //构建rpcRequest
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceProperties.getGroup())
                .version(rpcServiceProperties.getVersion())
                .build();
        log.info("<---- 构建 RpcRequest:[{}] 完成",rpcRequest);
        RpcResponse<Object> rpcResponse = null;
        // netty 传输调用请求,获取调用
        if (consumerTransport instanceof ConsumerTransport) {
            log.info("----> 通过 Netty 传输远程调用请求...");
            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) consumerTransport.sendRpcRequest(rpcRequest);
            // completableFuture.get()会一直阻塞直到 Future 完成。
            rpcResponse = completableFuture.get();
            log.info("<---- 获得调用结果 rpcResponse:[{}]",rpcResponse);
        }
        return rpcResponse.getData();
    }
}
