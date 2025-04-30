package com.ose.tasks.domain.model.service.plan;

import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessStageRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryGroupPlainRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryPlainRepository;
import com.ose.tasks.dto.MaterialISOMatchSummaryDTO;
import com.ose.tasks.dto.MaterialMatchSummaryDTO;
import com.ose.tasks.dto.WBSEntryDwgSummaryDTO;
import com.ose.tasks.dto.WBSEntryGroupPlainDTO;
import com.ose.tasks.dto.bpm.HierarchyBaseDTO;
import com.ose.tasks.dto.bpm.TaskHierarchyDTO;
import com.ose.tasks.dto.wbs.WBSEntryDwgDTO;
import com.ose.tasks.dto.wbs.WBSEntryIsoDTO;
import com.ose.tasks.dto.wbs.WBSEntryMaterialDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainQueryDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 扁平计划管理服务。
 */
@Component
public class PlanPlainService implements PlanPlainInterface {

    // 扁平 WBS 条目数据仓库
    private final WBSEntryPlainRepository wbsEntryPlainRepository;

    // 扁平 聚合的WBS 条目 数据仓库
    private final WBSEntryGroupPlainRepository wbsEntryGroupPlainRepository;

    private final BpmProcessStageRepository bpmProcessStageRepository;

    private final BpmProcessRepository bpmProcessRepository;


    /**
     * 构造方法。
     *
     * @param wbsEntryPlainRepository
     * @param wbsEntryGroupPlainRepository //     * @param spmMatchLnNodeFeignAPI
     * @param bpmProcessStageRepository
     * @param bpmProcessRepository
     */
    @Autowired
    public PlanPlainService(
        WBSEntryPlainRepository wbsEntryPlainRepository,
        WBSEntryGroupPlainRepository wbsEntryGroupPlainRepository,
//            SpmMatchLnNodeFeignAPI spmMatchLnNodeFeignAPI,
        BpmProcessStageRepository bpmProcessStageRepository,
        BpmProcessRepository bpmProcessRepository) {
        this.wbsEntryPlainRepository = wbsEntryPlainRepository;
        this.wbsEntryGroupPlainRepository = wbsEntryGroupPlainRepository;
        this.bpmProcessStageRepository = bpmProcessStageRepository;
        this.bpmProcessRepository = bpmProcessRepository;
//        this.spmMatchLnNodeFeignAPI = spmMatchLnNodeFeignAPI;
    }

