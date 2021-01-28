package com.chua.utils.tools.logger;

import com.chua.utils.tools.common.DateHelper;
import com.chua.utils.tools.expression.BshExpression;
import com.chua.utils.tools.expression.Expression;
import com.chua.utils.tools.function.LogInterceptor;
import com.chua.utils.tools.function.converter.Converter;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.chua.utils.tools.function.impl.log.IpLogInterceptor;
import com.chua.utils.tools.function.impl.log.PidLogInterceptor;
import com.chua.utils.tools.function.impl.log.TimeLogInterceptor;
import com.chua.utils.tools.util.ArrayUtils;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.SystemUtil;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.*;
import java.util.logging.*;

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

    private static final Logger LOGGER = Logger.getLogger("com.chua.tools");

    static {
        LOGGER.addHandler(new ConsoleHandler());
    }

    private static final List<LogInterceptor> INTERCEPTORS = new ArrayList<LogInterceptor>() {
        {
            add(new TimeLogInterceptor());
            add(new PidLogInterceptor());
            add(new IpLogInterceptor());
        }
    };

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     */
    public static void addInterceptor(LogInterceptor interceptor) {
        CollectionUtils.add(INTERCEPTORS, interceptor);
    }

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
            info("{} -> {}", script, eval);
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
     * Warn日志{@link Logger#warning(String)}
     *
     * @param message 信息
     * @param args    参数
     */
    public static void warn(String message, Object... args) {
        LOGGER.warning(formatMessage("WARN  ", message, args));
    }

    /**
     * error日志{@link Logger#severe(String)}
     *
     * @param message 信息
     * @param args    参数
     */
    public static void error(String message, Object... args) {
        try {
            LOGGER.severe(formatMessage("ERROR", message, args));
        } finally {
            for (Object arg : args) {
                if (arg instanceof Throwable) {
                    ((Throwable) arg).printStackTrace();
                }
            }
        }
    }

    /**
     * 输出日志{@link Logger#info(String)}
     *
     * @param message 信息
     * @param args    参数
     */
    public static void info(String message, Object... args) {
        LOGGER.info(formatMessage("INFO ", message, args));
    }

    /**
     * 格式化信息
     *
     * @param message 信息
     * @param args    数据
     * @return 信息
     */
    private static String formatMessage(String level, String message, Object[] args) {
        message = message.replace("{}", "%s");
        String format = " %time [".concat(level).concat("] [pid: (%pid)] - ");

        for (LogInterceptor interceptor : INTERCEPTORS) {
            format = format.replace(interceptor.identifier(), Optional.ofNullable(interceptor.intercept(args)).orElse("").toString());
        }
        StringBuffer stringBuffer = new StringBuffer(format);
        stringBuffer.append(String.format(message, args));

        return stringBuffer.toString();
    }

    static class ConsoleHandler extends java.util.logging.ConsoleHandler {

        private OutputStream output;
        private static final int offValue = Level.OFF.intValue();
        boolean sealed = true;
        private boolean doneHeader;
        private volatile Writer writer;
        private final Permission controlPermission = new LoggingPermission("control", null);

        public ConsoleHandler() {
            sealed = false;
            setOutputStream(System.out);
            sealed = true;
        }

        @Override
        protected synchronized void setOutputStream(OutputStream out) throws SecurityException {
            if (out == null) {
                throw new NullPointerException();
            }
            flushAndClose();
            output = out;
            doneHeader = false;
            String encoding = getEncoding();
            if (encoding == null) {
                writer = new OutputStreamWriter(output);
            } else {
                try {
                    writer = new OutputStreamWriter(output, encoding);
                } catch (UnsupportedEncodingException ex) {
                    // This shouldn't happen.  The setEncoding method
                    // should have validated that the encoding is OK.
                    throw new Error("Unexpected exception " + ex);
                }
            }
        }

        @Override
        public void publish(LogRecord record) {
            newPublish(record);
            flush();
        }

        private void newPublish(LogRecord record) {
            if (!isLoggable(record)) {
                return;
            }
            String msg = record.getMessage() + "\r\n";
            try {
                if (!doneHeader) {
                    writer.write(getFormatter().getHead(this));
                    doneHeader = true;
                }
                writer.write(msg);
            } catch (Exception ex) {
                reportError(null, ex, ErrorManager.WRITE_FAILURE);
            }
        }

        private synchronized void flushAndClose() throws SecurityException {
            checkPermission();
            if (writer != null) {
                try {
                    if (!doneHeader) {
                        writer.write(getFormatter().getHead(this));
                        doneHeader = true;
                    }
                    writer.write(getFormatter().getTail(this));
                    writer.flush();
                    writer.close();
                } catch (Exception ex) {
                    reportError(null, ex, ErrorManager.CLOSE_FAILURE);
                }
                writer = null;
                output = null;
            }
        }

        void checkPermission() throws SecurityException {
            if (sealed) {
                SecurityManager sm = System.getSecurityManager();
                if (sm != null) {
                    sm.checkPermission(controlPermission);
                }
            }
        }

        @Override
        public boolean isLoggable(LogRecord record) {
            if (writer == null || record == null) {
                return false;
            }
            final int levelValue = getLevel().intValue();
            if (record.getLevel().intValue() < levelValue || levelValue == offValue) {
                return false;
            }
            final Filter filter = getFilter();
            if (filter == null) {
                return true;
            }
            return filter.isLoggable(record);
        }

        @Override
        public synchronized void flush() {
            if (writer != null) {
                try {
                    writer.flush();
                } catch (Exception ex) {
                    // We don't want to throw an exception here, but we
                    // report the exception to any registered ErrorManager.
                    reportError(null, ex, ErrorManager.FLUSH_FAILURE);
                }
            }
        }
    }


}
