package com.chua.utils.tools.example;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.plugins.classes.ClassManager;
import com.chua.utils.tools.plugins.manager.PluginManager;
import com.chua.utils.tools.plugins.manager.StandardPluginManager;
import org.omg.CORBA.TIMEOUT;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/13
 */
public class PluginExample {

    public static void main(String[] args) throws IOException {
        PluginManager pluginManager = new StandardPluginManager("D:\\extension");
        while (true) {
            ThreadHelper.sleepQuietly(1, TimeUnit.SECONDS);
            System.out.println(pluginManager.getPlugin(Object.class).size());
        }
    }
}
