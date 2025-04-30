package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class StructurePartDetailListDTO extends BaseDTO {

    private static final long serialVersionUID = -8346966276728071408L;

    @Schema(description = "结构配送单打印实体")
    private List<StructurePartDetailDTO> printList;

    public List<StructurePartDetailDTO> getPrintList() {
        return printList;
    }

    public void setPrintList(List<StructurePartDetailDTO> printList) {
        this.printList = printList;
    }
}
