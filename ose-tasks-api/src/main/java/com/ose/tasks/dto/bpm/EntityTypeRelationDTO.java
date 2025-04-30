package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 实体分类关系 数据传输对象
 */
public class EntityTypeRelationDTO extends BaseDTO {


    private static final long serialVersionUID = -4613410278102155469L;

    private Long entityTypeId;

    // 实体大类英文
    private String wbsEntityType;

    // 实体规则分类
    private String relatedWbsEntityType;

    //关系代理
    private String relationDelegate;

    private String memo;

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntityType() {
        return wbsEntityType;
    }

    public void setWbsEntityType(String wbsEntityType) {
        this.wbsEntityType = wbsEntityType;
    }

    public String getRelatedWbsEntityType() {
        return relatedWbsEntityType;
    }

    public void setRelatedWbsEntityType(String relatedWbsEntityType) {
        this.relatedWbsEntityType = relatedWbsEntityType;
    }

    public String getRelationDelegate() {
        return relationDelegate;
    }

    public void setRelationDelegate(String relationDelegate) {
        this.relationDelegate = relationDelegate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
