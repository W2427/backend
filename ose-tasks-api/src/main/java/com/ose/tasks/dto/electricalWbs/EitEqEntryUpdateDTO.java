package com.ose.tasks.dto.electricalWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;

/**
 * EIT EQ 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EitEqEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -5188908612091311909L;
}
