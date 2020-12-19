package com.chua.utils.tools.bean.script;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 脚本信息
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/16
 */
@Getter
public final class BinderScript {
    /**
     * the clerical list
     */
    private final List<Clerical> clerical = new ArrayList<>();
    /**
     * 包
     */
    private final List<String> packages = new ArrayList<>();

    /**
     * clerical -> Method
     *
     * @see java.lang.reflect.Method
     */
    @Getter
    @Setter
    public static class Clerical {
        /**
         * 名称
         */
        private String name;
        /**
         * 类型
         */
        private String type;
        /**
         * 参数类型
         */
        private List<String> paramTypes;
        /**
         * 消息体
         */
        private String body;
    }
}
