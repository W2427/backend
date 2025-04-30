package com.ose.tasks.dto.material;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.report.dto.*;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 检验报告内容DTO
 */
public class FMaterialInspectionReportResultsDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "管材料验收报告-明细列表")
    private List<PipeMaterialInspectionItemDTO> pipeMaterialItemList;

    @Schema(description = "管附件验收报告-明细列表")
    private List<PipeFittingInspectionItemDTO> pipeFittingtemList;

    @Schema(description = "阀件验收报告-明细列表")
    private List<ValveInspectionItemDTO> valveMaterialItemList;

    @Schema(description = "结构材料入库报告-明细列表")
    private List<StructureMaterialReceiveItemDTO> structureMaterialReceiveItemList;

    @Schema(description = "南通结构材料入库报告-明细列表")
    private List<F253StructureMaterialInspectionReportItemDTO> f253StructureMaterialInspectionReportItemList;

    @Schema(description = "管系材料入库报告-明细列表")
    private List<PipingMaterialReceiveItemDTO> pipingMaterialReceiveItemList;

    public List<PipeMaterialInspectionItemDTO> getPipeMaterialItemList() {
        return pipeMaterialItemList;
    }

    public void setPipeMaterialItemList(List<PipeMaterialInspectionItemDTO> pipeMaterialItemList) {
        this.pipeMaterialItemList = pipeMaterialItemList;
    }

    public List<PipeFittingInspectionItemDTO> getPipeFittingtemList() {
        return pipeFittingtemList;
    }

    public void setPipeFittingtemList(List<PipeFittingInspectionItemDTO> pipeFittingtemList) {
        this.pipeFittingtemList = pipeFittingtemList;
    }

    public List<ValveInspectionItemDTO> getValveMaterialItemList() {
        return valveMaterialItemList;
    }

    public void setValveMaterialItemList(List<ValveInspectionItemDTO> valveMaterialItemList) {
        this.valveMaterialItemList = valveMaterialItemList;
    }

    public List<StructureMaterialReceiveItemDTO> getStructureMaterialReceiveItemList() {
        return structureMaterialReceiveItemList;
    }

    public void setStructureMaterialReceiveItemList(List<StructureMaterialReceiveItemDTO> structureMaterialReceiveItemList) {
        this.structureMaterialReceiveItemList = structureMaterialReceiveItemList;
    }

    public List<PipingMaterialReceiveItemDTO> getPipingMaterialReceiveItemList() {
        return pipingMaterialReceiveItemList;
    }

    public void setPipingMaterialReceiveItemList(List<PipingMaterialReceiveItemDTO> pipingMaterialReceiveItemList) {
        this.pipingMaterialReceiveItemList = pipingMaterialReceiveItemList;
    }

    public List<F253StructureMaterialInspectionReportItemDTO> getF253StructureMaterialInspectionReportItemList() {
        return f253StructureMaterialInspectionReportItemList;
    }

    public void setF253StructureMaterialInspectionReportItemList(List<F253StructureMaterialInspectionReportItemDTO> f253StructureMaterialInspectionReportItemList) {
        this.f253StructureMaterialInspectionReportItemList = f253StructureMaterialInspectionReportItemList;
    }
}
