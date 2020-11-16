package com.chua.utils.tools.plugins.monitor;

import java.io.IOException;

/**
 * 监视器
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public interface Monitor extends AutoCloseable{
    /**
     * 监视
     * @throws IOException IOException
     */
    void monitor() throws IOException;
}
