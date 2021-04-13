package github.veikkoroc.config;

import github.veikkoroc.utils.CuratorUtils;
import github.veikkoroc.utils.ThreadPoolFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务提供者初始化或关闭时执行一些方法
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 16:35
 */
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook customShutdownHook = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return customShutdownHook;
    }

    /**
     * 清除该服务器在 zk 上的残留数据
     */
    public void clearAll() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 获取本机 IP
            String localIp = null;
            try {
                localIp = InetAddress.getLocalHost().getAddress().toString();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            log.info("----> 开始清除:[{}]在 zk 上的残留数据...",localIp);
            CuratorUtils.clearRegistry(CuratorUtils.getZkClient());
            log.info("<---- 清除:[{}]在 zk 上的残留数据完成",localIp);
            ThreadPoolFactoryUtils.shutDownAllThreadPool();
        }));
    }
}
