package github.veikkoroc.loadbalance.impl;

import github.veikkoroc.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:27
 */
@Slf4j
public class LoadBalanceImpl implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddresses) {
        log.info("----> 负载均衡随机算法");
        if (serviceAddresses == null || serviceAddresses.size()==0){
            log.info("<---- 没有服务地址");
            return null;
        }
        if (serviceAddresses.size() == 1){
            log.info("<---- 只有一个服务地址");
            return serviceAddresses.get(0);
        }
        //如果有多台服务器的话，随机找一个
        log.info("<---- 含有多个服务地址");
        return doSelect(serviceAddresses);
    }

    /**
     * 随机算法
     * @param serviceAddresses
     * @return
     */
    private String doSelect(List<String> serviceAddresses){
        Random random = new Random();
        int i = random.nextInt(serviceAddresses.size());
        return serviceAddresses.get(i);
    }
}
