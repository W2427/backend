package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * CableTray 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EitComponentEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = -4932504783659503466L;

    @JsonCreator
    public EitComponentEntryCriteriaDTO() {
    }

}
