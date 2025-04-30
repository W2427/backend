package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * PANEL 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wp03EntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = -6537481542183394188L;

    @JsonCreator
    public Wp03EntryCriteriaDTO() {
    }

}
