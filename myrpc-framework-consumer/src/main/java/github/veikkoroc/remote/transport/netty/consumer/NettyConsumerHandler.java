package github.veikkoroc.remote.transport.netty.consumer;

import github.veikkoroc.enumeration.RpcMessageType;
import github.veikkoroc.factory.SingletonFactory;
import github.veikkoroc.remote.entity.RpcRequest;
import github.veikkoroc.remote.entity.RpcResponse;
import github.veikkoroc.remote.entity.UnProcessedRequests;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 22:55
 */
@Slf4j
public class NettyConsumerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 未处理的消息集合
     */
    private final UnProcessedRequests unProcessedRequests;
    /**
     * 通道提供器
     */
    private final ChannelProvider channelProvider;

    public NettyConsumerHandler() {
        this.unProcessedRequests = SingletonFactory.getInstance(UnProcessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 获取服务提供者返回的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            log.info("----> 服务消费者接收到服务提供者的消息:[{}]",msg);
            if (msg instanceof RpcResponse){
                log.info("====> 该消息是处理 RpcRequest 的返回的 RpcResponse 信息");
                RpcResponse<Object> rpcResponse = (RpcResponse<Object>) msg;
                unProcessedRequests.complete(rpcResponse);
                log.info("<---- 把未处理的消息集合更新 unProcessedRequests:[{}]",unProcessedRequests);
            }
        }finally {
            log.info("----> 开始释放 ByteBuf 内的数据...");
            ReferenceCountUtil.release(msg);
            log.info("<---- 释放 ByteBuf 内的数据完成");
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("====> userEventTriggered 方法执行...");
        if (evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.WRITER_IDLE){
                log.info("====> 客户端写数据发生闲置:[{}]",ctx.channel().remoteAddress());
                Channel channel = channelProvider.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcRequest rpcRequest = RpcRequest.builder().rpcMessageType(RpcMessageType.HEART_BEAT).build();
                log.info("----> 开始发送心跳消息:[{}]",rpcRequest);
                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                log.info("<---- 发送心跳消息完成");
            }
        }else {
            log.info("----> 不是空闲事件触发,交给父类处理....");
            super.userEventTriggered(ctx, evt);
            log.info("<---- 父类处理完成");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("----> 服务消费方Netty发生异常:[{}],正在关闭通道...",cause.getMessage());
        cause.printStackTrace();
        ctx.close();
        log.info("<---- 服务消费方通道关闭完成");
    }
}
