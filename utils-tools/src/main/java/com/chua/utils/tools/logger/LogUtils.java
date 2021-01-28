package com.chua.utils.tools.logger;

import com.chua.utils.tools.common.DateHelper;
import com.chua.utils.tools.expression.BshExpression;
import com.chua.utils.tools.expression.Expression;
import com.chua.utils.tools.function.converter.Converter;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.util.ArrayUtils;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.SystemUtil;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 日志工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
@Slf4j
public class LogUtils {
    private static final CharSequence NULL = "null";
    private static final String PID = SystemUtil.getPid();

    /**
     * 输出日志
     *
     * @param message
     */
    public static void time(String message) {
        if (!log.getClass().isInterface()) {
            log.info("{} : {}", DateHelper.currentString(), message);
            return;
        }
        System.out.println(DateHelper.currentString() + " : " + message);
    }

    /**
     * 输出日志{@link System#out#println(String)}
     *
     * @param message 信息
     * @param args    参数
     */
    public static void println(String message, Object... args) {
        message = message.replace("{}", "%s");
        System.out.println(String.format(message, args));
    }

    /**
     * 输出日志{@link System#out#println(String)}
     *
     * @param script 信息
     * @param env    环境
     */
    public static void script(String script, Map<String, Object> env) {
        Expression expression = new BshExpression();
        Optional.ofNullable(env).orElse(Collections.emptyMap()).entrySet().stream().forEach(entry -> {
            try {
                expression.set(entry.getKey(), entry.getValue());
            } catch (Exception ignore) {
            }
        });
        try {
            Object eval = expression.eval(script.replace("'", "\""));
            System.out.println("pid:(" + PID + ")> " + script + " -> " + eval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出日志{@link System#out#println(String)}
     *
     * @param args 参数
     */
    public static void println(Object args) {
        String message = "%s";
        System.out.println(String.format(message, args));
    }

    /**
     * 输出方法信息
     *
     * @param object 对象
     * @param method 方法
     */
    public static void printInvoke(Object object, String method) {
        int index = method.indexOf("(");
        int lastIndex = method.lastIndexOf(")");
        String methodName = method.substring(0, index);
        Set<Method> methods = new HashSet<>();
        List<String> params = Splitter.on(",").splitToList(method.substring(index + 1, lastIndex));
        List<Object> realParams = new ArrayList<>();
        Class<?> aClass = object.getClass();

        ClassUtils.doWithMethods(aClass, methodItem -> {
            if (methodItem.getParameterCount() != params.size() || !methodName.equals(methodItem.getName())) {
                return;
            }


            Class<?>[] parameterTypes = methodItem.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                String s = params.get(i);
                if (NULL.equals(s)) {
                    realParams.add(null);
                    continue;
                }
                TypeConverter typeConverter = Converter.getTypeConverter(parameterType);
                Object convert = typeConverter.convert(s);
                if (null == convert) {
                    return;
                }
                realParams.add(convert);
                methods.add(methodItem);
            }
        });

        if (methods.size() == 1) {
            Object methodValue = ClassUtils.getMethodValue(object, CollectionUtils.findFirst(methods), ArrayUtils.toArray(realParams));
            log.info("{}:{}", method, methodValue);
        }
    }

    /**
     * Info日志
     *
     * @param message 信息
     */
    public static void info(String message) {
        log.info(message);
    }
}
