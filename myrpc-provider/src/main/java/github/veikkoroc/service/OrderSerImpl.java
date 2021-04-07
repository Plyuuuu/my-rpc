package github.veikkoroc.service;

import github.veikkoroc.api.pojo.Order;
import github.veikkoroc.api.service.OrderSer;
import org.springframework.stereotype.Component;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 11:43
 */
@Component
public class OrderSerImpl implements OrderSer {
    @Override
    public Order getOrderById(Integer id) {
        return new Order(id,"迪丽热巴0","2020-02-20","口红");
    }
}
