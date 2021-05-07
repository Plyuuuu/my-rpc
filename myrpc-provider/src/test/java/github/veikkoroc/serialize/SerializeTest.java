package github.veikkoroc.serialize;

import java.util.Arrays;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/5/7 11:21
 */
public class SerializeTest {
    public static void main(String[] args) {
        People people = new People("迪丽热巴", 18, "001");
        KryoSerializer kryoSerializer = new KryoSerializer();
        long start = System.currentTimeMillis();
        byte[] serialize = kryoSerializer.serialize(people);
        long end = System.currentTimeMillis();
        System.err.println("序列化后的字节数组:"+Arrays.toString(serialize));
        System.err.println("序列化耗时:"+(end-start)+" ms");
        People deserialize = kryoSerializer.deserialize(serialize, People.class);
        deserialize.eat();
    }
}
