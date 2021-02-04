package com.chua.utils.tools.example;

import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.function.converter.Converter;
import com.chua.utils.tools.function.converter.TypeConverter;

import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class TypeConverterExample {

    public static void main(String[] args) {
        Converter.addTypeConverter(new TDemoInfoTypeConverter());

        TypeConverter typeConverter = Converter.getTypeConverter(List.class);
        // 12:00:00
        System.out.println(typeConverter.convert("[2020]"));
    }

    static class TDemoInfoTypeConverter implements TypeConverter<TDemoInfo> {
        @Override
        public TDemoInfo convert(Object value) {
            return null;
        }

        @Override
        public Class<TDemoInfo> getType() {
            return TDemoInfo.class;
        }
    }
}
