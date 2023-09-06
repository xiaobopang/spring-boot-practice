package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")//指定表名
public class User {

    private Integer id;
    private String name;
    private Integer age;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

