package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.ReflectionHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsHelper;
import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author CH
 */
public class ReflectionsHelperExample {

    @Test
    public void getSubTypesOf() {
        Set<Class<? extends Encrypt>> typesOf = (Set<Class<? extends Encrypt>>) ReflectionsHelper.getSubTypesOf(Encrypt.class);
        System.out.println(typesOf);
    }
}
