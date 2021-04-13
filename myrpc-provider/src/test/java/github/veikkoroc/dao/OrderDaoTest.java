package github.veikkoroc.dao;

import github.veikkoroc.StartProvide9996;
import github.veikkoroc.api.pojo.Order;
import github.veikkoroc.service.OrderSerImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/12 9:10
 */
public class OrderDaoTest {
    public static void main(String[] args) {
       AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(StartProvide9996.class);
        OrderSerImpl orderSer = ioc.getBean(OrderSerImpl.class);
        Order orderById = orderSer.getOrderById("1");
        System.out.println(orderById);
        Map<String,String> params = new HashMap<>(8);
        params.put("id","1");
        params.put("userName","古力娜扎");
        params.put("time","2022-02-03");
        params.put("productName","iphone13");
        orderSer.modOrderById(params);
        Order orderById1 = orderSer.getOrderById("1");
        System.err.println(orderById1);;

    }
}
