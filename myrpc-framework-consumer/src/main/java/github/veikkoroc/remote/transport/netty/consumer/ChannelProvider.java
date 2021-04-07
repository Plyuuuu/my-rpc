package github.veikkoroc.remote.transport.netty.consumer;

import github.veikkoroc.factory.SingletonFactory;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 22:42
 */
@Slf4j
public class ChannelProvider {
    /**
     * 存放通道
     */
    private final Map<String, Channel> channelMap;
    /**
     * nettyConsumer
     */
    private final NettyConsumer nettyConsumer;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
        nettyConsumer = SingletonFactory.getInstance(NettyConsumer.class);
    }

    /**
     * 获得与服务器通信的通道
     * @return 通道
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress){
        log.info("----> 开始获取 Channel 通道,inetSocketAddress:[{}]...",inetSocketAddress);
        // key = "/127.0.0.1:6668"
        String key = inetSocketAddress.toString();
        // 确定相应地址是否存在连接
        if (channelMap.containsKey(key)){
            Channel channel = channelMap.get(key);
            // 通道可以用就直接返回，否则移除掉
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        // Map中没有就重新获取缓存
        Channel channel = nettyConsumer.doConnect(inetSocketAddress);
        log.info("<---- 获取通道成功:[{}]",channel);
        channelMap.put(key, channel);
        log.info("====> 通道集合内通道:[{}]",channelMap);
        return channel;
    }
}
