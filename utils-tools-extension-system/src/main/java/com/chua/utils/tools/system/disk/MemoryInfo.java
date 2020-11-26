package com.chua.utils.tools.system.disk;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存信息
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
@Data
public class MemoryInfo {

    private long total;
    private String totalSize;
    private long available;
    private String availableSize;
    private Map<String, Object> virtualMemory;
    private List<Map<String, Object>> physicalMemorys;

}
