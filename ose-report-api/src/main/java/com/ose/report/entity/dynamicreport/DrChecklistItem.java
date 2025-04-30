package com.ose.report.entity.dynamicreport;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * 检查项实体类。
 */
@Entity
@Table(name = "dr_report_checklist_item")
public class DrChecklistItem extends BaseBizEntity {


    private static final long serialVersionUID = 1966546654598549674L;
    // 检查单ID
    @Column(nullable = false)
    @NotNull(message = "checklist's id is required")
    private Long checklistId;

    @Column
    private String checklistNo;

   // 检查项序号
    @Column(length = 8)
    private String itemNo;

    // 检查项描述
    @Column(length = 256)
    private String description;

    @Column
    private Long projectId;

    @Column
    private String yesStr;

    @Column
    private String noStr;

    @Column
    private String naStr;

    @Column
    private String resultStr;

    @Column
    private String remark;


    /**
     * 默认构造方法
     */
    public DrChecklistItem() {
    }

    /**
     * 构造方法
     *
     * @param id 检查项ID
     */
    public DrChecklistItem(Long id) {
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

    public String getYesStr() {
        return yesStr;
    }

    public void setYesStr(String yesStr) {
        this.yesStr = yesStr;
    }

    public String getNoStr() {
        return noStr;
    }

    public void setNoStr(String noStr) {
        this.noStr = noStr;
    }

    public String getNaStr() {
        return naStr;
    }

    public void setNaStr(String naStr) {
        this.naStr = naStr;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getChecklistNo() {
        return checklistNo;
    }

    public void setChecklistNo(String checklistNo) {
        this.checklistNo = checklistNo;
    }
}
