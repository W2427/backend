package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.material.FMaterialStructureNestDrawing;
import com.ose.tasks.entity.material.FMaterialStructureNestProgram;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class FMaterialStructureNestImportSheetReturnDTO extends BaseDTO {

    private static final long serialVersionUID = 9113257285053838632L;

    @Schema(description = "结构套料导出返回数据表")
    private Sheet sheetItem;

    @Schema(description = "结构套料导出返回排版列表")
    private List<FMaterialStructureNestProgram> fMaterialStructureNestPrograms;

    @Schema(description = "结构套料导出返回零件列表")
    private List<FMaterialStructureNestDrawing> fMaterialStructureNestDrawings;

    @Schema(description = "结构套料排版失败数")
    private Integer fMaterialStructureNestProgramFailCount;


    @Schema(description = "结构套料零件失败数")
    private Integer fMaterialStructureNestDrawingFailCount;

    public Sheet getSheetItem() {
        return sheetItem;
    }

    public void setSheetItem(Sheet sheetItem) {
        this.sheetItem = sheetItem;
    }

    public List<FMaterialStructureNestProgram> getfMaterialStructureNestPrograms() {
        return fMaterialStructureNestPrograms;
    }

    public void setfMaterialStructureNestPrograms(List<FMaterialStructureNestProgram> fMaterialStructureNestPrograms) {
        this.fMaterialStructureNestPrograms = fMaterialStructureNestPrograms;
    }

    public List<FMaterialStructureNestDrawing> getfMaterialStructureNestDrawings() {
        return fMaterialStructureNestDrawings;
    }

    public void setfMaterialStructureNestDrawings(List<FMaterialStructureNestDrawing> fMaterialStructureNestDrawings) {
        this.fMaterialStructureNestDrawings = fMaterialStructureNestDrawings;
    }

    public Integer getfMaterialStructureNestProgramFailCount() {
        return fMaterialStructureNestProgramFailCount;
    }

    public void setfMaterialStructureNestProgramFailCount(Integer fMaterialStructureNestProgramFailCount) {
        this.fMaterialStructureNestProgramFailCount = fMaterialStructureNestProgramFailCount;
    }

    public Integer getfMaterialStructureNestDrawingFailCount() {
        return fMaterialStructureNestDrawingFailCount;
    }

    public void setfMaterialStructureNestDrawingFailCount(Integer fMaterialStructureNestDrawingFailCount) {
        this.fMaterialStructureNestDrawingFailCount = fMaterialStructureNestDrawingFailCount;
    }
}
