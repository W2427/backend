package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * EIT Package 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EqPackageEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = 343606932636113914L;

    private String entityType;

    private String entitySubType;

    private String keyWord;

    @JsonCreator
    public EqPackageEntryCriteriaDTO() {
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }
}
