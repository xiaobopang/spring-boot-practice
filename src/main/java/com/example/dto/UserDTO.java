package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 用于验证User的实体类
 */
@Data
@Schema(description = "用户DTO")
public class UserDTO {
    private Integer id;

    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(description = "年龄")
    @NotNull(message = "age不能为null")
    @Min(value = 1, message = "年龄不符合要求")
    @Max(value = 200, message = "年龄不符合要求")
    private Integer age;

    @Schema(description = "密码")
    @NotBlank(message="密码不能为空")
    private String password;
}

