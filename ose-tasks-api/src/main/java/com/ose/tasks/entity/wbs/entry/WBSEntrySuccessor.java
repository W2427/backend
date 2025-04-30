package com.ose.tasks.entity.wbs.entry;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;

/**
 * WBS 条目关系数据。
 */
@Entity
@Table(name = "wbs_entry_relation")
public class WBSEntrySuccessor extends WBSEntryRelationBasic {

    private static final long serialVersionUID = -4350348942202008315L;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "successorId", referencedColumnName = "guid")
    })
    @Where(clause = "delete = 0")
    private WBSEntryBasic successor;

    public WBSEntryBasic getSuccessor() {
        return successor;
    }

    public void setSuccessor(WBSEntryBasic successor) {
        this.successor = successor;
    }

}
