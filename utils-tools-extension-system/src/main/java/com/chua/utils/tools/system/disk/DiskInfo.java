package com.chua.utils.tools.system.disk;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 磁盘信息
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
@Getter
@Setter
public class DiskInfo {
    /**
     * 磁盘
     */
    private List<Disk> diskList;

    /**
     * 添加信息
     */
    public void add(Disk disk) {
        if(null == diskList) {
            diskList = new ArrayList<>();
        }
        diskList.add(disk);
    }
}
