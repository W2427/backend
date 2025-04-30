package com.ose.report.entity.dynamicreport;

import com.ose.entity.BaseBizEntity;
import com.ose.report.vo.Position;

import jakarta.persistence.*;

/**
 * 报表-子报告模板关系类。
 */
@Entity
@Table(name = "report_composition")
public class ReportComposition extends BaseBizEntity {


    private static final long serialVersionUID = -4900092139361770777L;
    @Column
    private Long projectId;

    @Column
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column
    private int detailCnt;

    @Column
    private int sort;

    @Column
    private Long subReportTemplateId;

    @Column
    private Long reportTemplateId;

    // 模板文件
    @Column(length = 128)
    private String remark;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Long getSubReportTemplateId() {
        return subReportTemplateId;
    }

    public void setSubReportTemplateId(Long subReportTemplateId) {
        this.subReportTemplateId = subReportTemplateId;
    }

    public Long getReportTemplateId() {
        return reportTemplateId;
    }

    public void setReportTemplateId(Long reportTemplateId) {
        this.reportTemplateId = reportTemplateId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDetailCnt() {
        return detailCnt;
    }

    public void setDetailCnt(int detailCnt) {
        this.detailCnt = detailCnt;
    }
}
