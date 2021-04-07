package github.veikkoroc.remote.transport.netty.provider;

import github.veikkoroc.enumeration.RpcMessageType;
import github.veikkoroc.enumeration.RpcResponseCode;
import github.veikkoroc.factory.SingletonFactory;
import github.veikkoroc.remote.entity.RpcRequest;
import github.veikkoroc.remote.entity.RpcResponse;
import github.veikkoroc.remote.handler.RpcRequestHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 13:59
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * rpc请求处理器
     */
    private final RpcRequestHandler rpcRequestHandler;

    public NettyServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }
    /**
     * 读取数据（读取客户端发送的数据）
     * 1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
     * 2. Object msg: 就是客户端发送的数据 默认Object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            RpcRequest rpcRequest = (RpcRequest) msg;
            log.info("====> 服务提供者接收到服务消费者请求 rpcRequest:[{}]",rpcRequest);
            // 如果接收到心跳信息,直接return
            if (rpcRequest.getRpcMessageType()== RpcMessageType.HEART_BEAT){
                log.info("====> 接收到客户端的心跳信息");
                return;
            }
            // 执行目标方法（客户端需要执行的方法）并返回方法结果
            Object result = rpcRequestHandler.handle(rpcRequest);
            log.info(String.format("====> 调用目标方法获得的结果: %s", result.toString()));

            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                log.info("----> 服务调用者通道处于活跃并可写状态,开始写结果返回给服务调用者...");
                RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                log.info("<---- 服务提供者数据写回完毕");
            } else {
                log.info("----> 服务调用者通道无法使用...");
                RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCode.FAIL);
                ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                log.error("<---- 现在无法写回结果,消息已删除");
            }
        }finally{
            log.info("----> 正在释放 ByteBuf ...");
            // 确保已释放ByteBuf，否则可能会发生内存泄漏
            ReferenceCountUtil.release(msg);
            log.info("<---- ByteBuf 释放成功");
        }
    }

    /**
     * 发生异常执行
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("====> NettyServer 处理客户端访问出现异常");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 用户事件已触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("====> userEventTriggered 方法执行");
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("----> 发生空闲事件,因此关闭连接...");
                ctx.close();
                log.info("<---- 连接关闭完成");
            }
        } else {
            log.info("----> 不是空闲事件触发,交给父类处理....");
            super.userEventTriggered(ctx, evt);
            log.info("<---- 父类处理完成");
        }
    }
}
