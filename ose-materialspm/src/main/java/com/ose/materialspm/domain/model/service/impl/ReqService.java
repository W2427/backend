package com.ose.materialspm.domain.model.service.impl;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.exception.NotFoundError;
import com.ose.materialspm.domain.model.repository.ReqDetailRepository;
import com.ose.materialspm.domain.model.repository.ReqListRepository;
import com.ose.materialspm.domain.model.service.ReqInterface;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.ExportFileInputDataDTO;
import com.ose.materialspm.dto.ReqDetailDTO;
import com.ose.materialspm.dto.ReqListCriteriaDTO;
import com.ose.materialspm.entity.ReqDetail;
import com.ose.materialspm.entity.ViewMxjReqs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReqService implements ReqInterface {

    // 请购单信息操作仓库
    private final ReqListRepository reqListRepository;

    // 请购单详情操作仓库
    private final ReqDetailRepository reqDetailRepository;

    private final MaterialSPMExportExcelService exportExcelService;

    /**
     * 构造方法
     *
     * @param reqListRepository   List
     * @param reqDetailRepository Detail
     * @param exportExcelService  export
     */
    @Autowired
    public ReqService(
        ReqListRepository reqListRepository,
        ReqDetailRepository reqDetailRepository,
        MaterialSPMExportExcelService exportExcelService
    ) {
        this.reqListRepository = reqListRepository;
        this.reqDetailRepository = reqDetailRepository;
        this.exportExcelService = exportExcelService;
    }

    /**
     * 查询请购单列表信息。
     *
     * @param reqListDTO 查询条件
     * @return 请购单列表分页数据
     */
    @Override
    public Page<ViewMxjReqs> getList(ReqListCriteriaDTO reqListDTO) {

        Page<ViewMxjReqs> reqList = reqListRepository.getList(reqListDTO);

        return reqList;
    }

    /**
     * 查询请购单明细信息。
     *
     * @param reqDetailDTO 查询条件
     * @return 请购单详情分页数据
     */
    @Override
    public Page<ReqDetail> getDetail(ReqDetailDTO reqDetailDTO) {

        Page<ReqDetail> reqDetailList = reqDetailRepository.getDetail(reqDetailDTO);

        return reqDetailList;
    }

    /**
     * 查询请购单详情信息。
     *
     * @param reqDetailDTO 查询条件
     * @return 请购单信息
     */
    @Override
    public ViewMxjReqs getReq(ReqDetailDTO reqDetailDTO) {
        return reqListRepository.findByProjectIdAndId(reqDetailDTO.getSpmProjId(), reqDetailDTO.getReqId());
    }

    @Override
    public ExportFileDTO exportReq(Long orgId, Long projectId, ReqDetailDTO reqDetailDTO, UploadFeignAPI uploadFeignAPI) {

        String spmProjId = reqDetailDTO.getSpmProjId();

        ViewMxjReqs viewMxjReqs = reqListRepository.findByProjectIdAndId(spmProjId, reqDetailDTO.getReqId());
        List<ReqDetail> reqDetailList = reqDetailRepository.getDetailNoPage(reqDetailDTO);
        if (viewMxjReqs == null) {
            // TODO
            throw new NotFoundError("req id is not exist");
        }

        List<ExportFileInputDataDTO> exportFileInputDataDTOList = new ArrayList<>();
        List<Map<String, Object>> exportMainDataList = new ArrayList<>();
        List<Map<String, Object>> exportDetailDataList = new ArrayList<>();

        String reqCode = String.format("%s(%s)", viewMxjReqs.getReqCode(), viewMxjReqs.getReqSupp());

        Map<String, Object> exportMainData = new HashMap<>();
        exportMainData.put("proj_id", viewMxjReqs.getProjectId());
        exportMainData.put("r_code", reqCode);
        exportMainData.put("bom_no", viewMxjReqs.getBomNo());
        exportMainData.put("description", viewMxjReqs.getDescription());
        exportMainData.put("dp_code", viewMxjReqs.getDpCode());
        exportMainData.put("originator", viewMxjReqs.getOriginator());
        exportMainData.put("approved_date", viewMxjReqs.getApprovedDate());
        exportMainDataList.add(exportMainData);

        Map<String, Object> exportData;
        int idx = 1;
        for (ReqDetail reqDetail : reqDetailList) {
            exportData = new HashMap<>();
            exportData.put("index", idx + "");
            exportData.put("commodity_code", reqDetail.getCommodityCode());
            exportData.put("ident_code", reqDetail.getIdentCode());
            exportData.put("short_desc", reqDetail.getShortDesc());
            exportData.put("unit_code}", reqDetail.getUnitCode());
            exportData.put("increase_qty", reqDetail.getIncreasePty());
            exportData.put("total_release_qty", reqDetail.getTotalReleaseQty());
            exportDetailDataList.add(exportData);

            idx++;
        }

        ExportFileInputDataDTO exportFileInputDataDTO;
        // Main
        exportFileInputDataDTO = new ExportFileInputDataDTO();
        exportFileInputDataDTO.setSheetName("Req_Title");
        exportFileInputDataDTO.setTitle(getIssueTitle("MAIN"));
        exportFileInputDataDTO.setExportDataList(exportMainDataList);
        exportFileInputDataDTOList.add(exportFileInputDataDTO);


        // Detail
        exportFileInputDataDTO = new ExportFileInputDataDTO();
        exportFileInputDataDTO.setSheetName("Req_Line_Items");
        exportFileInputDataDTO.setTitle(getIssueTitle("DETAIL"));
        exportFileInputDataDTO.setExportDataList(exportDetailDataList);
        exportFileInputDataDTOList.add(exportFileInputDataDTO);

        return exportExcelService.exportExcelXlsxForMultipleSheet(
            orgId,
            projectId,
            uploadFeignAPI,
            exportFileInputDataDTOList,
            String.format("请购单-%s", reqCode)
        );
    }

    private LinkedHashMap<String, String> getIssueTitle(String titleType) {
        LinkedHashMap<String, String> title = new LinkedHashMap<>();

        switch (titleType) {
            case "MAIN":
                title.put("proj_id", "项目号");
                title.put("r_code", "请购单号");
                title.put("bom_no", "BOM");
                title.put("description", "请购单内容描述");
                title.put("dp_code", "专业代码");
                title.put("originator", "编制");
                title.put("approved_date", "日期");

                break;
            case "DETAIL":
                title.put("index", "序号");
                title.put("commodity_code", "材料编码");
                title.put("ident_code", "IDENT");
                title.put("short_desc", "材料描述");
                title.put("unit_code}", "单位");
                title.put("increase_qty", "本版请购发布总量");
                title.put("total_release_qty", "累计总量");

                break;
            default:
                break;
        }

        return title;
    }

}
