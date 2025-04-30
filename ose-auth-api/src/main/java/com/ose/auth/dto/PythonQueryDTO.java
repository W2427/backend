package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class PythonQueryDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 2090188150916713860L;

    private String prqlStr;
}
