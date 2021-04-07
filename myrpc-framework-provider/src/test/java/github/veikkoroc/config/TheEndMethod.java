package github.veikkoroc.config;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/6 12:07
 */
public class TheEndMethod {
    public static void main(String[] args) {
        System.out.println("正在关闭程序");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("程序已经关闭了？");
        }));
        System.out.println("程序已经关闭了");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