    /**
     * 返回扁平的 计划数据分页列表
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public Page<WBSEntryPlain> searchWbs(Long orgId,
                                         Long projectId,
                                         WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        return wbsEntryPlainRepository
            .search(orgId, projectId, wbsEntryPlainQueryDTO);
    }

    /**
     * 返回扁平的 分组聚合的计划数据
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public Page<WBSEntryGroupPlainDTO> searchGroupWbs(Long orgId,
                                                      Long projectId,
                                                      WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        return wbsEntryGroupPlainRepository
            .search(orgId, projectId, wbsEntryPlainQueryDTO);
    }

    /**
     * 配配材料的汇总情况
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public MaterialMatchSummaryDTO getMaterialInfo(Long orgId,
                                                   Long projectId,
                                                   WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        MaterialMatchSummaryDTO materialMatchSummaryDTO = new MaterialMatchSummaryDTO();
        //取得全部ISO的材料匹配率
        Double overallMatchPercent = 0.0;
        Page<WBSEntryMaterialDTO> wbsEntryMaterials = null;
        overallMatchPercent = wbsEntryPlainRepository.getOverallMatchPercent(orgId, projectId, wbsEntryPlainQueryDTO);

        wbsEntryMaterials = wbsEntryPlainRepository.getMatchedMaterials(orgId, projectId, wbsEntryPlainQueryDTO);

        /*
        Set<BigDecimal> spmLnIds = new HashSet<>();
        for(WBSEntryIsoDTO iso:isoSet){
            if(iso.getBomMatchPercent()==null) break;
            spmLnIds.add(iso.getSpmLnId());
        }
        //取得 spm 中的材料匹配情况
        if(!CollectionUtils.isEmpty(spmLnIds)){
            SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO = new SpmMatchLnCriteriaDTO();
            Project project = projectService.get(orgId, projectId);

            spmMatchLnCriteriaDTO.setLnIdsString(JSON.toJSONString(spmLnIds));
            spmMatchLnCriteriaDTO.setSpmProjId(project.getSpmId());
            matchedMaterials =
                    //spmMatchLnNodeFeignAPI.matchLns(projectId, orgId, spmMatchLnCriteriaDTO);
            PageRequest pageRequest = PageRequest.of(
                    wbsEntryPlainQueryDTO.toPageable().getPageNumber(),
                    wbsEntryPlainQueryDTO.toPageable().getPageSize()
            );
            Page<SpmMatchLns> spmMaterials = new PageImpl<>(matchedMaterials.getData(), pageRequest, 0);
            materialMatchSummaryDTO.setMatchList(spmMaterials);
        }
        */
        if (overallMatchPercent == null) {
            overallMatchPercent = (Double) 0.0;
        } else {
            materialMatchSummaryDTO.setSumMatchPercent(overallMatchPercent.toString());
        }
        materialMatchSummaryDTO.setMatchList(wbsEntryMaterials);
        return materialMatchSummaryDTO;
    }

    /**
     * ISO匹配的材料情况
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @Override
    public MaterialISOMatchSummaryDTO getIsoMaterialInfo(Long orgId,
                                                         Long projectId,
                                                         WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        MaterialISOMatchSummaryDTO materialISOMatchSummaryDTO = new MaterialISOMatchSummaryDTO();
        //取得全部ISO的材料匹配率
        Double overallMatchPercent = 0.0;
        overallMatchPercent = wbsEntryPlainRepository.getOverallMatchPercent(orgId, projectId, wbsEntryPlainQueryDTO);

        Page<WBSEntryIsoDTO> isoSet = wbsEntryPlainRepository.getMatchedIsos(orgId, projectId, wbsEntryPlainQueryDTO);

        materialISOMatchSummaryDTO.setIsoSet(isoSet);
        if (overallMatchPercent == null) {
            overallMatchPercent = (Double) 0.0;
        } else {
            materialISOMatchSummaryDTO.setSumMatchPercent(overallMatchPercent.toString());
        }
        return materialISOMatchSummaryDTO;
    }

    /**
     * 取得计划中的工序阶段和工序
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param stageId
     * @return
     */
    @Override
    public TaskHierarchyDTO getStageProcess(Long orgId, Long projectId, Long stageId) {
        TaskHierarchyDTO dto = new TaskHierarchyDTO();

        if (stageId == null) {
            //返回工序阶段
            List<String> stages = wbsEntryPlainRepository.getProcessStages(orgId, projectId);

            List<BpmProcessStage> stageList = bpmProcessStageRepository.findByOrgIdAndProjectIdAndNameEnInAndStatus(
                orgId,
                projectId,
                stages,
                EntityStatus.ACTIVE
            );
            List<HierarchyBaseDTO> stageDto = new ArrayList<>();
            for (BpmProcessStage bps : stageList) {
                HierarchyBaseDTO hbd = new HierarchyBaseDTO();
                hbd.setId(bps.getId());
                hbd.setNameCn(bps.getNameCn());
                hbd.setNameEn(bps.getNameEn());
                stageDto.add(hbd);
            }
            dto.setProcessStages(stageDto);
            return dto;

        } else {
            //返回工序
            List<BpmProcess> bps = bpmProcessRepository.findByOrgIdAndProjectIdAndStageIdAndStatus(orgId, projectId, stageId, EntityStatus.ACTIVE);
            List<HierarchyBaseDTO> bpDto = new ArrayList<>();
            for (BpmProcess bp : bps) {
                HierarchyBaseDTO hbd = new HierarchyBaseDTO();
                hbd.setNameEn(bp.getNameEn());
                hbd.setNameCn(bp.getNameCn());
                hbd.setId(bp.getId());
                bpDto.add(hbd);
            }
            dto.setProcesses(bpDto);
            return dto;
        }
    }

    @Override
    public WBSEntryDwgSummaryDTO getDwgInfo(Long orgId, Long projectId, WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO) {
        WBSEntryDwgSummaryDTO wbsEntryDwgSummaryDTO = new WBSEntryDwgSummaryDTO();
        //取得全部子图纸的发图率
        Double overallIssuePercent = 0.0;
        Page<WBSEntryDwgDTO> wbsEntryDwgDTOs = null;
        overallIssuePercent = wbsEntryPlainRepository.getOverallIssuePercent(orgId, projectId, wbsEntryPlainQueryDTO);

        wbsEntryDwgDTOs = wbsEntryPlainRepository.getSubDwgIssedList(orgId, projectId, wbsEntryPlainQueryDTO);

        if (overallIssuePercent == null) {
            overallIssuePercent = (Double) 0.0;
        }

        wbsEntryDwgSummaryDTO.setSumIssuePercent(overallIssuePercent.toString());

        wbsEntryDwgSummaryDTO.setSubDwgList(wbsEntryDwgDTOs);
        return wbsEntryDwgSummaryDTO;
    }
}
