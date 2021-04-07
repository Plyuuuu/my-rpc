package github.veikkoroc.remote.transport.netty.codec;

import github.veikkoroc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 13:47
 */
@Slf4j
@AllArgsConstructor
public class KryoEncoder extends MessageToByteEncoder<Object> {
    /**
     * 序列化器
     */
    private final Serializer serializer;
    /**
     * 序列化类型
     */
    private final Class<?> genericClass;

    /**
     * 将对象转换为字节码然后写入到 ByteBuf 对象中
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        log.info("----> 自定义编码器开始编码:[{}]",o);
        if (genericClass.isInstance(o)) {
            // 1. 将对象转换为byte
            byte[] body = serializer.serialize(o);
            // 2. 读取消息的长度
            int dataLength = body.length;
            // 3.写入消息对应的字节数组长度,writerIndex 加 4
            byteBuf.writeInt(dataLength);
            //4.将字节数组写入 ByteBuf 对象中
            byteBuf.writeBytes(body);
            log.info("<---- 自定义编码器编码对象成功:[{}]",byteBuf.toString());
        }
    }
}
