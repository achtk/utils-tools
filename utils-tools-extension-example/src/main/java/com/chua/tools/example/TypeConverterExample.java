package com.chua.tools.example;

import com.chua.utils.tools.function.converter.Converter;
import com.chua.utils.tools.function.converter.TypeConverter;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * 类型妆花
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class TypeConverterExample extends BaseExample {

    public static void main(String[] args) {
        TypeConverter listConverter = Converter.getTypeConverter(List.class);
        TypeConverter floatConverter = Converter.getTypeConverter(Float.class);
        TypeConverter timeConverter = Converter.getTypeConverter(Time.class);
        TypeConverter dateConverter = Converter.getTypeConverter(Date.class);
        TypeConverter stringConverter = Converter.getTypeConverter(String.class);
        // 12:00:00
        log.info("data [2020] -> {}({})", Converter.convertIfNecessary("[2020]", List.class), listConverter.getType());
        log.info("data 2020 -> {}({})", Converter.convertIfNecessary("2020", List.class), listConverter.getType());
        log.info("data 2020 -> {}({})", Converter.convertIfNecessary("2020", Float.class), floatConverter.getType());
        log.info("data 2020 -> {}({})", Converter.convertIfNecessary("2020", Time.class), Time.class);
        log.info("data 2020-10 -> {}({})", Converter.convertIfNecessary("2020-10", String.class), stringConverter.getType());
        log.info("data 2020-10 -> {}({})", Converter.convertIfNecessary("2020-10", Date.class), dateConverter.getType());
    }
}
