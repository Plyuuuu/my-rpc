package github.veikkoroc;

import github.veikkoroc.remote.entity.RpcServiceProperties;
import github.veikkoroc.remote.transport.netty.provider.NettyProvider;
import github.veikkoroc.service.OrderSerImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.context.annotation.ComponentScan;


/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 11:41
 */
@ComponentScan
public class StartProvide {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(StartProvide.class);
        // 获取需要发布的服务
        OrderSerImpl orderSerImpl = ioc.getBean(OrderSerImpl.class);
        // 创建 myrpc-framework-provider 上的 nettyProvider 对象
        NettyProvider nettyProvider = ioc.getBean(NettyProvider.class);
        // 获取服务属性,注册服务
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().group("1").version("1.0").build();
        nettyProvider.registerService(orderSerImpl,rpcServiceProperties);
        // 启动 NettyProvider 供消费者消费服务
        nettyProvider.startNettyServer();
    }
}
