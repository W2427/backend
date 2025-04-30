package com.ose.tasks.entity.wbs.entry;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.util.List;

/**
 * WBS 条目数据。
 */
@Entity
@Table(name = "wbs_entry")
public class WBSEntryWithRelations extends WBSEntryBase {

    private static final long serialVersionUID = 6247380257520304861L;

    @OneToMany
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(updatable = false, insertable = false, name = "successorId", referencedColumnName = "guid", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    })
    @Where(clause = "deleted = 0 AND optional = 0")
    private List<WBSEntryPredecessor> predecessors;

    @OneToMany
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(updatable = false, insertable = false, name = "predecessorId", referencedColumnName = "guid", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    })
    @Where(clause = "deleted = 0")
    private List<WBSEntrySuccessor> successors;

    public List<WBSEntryPredecessor> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<WBSEntryPredecessor> predecessors) {
        this.predecessors = predecessors;
    }

    public List<WBSEntrySuccessor> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<WBSEntrySuccessor> successors) {
        this.successors = successors;
    }


}
