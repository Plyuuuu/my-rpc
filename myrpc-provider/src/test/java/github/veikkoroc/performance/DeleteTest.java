package github.veikkoroc.performance;

import github.veikkoroc.StartProvide9996;
import github.veikkoroc.service.OrderSerImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/29 11:34
 */
public class DeleteTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(StartProvide9996.class);
        OrderSerImpl orderSer = ioc.getBean(OrderSerImpl.class);
        long start = System.currentTimeMillis();
        Integer integer = orderSer.delOrder("2");
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(integer);
    }
}
