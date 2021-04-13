package github.veikkoroc;

import github.veikkoroc.api.pojo.Order;
import github.veikkoroc.api.service.OrderSer;
import github.veikkoroc.proxy.RpcConsumerProxy;
import github.veikkoroc.remote.entity.RpcServiceProperties;
import github.veikkoroc.remote.transport.netty.consumer.NettyConsumerTransport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:48
 */
public class StartConsumer {
    public static void main(String[] args) {
        // 获取 rpc 调用请求数据传输对象
        NettyConsumerTransport nettyConsumerTransport = new NettyConsumerTransport();
        // 配置服务的属性
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder().group("1").version("1.0").build();
        // 获得代理对象
        RpcConsumerProxy rpcConsumerProxy = new RpcConsumerProxy(nettyConsumerTransport, rpcServiceProperties);
        OrderSer orderSer = rpcConsumerProxy.getProxy(OrderSer.class);


        // 执行查询方法,获取调用结果
        Order orderById = orderSer.getOrderById("2");
        System.err.println(orderById);
//        // 执行修改方法
//        Map<String,String> params = new HashMap<>(8);
//        params.put("id","1");
//        params.put("userName","迪丽热巴");
//        params.put("time","2022-02-03");
//        params.put("productName","iphone13");
//        Integer res = orderSer.modOrderById(params);
//        System.err.println("修改操作数据库影响行数:"+res);
        // 执行添加方法
//        Map<String,String> order = new HashMap<>(8);
//        order.put("id","2");
//        order.put("userName","迪丽热巴2");
//        order.put("time","2022-02-03");
//        order.put("productName","iphone132");
//        Integer res2 = orderSer.addOrder(order);
//        System.err.println("添加操作数据库影响行数:"+res2);
        // 执行删除方法
//        Integer res3 = orderSer.delOrder("1");
//        System.err.println("删除操作数据库影响行数:"+res3);
    }
}
