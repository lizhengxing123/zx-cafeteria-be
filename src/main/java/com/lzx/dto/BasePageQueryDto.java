package com.lzx.dto;

import com.lzx.constant.PageQueryConstant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * 基础分页查询传递的数据模型
 */
@Getter
public class BasePageQueryDto implements Serializable {
    /**
     * 页码
     */
    private int pageNum = PageQueryConstant.PAGE_NUM;

    /**
     * 每页页数
     */
    private int pageSize = PageQueryConstant.PAGE_SIZE;

    // 重写 setter
    public void setPageNum(int pageNum) {
        // 如果是 null 或者空字符串，使用默认值
        if (Objects.nonNull(pageNum) && pageNum > 0) {
            this.pageNum = pageNum;
        } else {
            this.pageNum = PageQueryConstant.PAGE_NUM;
        }
    }

    public void setPageSize(int pageSize) {
        // 如果是 null 或者空字符串，使用默认值
        if (Objects.nonNull(pageSize) && pageSize > 0) {
            this.pageSize = pageSize;
        } else {
            this.pageSize = PageQueryConstant.PAGE_SIZE;
        }
    }

    @Override
    public String toString() {
        return "pageNum=" + pageNum + ", pageSize=" + pageSize;
    }
}
