package com.ose.tasks.entity.wbs.entry;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;

/**
 * WBS 条目关系数据。
 */
@Entity
@Table(name = "wbs_entry_relation")
public class WBSEntryPredecessor extends WBSEntryRelationBasic {

    private static final long serialVersionUID = -8084637531131073481L;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "predecessorId", referencedColumnName = "guid")
    })
    @Where(clause = "delete = 0")
    private WBSEntryBasic predecessor;

    public WBSEntryBasic getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(WBSEntryBasic predecessor) {
        this.predecessor = predecessor;
    }

}
