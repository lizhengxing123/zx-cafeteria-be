package com.lzx.controller.user;

import com.lzx.constant.MessageConstant;
import com.lzx.entity.AddressBook;
import com.lzx.result.Result;
import com.lzx.service.AddressBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [用户端] 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/user/addressBooks")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AddressBookController {

    private final AddressBookService addressBookService;

    /**
     * 新增地址簿
     *
     * @param addressBook 地址簿实体对象
     * @return 新增地址簿成功返回的消息
     */
    @PostMapping
    public Result<String> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址簿：{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 获取当前用户地址簿列表
     *
     * @return 用户地址簿列表
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        log.info("获取当前用户地址簿列表");
        List<AddressBook> addressBookList = addressBookService.listQuery();
        return Result.success(MessageConstant.QUERY_SUCCESS, addressBookList);
    }

    /**
     * 查询默认地址簿
     *
     * @return 查询默认地址簿成功返回的数据模型
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址簿");
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(MessageConstant.QUERY_SUCCESS, addressBook);
    }

    /**
     * 设置默认地址簿
     *
     * @param id 地址簿 ID
     * @return 设置默认地址簿成功返回的消息
     */
    @PutMapping("/default/{id}")
    public Result<String> setDefault(@PathVariable Long id) {
        log.info("设置默认地址簿：{}", id);
        addressBookService.setDefault(id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据 ID 查询地址簿信息
     *
     * @param id 地址簿 ID
     * @return 根据 ID 查询地址簿信息成功返回的数据模型
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据 ID 查询地址簿信息：{}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(MessageConstant.QUERY_SUCCESS, addressBook);
    }

    /**
     * 根据 ID 删除地址簿
     *
     * @param id 地址簿 ID
     * @return 根据 ID 删除地址簿成功返回的消息
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        log.info("根据 ID 删除地址簿：{}", id);
        addressBookService.removeById(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    /**
     * 根据 ID 更新地址簿信息
     *
     * @param id          地址簿 ID
     * @param addressBook 更新地址簿信息传递的数据模型
     * @return 根据 ID 更新地址簿信息成功返回的消息
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody AddressBook addressBook) {
        log.info("根据 ID 更新地址簿信息：地址簿ID{}，地址簿信息{}", id, addressBook);
        addressBookService.updateById(id, addressBook);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }


}
