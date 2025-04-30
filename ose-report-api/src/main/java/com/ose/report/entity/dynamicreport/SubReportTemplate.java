package com.ose.report.entity.dynamicreport;

import com.ose.entity.BaseBizEntity;
import com.ose.report.vo.Position;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * 报表子模板实体类。
 */
@Entity
@Table(name = "sub_report_template")
public class SubReportTemplate extends BaseBizEntity {


    private static final long serialVersionUID = 4890065996402529624L;
    @Column
    private Long projectId;

    @Column
    private String subReportType;//repeat one-go

    // 模板名称
    @Column(nullable = false, length = 128)
    @NotNull(message = "report template's name is required")
    private String name;

    // 固定高度
    @Column(length = 4)
    private int fixedHeight;

    // 固定高度
    @Column(length = 4)
    private int fixedWidth;

    @Column
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column
    private String filePath;

    @Column
    private Long fileId;

    // 模板文件
    @Column(length = 128)
    private String remark;

    @Column
    private String templateDesc;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSubReportType() {
        return subReportType;
    }

    public void setSubReportType(String subReportType) {
        this.subReportType = subReportType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    public int getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedWidth(int fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
