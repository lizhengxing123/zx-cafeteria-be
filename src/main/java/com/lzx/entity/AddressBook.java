package com.lzx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 地址簿
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Getter
@Setter
@ToString
@TableName("address_book")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省级区划编号
     */
    private String provinceCode;

    /**
     * 省级名称
     */
    private String provinceName;

    /**
     * 市级区划编号
     */
    private String cityCode;

    /**
     * 市级名称
     */
    private String cityName;

    /**
     * 区级区划编号
     */
    private String districtCode;

    /**
     * 区级名称
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 标签
     */
    private String label;

    /**
     * 默认 0 否 1是
     */
    private Boolean isDefault;
}
