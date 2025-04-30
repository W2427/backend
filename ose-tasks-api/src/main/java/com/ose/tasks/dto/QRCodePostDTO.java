package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.QRCodeTargetType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

/**
 * 二维码创建表单。
 */
public class QRCodePostDTO extends BaseDTO {

    private static final long serialVersionUID = 3306151730639645725L;

    @Schema(description = "目标数据实体类型")
    private QRCodeTargetType targetType;

    @Schema(description = "目标数据实体 ID")
    @NotNull
    private Long targetId;

    public QRCodeTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(QRCodeTargetType targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

}
