package com.chua.utils.tools.classes;

/**
 * 方法访问
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public interface MethodAccess<T> {
    /**
     * 方法索引
     *
     * @param name           名称
     * @param parameterTypes 参数
     * @return 索引
     */
    int methodIndex(String name, Class<?>... parameterTypes);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param args        参数
     * @return 结果
     */
    Object invoke(T obj, int methodIndex, Object... args);

    /**
     * 执行方法
     *
     * @param obj            对象
     * @param name           名称
     * @param parameterTypes 方法类型
     * @param args           参数
     * @return 结果
     */
    default Object invoke(T obj, String name, Class<?>[] parameterTypes, Object... args) {
        return invoke(obj, methodIndex(name, parameterTypes), args);
    }

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @return 结果
     */
    Object call(T obj, int methodIndex);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @param arg16       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @param arg16       参数
     * @param arg17       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @param arg16       参数
     * @param arg17       参数
     * @param arg18       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @param arg16       参数
     * @param arg17       参数
     * @param arg18       参数
     * @param arg19       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18, Object arg19);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @param arg16       参数
     * @param arg17       参数
     * @param arg18       参数
     * @param arg19       参数
     * @param arg20       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20);

    /**
     * 执行方法
     *
     * @param obj         对象
     * @param methodIndex 方法索引
     * @param arg0        参数
     * @param arg1        参数
     * @param arg2        参数
     * @param arg3        参数
     * @param arg4        参数
     * @param arg5        参数
     * @param arg6        参数
     * @param arg7        参数
     * @param arg8        参数
     * @param arg9        参数
     * @param arg10       参数
     * @param arg11       参数
     * @param arg12       参数
     * @param arg13       参数
     * @param arg14       参数
     * @param arg15       参数
     * @param arg16       参数
     * @param arg17       参数
     * @param arg18       参数
     * @param arg19       参数
     * @param arg20       参数
     * @param arg21       参数
     * @return 结果
     */
    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20, Object arg21);
}
