package com.chua.utils.tools.http.exception;

import com.chua.utils.tools.http.entity.ResponseEntity;

/**
 * 异常处理
 * @author CHTK
 */
public interface ThrowableHandler {
    /**
     * 抛出异常
     * @param throwable 异常
     * @return
     */
    public ResponseEntity throwable(Throwable throwable);
}
