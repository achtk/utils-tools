package com.chua.utils.tools.spring.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;

/**
 * bean状态
 * @author CH
 * @date 2020-09-26
 */
@Getter
@AllArgsConstructor
public enum BeanStatus {
    /**
     * bean不存在
     */
    BEAN_NOT_EXIST(0, "bean does not exist"),
    /**
     * 其它异常信息
     */
    OTHER_ABNORMAL_INFORMATION(-1, "Other abnormal information");

    /**
     * 状态
     */
    private int status;
    /**
     * 信息
     */
    private String message;
}
