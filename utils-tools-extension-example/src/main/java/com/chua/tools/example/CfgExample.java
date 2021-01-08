package com.chua.tools.example;

import com.chua.utils.tools.cfg.CfgOptions;
import com.chua.utils.tools.cfg.Configurable;
import com.chua.utils.tools.logger.LogUtils;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/7
 */
public class CfgExample implements Configurable {

    public static void main(String[] args) {
        //测试代码解析配置文件
        testCodeParsingConfigurationFile();
        //测试接口解析配置文件
        testInterfaceParsingConfigurationFile();
    }

    /**
     * 测试接口解析配置文件
     */
    private static void testInterfaceParsingConfigurationFile() {
        CfgExample cfgExample = new CfgExample();
    }

    /**
     * 测试代码解析配置文件
     */
    private static void testCodeParsingConfigurationFile() {
        CfgOptions cfgOptions = CfgOptions.analysis("spi-config-default");
        LogUtils.println(cfgOptions.asMap());
    }

    @Override
    public String configurationFile() {
        return "spi-config-default";
    }
}
