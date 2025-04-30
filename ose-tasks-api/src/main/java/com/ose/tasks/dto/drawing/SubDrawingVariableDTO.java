package com.ose.tasks.dto.drawing;

import java.util.Map;

import com.ose.dto.BaseDTO;

/**
 * 传输对象
 */
public class SubDrawingVariableDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    private Map<String, Long> variable;

    public Map<String, Long> getVariable() {
        return variable;
    }

    public void setVariable(Map<String, Long> variable) {
        this.variable = variable;
    }

}
