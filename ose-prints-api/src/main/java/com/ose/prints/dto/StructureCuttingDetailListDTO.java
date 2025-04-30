package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class StructureCuttingDetailListDTO extends BaseDTO {

    private static final long serialVersionUID = 8686479002916882854L;

    @Schema(description = "结构下料打印信息")
    private List<StructureCuttingDetailDTO> printList;

    public List<StructureCuttingDetailDTO> getPrintList() {
        return printList;
    }

    public void setPrintList(List<StructureCuttingDetailDTO> printList) {
        this.printList = printList;
    }
}
