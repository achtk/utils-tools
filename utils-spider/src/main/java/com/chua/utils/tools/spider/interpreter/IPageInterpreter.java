package com.chua.utils.tools.spider.interpreter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 页面解释器
 * @author CH
 */
public interface IPageInterpreter {
    /**
     * 修饰符
     * @return
     */
    Set<String> getModifier();

    /**
     * 修饰符
     * @param modifiers 修饰符
     * @return
     */
    IPageInterpreter setModifier(Set<String> modifiers);

    /**
     * 修饰符
     * @param modifiers 修饰符
     * @return
     */
    IPageInterpreter addModifier(String modifiers);

    /**
     * 数据回调
     * @return
     */
    Consumer<Map<String, List<String>>> callback();

    /**
     * 数据回调
     * @param consumer
     */
    IPageInterpreter callback(Consumer<Map<String, List<String>>> consumer);

}
