package com.chua.utils.tools.exceptions;

/**
 * 不支持异常
 * @author CH
 * @date 2020-09-26
 */
public class NotSupportedException extends Exception {
    public NotSupportedException() {
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedException(Throwable cause) {
        super(cause);
    }

    public NotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
