package com.lzx;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

// @SpringBootTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class ZxCafeteriaApplicationTests {

    // 自动注入 RedisTemplate
    private final RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testRedisTemplate() {
        System.out.println(redisTemplate);
        // 测试 RedisTemplate
        // 1、string
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        stringObjectValueOperations.set("test-string", "string1");
        System.out.println(stringObjectValueOperations.get("test-string"));

        // 2、hash
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("test-hash", "hashKey1", "hashValue1");
        System.out.println(hashOperations.get("test-hash", "hashKey1"));

        // 3、list
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush("test-list", "list1");
        System.out.println(listOperations.leftPop("test-list"));

        // 4、set
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add("test-set", "set1", "set2");
        System.out.println(setOperations.members("test-set"));

        // 5、zset
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("test-zset", "zset1", 1);
        zSetOperations.add("test-zset", "zset2", 2);
        System.out.println(zSetOperations.score("test-zset", "zset1"));
    }
}
