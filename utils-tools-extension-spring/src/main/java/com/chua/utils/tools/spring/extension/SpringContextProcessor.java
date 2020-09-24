package com.chua.utils.tools.spring.extension;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.processor.IExtensionProcessor;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * spring上下文实现扩展接口
 * @author CH
 * @since 1.0
 */
@Component
public class SpringContextProcessor<T> implements IExtensionProcessor<T> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void init(SpiConfig spiConfig) {

    }

    @Override
    public Multimap<String, ExtensionClass<T>> analyze(Class<T> service, ClassLoader classLoader) {
        Map<String, T> stringTMap = applicationContext.getBeansOfType(service);
        if(!BooleanHelper.hasLength(stringTMap)) {
            return null;
        }
        Multimap<String, ExtensionClass<T>> result = MapHelper.newHashMultimap();

        for (Map.Entry<String, T> entry : stringTMap.entrySet()) {
            String key = entry.getKey();
            T value = entry.getValue();
            ExtensionClass<T> extensionClass = new ExtensionClass<>();
            extensionClass.setClazz(service);
            //解析@Spi注解
            analysisSpi(value, extensionClass);
            extensionClass.setName(key);
            //单例模式直接实例化
            if(extensionClass.isSingle()) {
                extensionClass.setObj(value);
            }
            //赋值
            result.put(extensionClass.getName(), extensionClass);
        }

        return result;
    }

    @Override
    public void refresh() {

    }
}
