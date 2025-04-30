package com.ose.tasks.dto;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ProjectInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 4597709308833365375L;
    @Schema(description = "项目信息Key")
    private String infoKey;

    @Schema(description = "项目信息Value")
    private String infoValue;

    @Schema(description = "")
    private List<HierarchyWBSDTO> wbsTreeData;

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }

    public List<HierarchyWBSDTO> getWbsTreeData() {
        return wbsTreeData;
    }

    public void setWbsTreeData(List<HierarchyWBSDTO> wbsTreeData) {
        this.wbsTreeData = wbsTreeData;
    }
}
