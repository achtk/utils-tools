package com.chua.utils.tools.manager.template;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.manager.ContextManager;
import com.chua.utils.tools.manager.parser.ClassDescriptionParser;
import com.chua.utils.tools.manager.parser.ClassModifyDescriptionParser;
import com.chua.utils.tools.manager.producer.StandardContextManager;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * MBean模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class MBeanTemplate {

    private static final ContextManager CONTEXT_MANAGER = new StandardContextManager();
    private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    private static final String M_BEAN = "MBean";

    /**
     * 注销对象
     *
     * @param object 对象
     */
    public void unregister(Object object) throws Exception {
        mBeanServer.unregisterMBean(getObjectName(object));
    }

    /**
     * 监听
     *
     * @param object 对象
     */
    public void addNotificationListener(Object object, NotificationListener listener, NotificationFilter filter, Object handback) throws Exception {
        mBeanServer.addNotificationListener(getObjectName(object), listener, filter, handback);
    }

    /**
     * 创建
     *
     * @param object 对象
     */
    public ObjectInstance createMBean(Object object) throws Exception {
        return mBeanServer.createMBean(getMBeanName(object), getObjectName(object));
    }

    /**
     * 注册对象
     *
     * @param object 对象
     */
    public void register(Object object) throws Exception {
        Class<?> aClass = object.getClass();
        String name = getMBeanName(object);
        if (!aClass.getName().endsWith(M_BEAN)) {
            ClassDescriptionParser<?> descriptionParser = CONTEXT_MANAGER.createClassDescriptionParser(aClass);
            ClassModifyDescriptionParser classModifyDescriptionParser = descriptionParser.modify();
            classModifyDescriptionParser.setName(name);
            Object forObject = ClassHelper.forObject(classModifyDescriptionParser.toClass().toClass());
            if (null != forObject) {
                object = forObject;
            }
        }

        mBeanServer.registerMBean(object, getObjectName(object));
    }

    /**
     * 获取注册对象名称
     *
     * @param object 对象
     */
    public String getMBeanName(Object object) {
        Class<?> aClass = object.getClass();
        String name = aClass.getName();
        if (!name.endsWith(M_BEAN)) {
            name += M_BEAN;
        }
        return name;
    }

    /**
     * 获取注册对象名称
     *
     * @param object 对象
     */
    public ObjectName getObjectName(Object object) throws MalformedObjectNameException {
        Class<?> aClass = object.getClass();
        String name = aClass.getName();
        if (!name.endsWith(M_BEAN)) {
            name += M_BEAN;
        }
        String domain = aClass.getPackage().getName();
        return new ObjectName(domain + ":name=" + name);
    }
}
