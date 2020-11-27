package com.chua.utils.tools.example;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.function.converter.TypeConverter;

import java.util.Date;
import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class TypeConverterExample {

    public static void main(String[] args) {
        TypeConverter typeConverter = EmptyOrBase.getTypeConverter(List.class);
        // 12:00:00
        System.out.println(typeConverter.convert("[2020]"));
    }
}
