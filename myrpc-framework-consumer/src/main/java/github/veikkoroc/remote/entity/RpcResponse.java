package github.veikkoroc.remote.entity;

import github.veikkoroc.enumeration.RpcResponseCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 12:02
 */
@Getter
@Setter
public class RpcResponse<T> implements Serializable {
    /**
     * 序列号
     */
    private static final long serialVersionUID = 202109032227L;
    /**
     * 响应请求的id
     */
    private String requestId;
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应的消息体
     */
    private String message;
    /**
     * 响应的数据
     */
    private T data;

    /**
     * 调用成功响应方法
     * @param data
     * @param requestId
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> success(T data,String requestId){
        RpcResponse<T> response = new RpcResponse<T>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        response.setMessage(RpcResponseCode.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null!=data){
            response.setData(data);
        }

        return response;
    }

    /**
     * 调用失败响应方法
     * @param rpcResponseCode
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode) {
        RpcResponse<T> response = new RpcResponse<T>();
        response.setCode(rpcResponseCode.getCode());
        response.setMessage(rpcResponseCode.getMessage());
        return response;
    }
}
