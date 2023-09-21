package com.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")//指定表名
public class User {

    @ExcelProperty("ID")
    private Integer id;
    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
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

