package github.veikkoroc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 19:04
 */
@ToString
@Getter
@AllArgsConstructor
public enum RpcResponseCode {
    SUCCESS(200,"远程调用成功"),
    FAIL(500,"远程调用失败");
    private final int code;
    private final String message;
}
