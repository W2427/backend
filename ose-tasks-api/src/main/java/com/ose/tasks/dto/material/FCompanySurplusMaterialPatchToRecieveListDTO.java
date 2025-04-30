package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 余料dto
 */
public class FCompanySurplusMaterialPatchToRecieveListDTO extends BaseDTO {
    private static final long serialVersionUID = -3548931677298658302L;

    @Schema(description = "待确认的余料DTO")
    private List<FCompanySurplusMaterialPatchToRecieveDTO> fCompanySurplusMaterialPatchToRecieveDTOList;

    public List<FCompanySurplusMaterialPatchToRecieveDTO> getfCompanySurplusMaterialPatchToRecieveDTOList() {
        return fCompanySurplusMaterialPatchToRecieveDTOList;
    }

    public void setfCompanySurplusMaterialPatchToRecieveDTOList(List<FCompanySurplusMaterialPatchToRecieveDTO> fCompanySurplusMaterialPatchToRecieveDTOList) {
        this.fCompanySurplusMaterialPatchToRecieveDTOList = fCompanySurplusMaterialPatchToRecieveDTOList;
    }
}
