package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_entity_sub_type",
    indexes = {
        @Index(columnList = "projectId,nameEn"),
        @Index(columnList = "projectId,id"),
        @Index(columnList = "projectId,entityTypeId")
    })
public class BpmEntitySubType extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    // 项目 ID
    @Column(nullable = false, length = 20)
    private Long projectId;

    // 组织 ID
    @Column(nullable = false, length = 20)
    private Long orgId;

    // 实体名称
    @Column(nullable = false, length = 128)
    @NotNull(message = "entity's name is required")
    private String nameCn;

    // 实体名称-英文
    @Column(nullable = false, length = 100)
    private String nameEn;

    // 排序
    @Column(length = 11, columnDefinition = "int default 0")
    private int orderNo = 0;

    // 是否包含子图纸
    private boolean subDrawingFlg;

    // 备注
    @Column(length = 500)
    private String memo;

    // 实体大类型ID
    @Column(length = 20)
    private Long entityTypeId;

    // 实体业务类型ID
    @Column(length = 20)
    private Long entityBusinessTypeId;

    @Schema(description = "专业")
    @Column
    private String discipline = "PIPING";

    private int drawingPositionX = 0;
    private int drawingPositionY = 0;
    private int drawingScaleToFit = 0;

    private int drawingShortX = 0;
    private int drawingShortY = 0;

    private int coverPositionX = 0;
    private int coverPositionY = 0;
    private int coverScaleToFit = 0;

    // checkList
    @Transient
    private List<BpmEntitySubTypeCheckList> checkList;

    @ManyToOne
    @JoinColumn(name = "entityTypeId", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmEntityType entityType;

    @ManyToOne
    @JoinColumn(name = "entityBusinessTypeId", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BpmEntityType entityBusinessType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "bpm_entity_type_process_relation",
        joinColumns = @JoinColumn(name = "entity_sub_type_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "process_id", referencedColumnName = "id"))
    private List<BpmProcess> processes;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "bpm_entity_type_coordinate_relation",
        joinColumns = @JoinColumn(name = "entity_sub_type_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "drawing_coordinate_id", referencedColumnName = "id"))
    private List<DrawingCoordinate> drawingCoordinates;

    @OneToMany(mappedBy = "entitySubType")
    private Set<BpmEntityTypeProcessRelation> entitySubTypeProcessList;

    public List<DrawingCoordinate> getDrawingCoordinates() {
        return drawingCoordinates;
    }

    public void setDrawingCoordinates(List<DrawingCoordinate> drawingCoordinates) {
        this.drawingCoordinates = drawingCoordinates;
    }

    @JsonIgnore
    public Set<BpmEntityTypeProcessRelation> getEntitySubTypeProcessList() {
        return entitySubTypeProcessList;
    }

    public void setEntitySubTypeProcessList(Set<BpmEntityTypeProcessRelation> entitySubTypeProcessList) {
        this.entitySubTypeProcessList = entitySubTypeProcessList;
    }

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<BpmProcess> getProcesses() {
        return processes;
    }

    @JsonIgnore
    public void setProcesses(List<BpmProcess> processes) {
        this.processes = processes;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Long getEntityBusinessTypeId() {
        return entityBusinessTypeId;
    }

    public void setEntityBusinessTypeId(Long entityBusinessTypeId) {
        this.entityBusinessTypeId = entityBusinessTypeId;
    }

    public BpmEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(BpmEntityType entityType) {
        this.entityType = entityType;
    }


    public BpmEntityType getEntityBusinessType() {
        return entityBusinessType;
    }

    public void setEntityBusinessType(BpmEntityType entityBusinessType) {
        this.entityBusinessType = entityBusinessType;
    }

    public boolean isSubDrawingFlg() {
        return subDrawingFlg;
    }

    public void setSubDrawingFlg(boolean subDrawingFlg) {
        this.subDrawingFlg = subDrawingFlg;
    }

    public List<BpmEntitySubTypeCheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<BpmEntitySubTypeCheckList> checkList) {
        this.checkList = checkList;
    }

    public int getDrawingPositionX() {
        return drawingPositionX;
    }

    public void setDrawingPositionX(int drawingPositionX) {
        this.drawingPositionX = drawingPositionX;
    }

    public int getDrawingPositionY() {
        return drawingPositionY;
    }

    public void setDrawingPositionY(int drawingPositionY) {
        this.drawingPositionY = drawingPositionY;
    }

    public int getDrawingScaleToFit() {
        return drawingScaleToFit;
    }

    public void setDrawingScaleToFit(int drawingScaleToFit) {
        this.drawingScaleToFit = drawingScaleToFit;
    }

    public int getCoverPositionX() {
        return coverPositionX;
    }

    public void setCoverPositionX(int coverPositionX) {
        this.coverPositionX = coverPositionX;
    }

    public int getCoverPositionY() {
        return coverPositionY;
    }

    public void setCoverPositionY(int coverPositionY) {
        this.coverPositionY = coverPositionY;
    }

    public int getCoverScaleToFit() {
        return coverScaleToFit;
    }

    public void setCoverScaleToFit(int coverScaleToFit) {
        this.coverScaleToFit = coverScaleToFit;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public int getDrawingShortX() {
        return drawingShortX;
    }

    public void setDrawingShortX(int drawingShortX) {
        this.drawingShortX = drawingShortX;
    }

    public int getDrawingShortY() {
        return drawingShortY;
    }

    public void setDrawingShortY(int drawingShortY) {
        this.drawingShortY = drawingShortY;
    }
}
