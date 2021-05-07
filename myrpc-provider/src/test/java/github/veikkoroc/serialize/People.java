package github.veikkoroc.serialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/5/7 11:19
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class People {
    private String name;
    private int age;
    private String id;
    public void eat(){
        System.out.println("Eat some food...");
    }
}
