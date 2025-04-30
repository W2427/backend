package com.ose.materialspm.domain.model.service.impl;


import com.ose.docs.api.UploadFeignAPI;
import com.ose.exception.NotFoundError;
import com.ose.materialspm.domain.model.repository.PohRepository;
import com.ose.materialspm.domain.model.service.PoInterface;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.ExportFileInputDataDTO;
import com.ose.materialspm.dto.PohDTO;
import com.ose.materialspm.entity.PoDetail;
import com.ose.materialspm.entity.ViewMxjValidPohEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PoService implements PoInterface {


    /**
     * 合同  操作仓库
     */
    private final PohRepository pohRepository;

    private final MaterialSPMExportExcelService exportExcelService;

    /**
     * 构造方法
     */
    @Autowired
    public PoService(
        PohRepository pohRepository,
        MaterialSPMExportExcelService exportExcelService
    ) {
        this.pohRepository = pohRepository;
        this.exportExcelService = exportExcelService;
    }

    @Override
    public Page<ViewMxjValidPohEntity> getPohs(PohDTO pohDTO) {

        Page<ViewMxjValidPohEntity> pohList = pohRepository.getPohs(pohDTO);

        return pohList;
    }

    @Override
    public Page<PoDetail> getDetail(PohDTO pohDTO) {

        Page<PoDetail> pohList = pohRepository.getDetail(pohDTO);

        return pohList;
    }

    @Override
    public ViewMxjValidPohEntity getPoh(PohDTO pohDTO) {
        return pohRepository.findByProjIdAndId(pohDTO.getSpmProjId(), pohDTO.getPohId());
    }

    @Override
    public ExportFileDTO exportPoh(Long orgId, Long projectId, PohDTO pohDTO, UploadFeignAPI uploadFeignAPI) {

        ViewMxjValidPohEntity viewMxjValidPohEntity = pohRepository.findByProjIdAndId(pohDTO.getSpmProjId(), pohDTO.getPohId());
        if (viewMxjValidPohEntity == null) {
            // TODO
            throw new NotFoundError("poh id is not exist");
        }
        List<PoDetail> poDetailList = pohRepository.getDetailNoPage(pohDTO);

        List<ExportFileInputDataDTO> exportFileInputDataDTOList = new ArrayList<>();
        List<Map<String, Object>> exportMainDataList = new ArrayList<>();

        Map<String, Object> exportData;
        int idx = 1;
        for (PoDetail poDetail : poDetailList) {
            exportData = new HashMap<>();
            exportData.put("index", idx + "");
            exportData.put("ident", poDetail.getIdent().toString());
            exportData.put("commodity_code", poDetail.getCommodityCode().toString());
            exportData.put("short_desc", poDetail.getShortDesc().toString());
            exportData.put("unit_code", poDetail.getUnitCode().toString());
            exportData.put("poli_qty", poDetail.getPoliQty().toString());
            exportData.put("inv_qty", poDetail.getInvQty().toString());
            // un_inv_qty = poli_qty - inv_qty
            BigDecimal poliQty = new BigDecimal(poDetail.getPoliQty().toString());
            BigDecimal invQty = new BigDecimal(poDetail.getInvQty().toString());
            exportData.put("un_inv_qty", poliQty.subtract(invQty).toString());
            exportMainDataList.add(exportData);

            idx++;
        }

        ExportFileInputDataDTO exportFileInputDataDTO;
        // Main
        exportFileInputDataDTO = new ExportFileInputDataDTO();
        exportFileInputDataDTO.setSheetName("Bom_List_POS");
        exportFileInputDataDTO.setTitle(getIssueTitle("MAIN"));
        exportFileInputDataDTO.setExportDataList(exportMainDataList);
        exportFileInputDataDTOList.add(exportFileInputDataDTO);

        return exportExcelService.exportExcelXlsxForMultipleSheet(
            orgId,
            projectId,
            uploadFeignAPI,
            exportFileInputDataDTOList,
            String.format("合同-%s", viewMxjValidPohEntity.getPoNumber())
        );
    }

    private LinkedHashMap<String, String> getIssueTitle(String titleType) {
        LinkedHashMap<String, String> title = new LinkedHashMap<>();

        switch (titleType) {
            case "MAIN":
                title.put("index", "节点序号");
                title.put("ident", "Ident");
                title.put("commodity_code", "材料编码");
                title.put("short_desc", "材料描述");
                title.put("unit_code", "计量单位");
                title.put("poli_qty", "订单数量");
                title.put("inv_qty", "入库数量");
                title.put("un_inv_qty", "未入库数量");
                break;
            default:
                break;
        }

        return title;
    }

}
