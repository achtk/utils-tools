package com.chua.utils.tools.common;

/**
 * 类型工具类
 * @author CH
 */
public class TypeHelper {
    /**
     * 获取int
     * @param objects
     * @return
     */
    public static int toInt(Object objects, int defaultValue) {
        if(null == objects) {
            return defaultValue;
        }

        if(objects instanceof Integer) {
            return (int) objects;
        } else if(objects instanceof Long) {
            return ((Long) objects).intValue();
        } else if(objects instanceof Float) {
            return ((Float) objects).intValue();
        } else if(objects instanceof Double) {
            return ((Double) objects).intValue();
        } else if(objects instanceof Short) {
            return ((Short) objects).intValue();
        } else if(objects instanceof Byte) {
            return ((Byte) objects).intValue();
        } else if(objects instanceof Boolean) {
            return (Boolean)objects ? 0 : 1;
        }

        return defaultValue;
    }

    /**
     * 获取 long
     * @param objects
     * @return
     */
    public static long toLong(Object objects, long defaultValue) {
        if(null == objects) {
            return defaultValue;
        }

        if(objects instanceof Integer) {
            return ((Integer) objects).longValue();
        } else if(objects instanceof Long) {
            return ((Long) objects).longValue();
        } else if(objects instanceof Float) {
            return ((Float) objects).longValue();
        } else if(objects instanceof Double) {
            return ((Double) objects).longValue();
        } else if(objects instanceof Short) {
            return ((Short) objects).longValue();
        } else if(objects instanceof Byte) {
            return ((Byte) objects).longValue();
        } else if(objects instanceof Boolean) {
            return (Boolean)objects ? 0L : 1L;
        }

        return defaultValue;
    }

    /**
     * 获取 float
     * @param objects
     * @return
     */
    public static float toFloat(Object objects, float defaultValue) {
        if(null == objects) {
            return defaultValue;
        }

        if(objects instanceof Integer) {
            return ((Integer) objects).floatValue();
        } else if(objects instanceof Long) {
            return ((Long) objects).floatValue();
        } else if(objects instanceof Float) {
            return ((Float) objects).floatValue();
        } else if(objects instanceof Double) {
            return ((Double) objects).floatValue();
        } else if(objects instanceof Short) {
            return ((Short) objects).floatValue();
        } else if(objects instanceof Byte) {
            return ((Byte) objects).floatValue();
        } else if(objects instanceof Boolean) {
            return (Boolean)objects ? 0f : 1f;
        }

        return defaultValue;
    }

    /**
     * 获取 float
     * @param objects
     * @return
     */
    public static double toDouble(Object objects, double defaultValue) {
        if(null == objects) {
            return defaultValue;
        }

        if(objects instanceof Integer) {
            return ((Integer) objects).doubleValue();
        } else if(objects instanceof Long) {
            return ((Long) objects).doubleValue();
        } else if(objects instanceof Float) {
            return ((Float) objects).doubleValue();
        } else if(objects instanceof Double) {
            return ((Double) objects).doubleValue();
        } else if(objects instanceof Short) {
            return ((Short) objects).doubleValue();
        } else if(objects instanceof Byte) {
            return ((Byte) objects).doubleValue();
        } else if(objects instanceof Boolean) {
            return (Boolean)objects ? 0D : 1D;
        }

        return defaultValue;
    }
}
