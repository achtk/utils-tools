package com.chua.utils.tools.resource.context;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.resource.Resource;
import com.chua.utils.tools.resource.manifest.Attribute;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * 资源上下文
 * @author CH
 * @since 1.0
 */
public class ResourceContext {

    /**
     * 资源对象
     */
    private Map<String, Resource> resources = new HashMap<>();
    /**
     * 属性
     */
    private Attribute attribute = new Attribute();

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }

    /**
     * 添加资源
     * @param resource
     * @return
     */
    public ResourceContext addResource(Resource resource) {
        if(null == resource) {
            return this;
        }
        resources.put(resource.getPath(), resource);
        return this;
    }

    /**
     * 添加属性
     * @param name 名称
     * @param value 值
     */
    public void addAttribute(final String name, final Object value) {
        if(Strings.isNullOrEmpty(name)) {
            return;
        }
        attribute.put(name, value);
    }
    /**
     * 添加属性
     */
    public void addAttributes(final Map<String, Object> attributes) {
        if(null == attributes) {
            return;
        }
        attribute.putAll(attributes);
    }

    /**
     * 所有资源文件
     * @return
     */
    public Map<String, Resource> allResource() {
        return this.resources;
    }

    /**
     * 查询文件
     * @param name 文件名
     * @return
     */
    public Set<Resource> findResource(final String name) {
        if(Strings.isNullOrEmpty(name) || !BooleanHelper.hasLength(resources)) {
            return null;
        }

        Set<Resource> sets = new HashSet<>();
        resources.values().parallelStream().forEach(new Consumer<Resource>() {
            @Override
            public void accept(Resource resource) {
                if(isMatch(resource)) {
                    sets.add(resource);
                }
            }

            private boolean isMatch(Resource resource) {
                if(name.indexOf(SYMBOL_ASTERISK) == -1 && name.indexOf(SYMBOL_QUESTION) == -1) {
                    return name.equals(resource.getName());
                }

                return StringHelper.wildcardMatch(resource.getName(), name);
            }
        });
        return sets;
    }
}
