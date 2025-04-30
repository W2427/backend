package com.ose.tasks.dto.trident;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

/**
 * WELD实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectNodeDetailCriteriaDTO extends PageDTO {


    private static final long serialVersionUID = 7805803550752486194L;
    @Schema(description = "类型")
    private String entityType;

    private String keyWord;

    private String isInSkid;

    private String sector;

    private String tagNo;

    private String subSystem;

    private Long currentInChargeId;

    @Schema(description = "子类型")
    private String entitySubType;

    @Schema(description = "fGroup")
    private String fGroup;

    @Schema(description = "子系统IDs")
    private List<Long> subSystemIds;

    @Schema(description = "包ID")
    private Long packageId;

    private List<Long> ancestorHierarchyIds;

    private Set<Long> subSystemNodeIds;

    private Boolean pulled;

    private Boolean t1;

    private Boolean t2;

    private Boolean cco;

    private Boolean submited;

    private Boolean signed;

    private Boolean closed;

    private Boolean isFetchAll;

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

    public List<Long> getSubSystemIds() {
        return subSystemIds;
    }

    public void setSubSystemIds(List<Long> subSystemIds) {
        this.subSystemIds = subSystemIds;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<Long> getAncestorHierarchyIds() {
        return ancestorHierarchyIds;
    }

    public void setAncestorHierarchyIds(List<Long> ancestorHierarchyIds) {
        this.ancestorHierarchyIds = ancestorHierarchyIds;
    }

    public Set<Long> getSubSystemNodeIds() {
        return subSystemNodeIds;
    }

    public void setSubSystemNodeIds(Set<Long> subSystemNodeIds) {
        this.subSystemNodeIds = subSystemNodeIds;
    }

    public Boolean getSubmited() {
        return submited;
    }

    public void setSubmited(Boolean submited) {
        this.submited = submited;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getfGroup() {
        return fGroup;
    }

    public void setfGroup(String fGroup) {
        this.fGroup = fGroup;
    }

    public Boolean getPulled() {
        return pulled;
    }

    public void setPulled(Boolean pulled) {
        this.pulled = pulled;
    }

    public Boolean getT1() {
        return t1;
    }

    public void setT1(Boolean t1) {
        this.t1 = t1;
    }

    public Boolean getT2() {
        return t2;
    }

    public void setT2(Boolean t2) {
        this.t2 = t2;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public Long getCurrentInChargeId() {
        return currentInChargeId;
    }

    public void setCurrentInChargeId(Long currentInChargeId) {
        this.currentInChargeId = currentInChargeId;
    }

    public Boolean getCco() {
        return cco;
    }

    public void setCco(Boolean cco) {
        this.cco = cco;
    }

    @Override
    public Boolean getFetchAll() {
        return isFetchAll;
    }

    @Override
    public void setFetchAll(Boolean fetchAll) {
        isFetchAll = fetchAll;
    }

    public String getIsInSkid() {
        return isInSkid;
    }

    public void setIsInSkid(String isInSkid) {
        this.isInSkid = isInSkid;
    }
}
