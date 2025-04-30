package com.ose.tasks.entity.bpm;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.util.StringUtils;

/**
 * 材料文档实体表。
 */
@Entity
@Table(name = "Bpm_Entity_Docs_Material"
    ,
    indexes = {
        @Index(columnList = "process_id,entity_id,actInstanceId"),
        @Index(columnList = "process_id,entity_id,actInstanceId,type")
        ,
        @Index(columnList = "entity_no")
}
)
public class BpmEntityDocsMaterials extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;


    // 组织id
    @Column(name = "project_id")
    private Long projectId;

    // 实体编号
    @Column(name = "entity_no")
    private String entityNo;
    // 实体id
    @Column(name = "entity_id")
    private Long entityId;

    //    工序id
    @Column(nullable = true, name = "process_id")
    private Long processId;

    //    文档
    @Column(name = "docs", columnDefinition = "text")
    private String docs;

    //    类型
    @Enumerated(EnumType.STRING)
    private ActInstDocType type;

    //    外检结果
    private String externalInspectionResult;

    //    最终外检
    private boolean externalInspectionFinal;

    //    操作者
    private Long operator;

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    // 流程id
    private Long actInstanceId;

    // 工序
//    @ManyToOne
//    @JoinColumn(name = "process_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private BpmProcess process;

    @JsonIgnore
    public void setJsonDocs(List<ActReportDTO> docs) {
        if (docs != null) {
            this.docs = StringUtils.toJSON(docs);
        }
    }

    @JsonProperty(value = "docsdocs", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonDocsReadOnly() {
        if (docs != null && !"".equals(docs)) {
            return StringUtils.decode(docs, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public ActInstDocType getType() {
        return type;
    }

    public void setType(ActInstDocType type) {
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getExternalInspectionResult() {
        return externalInspectionResult;
    }

    public void setExternalInspectionResult(String externalInspectionResult) {
        this.externalInspectionResult = externalInspectionResult;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public boolean isExternalInspectionFinal() {
        return externalInspectionFinal;
    }

    public void setExternalInspectionFinal(boolean externalInspectionFinal) {
        this.externalInspectionFinal = externalInspectionFinal;
    }

//    public BpmProcess getProcess() {
//        return process;
//    }
//
//    public void setProcess(BpmProcess process) {
//        this.process = process;
//    }
//
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getActInstanceId() {
        return actInstanceId;
    }

    public void setActInstanceId(Long actInstanceId) {
        this.actInstanceId = actInstanceId;
    }
}
