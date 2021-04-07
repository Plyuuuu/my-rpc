package github.veikkoroc.remote.handler;

import github.veikkoroc.factory.SingletonFactory;
import github.veikkoroc.provider.ServiceProvider;
import github.veikkoroc.provider.impl.ServiceProviderImpl;
import github.veikkoroc.remote.entity.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 18:44
 */
@Slf4j
public class RpcRequestHandler {
    /**
     * 服务提供器
     */
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    /**
     * 返回请求调用的目标方法的结果
     * @param rpcRequest
     * @return
     */
    public Object handle(RpcRequest rpcRequest){
        log.info("----> 根据 rpcRequest:[{}] 获取目标服务",rpcRequest);
        Object service = serviceProvider.getService(rpcRequest.toRpcServiceProperties());
        log.info("<---- 获取到目标服务:[{}]",service);
        return invokeTargetMethod(rpcRequest,service);

    }

    /**
     * 获取方法执行的结果
     * @param rpcRequest    客户端请求
     * @param service       服务对象
     * @return 目标方法执行结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        log.info("----> 根据 rpcRequest:[{}] 获取目标方法",rpcRequest);
        Object result =null;
        try{
            // 根据目标方法名和参数获得方法对象
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            log.info("<---- 获取到目标方法:[{}]",method);
            log.info("----> 开始调用目标方法");
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("<---- 调用目标方法返回结果:[{}]",result);
        }catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e){
            log.error("<---- 获取并调用目标方法失败 error:[{}]",e.getMessage());
            throw new RuntimeException("获取并调用目标方法失败",e);
        }
        return result;
    }
}
