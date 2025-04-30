package com.ose.tasks.entity.wbs.entry;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.util.List;

/**
 * WBS 条目数据。
 */
@Entity
@Table(name = "wbs_entry")
public class WBSEntryWithRelationsDetail extends WBSEntryBase {

    private static final long serialVersionUID = 6247380257520304861L;

    @OneToMany
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(updatable = false, insertable = false, name = "successorId", referencedColumnName = "guid",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    })
    @Where(clause = "deleted = 0")
    private List<WBSEntryPredecessorDetail> predecessors;

    @OneToMany
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(updatable = false, insertable = false, name = "predecessorId", referencedColumnName = "guid",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    })
    @Where(clause = "deleted = 0")
    private List<WBSEntrySuccessorDetail> successors;

//    @Schema(description = "备注")
//    @Lob
//    @Column(length = 4096)
//    private String remarks;

    public List<WBSEntryPredecessorDetail> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<WBSEntryPredecessorDetail> predecessors) {
        this.predecessors = predecessors;
    }

    public List<WBSEntrySuccessorDetail> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<WBSEntrySuccessorDetail> successors) {
        this.successors = successors;
    }

//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(String remarks) {
//        this.remarks = remarks;
//    }

}
