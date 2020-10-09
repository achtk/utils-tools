package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.encrypt.IEncrypt;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author CH
 */
public class ClassHelperTest {

    @Test
    public void getSubTypesOf() {
        Set<Class<? extends IEncrypt>> typesOf = ClassHelper.getSubTypesOf(IEncrypt.class);
        System.out.println(typesOf);
    }
}
