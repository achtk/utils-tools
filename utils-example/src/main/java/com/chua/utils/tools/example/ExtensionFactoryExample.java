package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.function.converter.definition.TypeConversionDefinition;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.spi.processor.ReflectionExtensionProcessor;
import com.chua.utils.tools.util.ClassUtils;
import org.apache.commons.codec.Encoder;
import org.testng.annotations.Test;

import java.lang.reflect.Type;

/**
 * spi工厂
 *
 * @author CH
 */
public class ExtensionFactoryExample {
    /**
     * 获取base64名称  IEncrypt子类
     *
     * @
     */
    @Test
    public void testGetExtensionLoader() throws Exception {
        ExtensionFactory.createSpi(TypeConversionDefinition.class, s -> {
            Type[] actualTypeArguments = ClassUtils.getActualTypeArguments(s, TypeConversionDefinition.class);
            return ClassUtils.getSimpleClassName(actualTypeArguments[0].getTypeName()) + "-" + ClassUtils.getSimpleClassName(actualTypeArguments[1].getTypeName());
        });

        ExtensionLoader<Encrypt> extensionLoader = ExtensionFactory.getExtensionLoader(Encrypt.class);
        System.out.println(extensionLoader);
        ExtensionLoader<Encoder> extensionLoader1 = ExtensionFactory.getExtensionLoader(Encoder.class, new ReflectionExtensionProcessor());
        System.out.println(extensionLoader1);
    }
}