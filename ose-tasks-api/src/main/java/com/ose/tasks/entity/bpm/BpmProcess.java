package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.Itp;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.tasks.vo.qc.ITPType;
import com.ose.tasks.vo.qc.InspectType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 工序实体类。
 */
@Entity
@Table(
    name = "bpm_process",
    indexes = {
        @Index(columnList = "name_en,status,process_stage_id")
    }
)
public class BpmProcess extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 工序名称
    @Column(name = "name_cn", nullable = false, length = 128)
    @NotNull(message = "Process's name is required")
    private String nameCn;

    // 工序名称-英文
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;

    // 排序
    @Column(name = "order_no", length = 11, columnDefinition = "int default 0")
    private int orderNo = 0;

    // 备注
    @Column(length = 500)
    private String memo;

    @Schema(description = "生成建造日志使用的类")
    @Column
    private String constructionLogClass;

    @Schema(description = "工序类型") //Delivery_list cut_list individual
    @Column
    @Enumerated(EnumType.STRING)
    private ProcessType processType;

    // 工序分类
    @ManyToOne
    @JoinColumn(name = "process_category_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmProcessCategory processCategory;

    // 工序阶段
    @ManyToOne
    @JoinColumn(name = "process_stage_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmProcessStage processStage;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "bpm_entity_type_process_relation",
        joinColumns = @JoinColumn(name = "process_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "entity_sub_type_id", referencedColumnName = "id"))
    private List<BpmEntitySubType> entitySubTypes;

    @OneToMany(mappedBy = "process")
    private Set<BpmEntityTypeProcessRelation> entityCategoryProcessList;

    @Schema(description = "功能分区")
    @Column
    private String funcPart;

    @Transient
    private List<ReportConfig> reportConfigs;

    @Schema(description = "专业")
    @Column
    private String discipline;

    @Transient
    private List<Itp> itps;

    // checkList
    @Transient
    private List<BpmProcessCheckList> checkList;

    @JsonIgnore
    public Set<BpmEntityTypeProcessRelation> getEntitySubTypeProcessList() {
        return entityCategoryProcessList;
    }

    public void setEntitySubTypeProcessList(Set<BpmEntityTypeProcessRelation> entityCategoryProcessList) {
        this.entityCategoryProcessList = entityCategoryProcessList;
    }

    @Schema(description = "ITP 检验类型, HOLD WITNESS REVIEW")
    @Enumerated(EnumType.STRING)
    private ITPType itpType;

    @Schema(description = "一次性处理报告，如入库外检等")
    @Column(columnDefinition = "bit default 0")
    private Boolean isOneOffReport;

    @Schema(description = "检验类型 外检 内检 不检")
    @Column
    @Enumerated(EnumType.STRING)
    private InspectType inspectType;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BpmProcessStage getProcessStage() {
        return processStage;
    }

    public void setProcessStage(BpmProcessStage processStage) {
        this.processStage = processStage;
    }

    public List<BpmEntitySubType> getEntitySubTypes() {
        return entitySubTypes;
    }

    @JsonIgnore
    public void setEntitySubTypes(List<BpmEntitySubType> entitySubTypes) {
        this.entitySubTypes = entitySubTypes;
    }

    public BpmProcessCategory getProcessCategory() {
        return processCategory;
    }

    public void setProcessCategory(BpmProcessCategory processCategory) {
        this.processCategory = processCategory;
    }

    public List<BpmProcessCheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<BpmProcessCheckList> checkList) {
        this.checkList = checkList;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public String getConstructionLogClass() {
        return constructionLogClass;
    }

    public void setConstructionLogClass(String constructionLogClass) {
        this.constructionLogClass = constructionLogClass;
    }

    public Boolean getOneOffReport() {
        return isOneOffReport;
    }

    public void setOneOffReport(Boolean oneOffReport) {
        isOneOffReport = oneOffReport;
    }

    public InspectType getInspectType() {
        return inspectType;
    }

    public void setInspectType(InspectType inspectType) {
        this.inspectType = inspectType;
    }

    public List<ReportConfig> getReportConfigs() {
        return reportConfigs;
    }

    public void setReportConfigs(List<ReportConfig> reportConfigs) {
        this.reportConfigs = reportConfigs;
    }

    public List<Itp> getItps() {
        return itps;
    }

    public void setItps(List<Itp> itps) {
        itps = itps;
    }

    public ITPType getItpType() {
        return itpType;
    }

    public void setItpType(ITPType itpType) {
        this.itpType = itpType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
