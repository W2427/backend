package com.ose.materialspm.domain.model.service.impl;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.domain.model.repository.MvMxjListNodesRepository;
import com.ose.materialspm.domain.model.repository.ViewMxjPosRepository;
import com.ose.materialspm.domain.model.service.BomInterface;
import com.ose.materialspm.dto.BomNodeDTO;
import com.ose.materialspm.dto.BomNodeResultsDTO;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.ExportFileInputDataDTO;
import com.ose.materialspm.entity.MvMxjListNodesEntity;
import com.ose.materialspm.entity.ViewMxjPosEntity;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BomService implements BomInterface {


    /**
     * 工序  操作仓库
     */
    private final MvMxjListNodesRepository mvMxjListNodesRepository;

    private final ViewMxjPosRepository viewMxjPosRepository;

    private Map<String, List<BomNodeResultsDTO>> rsMap;

    private final MaterialSPMExportExcelService exportExcelService;

    /**
     * 构造方法
     *
     * @param mvMxjListNodesRepository BOM节点 操作仓库
     * @param exportExcelService       Excel导出服务
     */
    @Autowired
    public BomService(
        MvMxjListNodesRepository mvMxjListNodesRepository,
        ViewMxjPosRepository viewMxjPosRepository,
        MaterialSPMExportExcelService exportExcelService
    ) {
        this.mvMxjListNodesRepository = mvMxjListNodesRepository;
        this.viewMxjPosRepository = viewMxjPosRepository;
        this.exportExcelService = exportExcelService;
    }

    @Override
    public List<MvMxjListNodesEntity> getByProjIdAndParentLnId(String spmProjId, String parentLnId) {
        // TODO Auto-generated method stub

        List<MvMxjListNodesEntity> rsList = null;
        if (StringUtils.isEmpty(parentLnId)) {
            MvMxjListNodesEntity bomRoot = new MvMxjListNodesEntity();
            bomRoot.setLnId("0");
            bomRoot.setParentLnId("-1");
            bomRoot.setLnCode(spmProjId);
            bomRoot.setBompath("/");
            bomRoot.setProjId(spmProjId);

            rsList = new ArrayList<MvMxjListNodesEntity>();
            rsList.add(bomRoot);
        } else {
            rsList = mvMxjListNodesRepository.findByProjIdAndParentLnId(spmProjId, parentLnId);
        }

        return rsList;
    }

    @Override
    public List<BomNodeResultsDTO> getByProjId(String spmProjId) {
        // TODO Auto-generated method stub
        List<MvMxjListNodesEntity> rsList = mvMxjListNodesRepository.findByProjId(spmProjId);

        this.rsMap = new HashMap<String, List<BomNodeResultsDTO>>();
        for (MvMxjListNodesEntity rs : rsList) {
            String parentLnId = rs.getParentLnId();
            List<BomNodeResultsDTO> list = null;
            if (this.rsMap.containsKey(parentLnId)) {
                list = this.rsMap.get(parentLnId);
            } else {
                list = new ArrayList<>();
            }
            BomNodeResultsDTO dto = new BomNodeResultsDTO();
            dto.setLnId(rs.getLnId());
            dto.setLnCode(rs.getLnCode());
            dto.setBompath(rs.getBompath());
            list.add(dto);
            this.rsMap.put(parentLnId, list);
        }

        return getNodeTree(this.rsMap.get("0"));
    }

    @Override
    public Page<ViewMxjPosEntity> getByProjIdAndBompath(BomNodeDTO bomNodeDTO) {
        // TODO Auto-generated method stub

        return viewMxjPosRepository.findByProjIdAndBompath(bomNodeDTO.getSpmProjId(), bomNodeDTO.getBompath(), bomNodeDTO.toPageable());

    }

    private List<BomNodeResultsDTO> getNodeTree(List<BomNodeResultsDTO> results) {

        for (BomNodeResultsDTO r : results) {
            r.setChildren(getNodeChlidren(r.getLnId()));
        }
        return results;
    }

    private List<BomNodeResultsDTO> getNodeChlidren(String lnid) {
        List<BomNodeResultsDTO> rs = null;

        if (this.rsMap.containsKey(lnid)) {
            rs = this.rsMap.get(lnid);
            for (BomNodeResultsDTO r : rs) {
                r.setChildren(getNodeChlidren(r.getLnId()));
            }
        } else {
            rs = new ArrayList<BomNodeResultsDTO>();
        }

        return rs;
    }

    @Override
    public ExportFileDTO exportBoms(Long orgId, Long projectId, BomNodeDTO bomNodeDTO, UploadFeignAPI uploadFeignAPI) {

        String spmProjId = bomNodeDTO.getSpmProjId();
        String bompath = bomNodeDTO.getBompath();

        List<ViewMxjPosEntity> viewMxjPosEntityList;
        if (!StringUtils.isEmpty(bompath)) {
            viewMxjPosEntityList = viewMxjPosRepository.findByProjIdAndBompath(spmProjId, bompath);
        } else {
            viewMxjPosEntityList = viewMxjPosRepository.findByProjId(spmProjId);
        }

        List<ExportFileInputDataDTO> exportFileInputDataDTOList = new ArrayList<>();
        List<Map<String, Object>> exportMainDataList = new ArrayList<>();

        Map<String, Object> exportData;
        int idx = 1;
        for (ViewMxjPosEntity viewMxjPosEntity : viewMxjPosEntityList) {
            exportData = new HashMap<>();
            exportData.put("index", idx + "");
            exportData.put("cg_group_code", viewMxjPosEntity.getCgGroupCode());
            exportData.put("part_code", viewMxjPosEntity.getPartCode());
            exportData.put("tag_number", viewMxjPosEntity.getTagNumber());
            exportData.put("short_desc", viewMxjPosEntity.getShortDesc());
            exportData.put("unit_code", viewMxjPosEntity.getUnitCode());
            exportData.put("quantity", viewMxjPosEntity.getQuantity());
            exportData.put("resv_qty", viewMxjPosEntity.getResvQty());
            exportData.put("issue_qty", viewMxjPosEntity.getIssueQty());
            exportData.put("ident", viewMxjPosEntity.getIdent());
            exportData.put("version", "00");
            exportData.put("pos", viewMxjPosEntity.getPos());
            exportData.put("bompath", viewMxjPosEntity.getBompath());
            exportData.put("lp_pos", viewMxjPosEntity.getLpPos());
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
            String.format("BOM综合材料表-%s", spmProjId)
        );
    }

    private LinkedHashMap<String, String> getIssueTitle(String titleType) {
        LinkedHashMap<String, String> title = new LinkedHashMap<>();

//	<mapping col = "0" type = "seq" > cg_group_code </mapping >
//	<mapping col = "1" > cg_group_code </mapping >
//	<mapping col = "2" > part_code </mapping >
//	<mapping col = "3" > tag_number </mapping >
//	<mapping col = "4" > short_desc </mapping >
//	<mapping col = "5" > unit_code </mapping >
//	<mapping col = "6" type = "number" > quantity </mapping >
//	<mapping col = "7" type = "number" > resv_qty </mapping >
//	<mapping col = "8" type = "number" > issue_qty </mapping >
//	<mapping col = "9" > ident </mapping >
//	<mapping col = "10" type = "fixed" > 00 </mapping >
//	<mapping col = "11" > pos </mapping >
//	<mapping col = "12" > bompath </mapping >
//	<mapping col = "13" > lp_pos </mapping >

        switch (titleType) {
            case "MAIN":
                title.put("index", "节点序号");
                title.put("cg_group_code", "大类代码");
                title.put("part_code", "小类代码");
                title.put("tag_number", "材料编码");
                title.put("short_desc", "材料描述");
                title.put("unit_code", "计量单位");
                title.put("quantity", "数量");
                title.put("resv_qty", "预留数量");
                title.put("issue_qty", "已出库数量");
                title.put("ident", "ident代码");
                title.put("version", "版本");
                title.put("pos", "设计询价单号");
                title.put("bompath", "BOM Path");
                title.put("lp_pos", "BOM主键");

                break;
            default:
                break;
        }

        return title;
    }
}
