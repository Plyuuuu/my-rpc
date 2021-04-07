package github.veikkoroc.zk;

import github.veikkoroc.Utils.CuratorUtils;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/6 11:13
 */
public class ClearRegistryTest {
    public static void main(String[] args) {
        CuratorUtils.clearRegistry(CuratorUtils.getZkClient());
    }
}
