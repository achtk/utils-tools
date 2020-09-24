package com.chua.utils.netx.ftp.handler;

import java.util.Map;

/**
 * @author CH
 */
@FunctionalInterface
public interface FtpFileHandler {
    /**
     *
     * @param temp
     * @param size
     */
    public void executor(Map<String, String> temp, int size);
}
