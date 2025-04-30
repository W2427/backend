package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

public class FCompanySurplusMaterialPostDTO extends BaseDTO {
    private static final long serialVersionUID = -8872969428561676388L;

    private String companyName;

    List<FCompanySurplusMaterialCreateDTO> createDTOList = new ArrayList<>();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<FCompanySurplusMaterialCreateDTO> getCreateDTOList() {
        return createDTOList;
    }

    public void setCreateDTOList(List<FCompanySurplusMaterialCreateDTO> createDTOList) {
        this.createDTOList = createDTOList;
    }
}
