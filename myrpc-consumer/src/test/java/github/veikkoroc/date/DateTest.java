package github.veikkoroc.date;

import java.util.Date;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/29 11:23
 */
public class DateTest {
    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        long l = System.currentTimeMillis();
        System.out.println(l);
    }
}
