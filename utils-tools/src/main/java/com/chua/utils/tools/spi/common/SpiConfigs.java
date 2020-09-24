package com.chua.utils.tools.spi.common;

import com.chua.utils.tools.cfg.CfgConfig;
import com.chua.utils.tools.cfg.CfgOptions;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.IOHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.options.IOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.chua.utils.tools.constant.StringConstant.SPI_CONFIG;
import static com.chua.utils.tools.constant.StringConstant.SPI_CONFIG_DEFAULT;
import static com.chua.utils.tools.spi.options.SpiOptions.*;
import static com.google.common.base.Charsets.UTF_8;

/**
 * 配置加载器和操作入口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:46
 */
public class SpiConfigs implements IOptions {

    /**
     * 全部配置
     */
    private final static ConcurrentMap<String, Object> CFG = new ConcurrentHashMap<>();
    
    public static IOptions options = null;

    static {
        init();
    }

    private static void init() {
        CfgConfig cfgConfig = new CfgConfig();
        cfgConfig.setMaster(SPI_CONFIG_DEFAULT);
        cfgConfig.setOrder(SPI_CFG_ORDER);
        cfgConfig.setSlaverName(SPI_CONFIG);
        cfgConfig.addSlaver("extension/", "META-INF/extension/");

        CFG.putAll(CfgOptions.initialCfg(cfgConfig));
        options = new IOptions() {
            @Override
            public ConcurrentMap<String, Object> values() {
                return CFG;
            }
        };
    }

    /**
     * extension方式文件转为meta信息
     */
    public static void transformFromResource2Meta(final String path) {
        List<String> listValue = options.getListValue(EXTENSION_LOAD_PATH);
        String metaName = options.getStringValue(EXTENSION_META_NAME);
        for (String s : listValue) {
            File temps = new File(path);
            File[] list = temps.listFiles();
            for (File s1 : list) {
                String path1 = s1.getAbsolutePath() +  "/src/main/resources/" + s;
                if(new File(path1).exists()) {
                    try {
                        makeMetaFile(path1, s, metaName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     *  @param resources
     * @param s
     * @param metaName
     */
    private static void makeMetaFile(String resources, String s, String metaName) throws IOException {
        File[] items = new File(resources).listFiles();
        String file = resources + "/" + metaName;

        Map<String, List<Map<String, Object>>> meta = new HashMap<>(items.length);
        for (File item : items) {

            String name = item.getName();
            if(name.equalsIgnoreCase(metaName)) {
                continue;
            }

            List<String> strings = IOHelper.readLines(new FileInputStream(item), UTF_8);
            List<Map<String, Object>> infos = new ArrayList<>();

            meta.put(name.replace(s + "/", ""), infos);
            for (String string : strings) {
                Map<String, Object> map = new HashMap<>(3);
                if(string.indexOf("=") > -1) {
                    String[] split = string.split("=");
                    String nameAndOrder = split[0];
                    if(nameAndOrder.indexOf("@") > -1) {
                        map.put("name", nameAndOrder.split("@")[0]);
                        map.put("order", nameAndOrder.split("@")[1]);
                    } else {
                        map.put("name", nameAndOrder);
                    }
                    map.put("subclass", split[1]);
                } else {
                    map.put("name", string);
                    map.put("subclass", string);
                }

                infos.add(map);
            }

        }

        FileHelper.write(new File(file), JsonHelper.toFormatJson(meta), "UTF-8", false);

    }

    @Override
    public ConcurrentMap<String, Object> values() {
        return CFG;
    }
}
