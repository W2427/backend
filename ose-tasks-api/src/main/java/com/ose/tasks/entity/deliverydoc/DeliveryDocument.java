package com.ose.tasks.entity.deliverydoc;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.util.StringUtils;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "delivery_document")
public class DeliveryDocument extends BaseVersionedBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    @Transient
    private Double rateOfProgress;

    //项目id
    private Long projectId;

    //模块名
    private String module;

    //工序名
    private String process;

    //类型
    private String type;

    //生成状态
    private Boolean generateFlag;

    //备注
    private String memo;

    //文档包
    @Column(name = "documents", columnDefinition = "text")
    private String documents;

    @JsonProperty(value = "jsonDocuments", access = JsonProperty.Access.READ_ONLY)
    public List<ActReportDTO> getJsonDocumentsReadOnly() {
        if (documents != null && !"".equals(documents)) {
            return StringUtils.decode(documents, new TypeReference<List<ActReportDTO>>() {
            });
        } else {
            return new ArrayList<ActReportDTO>();
        }
    }

    @JsonIgnore
    public void setJsonDocuments(List<ActReportDTO> documents) {
        if (documents != null) {
            this.documents = StringUtils.toJSON(documents);
        }
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Boolean getGenerateFlag() {
        return generateFlag;
    }

    public void setGenerateFlag(Boolean generateFlag) {
        this.generateFlag = generateFlag;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getRateOfProgress() {
        return rateOfProgress;
    }

    public void setRateOfProgress(Double rateOfProgress) {
        this.rateOfProgress = rateOfProgress;
    }
}
