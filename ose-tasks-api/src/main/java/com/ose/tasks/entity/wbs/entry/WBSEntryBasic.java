package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * WBS 条目数据。
 */
@Entity
@Table(name = "wbs_entry")
public class WBSEntryBasic extends BaseEntity {

    private static final long serialVersionUID = 1628983542879114435L;

    @Column
    private Long projectId;

    @Column
    private String guid;

    @Column
    private Boolean deleted;

    public WBSEntryBasic() {
        super();
    }

    public WBSEntryBasic(Long id) {
        super(id);
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
