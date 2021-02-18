package com.chua.utils.tools.table;

/**
 * 表
 *
 * @author CH* @since 2021/1/25
 * @version 1.0.0
 */
public interface Table {
    /**
     * 构构建表
     *
     * @return 表
     */
    String create();

    /**
     * 结束通知
     */
    default void notice() {

    }

    /**
     * 构建
     */
    interface Builder<T, D> {
        /**
         * 表名
         *
         * @param name 表名
         * @return this
         */
        Builder<T, D> table(String name);

        /**
         * 表名
         *
         * @param source 表名
         * @return this
         */
        Builder<T, D> source(D source);

        /**
         * 字段
         *
         * @param columnName 名称
         * @param columnType 类型
         * @return this
         */
        Builder<T, D> column(String columnName, String columnType);

        /**
         * 字段
         *
         * @param columnName 名称
         * @param columnType 类型
         * @return this
         */
        default Builder<T, D> columns(String[] columnName, String columnType) {
            for (String name : columnName) {
                column(name, columnType);
            }
            return this;
        }

        /**
         * 构建
         *
         * @return T
         */
        T build();
    }
}