package github.veikkoroc.remote.transport.netty.consumer;

import github.veikkoroc.remote.entity.RpcRequest;
import github.veikkoroc.remote.entity.RpcResponse;
import github.veikkoroc.remote.transport.netty.codec.KryoDecoder;
import github.veikkoroc.remote.transport.netty.codec.KryoEncoder;
import github.veikkoroc.serialize.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 22:55
 */
@Slf4j
public class NettyConsumer {
    /**
     * 客户端启动对象
     */
    private final Bootstrap bootstrap  = new Bootstrap();
    /**
     * 事件线程组
     */
    EventLoopGroup eventLoopGroup;

    /**
     * 初始化资源
     */

    public NettyConsumer() {
        start();
    }

    public void start(){
        log.info("----> 正在启动 NettyConsumer ...");
        this.eventLoopGroup = new NioEventLoopGroup();
        KryoSerializer kryoSerializer = new KryoSerializer();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        //如果15秒钟内没有数据发送到服务器，则发送心跳请求
                        channel.pipeline().addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
                        //配置自定义序列化编解码器
                        channel.pipeline().addLast(new KryoEncoder(kryoSerializer, RpcRequest.class));
                        channel.pipeline().addLast(new KryoDecoder(kryoSerializer, RpcResponse.class));
                        channel.pipeline().addLast(new NettyConsumerHandler());
                    }
                });
        log.info("<---- NettyConsumer 启动完成");
    }

    /**
     * 关闭循环事件组
     */
    public void close(){
        log.info("<---- 正在关闭 eventLoopGroup ...");
        eventLoopGroup.shutdownGracefully();
        log.info("----> 关闭 eventLoopGroup 成功");
    }
    /**
     * 连接服务器并获取频道，以便向服务器发送rpc消息
     * @param inetSocketAddress
     * @return
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress){
        log.info("----> 服务消费者将要连接服务提供者:[{}]",inetSocketAddress);
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        //连接到服务器
        ChannelFuture future = bootstrap.connect(inetSocketAddress).sync();
        log.info("<---- 服务消费者连接成功,获取通道:[{}]",future.channel());
        return future.channel();

    }
}
