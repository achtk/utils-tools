package com.chua.utils.tools.bean.creator;

import com.chua.utils.tools.annotations.Binder;
import com.chua.utils.tools.bean.script.BinderScript;
import com.chua.utils.tools.bean.script.ValueScript;
import com.chua.utils.tools.classes.JavassistHelper;
import com.chua.utils.tools.util.StringUtils;
import com.chua.utils.tools.util.UrlUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import javassist.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 脚本生成值
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class ScriptValueCreator implements ValueCreator {


    @Override
    public Object create(ValueScript valueScript) {
        String scriptPath = valueScript.getOperate().getString(ValueScript.VALUE);
        Class<?> targetClass = valueScript.getTargetClass();
        //脚本不存在
        if (Strings.isNullOrEmpty(scriptPath)) {
            return null;
        }

        URL url = UrlUtils.parseFileUrl(scriptPath);
        if (null == url) {
            return null;
        }
        BinderScript binderScript = getBinderScript(url);
        if (binderScript.getClerical().isEmpty()) {
            return null;
        }
        return createInterfaceImpl(targetClass, binderScript);
    }

    @Override
    public boolean isMatcher(Binder.Type type) {
        return type == Binder.Type.SCRIPT;
    }

    /**
     * 获取脚本
     *
     * @param url 地址
     * @return 脚本
     */
    private BinderScript getBinderScript(URL url) {
        BinderScript binderScript = new BinderScript();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            BinderScript.Clerical clerical = null;
            List<String> lines = new ArrayList<>();
            List<String> packages = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                //输入
                if (line.startsWith(">")) {
                    line = line.substring(1);
                    lines.clear();
                    clerical = new BinderScript.Clerical();
                    List<String> strings = Splitter.on(":").trimResults().splitToList(line);
                    if (strings.size() > 1) {
                        String methodName = strings.get(0).trim();
                        String paramTypes = strings.get(1).trim();

                        clerical.setName(methodName);
                        clerical.setParamTypes(Splitter.on(",").splitToList(paramTypes));
                    }
                    continue;
                }

                //输出
                if (line.startsWith("<") && null != clerical) {
                    line = line.substring(1);
                    clerical.setBody(Joiner.on(" ").join(lines));
                    clerical.setType(line.trim());
                    binderScript.getClerical().add(clerical);
                    binderScript.getPackages().addAll(packages);
                    lines.clear();
                    clerical = null;
                    continue;
                }

                if (line.startsWith("import")) {
                    line = line.substring("import".length()).trim();
                    int pi = line.lastIndexOf('.');
                    if (pi > 0) {
                        String pkgName = line.substring(0, pi);
                        packages.add(pkgName);
                    }
                    continue;
                }

                if (!line.endsWith(";")) {
                    line += ";";
                }
                if (null != clerical) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return binderScript;
    }

    /**
     * 创建接口脚本实现
     *
     * @param type         类型
     * @param binderScript 脚本
     * @return 对象
     */
    private Object createInterfaceImpl(Class<?> type, BinderScript binderScript) {
        String name = type.getName();

        ClassPool classPool = JavassistHelper.getClassPool();
        binderScript.getPackages().stream().forEach(classPool::importPackage);
        CtClass ctClass = null;
        try {
            ctClass = classPool.get(name);
        } catch (NotFoundException e) {
            return null;
        }
        String newName = name + "$Javassist";

        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
        CtClass ctClassImpl = classPool.makeClass(newName);
        if (type.isInterface()) {
            ctClassImpl.setInterfaces(new CtClass[]{ctClass});
        } else {
            try {
                ctClassImpl.setSuperclass(ctClass);
            } catch (CannotCompileException e) {
                return null;
            }
        }
        ctClassImpl.setModifiers(java.lang.reflect.Modifier.PUBLIC);

        List<BinderScript.Clerical> clerical = binderScript.getClerical();

        for (CtMethod declaredMethod : declaredMethods) {
            CtMethod ctMethod = null;
            try {
                ctMethod = getBinderScriptMethod(clerical, declaredMethod, ctClassImpl);
            } catch (Exception e) {
                try {
                    ctMethod = CtNewMethod.make(java.lang.reflect.Modifier.PUBLIC, declaredMethod.getReturnType(), declaredMethod.getName(), declaredMethod.getParameterTypes(), declaredMethod.getExceptionTypes(), "return null;", ctClassImpl);
                } catch (Exception e1) {
                    return null;
                }
            }
            if (null == ctMethod) {
                try {
                    ctMethod = CtNewMethod.make(java.lang.reflect.Modifier.PUBLIC, declaredMethod.getReturnType(), declaredMethod.getName(), declaredMethod.getParameterTypes(), declaredMethod.getExceptionTypes(), "return null;", ctClassImpl);
                } catch (Exception e1) {
                    return null;
                }
            }
            try {
                ctClassImpl.addMethod(ctMethod);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
        try {
            return JavassistHelper.toEntity(ctClassImpl, classPool);
        } catch (Exception e) {
            if (e.getMessage().contains("duplicate")) {
                return tryToRemoveDuplicate(ctClassImpl, classPool);
            }
            return null;
        }
    }

    /**
     * 尝试去除重名
     *
     * @param ctClassImpl 实现
     * @param classPool   类池
     * @return Object
     */
    private Object tryToRemoveDuplicate(CtClass ctClassImpl, ClassPool classPool) {
        int index = 0;
        Object result = null;
        while (true) {
            Object atomic = createAtomic(ctClassImpl, ctClassImpl.getName(), index);
            if (null == atomic) {
                break;
            }
            result = atomic;
            break;
        }
        return result;
    }

    /**
     * 尝试去除重名
     *
     * @param ctClassImpl 实现
     * @param name        名称
     * @param index       索引
     * @return Object
     */
    private Object createAtomic(CtClass ctClassImpl, String name, int index) {
        String newNewName = name + "#" + index;
        ctClassImpl.defrost();
        try {
            ctClassImpl.setName(newNewName);
            return JavassistHelper.toEntity(ctClassImpl, JavassistHelper.getClassPool());
        } catch (Exception e) {
            return createAtomic(ctClassImpl, name, ++index);
        }
    }

    /**
     * 创建方法
     *
     * @param clerical       脚本
     * @param declaredMethod 方法
     * @param ctClassImpl    实现
     * @return 方法
     */
    private CtMethod getBinderScriptMethod(List<BinderScript.Clerical> clerical, CtMethod declaredMethod, CtClass ctClassImpl) throws NotFoundException, CannotCompileException {
        for (BinderScript.Clerical clerical1 : clerical) {
            CtClass[] parameterTypes = declaredMethod.getParameterTypes();
            CtClass returnType = declaredMethod.getReturnType();
            CtClass[] exceptionTypes = declaredMethod.getExceptionTypes();
            String name = declaredMethod.getName();
            if (!name.equals(clerical1.getName())) {
                continue;
            }

            if (!returnType.getName().equals(clerical1.getType())) {
                continue;
            }

            if (!equals(parameterTypes, clerical1.getParamTypes())) {
                continue;
            }

            String body = clerical1.getBody();
            int count = StringUtils.count(body, ";");
            if (count > 1) {
                body = "{" + body + "}";
            }
            CtMethod newCtMethod = CtNewMethod.make(
                    Modifier.PUBLIC,
                    returnType,
                    declaredMethod.getName(),
                    parameterTypes,
                    exceptionTypes,
                    body,
                    ctClassImpl);

            return newCtMethod;
        }
        return null;
    }

    /**
     * 参数是否一致
     *
     * @param parameterTypes 目标参数
     * @param paramTypes     脚本参数
     * @return 一致返回true
     */
    private boolean equals(CtClass[] parameterTypes, List<String> paramTypes) {
        if (null == parameterTypes && null == paramTypes) {
            return true;
        }
        if (null == parameterTypes || null == paramTypes) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            CtClass parameterType = parameterTypes[i];
            if (!parameterType.getName().equals(paramTypes.get(i))) {
                return false;
            }
        }
        return true;
    }
}
