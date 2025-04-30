package com.ose.tasks.entity.trident;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table( name ="sub_system_tcp_relation" )
public class SubSystemTcpRelation extends BaseEntity {


    private static final long serialVersionUID = -1111745926194229617L;
    @Column
    private Long projectId;

    @Column //entityId
    private Long subSystemId;

    @Column
    private String subSystemNo;


    @Column
    private String tcpNo;

    @Column
    private String tcpFullName;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(Long subSystemId) {
        this.subSystemId = subSystemId;
    }

    public String getSubSystemNo() {
        return subSystemNo;
    }

    public void setSubSystemNo(String subSystemNo) {
        this.subSystemNo = subSystemNo;
    }

    public String getTcpNo() {
        return tcpNo;
    }

    public void setTcpNo(String tcpNo) {
        this.tcpNo = tcpNo;
    }

    public String getTcpFullName() {
        return tcpFullName;
    }

    public void setTcpFullName(String tcpFullName) {
        this.tcpFullName = tcpFullName;
    }
}
