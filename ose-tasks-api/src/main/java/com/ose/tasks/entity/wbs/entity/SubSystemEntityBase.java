package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 子系统实体数据实体基类。
 */
@Entity
@Table(name = "entity_sub_system",
    indexes = {
        @Index(columnList = "projectId,no,deleted")
    })
public class SubSystemEntityBase extends WBSEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = -7087823127044774929L;

    @Schema(description = "是否执行气密程序")
    @Column
    private Boolean airTightness = false;

    @Schema(description = "是否执行完工检查")
    @Column
    private Boolean checkMC = false;

    @Schema(description = "sub entity type")
    @Column
    private String entitySubType;

    @Schema(description = "description")
    @Column
    private String description;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    @Schema(description = "SECTOR HULL TOPSIDE")
    @Column
    private String sector;

    @Schema(description = "MC DUE DATE")
    @Column
    private Date mcDueDate;

    @Schema(description = "MC CLOSED DATE")
    @Column
    private Date mcClosedDate;


    @Schema(description = "PC DUE DATE")
    @Column
    private Date pcDueDate;

    @Schema(description = "COMM DUE DATE")
    @Column
    private Date commDueDate;

    @Schema(description = "Function Group")
    @Column
    private String fGroup;

    @Schema(description = "irn percent")
    @Column
    private Float irnPercent = 0f;

    @Schema(description = "irn done")
    @Column
    private boolean irnDone = false;

    @Schema(description = "rfcc percent")
    @Column
    private Float rfccPercent = 0f;

    @Schema(description = "rfcc done")
    @Column
    private boolean rfccDone = false;

    @Schema(description = "rfcc file id")
    @Column
    private Long rfccFileId;

    @Schema(description = "irn file id")
    @Column
    private Long irnFileId;

    @Schema(description = "pid file id")
    @Column
    private Long pidFileId;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Schema(description = "sub System color code")
    @Column
    private String color;

    @JsonCreator
    public SubSystemEntityBase() {
        this(null);
    }

    public SubSystemEntityBase(Project project) {
        super(project);
        setEntityType("SUB_SYSTEM");
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String getRemarks2() {
        return remarks2;
    }

    @Override
    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    @Override
    public String getName() {
        return "SubSystemEntityBase";
    }

    public Boolean getAirTightness() {
        return airTightness;
    }

    public void setAirTightness(Boolean airTightness) {
        this.airTightness = airTightness;
    }

    public Boolean getCheckMC() {
        return checkMC;
    }

    public void setCheckMC(Boolean checkMC) {
        this.checkMC = checkMC;
    }

    @Override
    public String getEntitySubType() {
        return entitySubType;
    }

    @Override
    public String getEntityBusinessType() {
        return null;
    }

    @Override
    public String getVariableName() {
        return "SUB_SYSTEM";
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Date getMcDueDate() {
        return mcDueDate;
    }

    public void setMcDueDate(Date mcDueDate) {
        this.mcDueDate = mcDueDate;
    }

    public String getfGroup() {
        return fGroup;
    }

    public void setfGroup(String fGroup) {
        this.fGroup = fGroup;
    }

    public Date getPcDueDate() {
        return pcDueDate;
    }

    public void setPcDueDate(Date pcDueDate) {
        this.pcDueDate = pcDueDate;
    }

    public Date getCommDueDate() {
        return commDueDate;
    }

    public void setCommDueDate(Date commDueDate) {
        this.commDueDate = commDueDate;
    }

    public Float getIrnPercent() {
        return irnPercent;
    }

    public void setIrnPercent(Float irnPercent) {
        this.irnPercent = irnPercent;
    }

    public boolean isIrnDone() {
        return irnDone;
    }

    public void setIrnDone(boolean irnDone) {
        this.irnDone = irnDone;
    }

    public Float getRfccPercent() {
        return rfccPercent;
    }

    public void setRfccPercent(Float rfccPercent) {
        this.rfccPercent = rfccPercent;
    }

    public boolean isRfccDone() {
        return rfccDone;
    }

    public void setRfccDone(boolean rfccDone) {
        this.rfccDone = rfccDone;
    }

    public Date getMcClosedDate() {
        return mcClosedDate;
    }

    public void setMcClosedDate(Date mcClosedDate) {
        this.mcClosedDate = mcClosedDate;
    }

    public Long getRfccFileId() {
        return rfccFileId;
    }

    public void setRfccFileId(Long rfccFileId) {
        this.rfccFileId = rfccFileId;
    }

    public Long getIrnFileId() {
        return irnFileId;
    }

    public void setIrnFileId(Long irnFileId) {
        this.irnFileId = irnFileId;
    }

    public Long getPidFileId() {
        return pidFileId;
    }

    public void setPidFileId(Long pidFileId) {
        this.pidFileId = pidFileId;
    }
}
