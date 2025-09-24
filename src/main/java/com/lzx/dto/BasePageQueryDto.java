package com.lzx.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础分页查询传递的数据模型
 */
@Getter
@Setter
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
