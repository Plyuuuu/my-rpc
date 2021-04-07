package github.veikkoroc.remote.entity;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 未处理的请求
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 22:32
 */
@Slf4j
public class UnProcessedRequests {
    private static final Map<String, CompletableFuture<RpcResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse<Object>> future) {
        log.info("----> 添加未处理的请求...");
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
        log.info("<---- 未处理完的请求添加完成 UNPROCESSED_RESPONSE_FUTURES:[{}]",UNPROCESSED_RESPONSE_FUTURES);
    }

    public void complete(RpcResponse<Object> rpcResponse) {
        log.info("----> 开始移除已经完成的请求,请求 id:[{}]...",rpcResponse.getRequestId());
        // 等待这个 Future 的客户端将得到该结果，并且 completableFuture.complete() 之后的调用将被忽略。
        // Map 的 remove()方法将返回 被移除键值对的value
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
        log.info("<---- 移除已经完成的请求成功,请求 id:[{}]...",rpcResponse.getRequestId());
    }
}
