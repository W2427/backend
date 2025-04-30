package com.ose.tasks.entity.wbs.entry;

import com.ose.tasks.vo.wbs.WBSEntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.TaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import java.util.Random;

/**
 * WBS 条目数据。
 */
@Entity
public class WBSEntryGroupPlain extends WBSEntryBase {


    private static final long serialVersionUID = 2818732168668878662L;

    private static Random random = new Random(System.currentTimeMillis());


    @Schema(description = "公司 ID")
    @Column
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "建造视图层级节点 ID")
    @Column
    private Long hierarchyNodeId;

    @Schema(description = "建造视图上级层级节点 ID")
    @Column
    private Long parentHierarchyNodeId;

    @Schema(description = "抽检随机编号")
    @Column
    private Integer randomNo;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "工作流实例 ID")
    @Column(length = 16)
    private String processInstanceId;

    @Schema(description = "备注")
    @Lob
    @Column(length = 4096)
    private String remarks;

    @Schema(description = "ISO 图纸编号")
    @Column
    private Integer dwgShtNo;

    @Schema(description = "工作量 物量")
    @Column
    private Integer workLoad;

    @Schema(description = "材料匹配率")
    private Double bomMatchPercent;

    @Schema(description = "图纸发放状态")
    private Boolean issueStatus;

    /**
     * 构造方法。
     */
    public WBSEntryGroupPlain() {
        super();
        randomNo = Math.abs(random.nextInt());
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

    public Long getHierarchyNodeId() {
        return hierarchyNodeId;
    }

    public void setHierarchyNodeId(Long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
    }

    public Long getParentHierarchyNodeId() {
        return parentHierarchyNodeId;
    }

    public void setParentHierarchyNodeId(Long parentHierarchyNodeId) {
        this.parentHierarchyNodeId = parentHierarchyNodeId;
    }

    public void setType(TaskType type) {
        setType(WBSEntryType.getInstance(type.name()));
    }

    public Integer getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(Integer randomNo) {
        this.randomNo = randomNo;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDwgShtNo() {
        return dwgShtNo;
    }

    public void setDwgShtNo(Integer dwgShtNo) {
        this.dwgShtNo = dwgShtNo;
    }

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }

    public Double getBomMatchPercent() {
        return bomMatchPercent;
    }

    public void setBomMatchPercent(Double bomMatchPercent) {
        this.bomMatchPercent = bomMatchPercent;
    }

    public Boolean getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Boolean issueStatus) {
        this.issueStatus = issueStatus;
    }
}
