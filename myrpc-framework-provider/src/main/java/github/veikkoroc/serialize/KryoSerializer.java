package github.veikkoroc.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.veikkoroc.remote.entity.RpcRequest;
import github.veikkoroc.remote.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 12:00
 */
@Slf4j
public class KryoSerializer implements Serializer {
    /**
     * 由于 Kryo 不是线程安全的。每个线程都应该有自己的 Kryo，Input 和 Output 实例。
     * 所以，使用 ThreadLocal 存放 Kryo 对象
     */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        log.info("----> 线程开始获取 kryo ");
        Kryo kryo = new Kryo();
        // 注册序列化类型
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        // 默认值为true,是否关闭注册行为,关闭之后可能存在序列化问题，一般推荐设置为 true
        kryo.setReferences(true);
        // 默认值为false,是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
        kryo.setRegistrationRequired(false);
        log.info("<---- 线程获取 kryo 完成:[{}]",kryo);
        return kryo;
    });

    /**
     * 序列化对象
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {
        log.info("----> 开始序列化对象:[{}]",object);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // Object->byte:将对象序列化为byte数组
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            log.info("<---- 序列化对象成功:[{}]",output);
            return output.toBytes();
        } catch (Exception e) {
            log.error("<---- 对象序列化失败:[{}]",e.getMessage());
            throw new RuntimeException("对象序列化失败");
        }
    }

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        log.info("----> 开始反序列化字节数组:[{}]",bytes);
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            log.info("<---- 反序列化字节数组成功:[{}]",clazz.cast(o));
            return clazz.cast(o);
        } catch (Exception e) {
            log.info("<---- 反序列化字节数组失败:[{}]",e.getMessage());
            throw new RuntimeException("对象反序列化失败");
        }
    }
}
