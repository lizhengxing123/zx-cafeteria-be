package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.AddressBook;
import com.lzx.mapper.AddressBookMapper;
import com.lzx.service.AddressBookService;
import com.lzx.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地址簿服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AddressBookServiceImpl implements AddressBookService {

    private final AddressBookMapper addressBookMapper;

    /**
     * 新增地址簿
     *
     * @param addressBook 地址簿实体对象
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(SecurityUtil.getCurrentUserId());
        addressBook.setIsDefault(StatusConstant.NOT_DEFAULT);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 获取当前用户地址簿列表
     *
     * @return 用户地址簿列表
     */
    @Override
    public List<AddressBook> listQuery() {
        return addressBookMapper.selectList(
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getUserId, SecurityUtil.getCurrentUserId())
        );
    }

    @Override
    public AddressBook getDefault() {
        return addressBookMapper.selectOne(
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getUserId, SecurityUtil.getCurrentUserId())
                        .eq(AddressBook::getIsDefault, StatusConstant.DEFAULT)
        );
    }

    @Override
    public void setDefault(Long addressBookId) {
        // 1、将所有地址簿的isDefault设置为false
        addressBookMapper.update(
                AddressBook.builder().isDefault(StatusConstant.NOT_DEFAULT).build(),
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getUserId, SecurityUtil.getCurrentUserId())
        );
        // 2、将当前地址簿的isDefault设置为true
        addressBookMapper.update(
                AddressBook.builder().isDefault(StatusConstant.DEFAULT).build(),
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getId, addressBookId)
                        .eq(AddressBook::getUserId, SecurityUtil.getCurrentUserId())
        );
    }

    @Override
    public AddressBook getById(Long addressBookId) {
        return addressBookMapper.selectById(addressBookId);
    }

    @Override
    public void removeById(Long addressBookId) {
        addressBookMapper.deleteById(addressBookId);
    }

    @Override
    public void updateById(Long addressBookId, AddressBook addressBook) {
        addressBook.setId(addressBookId);
        addressBookMapper.updateById(addressBook);
    }
}
