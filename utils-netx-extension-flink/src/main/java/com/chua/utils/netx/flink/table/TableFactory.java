package com.chua.utils.netx.flink.table;

/**
 * 表工厂
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/25
 */
public interface TableFactory {
    /**
     * 创建redis表
     *
     * @return redis表
     */
    static RedisTable.Builder ofRedis() {
        return new RedisTable.Builder();
    }
    /**
     * 创建mem表
     *
     * @return mem表
     */
    static MemTable.Builder ofMem() {
        return new MemTable.Builder();
    }
    /**
     * 创建file表
     *
     * @return file表
     */
    static FileTable.Builder ofFile() {
        return new FileTable.Builder();
    }
}
