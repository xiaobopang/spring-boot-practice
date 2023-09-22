package com.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user")//指定表名
public class User extends BaseEntity {

    @ExcelProperty("ID")
    private Integer id;
    @ExcelProperty("Name")
    private String name;
    @ExcelProperty("Age")
    private Integer age;
    private String salt;
    private String password;

}

