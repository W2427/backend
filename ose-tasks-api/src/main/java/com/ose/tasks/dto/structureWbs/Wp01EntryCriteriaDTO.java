package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * Module 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wp01EntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = 7806585079165916170L;


    @JsonCreator
    public Wp01EntryCriteriaDTO() {
    }

}
