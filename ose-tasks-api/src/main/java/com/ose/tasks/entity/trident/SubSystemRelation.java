package com.ose.tasks.entity.trident;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table( name ="sub_system_relation" )
public class SubSystemRelation extends BaseEntity {


    private static final long serialVersionUID = -4916545701057761365L;
    @Column
    private Long projectId;

    @Column
    private Long successorId;

    @Column
    private String successorNo;

    @Column
    private Long predecessorId;

    @Column
    private String predecessorNo;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSuccessorNo() {
        return successorNo;
    }

    public void setSuccessorNo(String successorNo) {
        this.successorNo = successorNo;
    }

    public String getPredecessorNo() {
        return predecessorNo;
    }

    public void setPredecessorNo(String predecessorNo) {
        this.predecessorNo = predecessorNo;
    }

    public Long getSuccessorId() {
        return successorId;
    }

    public void setSuccessorId(Long successorId) {
        this.successorId = successorId;
    }

    public Long getPredecessorId() {
        return predecessorId;
    }

    public void setPredecessorId(Long predecessorId) {
        this.predecessorId = predecessorId;
    }

}
