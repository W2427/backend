package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.dto.numeric.NDERatioDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.tasks.vo.qc.NDTExecuteFlag;
import com.ose.tasks.vo.qc.PMIExecuteFlag;
import com.ose.util.StringUtils;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 焊口实体数据实体。
 */
@MappedSuperclass
public class WeldEntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -2935303123767473807L;

    private static final Pattern RATIO_PATTERN = Pattern.compile(
        "^[0-9]+([.]{1}[0-9]{1,2})?(%?)$",
        Pattern.CASE_INSENSITIVE
    );

    private static final String COMMA = ",";

    @Schema(description = "父级工作包号")
    @Column
    private String workPackageNo;

    @Schema(description = "所属管线实体 ID")
    @Column
    private Long isoEntityId;

    @Schema(description = "焊工IDs")
    @Column
    private String welderIds;

    @Schema(description = "所属单管实体 ID")
    @Column
    private Long spoolEntityId;

    @Schema(description = "所属ISO号")
    @Column(nullable = false)
    private String isoNo;

    @Schema(description = "实施位置类型")
    @Column(nullable = false, length = 64)
    private String shopField;

    @Schema(description = "焊口类型代码")
    @Column(nullable = false, length = 64)
    private String weldType;

    @Schema(description = "焊口实体类型")
    @Column(nullable = false, length = 64)
    private String weldEntityType;

    @Schema(description = "焊口实体业务类型")
    @Column(length = 64)
    private String entityBusinessType;

    @Schema(description = "页码")
    @Column(nullable = false)
    private Integer sheetNo;

    @Schema(description = "总页数")
    @Column(nullable = false)
    private Integer sheetTotal;

    @Schema(description = "焊口版本")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "焊口标签")
    @Column
    @JsonIgnore
    private String weldMarks;

    @Schema(description = "焊口显示标签")
    @Transient
    private List<String> displayWeldMarks;

    @Schema(description = "焊接程序编号")
    @Column
    private String wpsNo;

    @Schema(description = "焊接程序ID")
    @Column
    private String wpsId;

    @Schema(description = "是否手动编辑过")
    @Column
    private Boolean wpsEdited;

    @Schema(description = "无损探伤类型")
    @Column
    @Enumerated(EnumType.STRING)
    private NDEType nde;

    @Schema(description = "无损探伤比例")
    @Column
    private Integer ndeRatio;

    @Schema(description = "是否执行焊接后热处理")
    @Column
    private Boolean pwht;

    @Schema(description = "是否执行硬度测试")
    @Column
    private Boolean hardnessTest;

    // TODO remove this property
    @Schema(description = "材质分析抽取比例")
    @Column
    private Integer pmiRatio;

    @Schema(description = "管道等级")
    @Column
    private String pipeClass;

    @Schema(description = "NPS 表示值")
    @Column
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    @Column(nullable = false)
    private Double nps;

    @Schema(description = "壁厚等级")
    @Column
    private String thickness;

    @Schema(description = "组件#1标签")
    @Column
    private String tagNo1;

    @Schema(description = "组件#1材质代码")
    @Column(nullable = false)
    private String materialCode1;

    @Schema(description = "组件#1材质描述")
    @Column(nullable = false)
    private String material1;

    @Schema(description = "组建#1备注")
    @Column
    private String remarks1;

    @Schema(description = "组件#2标签")
    @Column
    private String tagNo2;

    @Schema(description = "组件#2材质代码")
    @Column
    private String materialCode2;

    @Schema(description = "组件#2材质描述")
    @Column
    private String material2;

    @Schema(description = "组建#2备注")
    @Column
    private String remarks2;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks;

    @Schema(description = "备注")
    @Column
    private String remarks3;

    @Schema(description = "焊口材料分组代码")
    @Column
    private String materialGroupCode;

    @Schema(description = "最新无损探伤结果 ID")
    @Column
    private Long ndtId;

    @Schema(description = "最新焊后热处理结果 ID")
    @Column
    private Long pwhtId;

    @Schema(description = "最新硬度测试结果 ID")
    @Column
    private Long hdId;

    @Schema(description = "最新材质分析结果 ID")
    @Column
    private Long pmiId;

    @Schema(description = "最新外观检测结果 ID")
    @Column
    private Long visualId;

    @Schema(description = "最新焊接结果 ID")
    @Column
    private Long weldingId;

    @Schema(description = "最新坡口打磨结果 ID")
    @Column
    private Long grooveGrindingId;

    @Schema(description = "最新组对结果 ID")
    @Column
    private Long pairId;

    @Schema(description = "最新补漆结果 ID")
    @Column
    private Long paintRepairId;

    @Schema(description = "最新无损探伤结果")
    @Column
    @Enumerated(EnumType.STRING)
    private ActivityExecuteResult ndtResult;

    @Schema(description = "ndt执行结果")
    @Column
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

    @Schema(description = "油漆代码")
    @Column
    private String paintingCode;

    @Schema(description = "PMI结果")
    @Column
    @Enumerated(EnumType.STRING)
    private ActivityExecuteResult pmiResult;

    @Schema(description = "所属单管实体编号")
    @Column
    private String spoolNo;

    @Schema(description = "PMI执行结果")
    @Column
    @Enumerated(EnumType.STRING)
    private PMIExecuteFlag pmiExecuteFlag;

    @Schema(description = "焊接时间")
    @Transient
    private Date weldTime;

    @Schema(description = "材料类型")
    @Column
    private String materialType;

    @Schema(description = "rt比例")
    @Column
    private String rt;

    @Schema(description = "ut比例")
    @Column
    private String ut;

    @Schema(description = "mt比例")
    @Column
    private String mt;

    @Schema(description = "pt比例")
    @Column
    private String pt;

    @Schema(description = "rt比例")
    @Column
    private String fn;

    @Schema(description = "rt比例值")
    @Column
    private Double rtValue;

    @Schema(description = "ut比例值")
    @Column
    private Double utValue;

    @Schema(description = "mt比例值")
    @Column
    private Double mtValue;

    @Schema(description = "pt比例值")
    @Column
    private Double ptValue;

    @Schema(description = "fn比例值")
    @Column
    private Double fnValue;

    @Schema(description = "所属单管实体编号")
    @Column
    private Boolean materialTraceability;

    @Schema(description = "模块号")
    @Column
    private String moduleNo;

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    @JsonCreator
    public WeldEntityBase() {
        this(null);
    }

    public WeldEntityBase(Project project) {
        super(project);
        setEntityType("WELD_JOINT");
    }

    @Schema(description = "二维码")
    @Column
    private String qrCode;

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

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getShopField() {
        return shopField;
    }

    @JsonSetter
    public void setShopField(String shopField) {
        this.shopField = shopField;
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

    @Override
    public String getEntityBusinessType() {
        return entityBusinessType;
    }

    public void setEntityBusinessType(String entityBusinessType) {
        this.entityBusinessType = entityBusinessType;
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

    public String getWpsId() {
        return wpsId;
    }

    public void setWpsId(String wpsId) {
        this.wpsId = wpsId;
    }

    public Boolean getWpsEdited() {
        return wpsEdited;
    }

    public void setWpsEdited(Boolean wpsEdited) {
        this.wpsEdited = wpsEdited;
    }

    public NDEType getNde() {
        return nde;
    }

    @JsonSetter
    public void setNde(NDEType nde) {
        this.nde = nde;
    }

    /**
     * 设定nde值。
     *
     * @param nde nde值
     */
    public void setNde(String nde) {
        NDERatioDTO ratioDTO = new NDERatioDTO(nde);
        this.nde = ratioDTO.getType();
        this.ndeRatio = ratioDTO.getRatio();
    }

    public Integer getNdeRatio() {
        return ndeRatio;
    }

    public void setNdeRatio(Integer ndeRatio) {
        this.ndeRatio = ndeRatio;
    }

    public Boolean getPwht() {
        return pwht;
    }

    public void setPwht(Boolean pwht) {
        this.pwht = pwht;
    }

    public Boolean getHardnessTest() {
        return hardnessTest;
    }

    public void setHardnessTest(Boolean hardnessTest) {
        this.hardnessTest = hardnessTest;
    }

    public Integer getPmiRatio() {
        return pmiRatio;
    }

    public void setPmiRatio(Integer pmiRatio) {
        this.pmiRatio = pmiRatio;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
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
        this.thickness = thickness;
    }

    public String getTagNo1() {
        return tagNo1;
    }

    public void setTagNo1(String tagNo1) {
        this.tagNo1 = tagNo1;
    }

    public String getMaterialCode1() {
        return materialCode1;
    }

    public void setMaterialCode1(String materialCode1) {
        this.materialCode1 = materialCode1;
    }

    public String getMaterialGroupCode() {
        return materialGroupCode;
    }

    public void setMaterialGroupCode(String materialGroupCode) {
        this.materialGroupCode = materialGroupCode;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getTagNo2() {
        return tagNo2;
    }

    public void setTagNo2(String tagNo2) {
        this.tagNo2 = tagNo2;
    }

    public String getMaterialCode2() {
        return materialCode2;
    }

    public void setMaterialCode2(String materialCode2) {
        this.materialCode2 = materialCode2;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
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

    public Long getPwhtId() {
        return pwhtId;
    }

    public void setPwhtId(Long pwhtId) {
        this.pwhtId = pwhtId;
    }

    public Long getHdId() {
        return hdId;
    }

    public void setHdId(Long hdId) {
        this.hdId = hdId;
    }

    public Long getPmiId() {
        return pmiId;
    }

    public void setPmiId(Long pmiId) {
        this.pmiId = pmiId;
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

    public Long getGrooveGrindingId() {
        return grooveGrindingId;
    }

    public void setGrooveGrindingId(Long grooveGrindingId) {
        this.grooveGrindingId = grooveGrindingId;
    }

    public Long getPairId() {
        return pairId;
    }

    public void setPairId(Long pairId) {
        this.pairId = pairId;
    }

    public Long getPaintRepairId() {
        return paintRepairId;
    }

    public void setPaintRepairId(Long paintRepairId) {
        this.paintRepairId = paintRepairId;
    }

    @Override
    public String getEntitySubType() {
        return this.weldEntityType;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
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

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public ActivityExecuteResult getPmiResult() {
        return pmiResult;
    }

    public void setPmiResult(ActivityExecuteResult pmiResult) {
        this.pmiResult = pmiResult;
    }

    public PMIExecuteFlag getPmiExecuteFlag() {
        return pmiExecuteFlag;
    }

    public void setPmiExecuteFlag(PMIExecuteFlag pmiExecuteFlag) {
        this.pmiExecuteFlag = pmiExecuteFlag;
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

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getMt() {
        return mt;
    }

    public void setMt(String mt) {
        this.mt = mt;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks3() {
        return remarks3;
    }

    public void setRemarks3(String remarks3) {
        this.remarks3 = remarks3;
    }

    public Boolean getMaterialTraceability() {
        return materialTraceability;
    }

    public void setMaterialTraceability(Boolean materialTraceability) {
        this.materialTraceability = materialTraceability;
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

    public Double getFnValue() {
        return fnValue;
    }

    public void setFnValue(Double fnValue) {
        this.fnValue = fnValue;
    }


    public void setRtValue(String rt) {

        this.rtValue = getRatio(rt);
    }

    public void setUtValue(String ut) {
        this.utValue = getRatio(ut);
    }

    public void setMtValue(String mt) {
        this.mtValue = getRatio(mt);
    }

    public void setPtValue(String pt) {
        this.ptValue = getRatio(pt);
    }

    public void setFnValue(String fn) {
        this.fnValue = getRatio(fn);
    }

    private Double getRatio(String ratioStr) {
        if(StringUtils.isEmpty(ratioStr)) {
            return 0.0;
        }
        ratioStr = ratioStr.replaceAll(" ","").replaceAll("%","");
        if(RATIO_PATTERN.matcher(ratioStr).matches() && StringUtils.isNumeric(ratioStr)) {
            Double ratioValue = Double.parseDouble(ratioStr);
            if(ratioValue < 0.0) {
                ratioValue = 0.0;
            } else if(ratioValue > 100.0) {
                ratioValue = 100.0;
            }

            return ratioValue;
        }

        return 0.0;


    }

    public String getWelderIds() {
        return welderIds;
    }

    public void setWelderIds(String welderIds) {
        this.welderIds = welderIds;
    }

    @JsonProperty(value = "welderIds", access = JsonProperty.Access.READ_ONLY)
    public List<Long> getJsonWelderIds() {
        if(StringUtils.isEmpty(welderIds)) {
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
