package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "order")//指定表名
public class Order {

    private Integer id;
    private Integer userId;
    private String orderNo;
    private Integer productNum;
    private String productName;
    private Integer price;

    private LocalDateTime payAt;

    private Integer status;

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

