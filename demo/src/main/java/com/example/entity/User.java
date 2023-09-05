package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")//指定表名
public class User  {

    private String name;
    private Integer age;
    private String password;

    @TableId(value = "id" , type = IdType.AUTO)//指定自增策略
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8" , pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_at" , fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8" , pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updated_at" , fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}

