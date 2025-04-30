package com.ose.tasks.dto.setting;

import com.ose.dto.BaseDTO;

public class FuncPartCreateDTO extends BaseDTO {


    private static final long serialVersionUID = 9056785734874478713L;
    private Long projectId;

    // 组织 ID
    private Long orgId;

    // 工序名称
    private String nameCn;

    // 工序名称-英文
    private String nameEn;

    // 排序
    private int orderNo = 0;

    // 备注
    private String memo;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
