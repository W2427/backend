package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 项目信息更新数据传输对象。
 */
public class ProjectModifyDTO extends BaseDTO {

    private static final long serialVersionUID = 7880251353440238014L;

    @Schema(description = "项目在 SPM 系统中的 ID")
    private String spmId;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "建造视图可选节点类型列表（可选值：SUB_AREA,LAYER_PACKAGE）")
    private List<String> areaOptionalNodeTypes;

    @Schema(description = "系统视图可选节点类型列表（可选值：SUB_SECTOR）")
    private List<String> systemOptionalNodeTypes;

    @Schema(description = "系统视图可选节点类型列表 （可选值：SECTOR）")
    private List<String> engineeringOptionalNodeTypes;

    /**
     * 1. 对于材料匹配，需要在 总体的项目设定画面增加bom节点匹配规则设定：
     * - ISO BOM 节点匹配规则，ISO NO相对模块的位置。 “TAIL”，”HEAD“，”SAME“，”NONE“
     * - 上述后台接口待开发
     */
    @Schema(description = "材料匹配设置（可选值 HEAD，TAIL，SAME，NONE）")
    private String bomLnCodeGenerateMethod;

    /**
     * * 2. 对于项目的WBS匹配模式，需要则项目的总体设定画面增加
     * *         - 项目的设定模式。
     * *                 模式1：WBS LV1…阶段…模块/子系统.专业.工序阶段
     * *                 模式2：WBSLV1…模块.阶段….工序阶段.专业 （UT3）
     * *                 模式3：WBSLV1…模块.阶段…工序阶段.专业.层/包
     */
    @Schema(description = "WBS 解析模式设定")
    private Integer wbsMode;

    // NDT抽检规则
    @Schema(description = "NDT抽检规则")
    private List<String> ndtInspectionRules;
    // NDT扩口规则
    @Schema(description = "NDT扩口规则")
    private List<String> ndtPenaltyRules;
    // ND
    @Schema(description = "焊工排列数规则")
    private List<String> ndtWeldCountRules;
    // NDT焊口返修次数
    @Schema(description = "NDT焊口返修次数")
    private Integer ndtRepaireRules;
    // NDT扩口次数
    @Schema(description = "NDT扩口次数")
    private Integer ndtPenaltyNumber;
    // NDT扩口焊口数
    @Schema(description = "NDT扩口焊口数")
    private Integer ndtPenaltyWeldNo;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "业主logo临时文件名")
    private String clientTempName;

    @Schema(description = "建造方logo临时文件名")
    private String contractorTempName;

    @Schema(description = "图纸类型")
    private String drawingType;

    @Schema(description = "维度类型")
    private String hierarchyType;

    @Schema(description = "已选择的节点类型")
    private String optionalNodeTypes;

    @Schema(description = "按组织架构更新加班时间")
    private Boolean approveOvertime;

    @Schema(description = "是否能填Overtime")
    private Boolean haveOvertime;

    @Schema(description = "是否能填hour")
    private Boolean haveHour;

    public Boolean getHaveOvertime() {
        return haveOvertime;
    }

    public void setHaveOvertime(Boolean haveOvertime) {
        this.haveOvertime = haveOvertime;
    }

    public Boolean getHaveHour() {
        return haveHour;
    }

    public void setHaveHour(Boolean haveHour) {
        this.haveHour = haveHour;
    }

    public Boolean getApproveOvertime() {
        return approveOvertime;
    }

    public void setApproveOvertime(Boolean approveOvertime) {
        this.approveOvertime = approveOvertime;
    }

    public String getClientTempName() {
        return clientTempName;
    }

    public void setClientTempName(String clientTempName) {
        this.clientTempName = clientTempName;
    }

    public String getContractorTempName() {
        return contractorTempName;
    }

    public void setContractorTempName(String contractorTempName) {
        this.contractorTempName = contractorTempName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAreaOptionalNodeTypes() {
        return areaOptionalNodeTypes;
    }

    public void setAreaOptionalNodeTypes(List<String> areaOptionalNodeTypes) {
        this.areaOptionalNodeTypes = areaOptionalNodeTypes;
    }

    public List<String> getSystemOptionalNodeTypes() {
        return systemOptionalNodeTypes;
    }

    public void setSystemOptionalNodeTypes(List<String> systemOptionalNodeTypes) {
        this.systemOptionalNodeTypes = systemOptionalNodeTypes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpmId() {
        return spmId;
    }

    public void setSpmId(String spmId) {
        this.spmId = spmId;
    }

    public String getBomLnCodeGenerateMethod() {
        return bomLnCodeGenerateMethod;
    }

    public void setBomLnCodeGenerateMethod(String bomLnCodeGenerateMethod) {
        this.bomLnCodeGenerateMethod = bomLnCodeGenerateMethod;
    }

    public Integer getWbsMode() {
        return wbsMode;
    }

    public void setWbsMode(Integer wbsMode) {
        this.wbsMode = wbsMode;
    }

    public List<String> getNdtInspectionRules() {
        return ndtInspectionRules;
    }

    public void setNdtInspectionRules(List<String> ndtInspectionRules) {
        this.ndtInspectionRules = ndtInspectionRules;
    }

    public List<String> getNdtPenaltyRules() {
        return ndtPenaltyRules;
    }

    public void setNdtPenaltyRules(List<String> ndtPenaltyRules) {
        this.ndtPenaltyRules = ndtPenaltyRules;
    }

    public List<String> getNdtWeldCountRules() {
        return ndtWeldCountRules;
    }

    public void setNdtWeldCountRules(List<String> ndtWeldCountRules) {
        this.ndtWeldCountRules = ndtWeldCountRules;
    }


    public Integer getNdtRepaireRules() {
        return ndtRepaireRules;
    }

    public void setNdtRepaireRules(Integer ndtRepaireRules) {
        this.ndtRepaireRules = ndtRepaireRules;
    }

    public Integer getNdtPenaltyNumber() {
        return ndtPenaltyNumber;
    }

    public void setNdtPenaltyNumber(Integer ndtPenaltyNumber) {
        this.ndtPenaltyNumber = ndtPenaltyNumber;
    }

    public Integer getNdtPenaltyWeldNo() {
        return ndtPenaltyWeldNo;
    }

    public void setNdtPenaltyWeldNo(Integer ndtPenaltyWeldNo) {
        this.ndtPenaltyWeldNo = ndtPenaltyWeldNo;
    }


    public List<String> getEngineeringOptionalNodeTypes() {
        return engineeringOptionalNodeTypes;
    }

    public void setEngineeringOptionalNodeTypes(List<String> engineeringOptionalNodeTypes) {
        this.engineeringOptionalNodeTypes = engineeringOptionalNodeTypes;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public String getOptionalNodeTypes() {
        return optionalNodeTypes;
    }

    public void setOptionalNodeTypes(String optionalNodeTypes) {
        this.optionalNodeTypes = optionalNodeTypes;
    }
}
