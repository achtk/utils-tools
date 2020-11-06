package com.chua.utils.tools.spider.interpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 页面处理
 * @author CH
 */
public abstract class AbstractPageInterpreter implements IPageInterpreter{

    private Set<String> nodes = new HashSet<>();
    private Consumer<Map<String, List<String>>> consumer;

    @Override
    public Set<String> getModifier() {
        return nodes;
    }

    @Override
    public IPageInterpreter setModifier(Set<String> modifiers) {
        nodes.clear();
        nodes.addAll(modifiers);
        return this;
    }

    @Override
    public IPageInterpreter addModifier(String modifiers) {
        nodes.add(modifiers);
        return this;
    }

    @Override
    public IPageInterpreter callback(Consumer<Map<String, List<String>>> consumer) {
        this.consumer = consumer;
        return this;
    }

    @Override
    public Consumer<Map<String, List<String>>> callback() {
        return consumer;
    }
}
