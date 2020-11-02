package com.chua.utils.tools.properties;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Properties;

/**
 * 操作配置项
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
@Data
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class OperatorProperties {
    /**
     * 驱动
     */
    private String driver;
    /**
     * 地址
     */
    private String url;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 额外参数
     */
    private Properties properties = new Properties();
}
