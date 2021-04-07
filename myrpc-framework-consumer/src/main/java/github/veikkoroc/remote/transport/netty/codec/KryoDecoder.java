package github.veikkoroc.remote.transport.netty.codec;

import github.veikkoroc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 * 自定义的 Netty 解码器
 *
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 13:47
 */
@Slf4j
@AllArgsConstructor
public class KryoDecoder extends ByteToMessageDecoder {
    /**
     * 序列化工具
     */
    private final Serializer serializer;
    /**
     * 解码后的类型
     */
    private final Class<?> genericClass;

    /**
     * Netty传输的消息长度也就是对象序列化后对应的字节数组的大小，存储在 ByteBuf 头部
     */
    private static final int BODY_LENGTH = 4;

    /**
     * 解码 ByteBuf 对象
     * @param channelHandlerContext 解码器关联的 ChannelHandlerContext 对象
     * @param byteBuf   "入站"数据，也就是 ByteBuf 对象
     * @param list  解码之后的数据对象需要添加到 out 对象里面
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("----> 自定义解码器开始解码");
        //1.byteBuf中写入的消息长度所占的字节数已经是4了，所以 byteBuf 的可读字节必须大于 4，
        if (byteBuf.readableBytes() >= BODY_LENGTH) {
            //2.标记当前readIndex的位置，以便后面重置readIndex 的时候使用
            byteBuf.markReaderIndex();
            //3.读取消息的长度
            //注意： 消息长度是encode的时候我们自己写入的，参见 NettyKryoEncoder 的encode方法
            int dataLength = byteBuf.readInt();
            //4.遇到不合理的情况直接 return
            if (dataLength < 0 || byteBuf.readableBytes() < 0) {
                log.error("<---- 数据长度或 byteBuf 可读字节无效");
                return;
            }
            //5.如果可读字节数小于消息长度的话，说明是不完整的消息，重置readIndex
            if (byteBuf.readableBytes() < dataLength) {
                byteBuf.resetReaderIndex();
                log.info("<---- 消息不完整,重置 readIndex");
                return;
            }
            // 6.走到这里说明没什么问题了，可以反序列化了
            byte[] body = new byte[dataLength];
            byteBuf.readBytes(body);
            // 将bytes数组转换为我们需要的对象
            Object obj = serializer.deserialize(body, genericClass);
            list.add(obj);
            log.info("<---- 自定义解码器成功将 ByteBuf 解码为对象");
        }
    }

}
