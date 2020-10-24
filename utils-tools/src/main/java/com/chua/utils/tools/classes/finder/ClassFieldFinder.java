package com.chua.utils.tools.classes.finder;

import com.chua.utils.tools.classes.ClassExtensionHelper;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.factory.MemberFactory;
import com.chua.utils.tools.function.Filter;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * 查找字段
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
public class ClassFieldFinder implements Supplier<MemberFactory> {
    private FinderProperties finderProperties;
    /**
     * 字段名称
     */
    @Accessors(chain = true)
    private String fieldName;
    /**
     * 字段类型
     */
    @Accessors(chain = true)
    private Class<?> fieldType;

    ClassFieldFinder() {
    }

    public ClassFieldFinder(FinderProperties finderProperties) {
        this.finderProperties = finderProperties;
    }

    @Override
    public MemberFactory get() {
        MemberFactory memberFactory = new MemberFactory();
        Filter filter = finderProperties.getFilter();

        ClassHelper.doWithFields(finderProperties.getAClass(), new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if(null != filter && !filter.matcher(field)) {
                    return;
                }

                boolean isPass = false;
                if (!Strings.isNullOrEmpty(fieldName)) {
                    isPass = field.getName().equals(fieldName);
                }

                if (null == fieldType) {
                    isPass = field.getType().isAssignableFrom(fieldType);
                }
                if (isPass) {
                    memberFactory.add(field);
                }
            }
        });
        return memberFactory;
    }
}
