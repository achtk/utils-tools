package com.chua.utils.tools.example;

import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.manager.DefaultObjectContextManager;
import com.chua.utils.tools.manager.ObjectContextManager;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/27
 */
public class ObjectContextManagerExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ObjectContextManager objectContextManager = new DefaultObjectContextManager();
        objectContextManager.loadingFinished();

        Set<Class<? extends Encrypt>> types = objectContextManager.getSubTypesOf(Encrypt.class);
        System.out.println(types);
    }
}
