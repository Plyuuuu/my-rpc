package github.veikkoroc.remote.transport.netty.consumer;

import github.veikkoroc.discovery.ServiceDiscovery;
import github.veikkoroc.discovery.impl.ServiceDiscoveryImpl;
import github.veikkoroc.factory.SingletonFactory;
import github.veikkoroc.remote.entity.RpcRequest;
import github.veikkoroc.remote.entity.RpcResponse;
import github.veikkoroc.remote.entity.UnProcessedRequests;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 *  向 provider 发送 rpc 远程调用请求
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:16
 */
@Slf4j
public class NettyConsumerTransport implements ConsumerTransport {
    /**
     * 发现服务
     * 获得InetSocketAddress
     */
    private final ServiceDiscovery serviceDiscovery;

    /**
     * 未处理的消息
     */
    private final UnProcessedRequests unprocessedRequests;

    /**
     * 通道提供者
     */
    private final ChannelProvider channelProvider;

    public NettyConsumerTransport() {
        this.serviceDiscovery = new ServiceDiscoveryImpl();
        this.unprocessedRequests = SingletonFactory.getInstance(UnProcessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }
    @Override
    public CompletableFuture<RpcResponse<Object>> sendRpcRequest(RpcRequest rpcRequest) {
        log.info("----> 开始发送 rpcRequest:[{}] 到服务提供者服务器...",rpcRequest);
        // 创建返回值
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 通过 RpcRequest 获得服务名
        String rpcServiceName = rpcRequest.toRpcServiceProperties().getRpcServicePropertiesFields();
        // 获得服务地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        // 获取服务器地址相关通道
        Channel channel = channelProvider.getChannel(inetSocketAddress);
        log.info("====> 客户端获取的通道:[{}]",channel);
        log.info("====> 客户端获取的通道是否活跃:[{}]",channel.isActive());

        if (channel != null && channel.isActive()){
            // 存放未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(),resultFuture);
            log.info("====> 未处理的消息存放成功:[{}]",unprocessedRequests);
            // 发送消息到服务器
            // ChannelFuture的作用是用来保存Channel异步操作的结果。
            // 添加ChannelFutureListener，以便于在I/O操作完成的时候，能够获得通知。
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future->{
                if (future.isSuccess()){
                    log.info("<---- 服务消费者发送消息:[{}] 成功",rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("<---- 服务消费者发送消息出错:[{}]",future.cause().getMessage());
                }
            });

        }else {
            throw new IllegalArgumentException("NettyClientTransport 发送 RpcRequest 发送异常");
        }
        return resultFuture;
    }
}
