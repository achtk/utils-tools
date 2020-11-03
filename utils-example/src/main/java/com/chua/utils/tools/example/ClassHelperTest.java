package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.encrypt.Encrypt;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author CH
 */
public class ClassHelperTest {

    @Test
    public void getSubTypesOf() {
        Set<Class<? extends Encrypt>> typesOf = (Set<Class<? extends Encrypt>>) ClassHelper.getSubTypesOf(Encrypt.class);
        System.out.println(typesOf);
    }
}
