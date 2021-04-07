package github.veikkoroc.remote.transport.netty.consumer;

import github.veikkoroc.remote.entity.RpcRequest;

/**
 * 服务消费者传输消息接口
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 21:11
 */
public interface ConsumerTransport {
    /**
     * 发送 rpc 调用请求给 provider
     * @param rpcRequest  请求对象
     * @return  服务器返回请求结果
     */
    public Object sendRpcRequest(RpcRequest rpcRequest);
}
