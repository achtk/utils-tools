package com.chua.utils.netx.rpc.adaptor;

import com.chua.utils.tools.classes.ClassHelper;
import javassist.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * rmi接口代理
 * @author CH
 * @version 1.0.0
 * @className RmiAdaptor
 * @since 2020/8/1 2:02
 */
public class RmiAdaptor implements Adaptor {

    private static final String SUFFIX = "$rmi";

    @Override
    public <T> T adaptor(T implObj, Class<?> clazz) throws Throwable {
        if(!clazz.isAssignableFrom(implObj.getClass()) || !clazz.isInterface()) {
            throw new Exception(implObj.getClass().getName() + " is not the realization of interface " + clazz.getName());
        }
        Class<?> aClass = implObj.getClass();
        clazz = analysisInterfaces(clazz);
        if(!UnicastRemoteObject.class.isAssignableFrom(aClass)) {
            ClassPool classPool = ClassPool.getDefault();
            try {
                //创建类
                CtClass ctClass = classPool.get(aClass.getName());
                ctClass.setName(aClass.getName() + SUFFIX);
                //添加接口(implements)
                CtClass interf = classPool.getCtClass(clazz.getName());
                ctClass.setInterfaces(new CtClass[]{interf});
                //设置构造
                CtClass exception = classPool.getCtClass(RemoteException.class.getName());
                CtConstructor constructor = new CtConstructor(null, ctClass);
                constructor.setExceptionTypes(new CtClass[]{exception});
                try {
                    ctClass.addConstructor(constructor);
                } catch (CannotCompileException e) {
                }
                //添加父类(extends)
                ctClass.setSuperclass(classPool.get(UnicastRemoteObject.class.getName()));

                ClassLoader classLoader = ClassHelper.getCallerClassLoader(getClass());
                return (T) ClassHelper.forObject(ctClass.toClass(classLoader, this.getClass().getProtectionDomain()));
            } catch (Throwable e) {
                return null;
            }
        }

        return implObj;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public Class<?> analysisInterfaces(Class<?> clazz) throws Exception {
        if(!clazz.isInterface()) {
            throw new Exception(clazz.getName() + " is not interface ");
        }
        if(!Remote.class.isAssignableFrom(clazz)) {
            ClassPool classPool = ClassPool.getDefault();
            try {
                //创建类
                CtClass ctClass = classPool.get(clazz.getName());
                ctClass.setName(clazz.getName() + SUFFIX);
                //添加父类
                CtClass interfaceRemote = classPool.get(Remote.class.getName());
                //重新继承自身
                CtClass interfaceSelf = classPool.get(clazz.getName());
                ctClass.setSuperclass(interfaceRemote);
                ctClass.setSuperclass(interfaceSelf);
                //获取所有方法
                CtMethod[] methods = ctClass.getDeclaredMethods();
                CtClass excptionRemote = classPool.get(RemoteException.class.getName());
                for (CtMethod method : methods) {
                    //每个方法添加异常
                    method.setExceptionTypes(new CtClass[]{excptionRemote});
                }
                //获取当前类类加载器
                ClassLoader loader = ClassHelper.getCallerClassLoader(getClass());
                return ctClass.toClass(loader, this.getClass().getProtectionDomain());
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

        return clazz;
    }
}
