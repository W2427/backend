package com.ose.tasks.entity.trident;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sub_system_detail_info")
public class SubSystemDetail extends BaseBizEntity {

    private static final long serialVersionUID = 7160935294404633062L;

    @Schema(description = "关键字：susSystemID + disciplineCode")
    @Column
    private Long subSystemId;

    @Column
    private Long projectId;

    @Column
    private Long operatorId;

    @Column
    private String operatorName;

    @Column
    private String disciplineCode;

    @Column
    private String detailType;

    @Column
    private String value;

    public Long getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(Long subSystemId) {
        this.subSystemId = subSystemId;
    }

    public String getDisciplineCode() {
        return disciplineCode;
    }

    public void setDisciplineCode(String disciplineCode) {
        this.disciplineCode = disciplineCode;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
