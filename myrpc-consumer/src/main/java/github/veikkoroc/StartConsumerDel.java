package github.veikkoroc;

import github.veikkoroc.api.service.OrderSer;
import github.veikkoroc.proxy.RpcConsumerProxy;
import github.veikkoroc.remote.entity.RpcServiceProperties;
import github.veikkoroc.remote.transport.netty.consumer.NettyConsumerTransport;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/13 14:22
 */
public class StartConsumerDel {
    public static void main(String[] args) {
        // 获取 rpc 调用请求数据传输对象
        NettyConsumerTransport nettyConsumerTransport = new NettyConsumerTransport();
        // 配置服务的属性
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().group("1").version("1.0").build();
        // 获得代理对象
        RpcConsumerProxy rpcConsumerProxy = new RpcConsumerProxy(nettyConsumerTransport, rpcServiceProperties);
        OrderSer orderSer = rpcConsumerProxy.getProxy(OrderSer.class);
        // 执行删除方法
        Integer res = orderSer.delOrder("2");
        System.err.println("====> 删除操作数据库影响行数:"+res);
    }
}
