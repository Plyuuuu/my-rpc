package github.veikkoroc.registry.impl;

import github.veikkoroc.utils.CuratorUtils;
import github.veikkoroc.registry.ServiceRegister;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 14:20
 */
@Slf4j
public class ZkServiceRegisterImpl implements ServiceRegister {
    @Override//   github.veikkoroc.service.UserService11.0,   //192.168.1.101:9998
    public void registryService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        //“/my-rpc/toRpcServiceName()/IP:端口号”--->/my-rpc/github.veikkoroc.service.UserService11.0/192.168.1.101:9998
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
