package com.example.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充策略
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("新增数据开始调用自动填充。。。");
        this.strictInsertFill(metaObject, "createdAt" , LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "updatedAt" , LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新数据开始调用自动填充。。。");
        metaObject.setValue("updatedAt" , LocalDateTime.now());
        //this.strictUpdateFill(metaObject, "updatedAt" , LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
    }
}