package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.numeric.AreaDTO;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.dto.numeric.WeightDTO;
import com.ose.vo.unit.AreaUnit;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.WeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 交接单实体
 */
@Entity
@Table(name = "bpm_delivery_entity",
indexes = {
    @Index(columnList = "orgId,projectId,deliveryId,status"),
    @Index(columnList = "projectId,entityId,status")
})
public class BpmDeliveryEntity extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8635813266138087045L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "交接单id")
    private Long deliveryId;

    @Schema(description = "交接单no")
    private String deliveryNo;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "二维码")
    private String qrCode;

    @Column(nullable = true, columnDefinition = "bit default b'0'")
    @Schema(description = "打印标识")
    private boolean printFlag = false;

    @Schema(description = "实体模块名称")
    private String entityModuleName;

    @Schema(description = "实体模块ID")
    private Long entityModuleProjectNodeId;

    @Schema(description = "层名称")
    private String layerPackageName;

    @Schema(description = "层ID")
    private Long layerPackageId;

    @Schema(description = "任务包名")
    private String taskPackageName;

    @Schema(description = "任务包id")
    private Long taskPackageId;

    @Schema(description = "油漆代码")
    private String paintingCode;

    @Schema(description = "试压包")
    private String pressureTestPackage;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "长度")
    private String length;

    @Schema(description = "配送标识 NG Flag")
    private boolean executeNgFlag;

    @Schema(description = "实体类型")
    @Column(length = 32)

    private String entityType;

    @Column(nullable = false)
    @JsonIgnore
    @Schema(description = "操作人员id")
    private Long operatorId;

    @Column(nullable = false, length = 255)
    @Schema(description = "操作人员名称")
    private String operatorName;

    @Column(nullable = true)
    @Schema(description = "打印标识")
    private Boolean printFlg;

    @Schema(description = "手动创建标识")
    @Column
    private Boolean manuallyCreated = false;

    @Column
    @Schema(description = "材质")
    private String material;

    @Schema(description = "油漆面积")
    @Column
    private Double paintingArea;

    @Schema(description = "油漆面积单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private AreaUnit paintingAreaUnit;

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

    @Schema(description = "重量")
    @Column
    private Double weight;

    @Schema(description = "重量表示值")
    @Column
    private String weightText;

    @Schema(description = "专业")
    @Column
    private String discipline = "PIPING";

    @Schema(description = "重量单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    //实体类型
    @ManyToOne
    @JoinColumn(name = "entity_sub_type_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmEntitySubType entitySubType;

    //工序
    @ManyToOne
    @JoinColumn(name = "process_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmProcess process;

    @Transient
    private boolean printEnableFlag = true;

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

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public BpmEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(BpmEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public BpmProcess getProcess() {
        return process;
    }

    public void setProcess(BpmProcess process) {
        this.process = process;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public boolean isExecuteNgFlag() {
        return executeNgFlag;
    }

    public void setExecuteNgFlag(boolean executeNgFlag) {
        this.executeNgFlag = executeNgFlag;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEntityModuleName() {
        return entityModuleName;
    }

    public void setEntityModuleName(String entityModuleName) {
        this.entityModuleName = entityModuleName;
    }

    public Long getEntityModuleProjectNodeId() {
        return entityModuleProjectNodeId;
    }

    public void setEntityModuleProjectNodeId(Long entityModuleProjectNodeId) {
        this.entityModuleProjectNodeId = entityModuleProjectNodeId;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public boolean isPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(boolean printFlag) {
        this.printFlag = printFlag;
    }

    public boolean isPrintEnableFlag() {
        return printEnableFlag;
    }

    public void setPrintEnableFlag(boolean printEnableFlag) {
        this.printEnableFlag = printEnableFlag;
    }

    public String getPressureTestPackage() {
        return pressureTestPackage;
    }

    public void setPressureTestPackage(String pressureTestPackage) {
        this.pressureTestPackage = pressureTestPackage;
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

    public String getWeightText() {
        return weightText;
    }

    public void setWeightText(String weightText) {
        this.weightText = weightText;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    @JsonSetter
    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = WeightUnit.getByName(weightUnit);
    }

    public Double getWeight() {
        return weight;
    }

    @JsonSetter
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 设定重量。
     *
     * @param weight 重量
     */
    public void setWeight(String weight) {
        WeightDTO weightDTO = new WeightDTO(this.weightUnit, weight);
        this.weightText = weight;
        this.weight = weightDTO.getValue();
        this.weightUnit = weightDTO.getUnit();
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
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

    public Boolean getPrintFlg() {
        return printFlg;
    }

    public void setPrintFlg(Boolean printFlg) {
        this.printFlg = printFlg;
    }

    public Boolean getManuallyCreated() {
        return manuallyCreated;
    }

    public void setManuallyCreated(Boolean manuallyCreated) {
        this.manuallyCreated = manuallyCreated;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getPaintingArea() {
        return paintingArea;
    }

    public void setPaintingArea(Double paintingArea) {
        this.paintingArea = paintingArea;
    }

    public AreaUnit getPaintingAreaUnit() {
        return paintingAreaUnit;
    }

    @JsonSetter
    public void setPaintingAreaUnit(AreaUnit paintingAreaUnit) {
        this.paintingAreaUnit = paintingAreaUnit;
    }

    public void setPaintingAreaUnit(String paintingAreaUnit) {
        this.paintingAreaUnit = AreaUnit.getByName(paintingAreaUnit);
    }

    /**
     * 设定油漆面积。
     *
     * @param paintingArea 油漆面积
     */
    public void setPaintingArea(String paintingArea) {
        AreaDTO areaDTO = new AreaDTO(this.paintingAreaUnit, paintingArea);
        this.paintingArea = areaDTO.getValue();
        this.paintingAreaUnit = areaDTO.getUnit();
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getLayerPackageName() {
        return layerPackageName;
    }

    public void setLayerPackageName(String layerPackageName) {
        this.layerPackageName = layerPackageName;
    }

    public Long getLayerPackageId() {
        return layerPackageId;
    }

    public void setLayerPackageId(Long layerPackageId) {
        this.layerPackageId = layerPackageId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
