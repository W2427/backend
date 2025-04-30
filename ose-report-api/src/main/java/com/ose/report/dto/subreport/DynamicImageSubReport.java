package com.ose.report.dto.subreport;

/**
 * 子报表数据模型（动态报表）（附件图片）
 */
public class DynamicImageSubReport extends SubReport {

    /**
     * 图片类型（file/base64）
     */
    private String type;

    /**
     * 图片资源（路径/DATA）
     */
    private String resource;

    /**
     * 默认构造器
     */
    public DynamicImageSubReport() {
    }

    /**
     * 参数构造器
     *
     * @param type     图片类型（file/base64）
     * @param resource 图片资源（路径/DATA）
     */
    public DynamicImageSubReport(String type, String resource) {
        this.type = type;
        this.resource = resource;
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
     * Gets the value of resource.
     *
     * @return the value of resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource.
     *
     * <p>You can use getResource() to get the value of resource</p>
     *
     * @param resource resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }
}
