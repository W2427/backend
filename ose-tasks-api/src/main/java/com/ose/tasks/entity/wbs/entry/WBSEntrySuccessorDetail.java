package com.ose.tasks.entity.wbs.entry;


import jakarta.persistence.*;

/**
 * WBS 条目关系数据。
 */
@Entity
@Table(name = "wbs_entry_relation")
public class WBSEntrySuccessorDetail extends WBSEntryRelationBasic {

    private static final long serialVersionUID = -4350348942202008315L;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "successorId", referencedColumnName = "guid")
    })
//    @Where(clause = "delete = 0")
    private WBSEntry successor;

    public WBSEntry getSuccessor() {
        return successor;
    }

    public void setSuccessor(WBSEntry successor) {
        this.successor = successor;
    }

}
