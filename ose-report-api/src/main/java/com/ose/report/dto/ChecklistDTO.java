package com.ose.report.dto;

import com.ose.dto.BaseDTO;

/**
 * 检查单 数据传输对象
 */
public class ChecklistDTO extends BaseDTO {

    private static final long serialVersionUID = -7200665519501396071L;

    // 序号
    private String serial;

    // 名称
    private String name;

    // 标题
    private String title;

    // 表头模板
    private String headerTemplate;

    // 签字栏模板
    private String signatureTemplate;

    /**
     * 默认构造方法
     */
    public ChecklistDTO() {
    }

    /**
     * 构造方法
     *
     * @param serial            序号
     * @param name              名称
     * @param title             标题
     * @param headerTemplate    表头模板
     * @param signatureTemplate 签字栏模板
     */
    public ChecklistDTO(String serial, String name, String title,
                        String headerTemplate, String signatureTemplate) {

        this.serial = serial;
        this.name = name;
        this.title = title;
        this.headerTemplate = headerTemplate;
        this.signatureTemplate = signatureTemplate;
    }

    /**
     * Gets the value of serial.
     *
     * @return the value of serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Sets the serial.
     *
     * <p>You can use getSerial() to get the value of serial</p>
     *
     * @param serial serial
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * Gets the value of name.
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of title.
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * <p>You can use getTitle() to get the value of title</p>
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the value of headerTemplate.
     *
     * @return the value of headerTemplate
     */
    public String getHeaderTemplate() {
        return headerTemplate;
    }

    /**
     * Sets the headerTemplate.
     *
     * <p>You can use getHeaderTemplate() to get the value of headerTemplate</p>
     *
     * @param headerTemplate headerTemplate
     */
    public void setHeaderTemplate(String headerTemplate) {
        this.headerTemplate = headerTemplate;
    }

    /**
     * Gets the value of signatureTemplate.
     *
     * @return the value of signatureTemplate
     */
    public String getSignatureTemplate() {
        return signatureTemplate;
    }

    /**
     * Sets the signatureTemplate.
     *
     * <p>You can use getSignatureTemplate() to get the value of signatureTemplate</p>
     *
     * @param signatureTemplate signatureTemplate
     */
    public void setSignatureTemplate(String signatureTemplate) {
        this.signatureTemplate = signatureTemplate;
    }

    /**
     * 字符串转换
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ChecklistDTO{" +
            "serial='" + serial + '\'' +
            ", name='" + name + '\'' +
            ", title='" + title + '\'' +
            ", headerTemplate='" + headerTemplate + '\'' +
            ", signatureTemplate='" + signatureTemplate + '\'' +
            '}';
    }
}
