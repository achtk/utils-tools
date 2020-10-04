package com.chua.utils.tools.exceptions;

/**
 * 未实现异常
 * @author CH
 * @date 2020-09-26
 */
public class NoImplementationException extends Exception {

    public NoImplementationException() {
    }

    public NoImplementationException(String message) {
        super(message);
    }

    public NoImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoImplementationException(Throwable cause) {
        super(cause);
    }

    public NoImplementationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
