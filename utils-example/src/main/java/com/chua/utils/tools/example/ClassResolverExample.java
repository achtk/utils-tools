package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.DefaultModifiableClassResolver;
import com.chua.utils.tools.classes.ModifiableClassResolver;
import com.chua.utils.tools.classes.field.DefaultFieldResolver;
import com.chua.utils.tools.classes.field.FieldResolver;
import com.chua.utils.tools.encrypt.Base64Encrypt;
import com.chua.utils.tools.encrypt.IEncrypt;
import javassist.NotFoundException;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public class ClassResolverExample {

    public static void main(String[] args) throws Exception {
        DefaultModifiableClassResolver modifiableClassResolver = new DefaultModifiableClassResolver(Base64Encrypt.class);
        modifiableClassResolver.addField("private int i = 1;");
        Object resolver = modifiableClassResolver.toObject();

        FieldResolver fieldResolver = new DefaultFieldResolver(resolver);
        Object i = fieldResolver.getValue("i");
        System.out.println(i);

    }
}