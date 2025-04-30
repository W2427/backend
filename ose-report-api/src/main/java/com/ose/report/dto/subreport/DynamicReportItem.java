package com.ose.report.dto.subreport;

/**
 * 动态报表项目
 */
public class DynamicReportItem {

    /**
     * 字段名
     */
    private String field;

    /**
     * 显示名
     */
    private String displayName;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 显示宽度
     */
    private int width;

    /**
     * 默认构造器
     */
    public DynamicReportItem() {
    }

    /**
     * 参数构造器
     *
     * @param field       字段名
     * @param displayName 显示名
     * @param type        项目类型
     * @param width       显示宽度
     */
    public DynamicReportItem(String field, String displayName, String type, int width) {
        this.field = field;
        this.displayName = displayName;
        this.type = type;
        this.width = width;
    }

    /**
     * Gets the value of displayName.
     *
     * @return the value of displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the displayName.
     *
     * <p>You can use getDisplayName() to get the value of displayName</p>
     *
     * @param displayName displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the value of field.
     *
     * @return the value of field
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the field.
     *
     * <p>You can use getField() to get the value of field</p>
     *
     * @param field field
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Gets the value of type.
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * <p>You can use getType() to get the value of type</p>
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the value of width.
     *
     * @return the value of width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width.
     *
     * <p>You can use getWidth() to get the value of width</p>
     *
     * @param width width
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
