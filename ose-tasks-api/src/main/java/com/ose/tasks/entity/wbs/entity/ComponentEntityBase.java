package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.entity.Project;
import com.ose.util.StringUtils;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 管件实体数据实体。
 */
@MappedSuperclass
public class ComponentEntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -426367219257910579L;

    private static final Pattern THICKNESS = Pattern.compile(
        "(S-[\\d]{1,3}s?)|(X{1,3}S)|(STD)|(N/A)|(\\d+(\\.\\d+)?('|\"|ft|in|mm|cm|m).*)|(\\d+(\\.\\d+)?(mm|cm|m)\\((\\d+)/(\\d+)('|\"|ft|in)\\)\\s*THCK)|(\\d+(\\.\\d+)?)",
        Pattern.CASE_INSENSITIVE
    );

    @Schema(description = "父级工作包号")
    @Column
    private String workPackageNo;

    @Schema(description = "所属管线实体 ID")
    @Column
    private Long isoEntityId;

    @Schema(description = "所属单管实体 ID")
    @Column
    private Long spoolEntityId;

    @Schema(description = "管件短代码")
    @Column(nullable = false)
    private String shortCode;

    @Schema(description = "管件实体类型")
    @Column(nullable = false, length = 64)
    private String componentEntityType;

    @Schema(description = "管件实体业务类型")
    @Column(length = 64)
    private String entityBusinessType;

    @Schema(description = "材料描述")
    @Column
    private String material;

    @Schema(description = "管件材料编码")
    @Column
    private String materialCode;

    @Schema(description = "管件数量")
    @Column
    private Integer qty;

    @Schema(description = "管件数量单位")
    @Column
    private String qtyUnit;

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

    @Schema(description = "壁厚等级")
    @Column
    private String thickness;

    @Schema(description = "法兰管理")
    @Column
    private Boolean flangeManagement;

    @Schema(description = "版本号")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "管道等级")
    @Column
    private String pipeClass;

    @Schema(description = "绝缘代号")
    @Column
    private String insulationCode;

    @Schema(description = "页码")
    @Column
    private Integer sheetNo;

    @Schema(description = "总页数")
    @Column
    private Integer sheetTotal;

    @Schema(description = "ISO 图纸号 ")
    @Column
    private String isoDrawing;

    @Schema(description = "坐标 X ")
    @Column
    private String coordinateX;

    @Schema(description = "坐标 Y ")
    @Column
    private String coordinateY;

    @Schema(description = "坐标 Z ")
    @Column
    private String coordinateZ;

    @Schema(description = "预制类别")
    @Column
    private String fabricated;

    @Schema(description = "是否表面处理")
    @Column
    private Boolean surfaceTreatment = false;

    @Schema(description = "油漆代码")
    @Column
    private String paintingCode;

    @Schema(description = "节点表示名")
    @Column
    private String displayName;

    @Schema(description = "所属管线实体编号")
    @Column
    private String isoNo;

    @Schema(description = "所属单管实体编号")
    @Column
    private String spoolNo;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    @Schema(description = "位置标记")
    @Column
    private String materialWithPositionalMark;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    @JsonCreator
    public ComponentEntityBase() {
        this(null);
    }

    public ComponentEntityBase(Project project) {
        super(project);
        setEntityType("COMPONENT");
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

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @JsonGetter
    public String getNpsText() {
        return npsText;
    }

    @JsonSetter
    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    @JsonGetter
    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    /**
     * 设定nps单位。
     *
     * @param npsUnit nps单位
     */
    public void setNpsUnit(String npsUnit) {
        if (StringUtils.isEmpty(npsUnit)) {
            return;
        }
        this.npsUnit = LengthUnit.getByName(npsUnit);
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    /**
     * 设定nps值。
     *
     * @param nps nps值
     */
    @JsonSetter
    public void setNps(String nps) {
        // 管件的NPS可以为空
        if (StringUtils.isEmpty(nps)) {
            this.npsText = nps;
            this.npsUnit = null;
            this.nps = null;
            return;
        }
        this.npsText = nps;
        LengthDTO lengthDTO = new LengthDTO(this.npsUnit, nps);
        this.npsUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.nps = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
    }

    public String getThickness() {
        return thickness;
    }

    /**
     * 设定壁厚等级。
     *
     * @param thickness 壁厚等级
     */
    public void setThickness(String thickness) {
        // 壁厚等级 修改为可以为空
        if (!StringUtils.isEmpty(thickness)) {
            Matcher matcher = THICKNESS.matcher(thickness);

            if (matcher.find()) {
                this.thickness = matcher.group();
            }
        } else {
            // TODO 业务Error
        }
    }

    public Boolean getFlangeManagement() {
        return flangeManagement;
    }

    public void setFlangeManagement(Boolean flangeManagement) {
        this.flangeManagement = flangeManagement;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
    }

    public String getInsulationCode() {
        return insulationCode;
    }

    public void setInsulationCode(String insulationCode) {
        this.insulationCode = insulationCode;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getComponentEntityType() {
        return componentEntityType;
    }

    public void setComponentEntityType(String componentEntityType) {
        this.componentEntityType = componentEntityType;
    }

    @Override
    public String getEntityBusinessType() {
        return entityBusinessType;
    }

    public void setEntityBusinessType(String entityBusinessType) {
        this.entityBusinessType = entityBusinessType;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public Integer getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public Integer getSheetTotal() {
        return sheetTotal;
    }

    public void setSheetTotal(Integer sheetTotal) {
        this.sheetTotal = sheetTotal;
    }


    public String getIsoDrawing() {
        return isoDrawing;
    }

    public void setIsoDrawing(String isoDrawing) {
        this.isoDrawing = isoDrawing;
    }

    public String getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }

    public String getCoordinateZ() {
        return coordinateZ;
    }

    public void setCoordinateZ(String coordinateZ) {
        this.coordinateZ = coordinateZ;
    }

    public String getFabricated() {
        return fabricated;
    }

    public void setFabricated(String fabricated) {
        this.fabricated = fabricated;
    }

    public Boolean getSurfaceTreatment() {
        return surfaceTreatment;
    }

    public void setSurfaceTreatment(Boolean surfaceTreatment) {
        this.surfaceTreatment = surfaceTreatment;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    @Override
    public String getEntitySubType() {
        return this.componentEntityType;
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

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getMaterialWithPositionalMark() {
        return materialWithPositionalMark;
    }

    public void setMaterialWithPositionalMark(String materialWithPositionalMark) {
        this.materialWithPositionalMark = materialWithPositionalMark;
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
