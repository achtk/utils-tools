package com.chua.utils.tools.exceptions;

/**
 * 服务繁忙异常
 * @author CH
 */
public class BusyServiceException extends Exception {

    public BusyServiceException() {
        super();
    }

    public BusyServiceException(String message) {
        super(message);
    }

    public BusyServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusyServiceException(Throwable cause) {
        super(cause);
    }

    protected BusyServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
