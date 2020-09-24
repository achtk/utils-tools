package com.chua.utils.netx.ftp.status;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author CH
 */
@Getter
@Setter
@Accessors(fluent = true)
public class ListDeleteStatus {
    /**
     * 状态
     */
    private DeleteStatus deleteStatus;
    /**
     * 文件
     */
    private String file;
}
