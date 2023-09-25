package com.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user")//指定表名
public class User extends BaseEntity {

    @ExcelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
    private Integer age;
    private String salt;
    
    @TableField(
            insertStrategy = FieldStrategy.NOT_EMPTY,
            updateStrategy = FieldStrategy.NOT_EMPTY,
            whereStrategy = FieldStrategy.NOT_EMPTY
    )
    private String password;

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }

}

