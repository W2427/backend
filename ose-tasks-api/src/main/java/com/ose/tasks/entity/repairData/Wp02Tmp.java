package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "entity_wp02_tmp")
public class Wp02Tmp extends BaseEntity {

    private static final long serialVersionUID = -4969249490460479020L;
    private Long oldWp02HierarchyId;

    private Long newWp02HierarchyId;

    public Long getOldWp02HierarchyId() {
        return oldWp02HierarchyId;
    }

    public void setOldWp02HierarchyId(Long oldWp02HierarchyId) {
        this.oldWp02HierarchyId = oldWp02HierarchyId;
    }

    public Long getNewWp02HierarchyId() {
        return newWp02HierarchyId;
    }

    public void setNewWp02HierarchyId(Long newWp02HierarchyId) {
        this.newWp02HierarchyId = newWp02HierarchyId;
    }
}
