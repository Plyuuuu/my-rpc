package github.veikkoroc.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Veikko Roc
 * @version 1.0
 * @date 2021/4/5 13:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcServiceProperties {
    /**
     * 服务版本
     */
    private String version;
    /**
     * 服务群组
     */
    private String group;
    /**
     * 服务名称
     */
    private String serviceName;

    public String getRpcServicePropertiesFields(){
        return this.getServiceName()+this.getGroup()+this.getVersion();
    }
}
