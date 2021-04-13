package github.veikkoroc.service;

import github.veikkoroc.api.pojo.Order;
import github.veikkoroc.api.service.OrderSer;
import github.veikkoroc.dao.OrderDao;
import github.veikkoroc.dao.bean.DaoBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 11:43
 */
@Slf4j
@Service
public class OrderSerImpl implements OrderSer {

    @Autowired
    private DaoBeanFactory<OrderDao> daoBeanFactory;

    private OrderDao orderDao;

    /**
     * 查询订单通过订单编号
     * @param id
     * @return
     */
    @Override
    public Order getOrderById(String id) {
        OrderDao orderDao = getOrderDao();
        log.info("----> 开始通过id[{}]查询订单...",id);
        Order orderById = orderDao.getOrderById(id);
        log.info("<---- 通过id查询到订单order[{}]",orderById);
        return orderById;
    }

    /**
     * 通过 id 修改订单
     * @param params
     */
    @Override
    public Integer modOrderById(Map<String,String> params){
        OrderDao orderDao = getOrderDao();
        log.info("----> 开始修改订单[{}]...",params);
        int res = orderDao.modOrderById(params);
        log.info("<---- 修改订单成功 res:[{}]",res);
        return res;
    }

    /**
     * 添加订单
     * @param params
     * @return
     */
    @Override
    public Integer addOrder(Map<String, String> params) {
        OrderDao orderDao = getOrderDao();
        log.info("----> 开始添加订单[{}]...",params);
        int res = orderDao.addOrder(params);
        log.info("<---- 添加订单成功 res:[{}]",res);
        return res;
    }

    /**
     * 通过 id 删除订单
     * @param id
     * @return
     */
    @Override
    public Integer delOrder(String id) {
        OrderDao orderDao = getOrderDao();
        log.info("----> 开始删除订单[{}]...",id);
        int res = orderDao.delOrder(id);
        log.info("<---- 删除订单成功 res:[{}]",res);
        return res;
    }


    /**
     * 初始化 OrderDao
     * @return
     */
    private OrderDao getOrderDao(){
        if (this.orderDao == null) {
            this.orderDao = daoBeanFactory.getDaoBean(OrderDao.class);
        }
        return this.orderDao;
    }
}
