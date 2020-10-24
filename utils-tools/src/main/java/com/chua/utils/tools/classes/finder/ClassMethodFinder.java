package com.chua.utils.tools.classes.finder;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.callback.MethodCallback;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.factory.MemberFactory;
import com.chua.utils.tools.function.Filter;
import com.google.common.base.Strings;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 查找字段
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class ClassMethodFinder implements Supplier<MemberFactory> {

    private FinderProperties finderProperties;
    /**
     * 方法名称
     */
    @Accessors(chain = true)
    private String methodName;
    /**
     * 方法返回
     */
    @Accessors(chain = true)
    private Class<?> methodType;

    private ClassMethodFinder() {
    }

    public ClassMethodFinder(FinderProperties finderProperties) {
        this.finderProperties = finderProperties;
    }


    @Override
    public MemberFactory get() {
        Filter filter = finderProperties.getFilter();

        MemberFactory memberFactory = new MemberFactory();
        ClassHelper.doWithMethods(finderProperties.getAClass(), new MethodCallback() {

            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (null != filter && !filter.matcher(method)) {
                    return;
                }

                boolean isPass = false;
                if (!Strings.isNullOrEmpty(methodName)) {
                    isPass = method.getName().equals(methodName);
                }

                if (null != methodType) {
                    isPass = method.getReturnType().equals(methodType);
                }

                if (finderProperties.isStatic()) {
                    isPass = Modifier.isStatic(method.getModifiers());
                }

                if (isPass) {
                    memberFactory.add(method);
                }
            }
        });
        return memberFactory;
    }
}
