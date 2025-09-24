package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasePageQueryDto implements Serializable {
    /**
     * 页码
     */
    private int pageNum = 1;

    /**
     * 每页页数
     */
    private int pageSize = 10;

    @Override
    public String toString() {
        return "pageNum=" + pageNum + ", pageSize=" + pageSize;
    }
}
