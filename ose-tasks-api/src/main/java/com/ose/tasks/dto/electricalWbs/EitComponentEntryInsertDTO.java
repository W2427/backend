package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;

/**
 * ELEC-COMPONENT 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EitComponentEntryInsertDTO extends BaseDTO {

    private static final long serialVersionUID = 809142965116718269L;
}
