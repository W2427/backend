package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.qc.ITPType;
import com.ose.vo.InspectParty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(name = "itps")
public class Itp extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 998349632906068093L;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "内检等级")
    @Column
    @Enumerated(EnumType.STRING)
    private ITPType itpType;

    @Schema(description = "检验方")
    @Column
    @Enumerated(EnumType.STRING)
    private InspectParty inspectParty;

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

    public ITPType getItpType() {
        return itpType;
    }

    public void setItpType(ITPType itpType) {
        this.itpType = itpType;
    }

    public InspectParty getInspectParty() {
        return inspectParty;
    }

    public void setInspectParty(InspectParty inspectParty) {
        this.inspectParty = inspectParty;
    }

}
