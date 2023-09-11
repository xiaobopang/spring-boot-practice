package com.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * VO层,含义  前端需要展示那些字段则 定义返回对应字段
 **/
@Data
@Schema(description = "用户VO")
public class OrderVO {

    @Schema(description = "id")
    private Integer id;
    @Schema(description = "订单编号")
    private String orderNo;
    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "商品数量")
    private Integer productNum;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品价格")
    private Integer price;

    @Schema(description = "状态：0-未支付，1-已支付")
    private Integer status;

    @Schema(description = "支付时间")
    private Date payAt;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
