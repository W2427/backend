package com.ose.report.dto;

import com.ose.dto.BaseDTO;

import java.util.List;

public class ModuleRelationSearchDTO extends BaseDTO {

    private static final long serialVersionUID = 6050633736358203198L;

    private List<String> moduleNos;

    private String discipline;

    private String funcPart;

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public List<String> getModuleNos() {
        return moduleNos;
    }

    public void setModuleNos(List<String> moduleNos) {
        this.moduleNos = moduleNos;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
