package com.chua.utils.tools.proxy;

import com.chua.utils.tools.common.ObjectHelper;
import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.function.MethodIntercept;
import com.chua.utils.tools.loader.BalancerLoader;
import com.chua.utils.tools.loader.RotationBalancerLoader;
import com.chua.utils.tools.mapper.ProxyMapper;
import com.google.common.base.Joiner;
import javassist.*;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * javassist 代理
 * @author CH
 */
@AllArgsConstructor
public class JavassistProxyAgent<T> implements ProxyAgent<T> {
    private ProxyMapper proxyMapper;
    private BalancerLoader balancerLoader = new RotationBalancerLoader();
    /**
     * 生成的代理对象名称前缀
     */
    private static final String PROXYSUFFIX = "Impl$Proxy";

    private static final Pattern ZX_PATTERN = Pattern.compile("byte|short|char|int");


    @Override
    public T newProxy(Class<T> tClass) {
        T proxyObject = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(tClass.getName() + PROXYSUFFIX);
            //创建代理类对象
            //设置代理类的接口
            //获取代理对象的接口类
            CtClass interf = pool.getCtClass(tClass.getName());
            CtClass[] interfaces = new CtClass[]{interf};
            ctClass.setInterfaces(interfaces);
            //代理类的所有方法
            CtMethod[] methods = interf.getDeclaredMethods();
            for (CtMethod method : methods) {
                ctClass.addMethod(makeMethod(method, ctClass));
            }
            Class aClass = ctClass.toClass();
            proxyObject = (T) aClass.newInstance();
        } catch (NotFoundException | CannotCompileException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return proxyObject;
    }

    /**
     * 创建方法
     * @param method 方法
     * @param ctClass
     * @return
     */
    private CtMethod makeMethod(CtMethod method, CtClass ctClass) throws NotFoundException, CannotCompileException {
        if(null == proxyMapper) {
            return renderMethod(method, ctClass);
        }
        String methodName = method.getName();
        if(!proxyMapper.hasName(methodName)) {
            return renderMethod(method, ctClass);
        }

        MethodIntercept methodIntercept = proxyMapper.tryToGetProxy(methodName, balancerLoader);
        String methodString = "";
        if(null != methodIntercept) {
            try {
                methodString = ObjectHelper.toString(methodIntercept.invoke(null, null, null));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        if (methodString.indexOf("{") > -1) {
            return CtMethod.make(methodString, ctClass);
        } else {
            return renderBody(method, methodString, ctClass);
        }
    }

    @Override
    public Object invoker(ProxyMapper proxyMapper, Object obj, Method method, Object[] args, Object... proxy) throws Throwable {
        return null;
    }


    /**
     * @param method
     * @param source
     * @param ctClass
     * @return
     * @throws CannotCompileException
     * @throws NotFoundException
     */
    private CtMethod renderBody(CtMethod method, String source, CtClass ctClass) throws CannotCompileException, NotFoundException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getModifier(method));
        stringBuffer.append(getFinalStatic(method));
        stringBuffer.append(getRetureType(method));
        stringBuffer.append(method.getName());
        stringBuffer.append("(");
        stringBuffer.append(getParams(method));
        stringBuffer.append(") ");
        stringBuffer.append("{");
        stringBuffer.append(source);
        stringBuffer.append("}");

        return CtMethod.make(stringBuffer.toString(), ctClass);
    }

    /**
     * 渲染干哈
     *
     * @param method
     * @param ctClass
     * @return
     */
    private CtMethod renderMethod(CtMethod method, CtClass ctClass) throws NotFoundException, CannotCompileException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getModifier(method));
        stringBuffer.append(getFinalStatic(method));
        stringBuffer.append(getRetureType(method));
        stringBuffer.append(method.getName());
        stringBuffer.append("(");
        stringBuffer.append(getParams(method));
        stringBuffer.append(") ");
        stringBuffer.append("{");
        stringBuffer.append(getBody(method));
        stringBuffer.append("}");

        return CtMethod.make(stringBuffer.toString(), ctClass);
    }

    /**
     * 内容
     *
     * @param method
     * @return
     */
    private String getBody(CtMethod method) throws NotFoundException {
        String simpleName = method.getReturnType().getSimpleName();
        if (ZX_PATTERN.matcher(simpleName).find()) {
            return "return -1;";
        }
        if (StringConstant.CLASS_FLOAT.equals(simpleName)) {
            return "return -1f;";
        }

        if (StringConstant.CLASS_DOUBLE.equals(simpleName)) {
            return "return -1D;";
        }

        if (StringConstant.CLASS_LONG.equals(simpleName)) {
            return "return -1L;";
        }

        if (StringConstant.CLASS_BOOLEAN.equals(simpleName)) {
            return "return false;";
        }

        String lowerCase = method.getReturnType().getSimpleName().toLowerCase();
        return StringConstant.CLASS_VOID.equals(lowerCase) ? "" : "return null;";
    }

    /**
     * 请求参数
     *
     * @param method
     * @return
     */
    private String getParams(CtMethod method) throws NotFoundException {
        CtClass[] types = method.getParameterTypes();
        int size = types.length;
        List<String> params = new ArrayList<>(size);

        int count = 0;
        for (CtClass aClass : types) {
            params.add(aClass.getName() + " _" + count++);
        }
        return Joiner.on(",").join(params);
    }

    /**
     * @param method
     * @return
     */
    private String getFinalStatic(CtMethod method) {
        String finalStatic = "";
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            finalStatic += " static ";
        }
        if (Modifier.isFinal(modifiers)) {
            finalStatic += " final ";
        }
        if (Modifier.isSynchronized(modifiers)) {
            finalStatic += " synchronized ";
        }

        return finalStatic;
    }

    /**
     * 返回类型
     *
     * @param method
     * @return
     */
    private String getRetureType(CtMethod method) throws NotFoundException {
        return method.getReturnType().getName() + " ";
    }

    /**
     * 返回 public | private|protected
     *
     * @param method
     * @return
     */
    private String getModifier(CtMethod method) {
        int modifiers = method.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            return "private ";
        }
        if (Modifier.isPublic(modifiers)) {
            return "public ";
        }
        return "protected ";
    }
}
