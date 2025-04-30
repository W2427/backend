package com.ose.report.dto;

import com.ose.dto.BaseDTO;

import java.util.List;

public class ModuleRelationDTO extends BaseDTO {

    private static final long serialVersionUID = 2188574468955238539L;

    private List<String> moduleRelationNos;

    private List<String> moduleNos;

    public List<String> getModuleRelationNos() {
        return moduleRelationNos;
    }

    public void setModuleRelationNos(List<String> moduleRelationNos) {
        this.moduleRelationNos = moduleRelationNos;
    }

    public List<String> getModuleNos() {
        return moduleNos;
    }

    public void setModuleNos(List<String> moduleNos) {
        this.moduleNos = moduleNos;
    }
}
