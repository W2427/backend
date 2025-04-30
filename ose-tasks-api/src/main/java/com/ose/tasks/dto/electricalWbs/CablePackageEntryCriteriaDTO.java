package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;

/**
 * CablePullingPackage 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CablePackageEntryCriteriaDTO extends WBSEntryCriteriaBaseDTO {


    private static final long serialVersionUID = 2360909609552548718L;

    @JsonCreator
    public CablePackageEntryCriteriaDTO() {
    }

}
