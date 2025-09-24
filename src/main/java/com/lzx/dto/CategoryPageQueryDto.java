package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类分页查询数据模型
 */
@Data
public class CategoryPageQueryDto extends BasePageQueryDto implements Serializable {
    /**
     * 分类类型
     */
    private Integer type;

    /**
     * 分类名称
     */
    private String name;
}
