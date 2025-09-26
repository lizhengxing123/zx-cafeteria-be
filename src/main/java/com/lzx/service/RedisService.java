package com.lzx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 常用操作
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ============================ 字符串操作 ============================

    /**
     * 设置字符串键值对
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置字符串键值对并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置字符串键值对，如果不存在则设置
     *
     * @param key   键
     * @param value 值
     */
    public void setIfAbsent(String key, Object value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 获取字符串键对应的值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // ============================ 哈希操作 ============================

    /**
     * 设置哈希键值对
     *
     * @param key     键
     * @param hashKey 哈希键
     * @param value   值
     */
    public void hashSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取哈希键对应的值
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return 值
     */
    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取所有哈希键对应的值
     *
     * @param key 键
     * @return 所有哈希键对应的值
     */
    public Map<Object, Object> hashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取哈希值列表
     *
     * @param key 键
     * @return 哈希值列表
     */
    public List<Object> hashVals(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 获取哈希键列表
     *
     * @param key 键
     * @return 哈希键列表
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 删除哈希键对应的值
     *
     * @param key      键
     * @param hashKeys 哈希键列表
     * @return 删除的键数量
     */
    public long hashDelete(String key, List<String> hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys.toArray());
    }

    // ============================ 列表操作 ============================

    /**
     * 向列表左侧添加元素
     *
     * @param key   键
     * @param value 值
     */
    public void listLeftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从列表右侧弹出元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public Object listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     * @return 列表长度
     */
    public Long listLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取列表指定范围的元素
     *
     * @param key   键
     * @param start 起始索引（包含）
     * @param end   结束索引（包含）
     * @return 列表指定范围的元素
     */
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    // ============================ 集合操作 ============================

    /**
     * 添加元素到集合
     *
     * @param key   键
     * @param value 值
     */
    public void setAdd(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 从集合中移除元素
     *
     * @param key   键
     * @param value 值
     */
    public void setRemove(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 获取集合所有元素
     *
     * @param key 键
     * @return 集合所有元素
     */
    public Set<Object> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取集合元素数量
     *
     * @param key 键
     * @return 集合元素数量
     */
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 检查集合是否包含元素
     *
     * @param key   键
     * @param value 值
     * @return 如果集合包含元素则返回 true，否则返回 false
     */
    public Boolean setIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 求两个集合的交集
     *
     * @param key      键
     * @param otherKey 其他集合的键
     * @return 两个集合的交集
     */
    public Set<Object> setIntersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 求两个集合的并集
     *
     * @param key      键
     * @param otherKey 其他集合的键
     * @return 两个集合的并集
     */
    public Set<Object> setUnion(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    // ============================ 有序集合操作 ============================

    /**
     * 添加有序集合元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    public void zSetAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取有序集合元素数量
     *
     * @param key 键
     * @return 有序集合元素数量
     */
    public Long zSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取有序集合指定范围的元素
     *
     * @param key   键
     * @param start 起始索引（包含）
     * @param end   结束索引（包含）
     * @return 有序集合指定范围的元素
     */
    public Set<Object> zSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取有序集合指定元素的分数
     *
     * @param key   键
     * @param value 值
     * @return 有序集合指定元素的分数
     */
    public Double zSetScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 删除有序集合元素
     *
     * @param key   键
     * @param value 值，多个值之间用逗号隔开
     */
    public void zSetRemove(String key, Object... value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 给元素增加分数
     *
     * @param key   键
     * @param value 值
     * @param delta 增加的分数
     */
    public void zSetAddScore(String key, Object value, double delta) {
        redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    // ============================ 通用操作 ============================

    /**
     * 删除键
     *
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 检查键是否存在
     *
     * @param key 键
     * @return 如果键存在则返回 true，否则返回 false
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 查看键的类型
     *
     * @param key 键
     * @return 键的类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 查询所有符合模式的键
     *
     * @param pattern 匹配模式，例如："user:*" 表示匹配所有以 "user:" 开头的键
     * @return 所有符合模式的键的集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
