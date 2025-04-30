package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目数据实体。
 */
@Entity
@Table(name = "project",
    indexes = {
        @Index(columnList = "orgId,status")
    }
)
public class Project extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -2075758396963985983L;

    @Schema(description = "所属公司 ID")
    @Column(nullable = false)
    private Long companyId;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目在 SPM 系统中的 ID")
    @Column(length = 16)
    private String spmId;

    @Schema(description = "项目")
    @Column(nullable = false)
    private String name;

    // 出外检报告时会用到
    @Schema(description = "项目代码")
    @Column
    private String code;

    // 出外检报告时会用到
    @Schema(description = "项目业主")
    @Column
    private String client;

    // 出外检报告时会用到
    @Schema(description = "项目第三方")
    @Column
    private String thirdParty;

    @Schema(description = "建造视图可选节点类型列表")
    @Column(name = "opt_node_types_area")
    private String areaOptionalNodeTypes;

    @Schema(description = "建造层级导入模版文件 ID")
    @Column(name = "import_file_id_area")
    private Long areaImportFileId;

    @Schema(description = "建造层级导入模版文件版本")
    @Column(name = "import_file_version_area", length = 24)
    private String areaImportFileVersion;

    @Schema(description = "系统视图可选节点类型列表")
    @Column(name = "opt_node_types_sys")
    private String systemOptionalNodeTypes;

    public String getEngineeringOptionalNodeTypes() {
        return engineeringOptionalNodeTypes;
    }

    public void setEngineeringOptionalNodeTypes(String engineeringOptionalNodeTypes) {
        this.engineeringOptionalNodeTypes = engineeringOptionalNodeTypes;
    }

    @Schema(description = "设计视图可选节点类型列表")
    @Column(name = "opt_node_types_eng")
    private String engineeringOptionalNodeTypes;
    @Schema(description = "系统层级导入模版文件 ID")
    @Column(name = "import_file_id_sys")
    private Long systemImportFileId;

    @Schema(description = "系统层级导入模版文件版本")
    @Column(name = "import_file_version_sys", length = 24)
    private String systemImportFileVersion;


    public Long getEngineeringImportFileId() {
        return engineeringImportFileId;
    }

    public void setEngineeringImportFileId(Long engineeringImportFileId) {
        this.engineeringImportFileId = engineeringImportFileId;
    }

    public String getEngineeringImportFileVersion() {
        return engineeringImportFileVersion;
    }

    public void setEngineeringImportFileVersion(String engineeringImportFileVersion) {
        this.engineeringImportFileVersion = engineeringImportFileVersion;
    }

    @Schema(description = "系统层级导入模版文件 ID")
    @Column(name = "import_file_id_eng")
    private Long engineeringImportFileId;

    @Schema(description = "系统层级导入模版文件版本")
    @Column(name = "import_file_version_eng", length = 24)
    private String engineeringImportFileVersion;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "项目计划实施进度最后更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date progressRefreshedAt;

    @Schema(description = "项目计划实施结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date finishedAt;

    @Schema(description = "项目ISO BOM 节点 匹配 更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date bomMatchLastModified;

    @Schema(description = "bom_ln_code生成方法，HEAD首部/TAIL尾部/SAME同ISO_NO/NONE不生成")
    @Column
    private String bomLnCodeGenerateMethod;

    @Schema(description = "结构父级是在程序中组合，为true时使用组合模式，如 WP01, WP02, WP03, WP04" +
        "WP05的父级为 WP01-WP02-WP03-WP04")
    @Column
    @ColumnDefault("1")
    private boolean structureAssembleParent;

    // NDT抽检规则
    @Schema(description = "NDT抽检规则")
    @Column
    private String ndtInspectionRules;
    // NDT扩口规则
    @Schema(description = "NDT扩口规则")
    @Column
    private String ndtPenaltyRules;
    // 焊工排列数规则
    @Schema(description = "焊工排列数规则")
    @Column
    private String ndtWeldCountRules;
    // NDT焊口返修次数
    @Schema(description = "NDT焊口返修次数")
    @Column
    private Integer ndtRepaireRules;

    // NDT扩口次数
    @Schema(description = "NDT扩口次数")
    @Column
    private Integer ndtPenaltyNumber;

    // NDT扩口焊口数
    @Schema(description = "NDT扩口焊口数")
    @Column
    private Integer ndtPenaltyWeldNo;


    @Schema(description = "业主Logo图片ID")
    @Column
    private Long clientLogoFileId;

    @Schema(description = "建造方Logo图片ID")
    @Column
    private Long contractorLogoFileId;

    @Schema(description = "图纸是否自动升版")
    @Column
    private Boolean drawingUpdateAutoFlag;

    @Schema(description = "FITUP待办任务焊材校验是否需要")
    @Column
    private Boolean displayFitUpMaterial = false;

    @Schema(description = "WELD待办任务焊材校验是否需要")
    @Column
    private Boolean displayWeldMaterial = false;

    @Schema(description = "WELD待办任务焊材校验是否需要")
    @Column
    private Boolean displayWeldMaterialWps = false;

    @Schema(description = "待办任务焊材校验是否必填")
    @Column
    private Boolean isWeldMaterial = false;

    @Schema(description = "待办任务焊材校验是否必填")
    @Column
    private Boolean isWeldMaterialWps = false;

    @Schema(description = "待办任务是否显示'统计Excel'按钮")
    @Column
    private Boolean displayCountExcel = false;

//    @Schema(description = "待办任务是否使用'待办任务排序'")
//    @Column
//    private Boolean pendingTaskSort = false;
//
//    @Schema(description = "待办任务是否添加'待办任务排序字段'")
//    @Column
//    private Boolean pendingTaskSortWord = false;

    @Schema(description = "项目是否添加'UT_MT'子流程")
    @Column
    private Boolean subProcessUtMt = false;

    @Schema(description = "显示报告号")
    @Column
    private Boolean isReportNo = false;

    @Schema(description = "是否关联任务包")
    @Column
    private Boolean linkTaskPackage = false;

    @Schema(description = "是否有前置CUTTING")
    @Column
    private Boolean cutting = false;

    @Schema(description = "是否能填Overtime")
    @Column(name = "have_overtime")
    private Boolean haveOvertime = true;

    @Schema(description = "是否能填hour")
    @Column(name = "have_hour")
    private Boolean haveHour = true;

    @Schema(description = "按组织架构审批加班时间")
    @Column(name = "approve_overtime")
    private Boolean approveOvertime = false;

    @Schema(description = "图纸类型")
    @Column
    private String drawingType;

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

    public Boolean getHaveOvertime() {
        return haveOvertime;
    }

    public void setHaveOvertime(Boolean haveOvertime) {
        this.haveOvertime = haveOvertime;
    }

    public Project(){

    }
    @JsonCreator
    public Project(@JsonProperty("ndtInspectionRules") List<String> ndtInspectionRules,
                   @JsonProperty("ndtPenaltyRules") List<String> ndtPenaltyRules,
                   @JsonProperty("ndtWeldCountRules") List<String> ndtWeldCountRules) {
        this.ndtInspectionRules = StringUtils.toJSON(ndtInspectionRules);
        this.ndtPenaltyRules = StringUtils.toJSON(ndtPenaltyRules);
        this.ndtWeldCountRules = StringUtils.toJSON(ndtWeldCountRules);
    }

    public Long getClientLogoFileId() {
        return clientLogoFileId;
    }

    public void setClientLogoFileId(Long clientLogoFileId) {
        this.clientLogoFileId = clientLogoFileId;
    }

    public Long getContractorLogoFileId() {
        return contractorLogoFileId;
    }

    public void setContractorLogoFileId(Long contractorLogoFileId) {
        this.contractorLogoFileId = contractorLogoFileId;
    }

    /**
     * mode =1 格式：WBS_Lv1 . ... . 建造 . ... . 模块/子系统 . 专业 . 工序阶段
     * mode =2 // 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业
     * mode =3 格式：WBS_Lv1 . ... . 模块 . 建造 . ... . 工序阶段 . 专业 . 层/包
     */
    @Schema(description = "wbs mode, wbs的正则表达方式")
    @Column
    private Integer wbsMode = 0;

    public Integer getWbsMode() {
        return wbsMode;
    }

    public void setWbsMode(Integer wbsMode) {
        this.wbsMode = wbsMode;
    }

    /**
     * 将层级节点类型字符串转为层级节点类型枚举列表。
     *
     * @param nodeTypes 层级节点类型字符串
     * @return 层级节点类型枚举列表
     */
    public static List<String> toHierarchyNodeTypeList(String nodeTypes) {

        List<String> nodeTypeList = new ArrayList<>();

        if (nodeTypes == null) {
            return nodeTypeList;
        }

        for (String nodeType : nodeTypes.split(",")) {
            try {
                nodeTypeList.add(nodeType);
            } catch (Exception e) {
                // to do nothing
            }

        }

        return nodeTypeList;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getSpmId() {
        return spmId;
    }

    public void setSpmId(String spmId) {
        this.spmId = spmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getAreaOptionalNodeTypes() {
        return areaOptionalNodeTypes;
    }

    public void setAreaOptionalNodeTypes(String areaOptionalNodeTypes) {
        this.areaOptionalNodeTypes = areaOptionalNodeTypes;
    }

    public Long getAreaImportFileId() {
        return areaImportFileId;
    }

    public void setAreaImportFileId(Long areaImportFileId) {
        this.areaImportFileId = areaImportFileId;
    }

    public String getAreaImportFileVersion() {
        return areaImportFileVersion;
    }

    public void setAreaImportFileVersion(String areaImportFileVersion) {
        this.areaImportFileVersion = areaImportFileVersion;
    }

    public String getSystemOptionalNodeTypes() {
        return systemOptionalNodeTypes;
    }

    public void setSystemOptionalNodeTypes(String systemOptionalNodeTypes) {
        this.systemOptionalNodeTypes = systemOptionalNodeTypes;
    }

    public Long getSystemImportFileId() {
        return systemImportFileId;
    }

    public void setSystemImportFileId(Long systemImportFileId) {
        this.systemImportFileId = systemImportFileId;
    }

    public String getSystemImportFileVersion() {
        return systemImportFileVersion;
    }

    public void setSystemImportFileVersion(String systemImportFileVersion) {
        this.systemImportFileVersion = systemImportFileVersion;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getProgressRefreshedAt() {
        return progressRefreshedAt;
    }

    public void setProgressRefreshedAt(Date progressRefreshedAt) {
        this.progressRefreshedAt = progressRefreshedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Date getBomMatchLastModified() {
        return bomMatchLastModified;
    }

    public void setBomMatchLastModified(Date bomMatchLastModified) {
        this.bomMatchLastModified = bomMatchLastModified;
    }

    public String getBomLnCodeGenerateMethod() {
        return bomLnCodeGenerateMethod;
    }

    public void setBomLnCodeGenerateMethod(String bomLnCodeGenerateMethod) {
        this.bomLnCodeGenerateMethod = bomLnCodeGenerateMethod;
    }

    public boolean isStructureAssembleParent() {
        return structureAssembleParent;
    }

    public void setStructureAssembleParent(boolean structureAssembleParent) {
        this.structureAssembleParent = structureAssembleParent;
    }

    public String getNdtInspectionRules() {
        return ndtInspectionRules;
    }

    public void setNdtInspectionRules(String ndtInspectionRules) {
        this.ndtInspectionRules = ndtInspectionRules;
    }

    @JsonProperty(value = "ndtInspectionRules", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonNdtInspectionRules() {
        if (ndtInspectionRules != null && !"".equals(ndtInspectionRules)) {
            return StringUtils.decode(ndtInspectionRules, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<String>();
        }
    }

    @JsonIgnore
    public void setJsonNdtInspectionRules(List<String> ndtInspectionRules) {
        if (ndtInspectionRules != null) {
            this.ndtInspectionRules = StringUtils.toJSON(ndtInspectionRules);
        }
    }

    public String getNdtPenaltyRules() {
        return ndtPenaltyRules;
    }

    public void setNdtPenaltyRules(String ndtPenaltyRules) {
        this.ndtPenaltyRules = ndtPenaltyRules;
    }

    @JsonProperty(value = "ndtPenaltyRules", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonNdtPenaltyRules() {
        if (ndtPenaltyRules != null && !"".equals(ndtPenaltyRules)) {
            return StringUtils.decode(ndtPenaltyRules, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<String>();
        }
    }

    @JsonIgnore
    public void setJsonNdtPenaltyRules(List<String> ndtPenaltyRules) {
        if (ndtPenaltyRules != null) {
            this.ndtPenaltyRules = StringUtils.toJSON(ndtPenaltyRules);
        }
    }

    public String getNdtWeldCountRules() {
        return ndtWeldCountRules;
    }

    public void setNdtWeldCountRules(String ndtWeldCountRules) {
        this.ndtWeldCountRules = ndtWeldCountRules;
    }

    @JsonProperty(value = "ndtWeldCountRules", access = JsonProperty.Access.READ_ONLY)
    public List<String> getJsonNdtWeldCountRules() {
        if (ndtWeldCountRules != null && !"".equals(ndtWeldCountRules)) {
            return StringUtils.decode(ndtWeldCountRules, new TypeReference<List<String>>() {
            });
        } else {
            return new ArrayList<String>();
        }
    }

    @JsonIgnore
    public void setJsonNdtWeldCountRules(List<String> ndtWeldCountRules) {
        if (ndtWeldCountRules != null) {
            this.ndtWeldCountRules = StringUtils.toJSON(ndtWeldCountRules);
        }
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

    public Boolean getDrawingUpdateAutoFlag() {
        return drawingUpdateAutoFlag;
    }

    public void setDrawingUpdateAutoFlag(Boolean drawingUpdateAutoFlag) {
        this.drawingUpdateAutoFlag = drawingUpdateAutoFlag;
    }

    public Boolean getDisplayCountExcel() {
        return displayCountExcel;
    }

    public void setDisplayCountExcel(Boolean displayCountExcel) {
        this.displayCountExcel = displayCountExcel;
    }

    public Boolean getSubProcessUtMt() {
        return subProcessUtMt;
    }

    public void setSubProcessUtMt(Boolean subProcessUtMt) {
        this.subProcessUtMt = subProcessUtMt;
    }

    public Boolean getReportNo() {
        return isReportNo;
    }

    public void setReportNo(Boolean reportNo) {
        isReportNo = reportNo;
    }

    public Boolean getLinkTaskPackage() {
        return linkTaskPackage;
    }

    public void setLinkTaskPackage(Boolean linkTaskPackage) {
        this.linkTaskPackage = linkTaskPackage;
    }

    public Boolean getCutting() {
        return cutting;
    }

    public void setCutting(Boolean cutting) {
        this.cutting = cutting;
    }

    public Boolean getWeldMaterial() {
        return isWeldMaterial;
    }

    public void setWeldMaterial(Boolean weldMaterial) {
        isWeldMaterial = weldMaterial;
    }

    public Boolean getDisplayFitUpMaterial() {
        return displayFitUpMaterial;
    }

    public void setDisplayFitUpMaterial(Boolean displayFitUpMaterial) {
        this.displayFitUpMaterial = displayFitUpMaterial;
    }

    public Boolean getDisplayWeldMaterial() {
        return displayWeldMaterial;
    }

    public void setDisplayWeldMaterial(Boolean displayWeldMaterial) {
        this.displayWeldMaterial = displayWeldMaterial;
    }

    public Boolean getDisplayWeldMaterialWps() {
        return displayWeldMaterialWps;
    }

    public void setDisplayWeldMaterialWps(Boolean displayWeldMaterialWps) {
        this.displayWeldMaterialWps = displayWeldMaterialWps;
    }

    public Boolean getWeldMaterialWps() {
        return isWeldMaterialWps;
    }

    public void setWeldMaterialWps(Boolean weldMaterialWps) {
        isWeldMaterialWps = weldMaterialWps;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }
}
