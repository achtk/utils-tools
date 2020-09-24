package com.chua.utils.tools.options;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.MapHelper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

/**
 * options
 * @author CH
 * @since 1.0
 */
public interface IOptions {
    /**
     * 获取values
     * @return
     */
    public ConcurrentMap<String, Object> values();

    /**
     * 处理占位符信息
     * @return
     */
    default public ConcurrentMap<String, Object> valuesFinal() {
        return values();
    }

    default public Properties yamlValue() {
        return MapHelper.map2Yaml(values());
    }
    /**
     * Gets boolean value.
     *
     * @param primaryKey the primary key
     * @return the boolean value
     */
    default public Boolean getBooleanValue(String primaryKey) {
        Object val = valuesFinal().get(primaryKey);
        if (val == null) {
            return null;
        } else {
            return Boolean.valueOf(val.toString());
        }
    }

    /**
     * Gets boolean value.
     *
     * @param primaryKey   the primary key
     * @param secondaryKey the secondary key
     * @return the boolean value
     */
    default public boolean getBooleanValue(String primaryKey, String secondaryKey) {
        Boolean booleanValue = getBooleanValue(primaryKey);
        return null == booleanValue ? getBooleanValue(secondaryKey) : booleanValue;
    }

    /**
     * Gets int value.
     *
     * @param primaryKey the primary key
     * @return the int value
     */
    default public int getIntValue(String primaryKey) {
        Object val = valuesFinal().get(primaryKey);
        if (val == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(val.toString());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    /**
     * Gets int value.
     *
     * @param primaryKey   the primary key
     * @param secondaryKey the secondary key
     * @return the int value
     */
    default public int getIntValue(String primaryKey, String secondaryKey) {
        int intValue = getIntValue(primaryKey);
        return -1 == intValue ? getIntValue(secondaryKey) : intValue;
    }

    /**
     * Gets string value.
     *
     * @param primaryKey the primary key
     * @return the string value
     */
    default public String getStringValue(String primaryKey) {
        Object object = valuesFinal().get(primaryKey);
        if(null == object) {
            return null;
        }
        if(object instanceof String) {
            return (String) object;
        } else {
            return JsonHelper.toJson(object);
        }
    }

    /**
     * Gets string value.
     *
     * @param primaryKey   the primary key
     * @param secondaryKey the secondary key
     * @return the string value
     */
   default public String getStringValue(String primaryKey, String secondaryKey) {
       String stringValue = getStringValue(primaryKey);
       if(null == stringValue) {
           return getStringValue(secondaryKey);
       }
       return stringValue;
   }
    /**
     * Gets list value.
     *
     * @param primaryKey the primary key
     * @return the list value
     */
    default public List getListValue(String primaryKey) {
        return getListValue(primaryKey, ",");
    }
    /**
     * Gets list value.
     *
     * @param primaryKey the primary key
     * @param separator the separator key
     * @return the list value
     */
    default public List getListValue(String primaryKey, String separator) {
        Object item = valuesFinal().get(primaryKey);
        if(null == item) {
            return null;
        }

        if(item instanceof List) {
            return (List) item;
        } else if(item instanceof CharSequence){
            if(null == separator) {
                return Lists.newArrayList(item);
            }
            return Splitter.on(separator).splitToList((CharSequence) item);
        } else {
            return Collections.emptyList();
        }
    }
}
