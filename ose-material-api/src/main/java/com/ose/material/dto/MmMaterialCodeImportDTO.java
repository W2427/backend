package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料导入数据传输对象。
 */
public class MmMaterialCodeImportDTO extends BaseDTO {

    private static final long serialVersionUID = -4717353117236271068L;

    @Schema(description = "上传的导入文件的临时文件名")
    private String fileName;

    @Schema(description = "专业")
    private DisciplineCode discipline;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }
}
