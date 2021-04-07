package github.veikkoroc.serialize;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 11:59
 */
public interface Serializer {
    /**
     * 序列化
     * @param object
     * @return object 的字节数组
     */
    public byte[] serialize(Object object);

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @param <T>
     * @return  反序列化的对象
     */
    public <T>T deserialize(byte[] bytes, Class<T> clazz);
}

