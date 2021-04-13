package github.veikkoroc;


import github.veikkoroc.api.service.OrderSer;
import github.veikkoroc.proxy.RpcConsumerProxy;
import github.veikkoroc.remote.entity.RpcServiceProperties;
import github.veikkoroc.remote.transport.netty.consumer.NettyConsumerTransport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/13 14:22
 */
public class StartConsumerMod {
    public static void main(String[] args) {
        // 获取 rpc 调用请求数据传输对象
        NettyConsumerTransport nettyConsumerTransport = new NettyConsumerTransport();
        // 配置服务的属性
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().group("1").version("1.0").build();
        // 获得代理对象
        RpcConsumerProxy rpcConsumerProxy = new RpcConsumerProxy(nettyConsumerTransport, rpcServiceProperties);
        OrderSer orderSer = rpcConsumerProxy.getProxy(OrderSer.class);

        // 执行修改方法
        Map<String,String> params = new HashMap<>(8);
        params.put("id","2");
        params.put("userName","张三");
        params.put("time","2021-04-13");
        params.put("productName","iphone12 Pro Plus Max 512G");
        Integer res = orderSer.modOrderById(params);
        System.err.println("====> 修改操作数据库影响行数:"+res);
    }
}
