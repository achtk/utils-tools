package com.chua.tools.example;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.plugins.manager.PluginManager;
import com.chua.utils.tools.plugins.manager.StandardPluginManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 插件例子
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/13
 */
public class PluginExample {

    public static void main(String[] args) throws IOException {
        //监听目录
        PluginManager pluginManager = new StandardPluginManager("D:\\extension");
        //测试类更新是否变化
        while (true) {
            ThreadHelper.sleepQuietly(1, TimeUnit.SECONDS);
            System.out.println(pluginManager.getPlugin(Object.class).size());
        }
    }
}
