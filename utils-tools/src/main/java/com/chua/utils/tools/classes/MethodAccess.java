package com.chua.utils.tools.classes;

/**
 * 方法访问
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public interface MethodAccess<T> {
    int methodIndex(String name, Class<?>... parameterTypes);

    Object invoke(T obj, int methodIndex, Object... args);

    Object call(T obj, int methodIndex);

    Object call(T obj, int methodIndex, Object arg0);

    Object call(T obj, int methodIndex, Object arg0, Object arg1);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18, Object arg19);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20);

    Object call(T obj, int methodIndex, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16, Object arg17, Object arg18, Object arg19, Object arg20, Object arg21);
}
