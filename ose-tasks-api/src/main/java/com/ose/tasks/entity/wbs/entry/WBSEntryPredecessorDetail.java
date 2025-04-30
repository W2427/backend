package com.ose.tasks.entity.wbs.entry;

import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

/**
 * WBS 条目关系数据。
 */
@Entity
@Table(name = "wbs_entry_relation")
public class WBSEntryPredecessorDetail extends WBSEntryRelationBasic {

    private static final long serialVersionUID = -8084637531131073481L;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "predecessorId", referencedColumnName = "guid")
    })
    @Where(clause = "delete = 0 AND (optional IS NULL OR optional = 0)")
    private WBSEntry predecessor;

    @Transient
    @Enumerated(EnumType.STRING)
    private WBSEntryRunningStatus runningStatus;

    public WBSEntryRunningStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(WBSEntryRunningStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public WBSEntry getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(WBSEntry predecessor) {
        this.predecessor = predecessor;
    }

}
