package com.ose.tasks.entity.wps;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "weld_welder_relation",
    indexes = {
        @Index(columnList = "orgId,projectId,weldId,status"),
        @Index(columnList = "orgId,projectId,weldNo,status"),
        @Index(columnList = "orgId,projectId,weldId,repairCount,status"),
        @Index(columnList = "weldId")
    })
public class WeldWelderRelation extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -8452360311557389910L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "焊工 ID")
    @Column
    private Long welderId;

    @Schema(description = "焊工编号")
    @Column
    private String welderNo;

    @Schema(description = "焊口 ID")
    @Column
    private Long weldId;

    @Schema(description = "焊口编号")
    @Column
    private String weldNo;

    @Schema(description = "焊接时间")
    @Column
    private Date weldTime;

    @Schema(description = "工序（打底A、填充B、盖面C）")
    @Column
    private String process;

    @Schema(description = "返修数量")
    @Column(columnDefinition = "INT default 0")
    private Integer repairCount;

    @Schema(description = "专业")
    @Column
    @Enumerated(EnumType.STRING)
    private DisciplineCode discipline;

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

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public Long getWeldId() {
        return weldId;
    }

    public void setWeldId(Long weldId) {
        this.weldId = weldId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public Integer getRepairCount() {
        return repairCount;
    }

    public void setRepairCount(Integer repairCount) {
        this.repairCount = repairCount;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }

    public Date getWeldTime() {
        return weldTime;
    }

    public void setWeldTime(Date weldTime) {
        this.weldTime = weldTime;
    }
}
