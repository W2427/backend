package com.ose.auth.dto;

import com.ose.auth.vo.OrgType;
import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 部门数据传输对象类。
 */
public class OrganizationDTO extends BaseDTO {

    private static final long serialVersionUID = 3735782596170410285L;

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "上级部门ID")
    private Long parentId;

    @Schema(name = "组织类型（DEPARTMENT：部门；PROJECT：项目组织；COMPANY：公司，默认：DEPARTMENT）")
    private OrgType type;

    @Schema(name = "排序目标部门ID（top：置顶；ID：排在目标 ID 后面）")
    private Long afterId;

    @Schema(name = "部门状态")
    private EntityStatus status;

    // 部门编号
    private String no;

    @Schema(name = "是否为IDC")
    private Boolean idc;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getAfterId() {
        return afterId;
    }

    public void setAfterId(Long afterId) {
        this.afterId = afterId;
    }

    public OrgType getType() {
        return type;
    }

    public void setType(OrgType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Boolean getIdc() {
        return idc;
    }

    public void setIdc(Boolean idc) {
        this.idc = idc;
    }
}
