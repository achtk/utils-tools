package com.chua.utils.tools.example;

import com.chua.utils.tools.encrypt.IEncrypt;
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

        Set<Class<? extends IEncrypt>> types = objectContextManager.getSubTypesOf(IEncrypt.class);
        System.out.println(types);
    }
}
