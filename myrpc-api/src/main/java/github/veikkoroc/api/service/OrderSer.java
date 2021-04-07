package github.veikkoroc.api.service;

import github.veikkoroc.api.pojo.Order;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 11:24
 */
public interface OrderSer {
    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    public Order getOrderById(Integer id);
}
