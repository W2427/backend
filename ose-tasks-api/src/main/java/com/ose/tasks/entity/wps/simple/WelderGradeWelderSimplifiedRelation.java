package com.ose.tasks.entity.wps.simple;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "welder_grade_welder_relation_simplified")
public class WelderGradeWelderSimplifiedRelation extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8255656504104969554L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "焊工编号")
    @Column
    private String welderNo;

    @Schema(description = "焊工id")
    @Column
    private Long welderId;

    @Schema(description = "焊工等级编号")
    @Column
    private String welderGradeNo;

    @Schema(description = "焊工等级id")
    @Column
    private Long welderGradeId;

    @Schema(description = "备注")
    @Column
    private String remark;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getWelderGradeNo() {
        return welderGradeNo;
    }

    public void setWelderGradeNo(String welderGradeNo) {
        this.welderGradeNo = welderGradeNo;
    }

    public Long getWelderGradeId() {
        return welderGradeId;
    }

    public void setWelderGradeId(Long welderGradeId) {
        this.welderGradeId = welderGradeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
