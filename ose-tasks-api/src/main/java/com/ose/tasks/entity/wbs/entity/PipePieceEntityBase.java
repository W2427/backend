package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.entity.Project;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 管段实体数据实体。
 */
@MappedSuperclass
public class PipePieceEntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -7132756902011315357L;

    @Schema(description = "父级工作包号")
    @Column
    private String workPackageNo;

    @Schema(description = "所属管线实体 ID")
    @Column
    private Long isoEntityId;

    @Schema(description = "所属单管实体 ID")
    @Column
    private Long spoolEntityId;

    @Schema(description = "坡口#1代码")
    @Column
    private String bevelCode1;

    @Schema(description = "坡口#2代码")
    @Column
    private String bevelCode2;

    @Schema(description = "弯管信息")
    @Column
    private String bendInfo;

    @Schema(description = "下料图纸")
    @Column
    private String cutDrawing;

    @Schema(description = "版次号")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "材质代码")
    @Column(nullable = false)
    private String materialCode;

    @Schema(description = "材质描述")
    @Column(nullable = false)
    private String material;

    @Schema(description = "NPS 表示值")
    @Column
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    @Column
    private Double nps;

    @Schema(description = "长度表示值")
    @Column
    private String lengthText;

    @Schema(description = "长度单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "长度")
    @Column(nullable = false)
    private Double length;

    @Schema(description = "所属管线实体编号")
    @Column
    private String isoNo;

    @Schema(description = "所属单管实体编号")
    @Column
    private String spoolNo;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "管道等级")
    @Column
    private String pipeClass;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    @Schema(description = "二维码")
    @Column
    private String qrCode;



    @JsonCreator
    public PipePieceEntityBase() {
        this(null);
    }

    public PipePieceEntityBase(Project project) {
        super(project);
        setEntityType("PIPE_PIECE");
    }

    public Long getIsoEntityId() {
        return isoEntityId;
    }

    public void setIsoEntityId(Long isoEntityId) {
        this.isoEntityId = isoEntityId;
    }

    public Long getSpoolEntityId() {
        return spoolEntityId;
    }

    public void setSpoolEntityId(Long spoolEntityId) {
        this.spoolEntityId = spoolEntityId;
    }

    public String getBevelCode1() {
        return bevelCode1;
    }

    public void setBevelCode1(String bevelCode1) {
        this.bevelCode1 = bevelCode1;
    }

    public String getBevelCode2() {
        return bevelCode2;
    }

    public void setBevelCode2(String bevelCode2) {
        this.bevelCode2 = bevelCode2;
    }

    public String getBendInfo() {
        return bendInfo;
    }

    public void setBendInfo(String bendInfo) {
        this.bendInfo = bendInfo;
    }

    public String getCutDrawing() {
        return cutDrawing;
    }

    public void setCutDrawing(String cutDrawing) {
        this.cutDrawing = cutDrawing;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public void setNpsUnit(String npsUnit) {
        this.npsUnit = LengthUnit.getByName(npsUnit);
    }

    public Double getNps() {
        return nps;
    }

    @JsonSetter
    public void setNps(Double nps) {
        this.nps = nps;
    }

    /**
     * 设定nps值。
     *
     * @param nps nps值
     */
    public void setNps(String nps) {
        this.npsText = nps;
        LengthDTO lengthDTO = new LengthDTO(this.npsUnit, nps);
        this.npsUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.nps = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    @JsonSetter
    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = LengthUnit.getByName(lengthUnit);
    }

    public Double getLength() {
        return length;
    }

    @JsonSetter
    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * 设定长度值。
     *
     * @param length 长度
     */
    public void setLength(String length) {
        this.lengthText = length;
        LengthDTO lengthDTO = new LengthDTO(this.lengthUnit, length);
        this.lengthUnit = lengthDTO.getUnit();
        this.length = lengthDTO.getMillimeters();
    }

    @Override
    public String getEntitySubType() {
        return "PIPE_PIECE";
    }

    @Override
    public String getEntityBusinessType() {
        return "PIPE_PIECE";
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getWorkPackageNo() {
        return workPackageNo;
    }

    public void setWorkPackageNo(String workPackageNo) {
        this.workPackageNo = workPackageNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
