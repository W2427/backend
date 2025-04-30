package com.ose.tasks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.PrintHeight;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "print_config")
public class PrintConfig extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private Long orgId;

    private Long projectId;

    //类型
    private String type;

    //服务地址
    private String service;

    //名称
    private String name;

    //编号
    private int no;

    //备注
    private String memo;

    @Enumerated(EnumType.STRING)
    private PrintHeight height;

    //打印方向
    @Column(columnDefinition = "bit default b'1'")
    private boolean cuttingRFlag;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public PrintHeight getHeight() {
        return height;
    }

    public void setHeight(PrintHeight height) {
        this.height = height;
    }

    public boolean getCuttingRFlag() {
        return cuttingRFlag;
    }

    public void setCuttingRFlag(boolean cuttingRFlag) {
        this.cuttingRFlag = cuttingRFlag;
    }

}
