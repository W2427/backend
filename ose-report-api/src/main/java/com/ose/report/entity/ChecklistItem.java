package com.ose.report.entity;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * 检查项实体类。
 */
@Entity
@Table(name = "report_checklist_items")
public class ChecklistItem extends BaseBizEntity {

    private static final long serialVersionUID = 385464539385959630L;

    // 检查单ID
    @Column(nullable = false)
    @NotNull(message = "checklist's id is required")
    private Long checklistId;

    // 检查项序号
    @Column(length = 8)
    private String itemNo;

    // 检查项描述
    @Column(length = 256)
    private String description;

    /**
     * 默认构造方法
     */
    public ChecklistItem() {
    }

    /**
     * 构造方法
     *
     * @param id 检查项ID
     */
    public ChecklistItem(Long id) {
        super(id);
    }

    /**
     * Gets the value of checklistId.
     *
     * @return the value of checklistId
     */
    public Long getChecklistId() {
        return checklistId;
    }

    /**
     * Sets the checklistId.
     *
     * <p>You can use getChecklistId() to get the value of checklistId</p>
     *
     * @param checklistId checklistId
     */
    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    /**
     * Gets the value of itemNo.
     *
     * @return the value of itemNo
     */
    public String getItemNo() {
        return itemNo;
    }

    /**
     * Sets the itemNo.
     *
     * <p>You can use getItemNo() to get the value of itemNo</p>
     *
     * @param itemNo itemNo
     */
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * Gets the value of description.
     *
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * <p>You can use getDescription() to get the value of description</p>
     *
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
