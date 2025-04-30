package com.ose.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 检查单实体类。
 */
@Entity
@Table(
    name = "report_checklists",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serial"})
    }
)
public class Checklist extends BaseEntity {

    private static final long serialVersionUID = 377688955338188538L;

    // 组织ID
    @Column(nullable = false)
    @NotNull(message = "checklist's org id is required")
    private Long orgId;

    // 项目ID
    @Column(nullable = false)
    @NotNull(message = "checklist's project id is required")
    private Long projectId;

    // 检查单序号
    @Column(length = 32)
    private String serial;

    // 检查单名称
    @Column(nullable = false, length = 128)
    @NotNull(message = "checklist's name is required")
    private String name;

    // 检查单标题
    @Column(length = 128)
    private String title;

    // 检查单表头模板
    @Column
    @JsonIgnore
    private Long headerTemplateId;

    // 检查单签字栏模板
    @Column
    @JsonIgnore
    private Long signatureTemplateId;

    // 预览文件
    @Column(length = 256)
    private Long previewFile;

    /**
     * 默认构造方法
     */
    public Checklist() {
    }

    /**
     * Gets the value of orgId.
     *
     * @return the value of orgId
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * Sets the orgId.
     *
     * <p>You can use getOrgId() to get the value of orgId</p>
     *
     * @param orgId orgId
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * 构造方法
     *
     * @param id 检查单ID
     */
    public Checklist(Long id) {
        super(id);
    }

    /**
     * Gets the value of projectId.
     *
     * @return the value of projectId
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * Sets the projectId.
     *
     * <p>You can use getProjectId() to get the value of projectId</p>
     *
     * @param projectId projectId
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
    public Long getHeaderTemplateId() {
        return headerTemplateId;
    }

    /**
     * Sets the headerTemplate.
     *
     * <p>You can use getHeaderTemplate() to get the value of headerTemplate</p>
     *
     * @param headerTemplateId headerTemplate
     */
    public void setHeaderTemplateId(Long headerTemplateId) {
        this.headerTemplateId = headerTemplateId;
    }

    /**
     * Gets the value of signatureTemplate.
     *
     * @return the value of signatureTemplate
     */
    public Long getSignatureTemplateId() {
        return signatureTemplateId;
    }

    /**
     * Sets the signatureTemplate.
     *
     * <p>You can use getSignatureTemplate() to get the value of signatureTemplate</p>
     *
     * @param signatureTemplateId signatureTemplate
     */
    public void setSignatureTemplateId(Long signatureTemplateId) {
        this.signatureTemplateId = signatureTemplateId;
    }

    /**
     * Gets the value of previewFile.
     * s
     *
     * @return the value of previewFile
     */
    public Long getPreviewFile() {
        return previewFile;
    }

    /**
     * Sets the previewFile.
     *
     * <p>You can use getPreviewFile() to get the value of previewFile</p>
     *
     * @param previewFile previewFile
     */
    public void setPreviewFile(Long previewFile) {
        this.previewFile = previewFile;
    }

    /**
     * 关联信息（表头模板ID）
     *
     * @return $ref id
     */
    @JsonProperty(value = "headerTemplateId", access = READ_ONLY)
    public ReferenceData getHeaderTemplateIdByRef() {
        return this.headerTemplateId == null
            ? null
            : new ReferenceData(this.headerTemplateId);
    }

    /**
     * 关联信息（签字栏ID）
     *
     * @return $ref id
     */
    @JsonProperty(value = "signatureTemplateId", access = READ_ONLY)
    public ReferenceData getSignatureTemplateIdByRef() {
        return this.signatureTemplateId == null
            ? null
            : new ReferenceData(this.signatureTemplateId);
    }
}
