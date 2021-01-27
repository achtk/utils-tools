package com.chua.utils.tools.script;

import com.chua.utils.tools.util.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * 脚本
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/11
 */
public interface Script {
    /**
     * 执行脚本
     *
     * @param script 脚本
     * @param params 参数
     * @return 结果
     * @throws Exception Exception
     */
    Object eval(String script, Map<String, Object> params) throws Exception;

    /**
     * 编译脚本
     *
     * @param script      脚本
     * @param classLoader 类加载器
     * @return 结果
     * @throws Exception Exception
     */
    Class<?> compiler(String script, ClassLoader classLoader) throws Exception;

    /**
     * 编译脚本
     *
     * @param script 脚本
     * @return 结果
     * @throws Exception Exception
     */
    default Class<?> compiler(String script) throws Exception {
        return compiler(script, this.getClass().getClassLoader());
    }

    /**
     * 编译脚本
     *
     * @param script      脚本
     * @param classLoader 类加载器
     * @return 结果
     * @throws Exception Exception
     */
    Class<?> compiler(File script, ClassLoader classLoader) throws Exception;

    /**
     * 编译脚本
     *
     * @param script 脚本
     * @return 结果
     * @throws Exception Exception
     */
    default Class<?> compiler(File script) throws Exception {
        return compiler(script, this.getClass().getClassLoader());
    }

    /**
     * 编译脚本
     *
     * @param script      脚本
     * @param classLoader 类加载器
     * @return 结果
     * @throws Exception Exception
     */
    Class<?> compiler(URL script, ClassLoader classLoader) throws Exception;

    /**
     * 编译脚本
     *
     * @param script 脚本
     * @return 结果
     * @throws Exception Exception
     */
    default Class<?> compiler(URL script) throws Exception {
        return compiler(script, this.getClass().getClassLoader());
    }

    /**
     * 执行方法
     *
     * @param script     脚本
     * @param methodName 方法名称
     * @param params     参数
     * @return 方法值
     * @throws Exception Exception
     */
    default Object invokeMethod(String script, String methodName, Object[] params) throws Exception {
        Class<?> compiler = compiler(script);
        if (null == compiler) {
            throw new NullPointerException("the pre compilation failed!");
        }

        Object forObject = ClassUtils.forObject(compiler);
        if (null == forObject) {
            throw new NullPointerException("Failed to instantiate object!");
        }
        return ClassUtils.getMethodValue(forObject, methodName, params);
    }
}
