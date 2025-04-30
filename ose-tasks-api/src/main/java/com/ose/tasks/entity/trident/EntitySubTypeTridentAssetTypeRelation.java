package com.ose.tasks.entity.trident;

import com.ose.entity.BaseEntity;

import jakarta.persistence.*;

/**
 * @Description
 * @Author  Hunter
 * @Date 2021-10-01
 * TRIDENT ASSET TYPE
 */

@Entity
@Table ( name ="bpm_entity_sub_type_trident_asset_type_relation" )
public class EntitySubTypeTridentAssetTypeRelation extends BaseEntity {


    private static final long serialVersionUID = -8113665908003836277L;

    @Column(name = "AssetTypeID")
    private Integer assetTypeId;

    @Column(name = "AssetType")
    private String assetType;

    @Column(name = "AssetSubType")
    private String assetSubType;

    @Column
    private Long projectId;

    @Column(name = "entity_type")

    private String entityType;


    @Column(name = "discipline")
    private String discipline;

    @Column(name = "entity_sub_type")
    private String entitySubType;

    @Column(name = "entity_sub_type_id")
    private Long entitySubTypeId;

    @Column
    private Boolean isInTrident;


    public Integer getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Integer assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetSubType() {
        return assetSubType;
    }

    public void setAssetSubType(String assetSubType) {
        this.assetSubType = assetSubType;
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

    public Boolean getInTrident() {
        return isInTrident;
    }

    public void setInTrident(Boolean inTrident) {
        isInTrident = inTrident;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
