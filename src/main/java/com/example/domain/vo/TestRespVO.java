package com.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TestRespVO {
    @Schema(description = "用户id")
    private Integer id;
    @Schema(description = "用户名称")
    private String name;
    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}