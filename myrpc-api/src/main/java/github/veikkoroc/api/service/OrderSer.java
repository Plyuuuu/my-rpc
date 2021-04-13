package github.veikkoroc.api.service;

import github.veikkoroc.api.pojo.Order;

import java.util.Map;

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
    public Order getOrderById(String id);

    /**
     * 根据 id 修改订单
     * @param params
     * @return 影响行数
     */
    public Integer modOrderById(Map<String,String> params);

    /**
     * 添加订单
     * @param params
     * @return
     */
    public Integer addOrder(Map<String,String> params);

    /**
     * 删除订单
     * @param id
     * @return
     */
    public Integer delOrder(String id);
}
