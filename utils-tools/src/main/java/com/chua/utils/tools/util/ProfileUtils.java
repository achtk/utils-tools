package com.chua.utils.tools.util;

import com.chua.utils.tools.empty.EmptyOrBase;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class ProfileUtils {

    /**
     * 获取用户目录
     *
     * @return 用户目录
     */
    public static String getUserHome() {
        return EmptyOrBase.USER_HOME;
    }

    /**
     * 加载.properties|.xml文件
     *
     * @param url 配置文件
     * @return 配置项
     */
    public static Properties loadProperties(final URL url) {
        try {
            return loadProperties(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EmptyOrBase.EMPTY_PROPERTIES;
    }

    /**
     * 加载.properties|.xml文件
     *
     * @param propertiesFile 配置文件目录
     * @return 配置项
     */
    public static Properties loadProperties(final String propertiesFile) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(propertiesFile)) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 加载.properties|.xml文件
     *
     * @param inputStream 配置文件目录
     * @return 配置项
     */
    public static Properties loadProperties(final InputStream inputStream) {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(inputStreamReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 加载.json 文件
     *
     * @param url 配置文件
     * @return 配置项
     */
    public static Properties loadJson(final URL url) {
        try {
            return loadJson(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EmptyOrBase.EMPTY_PROPERTIES;
    }

    /**
     * 加载.json 文件
     *
     * @param inputStream 配置文件目录
     * @return 配置项
     */
    public static Properties loadJson(final InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            Map<String, Object> jsonMap = JsonUtils.fromJson(inputStreamReader, JsonUtils.MAP_STRING_OBJECT);
            return toProperties(jsonMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EmptyOrBase.EMPTY_PROPERTIES;
    }

    /**
     * 加载.json 文件
     *
     * @param url 配置文件
     * @return 配置项
     */
    public static Properties loadYaml(final URL url) {
        try {
            return loadYaml(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EmptyOrBase.EMPTY_PROPERTIES;
    }

    /**
     * 加载.json 文件
     *
     * @param inputStream 配置文件目录
     * @return 配置项
     */
    @SuppressWarnings("all")
    public static Properties loadYaml(final InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            Yaml yaml = new Yaml();
            Map map = yaml.loadAs(inputStreamReader, Map.class);
            return toProperties(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EmptyOrBase.EMPTY_PROPERTIES;
    }

    /**
     * Map转Properties
     *
     * @param map map
     * @return properties
     */
    @SuppressWarnings("all")
    public static Properties toProperties(final Map map) {
        if (null == map || map.isEmpty()) {
            return EmptyOrBase.EMPTY_PROPERTIES;
        }
        Map<Object, Object> objectObjectMap = map;
        Properties properties = new Properties();
        for (Map.Entry<Object, Object> entry : objectObjectMap.entrySet()) {
            if (null == entry.getKey() || null == entry.getValue()) {
                continue;
            }
            properties.put(entry.getKey(), entry.getValue());
        }

        return properties;
    }
}
