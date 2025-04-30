package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import com.ose.tasks.vo.qc.NDTExecuteFlag;
import com.ose.util.StringUtils;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 结构焊口实体数据实体。
 */
@MappedSuperclass
public class StructureWeldEntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -2935303123767473807L;

    private static final String COMMA = ",";

    @Schema(description = "父级工作包号")
    @Column
    private String workPackageNo;

    @Schema(description = "层级标识")
    @Column
    private String hierachyParent;

    @Schema(description = "实施阶段，F(abrication),A(ssemble),E(rection),C(ompletion)")
    @Column(nullable = false, length = 64)
    private String stage;

    @Schema(description = "焊口类型代码")
    @Column(nullable = false, length = 64)
    private String weldType;

    @Schema(description = "焊口实体子类型")
    @Column(nullable = false, length = 64)
    private String weldEntityType;

    @Schema(description = "焊口版本")
    @Column(nullable = true)
    private String revision;

    @Schema(description = "工作项代码")
    @Column
    private String workClass;

    @Schema(description = "焊点图纸号")
    @Column
    private String weldingMapNo;

    @Schema(description = "焊接等级")
    @Column
    private String inspectionClass;

    @Schema(description = "焊接工艺编号")
    @Column
    private String wpsNo;

    @Schema(description = "焊接工艺ID")
    @Column
    private Long wpsId;

    @Schema(description = "VT比例")
    @Column
    private Double vtValue;

    @Schema(description = "RT比例")
    @Column
    private Double rtValue;

    @Schema(description = "UT比例")
    @Column
    private Double utValue;

    @Schema(description = "MT比例")
    @Column
    private Double mtValue;

    @Schema(description = "PT比例")
    @Column
    private Double ptValue;

    @Schema(description = "PAUT比例")
    @Column
    private Double pautValue;

    @Override
    public String getEntityBusinessType() {
        return entityBusinessType;
    }

    public void setEntityBusinessType(String entityBusinessType) {
        this.entityBusinessType = entityBusinessType;
    }

    @Schema(description = "焊口长度 文本")
    @Column
    private String lengthText;

    @Schema(description = "焊口长度 单位")
    @Column
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "焊口长度")
    @Column
    private Double length;

    @Schema(description = "片体号1")
    @Column(name = "wp03_no_1")
    private String wp03No1;

    @Schema(description = "构件号1")
    @Column(name = "wp04_no_1")
    private String wp04No1;

    @Schema(description = "零件号1")
    @Column(name = "wp05_no_1")
    private String wp05No1;

    @Schema(description = "板厚1 文本")
    @Column(name = "thickness_1_text")
    private String thickness1Text;

    @Schema(description = "板厚1 单位")
    @Column(length = 200)
    @Enumerated(EnumType.STRING)
    private LengthUnit thickness1Unit;

    @Schema(description = "thickness1")
    @Column(nullable = false)
    private Double thickness1;

    @Schema(description = "材质等级1")
    @Column(name = "material_grade_1")
    private String materialGrade1;

    @Schema(description = "片体号2")
    @Column(name = "wp03_no_2")
    private String wp03No2;

    @Schema(description = "构件号2")
    @Column(name = "wp04_no_2")
    private String wp04No2;

    @Schema(description = "零件号2")
    @Column(name = "wp05_no_2")
    private String wp05No2;

    @Schema(description = "零件号1ID")
    @Column(name = "part1_id")
    private Long part1Id;

    @Schema(description = "零件号2ID")
    @Column(name = "part2_id")
    private Long part2Id;

    @Schema(description = "板厚2 文本")
    @Column(name = "thickness_2_text")
    private String thickness2Text;

    @Schema(description = "板厚2 单位")
    @Column(length = 200)
    @Enumerated(EnumType.STRING)
    private LengthUnit thickness2Unit;

    @Schema(description = "thickness1")
    @Column(nullable = false)
    private Double thickness2;

    @Schema(description = "材质等级2")
    @Column(name = "material_grade_2")
    private String materialGrade2;

    @Schema(description = "油漆代码")
    @Column
    private String paintCode;

    @Schema(description = "焊口标签")
    @Column
    @JsonIgnore
    private String weldMarks;

    @Schema(description = "焊口显示标签")
    @Transient
    private List<String> displayWeldMarks;

    @Schema(description = "RT结果")
    @Column
    private String rtResult;

    @Schema(description = "UT结果")
    @Column
    private String utResult;

    @Schema(description = "PT结果")
    @Column
    private String ptResult;

    @Schema(description = "MT结果")
    @Column
    private String mtResult;

    @Schema(description = "最新无损探伤结果 ID")
    @Column
    private Long ndtId;

    @Schema(description = "最新外观检测结果 ID")
    @Column
    private Long visualId;

    @Schema(description = "最新焊接结果 ID")
    @Column
    private Long weldingId;

    @Schema(description = "最新组对结果 ID")
    @Column
    private Long fitupId;

    @Schema(description = "最新无损探伤结果")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private ActivityExecuteResult ndtResult;

    @Schema(description = "ndt执行结果")
    @Column(length = 16)
    private NDTExecuteFlag ndtExecuteFlag;

    @Schema(description = "返修次数")
    @Column(columnDefinition = "INT default 0")
    private Integer repairCount = 0;

    @Schema(description = "扩口次数")
    @Column(columnDefinition = "INT default 0")
    private Integer penaltyCount = 0;

    @Schema(description = "焊工ID")
    @Column
    private Long welderId;

    @Schema(description = "焊工IDs")
    @Column
    private String welderIds;

    @Schema(description = "焊接时间")
    @Transient
    private Date weldTime;

    @Schema(description = "焊口实体业务类型")
    @Column(length = 64)
    private String entityBusinessType;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    @JsonCreator
    public StructureWeldEntityBase() {
        this(null);
    }

    public StructureWeldEntityBase(Project project) {
        super(project);
//        setWbsEntityType(WBSEntityType.STRUCT_WELD_JOINT);
        setEntityType("STRUCT_WELD_JOINT");
    }

    public String getStage() {
        return stage;
    }

    @JsonSetter
    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getWeldType() {
        return weldType;
    }

    @JsonSetter
    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getWeldEntityType() {
        return weldEntityType;
    }


    /**
     * 设定焊口实体类型。
     * 需要在焊口的焊接类型和焊接阶段之后赋值
     */
    public void setWeldEntityType(String weldEntityType) {
        this.weldEntityType = weldEntityType;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getWeldMarks() {
        return weldMarks;
    }

    public void setWeldMarks(String weldMarks) {
        this.weldMarks = weldMarks;
        if (!StringUtils.isEmpty(weldMarks)) {
            this.displayWeldMarks = Arrays.asList(weldMarks.split(COMMA));
        }
    }

    public List<String> getDisplayWeldMarks() {
        if (displayWeldMarks != null && displayWeldMarks.size() != 0) {
            return displayWeldMarks;
        } else if (!StringUtils.isEmpty(weldMarks)) {
            displayWeldMarks = Arrays.asList(weldMarks.split(COMMA));
            return displayWeldMarks;
        }
        return null;
    }

    public void setDisplayWeldMarks(List<String> displayWeldMarks) {
        this.displayWeldMarks = displayWeldMarks;
        if (displayWeldMarks != null && displayWeldMarks.size() != 0) {
            this.weldMarks = org.apache.commons.lang.StringUtils.join(displayWeldMarks.toArray(), COMMA);
        }
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public Long getWpsId() {
        return wpsId;
    }

    public void setWpsId(Long wpsId) {
        this.wpsId = wpsId;
    }

    public String getHierachyParent() {
        return hierachyParent;
    }

    public void setHierachyParent(String hierachyParent) {
        this.hierachyParent = hierachyParent;
    }

    public Integer getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(Integer repairCount) {
        this.repairCount = repairCount;
    }

    public Long getNdtId() {
        return ndtId;
    }

    public void setNdtId(Long ndtId) {
        this.ndtId = ndtId;
    }

    public Long getVisualId() {
        return visualId;
    }

    public void setVisualId(Long visualId) {
        this.visualId = visualId;
    }

    public Long getWeldingId() {
        return weldingId;
    }

    public void setWeldingId(Long weldingId) {
        this.weldingId = weldingId;
    }

    public String getEntitySubType() {
        return this.weldEntityType;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public ActivityExecuteResult getNdtResult() {
        return ndtResult;
    }

    public void setNdtResult(ActivityExecuteResult ndtResult) {
        this.ndtResult = ndtResult;
    }

    public NDTExecuteFlag getNdtExecuteFlag() {
        return ndtExecuteFlag;
    }

    public void setNdtExecuteFlag(NDTExecuteFlag ndtExecuteFlag) {
        this.ndtExecuteFlag = ndtExecuteFlag;
    }

    public Date getWeldTime() {
        return weldTime;
    }

    public void setWeldTime(Date weldTime) {
        this.weldTime = weldTime;
    }

    public Integer getPenaltyCount() {
        return penaltyCount;
    }

    public void setPenaltyCount(Integer penaltyCount) {
        this.penaltyCount = penaltyCount;
    }

    public Long getFitupId() {
        return fitupId;
    }

    public void setFitupId(Long fitupId) {
        this.fitupId = fitupId;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = LengthUnit.getByName(lengthUnit);
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    @JsonSetter
    public void setLength(String length) {
        this.lengthText = length;
        LengthDTO lengthDTO = new LengthDTO(this.lengthUnit, length);
        this.lengthUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.length = mm.doubleValue();
    }

    public String getRtResult() {
        return rtResult;
    }

    public void setRtResult(String rtResult) {
        this.rtResult = rtResult;
    }

    public String getUtResult() {
        return utResult;
    }

    public void setUtResult(String utResult) {
        this.utResult = utResult;
    }

    public String getPtResult() {
        return ptResult;
    }

    public void setPtResult(String ptResult) {
        this.ptResult = ptResult;
    }

    public String getMtResult() {
        return mtResult;
    }

    public void setMtResult(String mtResult) {
        this.mtResult = mtResult;
    }

//    public String getDwgNo() {
//        return dwgNo;
//    }
//
//    @JsonSetter
//    public void setDwgNo(String dwgNo) {
//        this.dwgNo = dwgNo;
//    }
//
//    public void setDwgNo(BpmEntityCategoryType bpmEntityCategoryType, Project project,
//                         Map<String, Map<String, String>> codeMap) {
//
//        String wp01_part = wp01No;
//        String wp02_part = "";
//        String wp03_part = "";
//        String wp04_part = "";
//
//        String dwgPattern = bpmEntityCategoryType.getDwgPattern();
//        if (StringUtils.isEmpty(dwgPattern)) {
//            return;
//        }
//
//        if (project.isStructureAssembleParent()) {
//            if (!StringUtils.isEmpty(wp02No)) {
//                wp02_part = wp02No.replaceFirst(StringUtils.escapeExprSpecialWord(wp01No) + "-", "");
//            }
//            if (!StringUtils.isEmpty(wp03No)) {
//                wp03_part = wp03No.replaceFirst(StringUtils.escapeExprSpecialWord(wp02No) + "-", "");
//            }
//            if (!StringUtils.isEmpty(wp04No)) {
//                wp04_part = wp04No.replaceFirst(StringUtils.escapeExprSpecialWord(wp03No) + "-", "");
//            }
//
//        } else {
//            wp02_part = wp02No;
//            wp03_part = wp03No;
//            wp04_part = wp04No;
//        }
//
//        //Replace project No
//        dwgPattern.replaceFirst("\\{PROJECT_NO\\}", project.getCode());
//
//        dwgPattern.replaceFirst("\\{WP01_NO\\}", wp01_part);
//        dwgPattern.replaceFirst("\\{WP02_CODE\\}", codeMap.get("WP02").get(wp02_part));
//        dwgPattern.replaceFirst("\\{WP03_CODE\\}", codeMap.get("WP03").get(wp03_part));
//        dwgPattern.replaceFirst("\\{WP04_CODE\\}", codeMap.get("WP04").get(wp04_part));
//
//        setDwgNo(dwgPattern);
//
//    }

    public String getWorkPackageNo() {
        return workPackageNo;
    }

    public void setWorkPackageNo(String workPackageNo) {
        this.workPackageNo = workPackageNo;
    }

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass;
    }

    public String getWeldingMapNo() {
        return weldingMapNo;
    }

    public void setWeldingMapNo(String weldingMapNo) {
        this.weldingMapNo = weldingMapNo;
    }

    public String getInspectionClass() {
        return inspectionClass;
    }

    public void setInspectionClass(String inspectionClass) {
        this.inspectionClass = inspectionClass;
    }

    public String getWp04No1() {
        return wp04No1;
    }

    public void setWp04No1(String wp04No1) {
        this.wp04No1 = wp04No1;
    }

    public String getWp05No1() {
        return wp05No1;
    }

    public void setWp05No1(String wp05No1) {
        this.wp05No1 = wp05No1;
    }

    public String getMaterialGrade1() {
        return materialGrade1;
    }

    public void setMaterialGrade1(String materialGrade1) {
        this.materialGrade1 = materialGrade1;
    }

    public String getWp04No2() {
        return wp04No2;
    }

    public void setWp04No2(String wp04No2) {
        this.wp04No2 = wp04No2;
    }

    public String getWp05No2() {
        return wp05No2;
    }

    public void setWp05No2(String wp05No2) {
        this.wp05No2 = wp05No2;
    }

    public String getMaterialGrade2() {
        return materialGrade2;
    }

    public void setMaterialGrade2(String materialGrade2) {
        this.materialGrade2 = materialGrade2;
    }

    public Double getVtValue() {
        return vtValue;
    }

    public void setVtValue(Double vtValue) {
        this.vtValue = vtValue;
    }

    public Double getRtValue() {
        return rtValue;
    }

    public void setRtValue(Double rtValue) {
        this.rtValue = rtValue;
    }

    public Double getUtValue() {
        return utValue;
    }

    public void setUtValue(Double utValue) {
        this.utValue = utValue;
    }

    public Double getMtValue() {
        return mtValue;
    }

    public void setMtValue(Double mtValue) {
        this.mtValue = mtValue;
    }

    public Double getPtValue() {
        return ptValue;
    }

    public void setPtValue(Double ptValue) {
        this.ptValue = ptValue;
    }

    public Double getPautValue() {
        return pautValue;
    }

    public void setPautValue(Double pautValue) {
        this.pautValue = pautValue;
    }


    public String getWelderIds() {
        return welderIds;
    }

    public void setWelderIds(String welderIds) {
        this.welderIds = welderIds;
    }

    public String getThickness1Text() {
        return thickness1Text;
    }

    public void setThickness1Text(String thickness1Text) {
        this.thickness1Text = thickness1Text;
    }

    public LengthUnit getThickness1Unit() {
        return thickness1Unit;
    }

    public void setThickness1Unit(String thickness1Unit) {
        this.thickness1Unit = LengthUnit.getByName(thickness1Unit);
    }

    public Double getThickness1() {
        return thickness1;
    }

    public void setThickness1(String thickness1) {
        this.thickness1Text = thickness1;
        LengthDTO lengthDTO = new LengthDTO(this.thickness1Unit, thickness1);
        this.thickness1Unit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.thickness1 = mm.doubleValue();
    }

    public String getThickness2Text() {
        return thickness2Text;
    }

    public void setThickness2Text(String thickness2Text) {
        this.thickness2Text = thickness2Text;
    }

    public LengthUnit getThickness2Unit() {
        return thickness2Unit;
    }

    public void setThickness2Unit(String thickness2Unit) {
        this.thickness2Unit = LengthUnit.getByName(thickness2Unit);
    }

    public void setThickness2(String thickness2) {
        this.thickness2Text = thickness2;
        LengthDTO lengthDTO = new LengthDTO(this.thickness2Unit, thickness2);
        this.thickness2Unit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.thickness2 = mm.doubleValue();
    }

    public Double getThickness2() {
        return thickness2;
    }

    public void setThickness2(Double thickness2) {
        this.thickness2 = thickness2;
    }

    @JsonProperty(value = "welderIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonWelderIds() {
        if (StringUtils.isEmpty(welderIds)) {
            return new ArrayList<>();
        }

        return StringUtils.decode(welderIds, new TypeReference<List<Long>>() {
        });

    }

    @JsonIgnore
    public void setJsonWelderIds(List<Long> welderIds) {
        if (welderIds != null) {
            this.welderIds = StringUtils.toJSON(welderIds);
        }
    }

    public void setThickness1(Double thickness1) {
        this.thickness1 = thickness1;
    }

    public Long getPart1Id() {
        return part1Id;
    }

    public void setPart1Id(Long part1Id) {
        this.part1Id = part1Id;
    }

    public Long getPart2Id() {
        return part2Id;
    }

    public void setPart2Id(Long part2Id) {
        this.part2Id = part2Id;
    }

    public String getWp03No1() {
        return wp03No1;
    }

    public void setWp03No1(String wp03No1) {
        this.wp03No1 = wp03No1;
    }

    public String getWp03No2() {
        return wp03No2;
    }

    public void setWp03No2(String wp03No2) {
        this.wp03No2 = wp03No2;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
