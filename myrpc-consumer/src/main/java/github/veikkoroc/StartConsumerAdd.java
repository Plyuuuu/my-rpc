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
public class StartConsumerAdd {
    public static void main(String[] args) {
        // 获取 rpc 调用请求数据传输对象
        NettyConsumerTransport nettyConsumerTransport = new NettyConsumerTransport();
        // 配置服务的属性
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().group("1").version("1.0").build();
        // 获得代理对象
        RpcConsumerProxy rpcConsumerProxy = new RpcConsumerProxy(nettyConsumerTransport, rpcServiceProperties);
        OrderSer orderSer = rpcConsumerProxy.getProxy(OrderSer.class);
        // 执行添加方法
        Map<String,String> order = new HashMap<>(8);
        order.put("id","1");
        order.put("userName","李四");
        order.put("time","2021-04-13");
        order.put("productName","HUAWEI Mate50");
        Integer res = orderSer.addOrder(order);
        System.err.println("====> 添加操作数据库影响行数:"+res);
    }
}
