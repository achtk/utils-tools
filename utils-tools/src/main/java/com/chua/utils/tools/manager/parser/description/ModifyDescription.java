package com.chua.utils.tools.manager.parser.description;

import com.chua.utils.tools.util.ClassUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改修饰
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class ModifyDescription<T> {
    /**
     * 异常说明
     */
    @Getter
    private final List<Throwable> throwableList = new ArrayList<>();
    /**
     * 类
     */
    private Class<T> modifyType;

    public void setModifyType(Class<T> modifyType) {
        this.modifyType = modifyType;
        this.toObject = ClassUtils.forObject(modifyType);
    }

    /**
     * 类
     */
    @Getter
    private T toObject;

    /**
     * 获取class
     *
     * @return class
     */
    public Class<T> toClass() {
        return modifyType;
    }

    /**
     * 添加异常
     *
     * @param throwable 异常
     */
    public void addThrowable(Throwable throwable) {
        throwableList.add(throwable);
    }
}
