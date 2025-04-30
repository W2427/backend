package com.ose.tasks.entity.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "welder")
public class Welder extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8455722264222411792L;

    @Column
    private String name;

    @Column
    private String nameEn;

    @Column
    private Long photo;

    @Column(length = 18)
    private String idCard;

    @Column
    private String no;

    @Column
    private Long subCon;

    @Column
    private Long orgId;

    @Column
    private Long projectId;

    @Column
    private Long userId;

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoto() {
        return photo;
    }

    public void setPhoto(Long photo) {
        this.photo = photo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getSubCon() {
        return subCon;
    }

    public void setSubCon(Long subCon) {
        this.subCon = subCon;
    }

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

    @JsonProperty(value = "subCon", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getSubConReference() {
        return this.subCon == null ? null : new ReferenceData(this.subCon);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @JsonProperty(value = "userId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getUserIdReference() {
        return this.userId == null ? null : new ReferenceData(this.userId);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.userId);
        return relatedUserIDs;
    }
}
