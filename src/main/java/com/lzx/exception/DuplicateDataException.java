package com.lzx.exception;

/**
 * 重复数据异常
 */
public class DuplicateDataException extends BaseException {
    public DuplicateDataException() {
    }
    public DuplicateDataException(String msg) {
        super(msg);
    }
}
