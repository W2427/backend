package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.numeric.PressureDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.setting.CleanMethodType;
import com.ose.util.StringUtils;
import com.ose.vo.unit.PressureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 清洁包实体数据实体基类。
 */
@Entity
@Table(name = "entity_clean_package",
    indexes = {
        @Index(columnList = "no,projectId,deleted")
    })
public class CleanPackageEntityBase extends WBSEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = -7087823127044774929L;

    @Schema(description = "版本号")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "清洁压力")
    @Column(nullable = false)
    private Double cleanPressure;

    @Schema(description = "清洁压力表示值")
    @Column(nullable = false)
    private String cleanPressureText;

    @Schema(description = "清洁压力单位")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PressureUnit cleanPressureUnit;

    @Schema(description = "清洁介质")
    @Column(nullable = false)
    private String cleanMedium;

    @Schema(description = "清洁包文件号")
    @Column(nullable = false)
    private String clpDrawingNo;

    @Schema(description = "清洁方式")
    @Column
    @Enumerated(EnumType.STRING)
    private CleanMethodType cleanMethod;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    @JsonCreator
    public CleanPackageEntityBase() {
        this(null);
    }

    public CleanPackageEntityBase(Project project) {
        super(project);
        setEntityType("CLEAN_PACKAGE");
    }

    @Override
    public String getEntitySubType() {
        return null;
    }

    @Override
    public String getEntityBusinessType() {
        return null;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getCleanPressureText() {
        return cleanPressureText;
    }

    public void setCleanPressureText(String cleanPressureText) {
        this.cleanPressureText = cleanPressureText;
    }

    @JsonGetter
    public PressureUnit getCleanPressureUnit() {
        return cleanPressureUnit;
    }

    @JsonSetter
    public void setCleanPressureUnit(PressureUnit cleanPressureUnit) {
        this.cleanPressureUnit = cleanPressureUnit;
    }

    public void setCleanPressureUnit(String cleanPressureUnit) {
        this.cleanPressureUnit = PressureUnit.getByName(cleanPressureUnit);
    }

    public Double getCleanPressure() {
        return cleanPressure;
    }

    @JsonSetter
    public void setCleanPressure(Double cleanPressure) {
        this.cleanPressure = cleanPressure;
    }

    /**
     * 设定清洁压力。
     *
     * @param cleanPressure 清洁压力
     */
    public void setCleanPressure(String cleanPressure) {

        PressureDTO pressureDTO = new PressureDTO(
            this.cleanPressureUnit,
            cleanPressure
        );

        this.cleanPressureText = cleanPressure;
        this.cleanPressure = pressureDTO.getValue();
        this.cleanPressureUnit = pressureDTO.getUnit();
    }

    public String getCleanMedium() {
        return cleanMedium;
    }

    public void setCleanMedium(String cleanMedium) {
        this.cleanMedium = cleanMedium;
    }

    public String getClpDrawingNo() {
        return clpDrawingNo;
    }

    public void setClpDrawingNo(String clpDrawingNo) {
        this.clpDrawingNo = clpDrawingNo;
    }

    public CleanMethodType getCleanMethod() {
        return cleanMethod;
    }

    @JsonSetter
    public void setCleanMethod(String cleanMethod) {
        if (!StringUtils.isEmpty(cleanMethod)) {
            this.cleanMethod = CleanMethodType.getByName(cleanMethod);
        }
    }

    public void setCleanMethod(CleanMethodType cleanMethod) {
        this.cleanMethod = cleanMethod;
    }

    @Override
    public String getVariableName() {
        return "CLEAN_PACKAGE";
    }


    @Override
    public String getName() {
        return "CleanPackageEntityBase";
    }


}
