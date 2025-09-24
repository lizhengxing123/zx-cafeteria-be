package com.lzx.exception;

/**
 * 分类不存在异常
 */
public class DataNotFoundException extends BaseException {

    public DataNotFoundException() {
    }

    public DataNotFoundException(String msg) {
        super(msg);
    }

}
