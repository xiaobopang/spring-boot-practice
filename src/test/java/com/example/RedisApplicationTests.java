package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class RedisApplicationTests {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
//        // 添加
//        redisTemplate.opsForValue().set("name","张三");
//        // 查询
//        System.out.println(redisTemplate.opsForValue().get("name"));
//        // 删除
//        redisTemplate.delete("name");
//        // 更新
//        redisTemplate.opsForValue().set("name","张三哈哈哈");
//        // 查询
//        System.out.println(redisTemplate.opsForValue().get("name"));
//
//        // 添加
//        stringRedisTemplate.opsForValue().set("name","张三");
//        // 查询
//        System.out.println(stringRedisTemplate.opsForValue().get("name"));
//        // 删除
//        stringRedisTemplate.delete("name");
//        // 更新
//        stringRedisTemplate.opsForValue().set("name","张三的呵呵呵呵");
//        // 查询
//        System.out.println(stringRedisTemplate.opsForValue().get("name"));

    }

}

