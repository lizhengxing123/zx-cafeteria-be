package com.lzx.dto;

import lombok.Data;

@Data
public class BasePageQueryDto {
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
