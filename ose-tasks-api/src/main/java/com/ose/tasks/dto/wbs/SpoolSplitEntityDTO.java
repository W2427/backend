package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.wbs.WBSEntityType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class SpoolSplitEntityDTO extends BaseDTO {

    private static final long serialVersionUID = 4363754558307911248L;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "实体编号")
    private String no;

    @Schema(description = "实体类型")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private WBSEntityType entityType;

    @Schema(description = "实体数量")
    @Column
    private Integer count;

    @Schema(description = "实体移动数量")
    @Column
    private Integer removeCount;

    @Schema(description = "变更状态")
    private boolean changeStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public WBSEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(WBSEntityType entityType) {
        this.entityType = entityType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Integer getRemoveCount() {
        return removeCount;
    }

    public void setRemoveCount(Integer removeCount) {
        this.removeCount = removeCount;
    }
}
