package com.ose.tasks.dto.taskpackage;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.material.NestGateWay;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务包-实体创建数据传输对象。
 */
public class TaskPackageEntityRelationSearchDTO extends PageDTO {

    private static final long serialVersionUID = 4334633102268533197L;

    private Boolean used;

    @Schema(description = "套料状态")
    private NestGateWay nestGateWay;

    @Schema(description = "实体编号")
    private String entityNo;

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }
}
