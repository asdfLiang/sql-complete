package com.liang.service.support.exceptions;

/**
 * @since 2023/9/10 19:13
 * @author by liangzj
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
