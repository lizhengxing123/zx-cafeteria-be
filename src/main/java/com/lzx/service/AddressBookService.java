package com.lzx.service;

import com.lzx.entity.AddressBook;

import java.util.List;

/**
 * 地址簿服务接口
 */
public interface AddressBookService {

    /**
     * 新增地址簿
     *
     * @param addressBook 地址簿实体对象
     */
    void save(AddressBook addressBook);

    /**
     * 获取当前用户地址簿列表
     *
     * @return 用户地址簿列表
     */
    List<AddressBook> listQuery();

    /**
     * 查询默认地址簿
     *
     * @return 查询默认地址簿成功返回的数据模型
     */
    AddressBook getDefault();

    /**
     * 设置默认地址簿
     *
     * @param addressBookId 地址簿 ID
     */
    void setDefault(Long addressBookId);

    /**
     * 根据 ID 查询地址簿信息
     *
     * @param addressBookId 地址簿 ID
     * @return 根据 ID 查询地址簿信息成功返回的数据模型
     */
    AddressBook getById(Long addressBookId);

    /**
     * 根据 ID 删除地址簿
     *
     * @param addressBookId 地址簿 ID
     */
    void removeById(Long addressBookId);

    /**
     * 根据 ID 更新地址簿信息
     *
     * @param addressBook 根据 ID 更新地址簿信息传递的数据模型
     */
    void updateById(Long addressBookId, AddressBook addressBook);
}
