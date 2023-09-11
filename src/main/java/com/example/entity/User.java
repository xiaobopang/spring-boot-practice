package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")//指定表名
public class User {

    private Integer id;
    private String name;
    private Integer age;
    private String salt;
    private String password;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除标志位
     */
    @TableLogic
    @JsonIgnore
    private LocalDateTime deletedAt;

}

