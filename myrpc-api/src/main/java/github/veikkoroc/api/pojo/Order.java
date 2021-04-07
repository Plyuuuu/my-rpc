package github.veikkoroc.api.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 11:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    /**
     * 订单编号
     */
    private int id;

    /**
     * 订单主人名字
     */
    private String userName;
    /**
     * 订单时间
     */
    private String time;
    /**
     * 产品名
     */
    private String productName;
}
