package com.chua.utils.tools.common;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.converter.TypeConverter;
import com.google.common.base.Preconditions;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * bean转化
 * <p>当前工具方法已过期建议使用{@link com.chua.utils.tools.bean.copy.BeanCopy}</p>
 *
 * @author CH
 * @see com.chua.utils.tools.bean.copy.BeanCopy
 */
@Deprecated
public class BeansHelper {

    /**
     * 实体对象赋值
     *
     * @param entity 对象
     * @param params 值
     * @return
     */
    public static void reflectionAssignment(final Object entity, final Map<String, Object> params) {
        ClassHelper.doWithLocalFields(entity.getClass(), new FieldCallback() {
            @Override
            public void doWith(Field item) throws Exception {
                String name = item.getName();
                if (!params.containsKey(name)) {
                    return;
                }
                Object value = params.get(name);
                if (null == value) {
                    return;
                }
                Class<?> type = item.getType();
                item.setAccessible(true);
                TypeConverter typeConverter = EmptyOrBase.getTypeConverter(type);
                if (null == typeConverter) {
                    try {
                        item.set(entity, value);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Object convert = typeConverter.convert(value);
                if (null == convert) {
                    return;
                }
                try {
                    item.set(entity, convert);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return;
            }
        });
    }

    /**
     * 实体对象赋值
     *
     * @param <T>
     * @param t     对象
     * @param fName key
     * @param value value
     * @return
     */
    public static <T> T setProperty(final T t, final String fName, final Object value) {
        if (StringHelper.isNotBlank(fName)) {
            BeanMap beanMap = BeanMap.create(t);
            beanMap.put(fName, value);
            return (T) beanMap.getBean();
        }
        return t;
    }

    /**
     * 实体对象复制
     *
     * @param from 对象
     * @param to   对象
     * @return
     */
    public static <E, E1> E1 copierIfEffective(final E from, E1 to) {
        Preconditions.checkArgument(null != from);
        Preconditions.checkArgument(null != to);
        BeanMap toBeanMap = BeanMap.create(to);
        BeanMap fromBeanMap = BeanMap.create(from);
        AtomicBoolean fail = new AtomicBoolean(false);
        fromBeanMap.forEach(new BiConsumer() {
            @Override
            public void accept(Object o, Object o2) {
                if (o2 == null) {
                    return;
                }
                toBeanMap.put(o, o2);
                if (toBeanMap.get(o) != o2) {
                    fail.set(true);
                }
            }
        });
        if (fail.get()) {
            ClassHelper.doWithFields(to.getClass(), new FieldCallback() {
                @Override
                public void doWith(Field item) throws Exception {
                    if (!fromBeanMap.containsKey(item.getName())) {
                        return;
                    }
                    Class<?> type = item.getType();
                    Object o = fromBeanMap.get(item.getName());
                    if (!type.isAssignableFrom(o.getClass())) {
                        return;
                    }
                    item.setAccessible(true);
                    item.set(to, o);
                }
            });
        }
        return (E1) toBeanMap.getBean();
    }

    /**
     * 实体对象复制
     *
     * @param from 对象
     * @param to   对象
     * @return
     */
    public static <E, E1> E1 copierIfEffective(final E from, E1 to, final BiFunction biFunction) {
        Preconditions.checkArgument(null != from);
        Preconditions.checkArgument(null != to);
        BeanMap toBeanMap = BeanMap.create(to);
        BeanMap fromBeanMap = BeanMap.create(from);
        fromBeanMap.forEach(new BiConsumer() {
            @Override
            public void accept(Object o, Object o2) {
                Object apply = biFunction.apply(o, o2);
                toBeanMap.put(o, apply);
            }
        });
        return (E1) toBeanMap.getBean();
    }

    /**
     * 实体对象复制
     *
     * @param from 对象
     * @param to   对象
     * @return
     */
    public static <E, E1> E1 copierIfEffective(final E from, final Class<E1> to) {
        Preconditions.checkArgument(null != from);
        Preconditions.checkArgument(null != to);

        try {
            E1 e1 = to.newInstance();
            return copierIfEffective(from, e1);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 实体对象复制
     *
     * @param <T>
     * @param t      对象
     * @param params 对象
     * @return
     */
    public static <T> T copierIfEffective(final T t, final Map<String, Object> params) {
        BeanMap beanMap = BeanMap.create(t);
        beanMap.putAll(params);
        return (T) beanMap.getBean();
    }

    /**
     * 实体对象复制
     *
     * @param from 对象
     * @param to   对象
     * @return
     */
    public static <E, E1> E1 copier(final E from, final E1 to) {
        Preconditions.checkArgument(null != from);
        Preconditions.checkArgument(null != to);

        final BeanCopier beanCopier = BeanCopier.create(from.getClass(), to.getClass(), false);
        beanCopier.copy(from, to, null);
        return to;
    }

    /**
     * 实体对象复制
     *
     * @param from 对象
     * @param to   对象
     * @return
     */
    public static <E, E1> E1 copier(final E from, final Class<E1> to) {
        Preconditions.checkArgument(null != from);
        Preconditions.checkArgument(null != to);

        try {
            E1 e1 = to.newInstance();
            return copier(from, e1);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 实体对象复制
     *
     * @param from 对象
     * @param to   对象
     * @return
     */
    public static <E, E1> List<E1> copier(final List<E> from, final Class<E1> to) {
        Preconditions.checkArgument(null != from);
        Preconditions.checkArgument(null != to);

        List<E1> result = new ArrayList<>();
        Class<?> aClass = to.getClass();
        for (E e : from) {
            try {
                E1 instance = (E1) aClass.newInstance();
                copier(e, instance);
                result.add(instance);
            } catch (Throwable ex) {
                continue;
            }
        }

        return result;
    }

    /**
     * object 2 byte[]
     *
     * @param obj
     * @return
     */
    public static byte[] convert(final Object obj) {
        Preconditions.checkArgument(null != obj);

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            //读取对象并转换成二进制数据
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * object 2 byte[]
     *
     * @param obj
     * @return
     */
    public static Object convert(final byte[] obj) {
        Preconditions.checkArgument(null != obj);

        try (ObjectInputStream oos = new ObjectInputStream(new ByteArrayInputStream(obj))) {
            //读取对象并转换成二进制数据
            return oos.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拷贝集合到对象
     *
     * @param object     对象
     * @param properties 集合
     * @return
     */
    public static <T> T copierProperties2Object(T object, final Properties properties) {
        if (!BooleanHelper.hasLength(properties) || null == object) {
            return object;
        }

        BeanMap beanMap = BeanMap.create(object);
        beanMap.putAll(properties);
        return (T) beanMap.getBean();
    }

    /**
     * 集合转对象
     *
     * @param t      类
     * @param source 数据
     * @param <T>
     * @return
     */
    public static <T> T setProperty(T t, Map<String, Object> source) {
        BeanMap beanMap = BeanMap.create(t);
        beanMap.putAll(source);
        return (T) beanMap.getBean();
    }

    /**
     * 集合转对象
     *
     * @param tClass 类
     * @param source 数据
     * @param <T>
     * @return
     */
    public static <T> T setProperty(Class<T> tClass, List<String> source) {
        if (null == tClass) {
            return null;
        }
        if (!BooleanHelper.hasLength(source)) {
            return ClassHelper.safeForObject(tClass);
        }
        BeanMap beanMap = BeanMap.create(ClassHelper.safeForObject(tClass));
        Field[] fields = tClass.getDeclaredFields();

        int max = Math.min(fields.length, source.size());
        for (int i = 0; i < max; i++) {
            Field field = fields[i];
            beanMap.put(field.getName(), source.get(i));
        }
        return (T) beanMap.getBean();
    }

    /**
     * 获取属性
     *
     * @param obj 对象
     * @return
     */
    public static Map<String, Object> getAttributes(Object obj) {
        BeanMap beanMap = BeanMap.create(obj);
        Map<String, Object> result = new HashMap<>(beanMap.size());
        result.putAll(beanMap);
        return result;
    }

    /**
     * 获取属性
     *
     * @param obj 对象
     * @return
     */
    public static Map<String, Object> toMap(Object obj) {
        return getAttributes(obj);
    }

    /**
     * 获取类型转化器
     *
     * @param type 类型
     * @return 类型转化器
     * @see EmptyOrBase#getTypeConverter(Class)
     */
    public static TypeConverter getTypeConverter(Class<?> type) {
        return null == type ? null : EmptyOrBase.getTypeConverter(type);
    }
}
