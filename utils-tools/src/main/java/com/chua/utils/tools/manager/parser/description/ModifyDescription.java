package com.chua.utils.tools.manager.parser.description;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改修饰
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class ModifyDescription {
    /**
     * 异常说明
     */
    private List<Throwable> throwableList = new ArrayList<>();
    /**
     * 类
     */
    @Setter
    @Getter
    private Class<?> aClass;

    /**
     * 添加异常
     *
     * @param throwable 异常
     */
    public void addThrowable(Throwable throwable) {
        throwableList.add(throwable);
    }
}
