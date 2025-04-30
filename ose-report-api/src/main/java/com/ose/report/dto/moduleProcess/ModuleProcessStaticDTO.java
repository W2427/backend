package com.ose.report.dto.moduleProcess;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ModuleProcessStaticDTO extends BaseDTO {

    private static final long serialVersionUID = -9039391979478949169L;

    @Schema(description = "模块进度")
    private List<ModuleProcessStaticItemDTO> moduleProcessStaticItemDTOs;

    @Schema(description = "功能块")
    private String funcPart;

    public List<ModuleProcessStaticItemDTO> getModuleProcessStaticItemDTOs() {
        return moduleProcessStaticItemDTOs;
    }

    public void setModuleProcessStaticItemDTOs(List<ModuleProcessStaticItemDTO> moduleProcessStaticItemDTOs) {
        this.moduleProcessStaticItemDTOs = moduleProcessStaticItemDTOs;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
