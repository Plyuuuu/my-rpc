package github.veikkoroc.remote.entity;

import github.veikkoroc.enumeration.RpcMessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  调用请求消息实体
 *
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 12:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    /**
     *随便设，就设为日期时间吧
     */
    private static final long serialVersionID = 202009032124L;
    /**
     *请求编号
     */
    private String requestId;
    /**
     *传输的接口名
     */
    private String interfaceName;
    /**
     *调用的方法名
     */
    private String methodName;
    /**
     *传入的参数
     */
    private Object[] parameters;
    /**
     *传入参数的类型
     */
    private Class<?>[] paramTypes;
    /**
     *调用类型信息
     */
    private RpcMessageType rpcMessageType ;
    /**
     *服务版本
     */
    private String version;
    /**
     *处理一个类有多个接口
     */
    private String group;

    /**
     * 服务属性设置
     *      服务名、版本号、群组
     * @return 服务属性（接口名、版本和组）
     */
    public RpcServiceProperties toRpcServiceProperties(){
        return RpcServiceProperties.builder()
                .serviceName(this.interfaceName)
                .version(this.version)
                .group(this.group).build();
    }
}
