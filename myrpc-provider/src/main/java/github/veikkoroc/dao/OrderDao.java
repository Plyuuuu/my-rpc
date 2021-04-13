package github.veikkoroc.dao;

import github.veikkoroc.api.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/12 9:01
 */
@Repository
public interface OrderDao {
    /**
     * 获取订单通过 id
     * @return
     * @param id
     */
    Order getOrderById(String id);

    /**
     * 修改订单
     * @param params
     * @return
     */
    int modOrderById(Map<String,String> params);

    /**
     * 添加订单
     * @param params
     * @return
     */
    int addOrder(Map<String,String> params);

    /**
     * 删除订单
     * @param id
     * @return
     */
    int delOrder(String id);
}
