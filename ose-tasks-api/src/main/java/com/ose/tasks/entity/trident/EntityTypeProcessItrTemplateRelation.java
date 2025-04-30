package com.ose.tasks.entity.trident;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetType;

import jakarta.persistence.*;


@Entity
@Table ( name ="entity_type_process_itr_template_relation" )
public class EntityTypeProcessItrTemplateRelation extends BaseBizEntity {


    private static final long serialVersionUID = 2955299008830618850L;
    @Column(name = "entity_sub_type_process_relation_id" )
	private Long entitySubTypeProcessRelationId;

    @Column
	private String entityType;

   	@Column(name = "entity_sub_type" )
	private String entitySubType;

   	@Column(name = "entity_sub_type_id")
	private Long entitySubTypeId;

   	@Column(name = "process_id" )
	private Long processId;

    @Column(name = "process_stage" )
    private String processStage;

    @Column(name = "process" )
    private String process;

    @Column
    @Enumerated(EnumType.STRING)
    private CheckSheetStage checkSheetStage;

    @Column
    @Enumerated(EnumType.STRING)
    private CheckSheetType checkSheetType;

   	@Column
    private Long projectId;

   	@Column
    private Long fileId;

    @Column
    private String itrTemplateNo;

    @Column
    private Long itrTemplateId;

   	@Column
    private String itrTemplateDesc;

    @Column
    private Integer assetTypeId;

    @Column
    private String clazzForList;

    @Column
    private Integer seq;

    public Long getEntitySubTypeProcessRelationId() {
        return entitySubTypeProcessRelationId;
    }

    public void setEntitySubTypeProcessRelationId(Long entitySubTypeProcessRelationId) {
        this.entitySubTypeProcessRelationId = entitySubTypeProcessRelationId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public CheckSheetStage getCheckSheetStage() {
        return checkSheetStage;
    }

    public void setCheckSheetStage(CheckSheetStage checkSheetStage) {
        this.checkSheetStage = checkSheetStage;
    }

    public CheckSheetType getCheckSheetType() {
        return checkSheetType;
    }

    public void setCheckSheetType(CheckSheetType checkSheetType) {
        this.checkSheetType = checkSheetType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getItrTemplateNo() {
        return itrTemplateNo;
    }

    public void setItrTemplateNo(String itrTemplateNo) {
        this.itrTemplateNo = itrTemplateNo;
    }

    public String getItrTemplateDesc() {
        return itrTemplateDesc;
    }

    public void setItrTemplateDesc(String itrTemplateDesc) {
        this.itrTemplateDesc = itrTemplateDesc;
    }

    public Integer getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Integer assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public Long getItrTemplateId() {
        return itrTemplateId;
    }

    public void setItrTemplateId(Long itrTemplateId) {
        this.itrTemplateId = itrTemplateId;
    }

    public String getClazzForList() {
        return clazzForList;
    }

    public void setClazzForList(String clazzForList) {
        this.clazzForList = clazzForList;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
