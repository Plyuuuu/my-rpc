package github.veikkoroc.utils;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 14:05
 */
public class Constants {
    /**
     * 服务提供者的默认端口
     */
    public static Integer PROVIDER_PORT = 9996;
    /**
     * zk 上服务的根路径
     */
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";

    /**
     * zk 默认的套接字
     */
    public static final String DEFAULT_ZOOKEEPER_SOCKET = "127.0.0.1:2181";

    /**
     * 设置服务的端口
     * @param port
     */
    public static void setServicePort(Integer port){
        Constants.PROVIDER_PORT = port;
    }
}
