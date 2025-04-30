package com.ose.report.dto;

import java.util.Map;

/**
 * 检查项 数据传输对象（导入用）
 */
public class ChecklistItemImportDTO extends BaseChecklistItemDTO {

    private static final long serialVersionUID = 5536000768141672957L;

    // 检查单编号
    private String serial;

    // 错误信息
    private Map<String, String> errors;

    /**
     * 默认构造方法
     */
    public ChecklistItemImportDTO() {
    }

    /**
     * 构造方法。
     */
    public ChecklistItemImportDTO(String serial, String itemNo, String description) {

        super(itemNo, description);
        this.serial = serial;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ChecklistItemDTO{serial='"
            + serial
            + "', itemNo='"
            + super.getItemNo()
            + "', description='"
            + super.getDescription()
            + "}";
    }
}
