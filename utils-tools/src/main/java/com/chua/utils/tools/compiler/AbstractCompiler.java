package com.chua.utils.tools.compiler;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.constant.SymbolConstant;
import com.chua.utils.tools.spi.Spi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编译器
 *
 * @author CHTK
 */
@Spi("jdk")
public abstract class AbstractCompiler {

    public static final Pattern PARENT_PATTERN = Pattern.compile("extends\\s+([a-zA-z][$_a-zA-z0-9\\.]*)");
    public static final Pattern INTERFACE_PATTERN = Pattern.compile("implements\\s+([a-zA-z][$_a-zA-z0-9\\.]*)");
    public static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([a-zA-z][$_a-zA-z0-9\\.]*)");
    public static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-z][$_a-zA-z0-9]*)");
    public static final Pattern IMPORT_PATTERN = Pattern.compile("import\\s+(.*)\\;");
    public static final Pattern FIELD_PATTERN = Pattern.compile("(private|public|protect){1}\\s+(.*)\\;");
    public static final Pattern METHOD_PATTERN = Pattern.compile("(private|public|protect){1}\\s+(([a-zA-z][$_a-zA-z0-9\\.]*)(\\<(.*?)\\>){0,})\\s+([a-zA-z][$_a-zA-z0-9\\.]*)(\\s+){0,}\\((.*)\\)(\\s+){0,}\\{((.*?)|\n){0,}\\}");

    /**
     * 編譯器
     *
     * @param code        源码
     * @param classLoader 类加载器
     * @return
     */
    public Class<?> compiler(String code, final ClassLoader classLoader) {
        code = code.trim();
        Matcher matcher = PACKAGE_PATTERN.matcher(code);
        String pkg;
        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }
        matcher = CLASS_PATTERN.matcher(code);
        String cls;
        if (matcher.find()) {
            cls = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + code);
        }
        String className = pkg != null && pkg.length() > 0 ? pkg + "." + cls : cls;
        try {
            return Class.forName(className, true, ClassHelper.getCallerClassLoader(getClass()));
        } catch (ClassNotFoundException e) {
            if (!code.endsWith(SymbolConstant.SYMBOL_RIGHT_BIG_PARENTHESES)) {
                throw new IllegalStateException("The java code not endsWith \"}\", code: \n" + code + "\n");
            }
            try {
                return doCompile(className, code);
            } catch (RuntimeException t) {
                throw t;
            } catch (Throwable t) {
                throw new IllegalStateException("Failed to compile class, cause: " + t.getMessage() + ", class: " + className + ", code: \n" + code);
            }
        }
    }

    /**
     * 编译
     *
     * @param name   名称
     * @param source 源码
     * @return Class
     * @throws Throwable Throwable
     */
    abstract protected Class<?> doCompile(String name, String source) throws Throwable;

    /**
     * 获取类名
     *
     * @param code 源码
     * @return 类名
     */
    public String getClassName(String code) {
        //获取类名
        Matcher matcher1 = CLASS_PATTERN.matcher(code);
        String classes = "";
        if (matcher1.find()) {
            return matcher1.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in \n" + code);
        }
    }

    /**
     * 获取包名
     *
     * @return
     */
    public String getPkg(String code) {
        //获取包名
        String pkg = "";
        Matcher matcher = PACKAGE_PATTERN.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
