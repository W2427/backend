package com.ose.report.dto;

/**
 * 检查项 数据传输对象
 */
public class ChecklistItemDTO extends BaseChecklistItemDTO {

    private static final long serialVersionUID = -7753242053119023402L;

    // 检查单ID
    private Long checklistId;

    /**
     * 构造方法。
     */
    public ChecklistItemDTO() {
    }

    /**
     * 构造方法。
     */
    public ChecklistItemDTO(Long checklistId, String itemNo, String description) {

        super(itemNo, description);
        this.checklistId = checklistId;
    }

    public Long getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    @Override
    public String toString() {
        return "ChecklistItemDTO{checklistId='"
            + checklistId
            + "', itemNo='"
            + super.getItemNo()
            + "', description='"
            + super.getDescription()
            + "}";
    }
}
