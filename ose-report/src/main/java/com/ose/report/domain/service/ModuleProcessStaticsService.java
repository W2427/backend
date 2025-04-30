package com.ose.report.domain.service;

import com.ose.dto.PageDTO;
import com.ose.report.domain.repository.moduleProcess.ModuleProcessStaticsRepository;
import com.ose.report.dto.ModuleRelationDTO;
import com.ose.report.dto.ModuleRelationSearchDTO;
import com.ose.report.dto.ModuleShipmentDetailDTO;
import com.ose.report.dto.ModuleShipmentDetailItemDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessModuleAssembledDetailItemDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessModuleAssembledStaticItemDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessStaticDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessStaticItemDTO;
import com.ose.report.entity.moduleProcess.ModuleProcessStatics;
import com.ose.util.DateUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模块进度完成统计数据服务接口。
 */
@Component
public class ModuleProcessStaticsService implements ModuleProcessStaticsInterface {

    private final ModuleProcessStaticsRepository moduleProcessStaticsRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ModuleProcessStaticsService(
        final ModuleProcessStaticsRepository moduleProcessStaticsRepository
    ) {
        this.moduleProcessStaticsRepository = moduleProcessStaticsRepository;
    }

    /**
     * 创建模块完成进度统计。
     *
     * @param orgId
     * @param projectId
     * @param moduleProcessStaticDTO
     */
    @Override
    public void create(
        final Long orgId,
        final Long projectId,
        final ModuleProcessStaticDTO moduleProcessStaticDTO
    ) {
        if (moduleProcessStaticDTO.getFuncPart().equals("PIPING")) {
            createPiping(orgId, projectId, moduleProcessStaticDTO);
        }
        if (moduleProcessStaticDTO.getFuncPart().equals("ELECTRICAL")) {
            createElectrical(orgId, projectId, moduleProcessStaticDTO);
        }
        if (moduleProcessStaticDTO.getFuncPart().equals("STRUCTURE")) {
            createStructure(orgId, projectId, moduleProcessStaticDTO);
        }
        if (moduleProcessStaticDTO.getFuncPart().equals("GENERAL")) {

        }

    }

    private void createStructure(
        final Long orgId,
        final Long projectId,
        final ModuleProcessStaticDTO moduleProcessStaticDTO
    ) {

        Date beforeDate = DateUtils.getBeforeDate();
        Date fixedTime = DateUtils.getFixedTime(beforeDate);
        for (ModuleProcessStaticItemDTO moduleProcessStaticItemDTO : moduleProcessStaticDTO.getModuleProcessStaticItemDTOs()) {

            ModuleProcessStatics moduleProcessStatics = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndFuncPartAndArchiveDateAndShipmentNoAndModuleNo(
                orgId,
                projectId,
                "STRUCTURE",
                fixedTime,
                moduleProcessStaticItemDTO.getShipmentNo(),
                moduleProcessStaticItemDTO.getModuleNo()
            );
            if (moduleProcessStatics == null) {
                moduleProcessStatics = new ModuleProcessStatics();
            }
            BeanUtils.copyProperties(
                moduleProcessStaticItemDTO,
                moduleProcessStatics
            );
            if(moduleProcessStatics.getCuttingTotalQty()==null){
                moduleProcessStatics.setCuttingTotalQty(0);
            }
            if(moduleProcessStatics.getCuttingFinishQty()==null){
                moduleProcessStatics.setCuttingFinishQty(0);
            }
            if(moduleProcessStatics.getFitUpFinishQty()==null){
                moduleProcessStatics.setFitUpFinishQty(0);
            }
            if(moduleProcessStatics.getOutFittingTotalQty()==null){
                moduleProcessStatics.setFitUpTotalQty(0);
            }

            if(moduleProcessStatics.getWeldFinishQty()==null){
                moduleProcessStatics.setWeldFinishQty(0);
            }
            if(moduleProcessStatics.getWeldTotalQty()==null){
                moduleProcessStatics.setWeldTotalQty(0);
            }

            if(moduleProcessStatics.getPaintedFinishQty()==null){
                moduleProcessStatics.setPaintedFinishQty(0);
            }

            if(moduleProcessStatics.getPaintedTotalQty()==null){
                moduleProcessStatics.setPaintedTotalQty(0);
            }

            if(moduleProcessStatics.getModuleAssembledFinishQty()==null){
                moduleProcessStatics.setModuleAssembledFinishQty(0.0);
            }
            if(moduleProcessStatics.getModuleAssembledTotalQty()==null){
                moduleProcessStatics.setModuleAssembledTotalQty(0.0);
            }
            if(moduleProcessStatics.getWeightTotal()==null){
                moduleProcessStatics.setWeightTotal(0.0);
            }
            if(moduleProcessStatics.getLengthTotal()==null){
                moduleProcessStatics.setLengthTotal(0.0);
            }
            if(moduleProcessStatics.getQtyTotal()==null){
                moduleProcessStatics.setQtyTotal(0);
            }

            if(moduleProcessStatics.getHandrailFinishQty()==null){
                moduleProcessStatics.setHandrailFinishQty(0.0);
            }
            if(moduleProcessStatics.getHandrailTotalQty()==null){
                moduleProcessStatics.setHandrailTotalQty(0.0);
            }

            if(moduleProcessStatics.getGratingFinishQty()==null){
                moduleProcessStatics.setGratingFinishQty(0.0);
            }
            if(moduleProcessStatics.getGratingTotalQty()==null){
                moduleProcessStatics.setGratingTotalQty(0.0);
            }
            if(moduleProcessStatics.getFloorPlateFinishQty()==null){
                moduleProcessStatics.setFloorPlateFinishQty(0.0);
            }
            if(moduleProcessStatics.getFloorPlateTotalQty()==null){
                moduleProcessStatics.setFloorPlateTotalQty(0.0);
            }
            if(moduleProcessStatics.getThreadFinishQty()==null){
                moduleProcessStatics.setThreadFinishQty(0.0);
            }
            if(moduleProcessStatics.getThreadTotalQty()==null){
                moduleProcessStatics.setThreadTotalQty(0.0);
            }
            if(moduleProcessStatics.getOutFittingTotalQty()==null){
                moduleProcessStatics.setOutFittingTotalQty(0.0);
            }
            if(moduleProcessStatics.getOutFittingFinishQty()==null){
                moduleProcessStatics.setOutFittingFinishQty(0.0);
            }
            if(moduleProcessStatics.getCableTotalQty()==null){
                moduleProcessStatics.setCableTotalQty(0.0);
            }
            if(moduleProcessStatics.getCableFinishQty()==null){
                moduleProcessStatics.setCableFinishQty(0.0);
            }

            if(moduleProcessStatics.getEquipmentTotalQty()==null){
                moduleProcessStatics.setEquipmentTotalQty(0.0);
            }
            if(moduleProcessStatics.getEquipmentFinishQty()==null){
                moduleProcessStatics.setEquipmentFinishQty(0.0);
            }

            if(moduleProcessStatics.getPipeLengthFinish()==null){
                moduleProcessStatics.setPipeLengthFinish(0.0);
            }
            if(moduleProcessStatics.getPipeLengthTotal()==null){
                moduleProcessStatics.setPipeLengthTotal(0.0);
            }

            if(moduleProcessStatics.getComponentQtyFinish()==null){
                moduleProcessStatics.setComponentQtyFinish(0.0);
            }

            if(moduleProcessStatics.getComponentQtyTotal()==null){
                moduleProcessStatics.setComponentQtyTotal(0.0);
            }

            if(moduleProcessStatics.getCableLadderQtyFinish()==null){
                moduleProcessStatics.setCableLadderQtyFinish(0.0);
            }
            if(moduleProcessStatics.getCableLadderQtyTotal()==null){
                moduleProcessStatics.setCableTotalQty(0.0);
            }
            if(moduleProcessStatics.getCableLadderOtherQtyFinish()==null){
                moduleProcessStatics.setCableLadderOtherQtyFinish(0.0);
            }
            if(moduleProcessStatics.getCableLadderOtherQtyTotal()==null){
                moduleProcessStatics.setCableLadderOtherQtyTotal(0.0);
            }
            if(moduleProcessStatics.getConduitQtyFinish()==null){
                moduleProcessStatics.setConduitQtyFinish(0.0);
            }
            if(moduleProcessStatics.getConduitQtyTotal()==null){
                moduleProcessStatics.setConduitQtyTotal(0.0);
            }
            if(moduleProcessStatics.getUnistrutQtyFinish()==null){
                moduleProcessStatics.setUnistrutQtyFinish(0.0);
            }
            if(moduleProcessStatics.getUnistrutQtyTotal()==null){
                moduleProcessStatics.setUnistrutQtyTotal(0.0);
            }

            moduleProcessStatics.setArchiveYear(DateUtils.getYear(beforeDate));
            moduleProcessStatics.setArchiveMonth(DateUtils.monthOfYear(beforeDate));
            moduleProcessStatics.setArchiveDay(DateUtils.dayOfYear(beforeDate));
            moduleProcessStatics.setArchiveWeek(DateUtils.weekOfYear(beforeDate));
            moduleProcessStatics.setOrgId(orgId);
            moduleProcessStatics.setFuncPart(moduleProcessStaticItemDTO.getFuncPart());
            moduleProcessStatics.setProjectId(projectId);
            moduleProcessStatics.setCreatedAt(new Date());
            moduleProcessStatics.setLastModifiedAt(new Date());
            moduleProcessStatics.setArchiveDate(fixedTime);
            moduleProcessStatics.setFuncPart("STRUCTURE");
            moduleProcessStatics.setStatus(EntityStatus.ACTIVE);
            moduleProcessStaticsRepository.save(moduleProcessStatics);
        }
    }

    private void createPiping(
        final Long orgId,
        final Long projectId,
        final ModuleProcessStaticDTO moduleProcessStaticDTO
    ) {

        Date beforeDate = DateUtils.getBeforeDate();
        Date fixedTime = DateUtils.getFixedTime(beforeDate);
        for (ModuleProcessStaticItemDTO moduleProcessStaticItemDTO : moduleProcessStaticDTO.getModuleProcessStaticItemDTOs()) {

            ModuleProcessStatics moduleProcessStatics = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndFuncPartAndArchiveDateAndShipmentNoAndModuleNo(
                orgId,
                projectId,
                "PIPING",
                fixedTime,
                moduleProcessStaticItemDTO.getShipmentNo(),
                moduleProcessStaticItemDTO.getModuleNo()
            );
            if (moduleProcessStatics == null) {
                moduleProcessStatics = new ModuleProcessStatics();
            }
            BeanUtils.copyProperties(
                moduleProcessStaticItemDTO,
                moduleProcessStatics
            );
            if(moduleProcessStatics.getCuttingTotalQty()==null){
                moduleProcessStatics.setCuttingTotalQty(0);
            }
            if(moduleProcessStatics.getCuttingFinishQty()==null){
                moduleProcessStatics.setCuttingFinishQty(0);
            }
            if(moduleProcessStatics.getFitUpFinishQty()==null){
                moduleProcessStatics.setFitUpFinishQty(0);
            }
            if(moduleProcessStatics.getOutFittingTotalQty()==null){
                moduleProcessStatics.setFitUpTotalQty(0);
            }

            if(moduleProcessStatics.getWeldFinishQty()==null){
                moduleProcessStatics.setWeldFinishQty(0);
            }
            if(moduleProcessStatics.getWeldTotalQty()==null){
                moduleProcessStatics.setWeldTotalQty(0);
            }

            if(moduleProcessStatics.getPaintedFinishQty()==null){
                moduleProcessStatics.setPaintedFinishQty(0);
            }

            if(moduleProcessStatics.getPaintedTotalQty()==null){
                moduleProcessStatics.setPaintedTotalQty(0);
            }

            if(moduleProcessStatics.getModuleAssembledFinishQty()==null){
                moduleProcessStatics.setModuleAssembledFinishQty(0.0);
            }
            if(moduleProcessStatics.getModuleAssembledTotalQty()==null){
                moduleProcessStatics.setModuleAssembledTotalQty(0.0);
            }
            if(moduleProcessStatics.getWeightTotal()==null){
                moduleProcessStatics.setWeightTotal(0.0);
            }
            if(moduleProcessStatics.getLengthTotal()==null){
                moduleProcessStatics.setLengthTotal(0.0);
            }
            if(moduleProcessStatics.getQtyTotal()==null){
                moduleProcessStatics.setQtyTotal(0);
            }

            if(moduleProcessStatics.getHandrailFinishQty()==null){
                moduleProcessStatics.setHandrailFinishQty(0.0);
            }
            if(moduleProcessStatics.getHandrailTotalQty()==null){
                moduleProcessStatics.setHandrailTotalQty(0.0);
            }

            if(moduleProcessStatics.getGratingFinishQty()==null){
                moduleProcessStatics.setGratingFinishQty(0.0);
            }
            if(moduleProcessStatics.getGratingTotalQty()==null){
                moduleProcessStatics.setGratingTotalQty(0.0);
            }
            if(moduleProcessStatics.getFloorPlateFinishQty()==null){
                moduleProcessStatics.setFloorPlateFinishQty(0.0);
            }
            if(moduleProcessStatics.getFloorPlateTotalQty()==null){
                moduleProcessStatics.setFloorPlateTotalQty(0.0);
            }
            if(moduleProcessStatics.getThreadFinishQty()==null){
                moduleProcessStatics.setThreadFinishQty(0.0);
            }
            if(moduleProcessStatics.getThreadTotalQty()==null){
                moduleProcessStatics.setThreadTotalQty(0.0);
            }
            if(moduleProcessStatics.getOutFittingTotalQty()==null){
                moduleProcessStatics.setOutFittingTotalQty(0.0);
            }
            if(moduleProcessStatics.getOutFittingFinishQty()==null){
                moduleProcessStatics.setOutFittingFinishQty(0.0);
            }
            if(moduleProcessStatics.getCableTotalQty()==null){
                moduleProcessStatics.setCableTotalQty(0.0);
            }
            if(moduleProcessStatics.getCableFinishQty()==null){
                moduleProcessStatics.setCableFinishQty(0.0);
            }

            if(moduleProcessStatics.getEquipmentTotalQty()==null){
                moduleProcessStatics.setEquipmentTotalQty(0.0);
            }
            if(moduleProcessStatics.getEquipmentFinishQty()==null){
                moduleProcessStatics.setEquipmentFinishQty(0.0);
            }

            if(moduleProcessStatics.getPipeLengthFinish()==null){
                moduleProcessStatics.setPipeLengthFinish(0.0);
            }
            if(moduleProcessStatics.getPipeLengthTotal()==null){
                moduleProcessStatics.setPipeLengthTotal(0.0);
            }

            if(moduleProcessStatics.getComponentQtyFinish()==null){
                moduleProcessStatics.setComponentQtyFinish(0.0);
            }

            if(moduleProcessStatics.getComponentQtyTotal()==null){
                moduleProcessStatics.setComponentQtyTotal(0.0);
            }

            if(moduleProcessStatics.getCableLadderQtyFinish()==null){
                moduleProcessStatics.setCableLadderQtyFinish(0.0);
            }
            if(moduleProcessStatics.getCableLadderQtyTotal()==null){
                moduleProcessStatics.setCableTotalQty(0.0);
            }
            if(moduleProcessStatics.getCableLadderOtherQtyFinish()==null){
                moduleProcessStatics.setCableLadderOtherQtyFinish(0.0);
            }
            if(moduleProcessStatics.getCableLadderOtherQtyTotal()==null){
                moduleProcessStatics.setCableLadderOtherQtyTotal(0.0);
            }
            if(moduleProcessStatics.getConduitQtyFinish()==null){
                moduleProcessStatics.setConduitQtyFinish(0.0);
            }
            if(moduleProcessStatics.getConduitQtyTotal()==null){
                moduleProcessStatics.setConduitQtyTotal(0.0);
            }
            if(moduleProcessStatics.getUnistrutQtyFinish()==null){
                moduleProcessStatics.setUnistrutQtyFinish(0.0);
            }
            if(moduleProcessStatics.getUnistrutQtyTotal()==null){
                moduleProcessStatics.setUnistrutQtyTotal(0.0);
            }

            moduleProcessStatics.setArchiveYear(DateUtils.getYear(beforeDate));
            moduleProcessStatics.setArchiveMonth(DateUtils.monthOfYear(beforeDate));
            moduleProcessStatics.setArchiveDay(DateUtils.dayOfYear(beforeDate));
            moduleProcessStatics.setArchiveWeek(DateUtils.weekOfYear(beforeDate));
            moduleProcessStatics.setOrgId(orgId);
            moduleProcessStatics.setFuncPart(moduleProcessStaticItemDTO.getFuncPart());
            moduleProcessStatics.setProjectId(projectId);
            moduleProcessStatics.setCreatedAt(new Date());
            moduleProcessStatics.setLastModifiedAt(new Date());
            moduleProcessStatics.setArchiveDate(fixedTime);
            moduleProcessStatics.setFuncPart("PIPING");
            moduleProcessStatics.setStatus(EntityStatus.ACTIVE);
            moduleProcessStaticsRepository.save(moduleProcessStatics);
        }
    }

    private void createElectrical(
        final Long orgId,
        final Long projectId,
        final ModuleProcessStaticDTO moduleProcessStaticDTO
    ) {

        Date beforeDate = DateUtils.getBeforeDate();
        Date fixedTime = DateUtils.getFixedTime(beforeDate);
        for (ModuleProcessStaticItemDTO moduleProcessStaticItemDTO : moduleProcessStaticDTO.getModuleProcessStaticItemDTOs()) {

            ModuleProcessStatics moduleProcessStatics = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndFuncPartAndArchiveDateAndShipmentNoAndModuleNo(
                orgId,
                projectId,
                "ELECTRICAL",
                fixedTime,
                moduleProcessStaticItemDTO.getShipmentNo(),
                moduleProcessStaticItemDTO.getModuleNo()
            );
            if (moduleProcessStatics == null) {
                moduleProcessStatics = new ModuleProcessStatics();
            }
            BeanUtils.copyProperties(
                moduleProcessStaticItemDTO,
                moduleProcessStatics
            );
            if(moduleProcessStatics.getCuttingTotalQty()==null){
                moduleProcessStatics.setCuttingTotalQty(0);
            }
            if(moduleProcessStatics.getCuttingFinishQty()==null){
                moduleProcessStatics.setCuttingFinishQty(0);
            }
            if(moduleProcessStatics.getFitUpFinishQty()==null){
                moduleProcessStatics.setFitUpFinishQty(0);
            }
            if(moduleProcessStatics.getOutFittingTotalQty()==null){
                moduleProcessStatics.setFitUpTotalQty(0);
            }

            if(moduleProcessStatics.getWeldFinishQty()==null){
                moduleProcessStatics.setWeldFinishQty(0);
            }
            if(moduleProcessStatics.getWeldTotalQty()==null){
                moduleProcessStatics.setWeldTotalQty(0);
            }

            if(moduleProcessStatics.getPaintedFinishQty()==null){
                moduleProcessStatics.setPaintedFinishQty(0);
            }

            if(moduleProcessStatics.getPaintedTotalQty()==null){
                moduleProcessStatics.setPaintedTotalQty(0);
            }

            if(moduleProcessStatics.getModuleAssembledFinishQty()==null){
                moduleProcessStatics.setModuleAssembledFinishQty(0.0);
            }
            if(moduleProcessStatics.getModuleAssembledTotalQty()==null){
                moduleProcessStatics.setModuleAssembledTotalQty(0.0);
            }
            if(moduleProcessStatics.getWeightTotal()==null){
                moduleProcessStatics.setWeightTotal(0.0);
            }
            if(moduleProcessStatics.getLengthTotal()==null){
                moduleProcessStatics.setLengthTotal(0.0);
            }
            if(moduleProcessStatics.getQtyTotal()==null){
                moduleProcessStatics.setQtyTotal(0);
            }

            if(moduleProcessStatics.getHandrailFinishQty()==null){
                moduleProcessStatics.setHandrailFinishQty(0.0);
            }
            if(moduleProcessStatics.getHandrailTotalQty()==null){
                moduleProcessStatics.setHandrailTotalQty(0.0);
            }

            if(moduleProcessStatics.getGratingFinishQty()==null){
                moduleProcessStatics.setGratingFinishQty(0.0);
            }
            if(moduleProcessStatics.getGratingTotalQty()==null){
                moduleProcessStatics.setGratingTotalQty(0.0);
            }
            if(moduleProcessStatics.getFloorPlateFinishQty()==null){
                moduleProcessStatics.setFloorPlateFinishQty(0.0);
            }
            if(moduleProcessStatics.getFloorPlateTotalQty()==null){
                moduleProcessStatics.setFloorPlateTotalQty(0.0);
            }
            if(moduleProcessStatics.getThreadFinishQty()==null){
                moduleProcessStatics.setThreadFinishQty(0.0);
            }
            if(moduleProcessStatics.getThreadTotalQty()==null){
                moduleProcessStatics.setThreadTotalQty(0.0);
            }
            if(moduleProcessStatics.getOutFittingTotalQty()==null){
                moduleProcessStatics.setOutFittingTotalQty(0.0);
            }
            if(moduleProcessStatics.getOutFittingFinishQty()==null){
                moduleProcessStatics.setOutFittingFinishQty(0.0);
            }
            if(moduleProcessStatics.getCableTotalQty()==null){
                moduleProcessStatics.setCableTotalQty(0.0);
            }
            if(moduleProcessStatics.getCableFinishQty()==null){
                moduleProcessStatics.setCableFinishQty(0.0);
            }

            if(moduleProcessStatics.getEquipmentTotalQty()==null){
                moduleProcessStatics.setEquipmentTotalQty(0.0);
            }
            if(moduleProcessStatics.getEquipmentFinishQty()==null){
                moduleProcessStatics.setEquipmentFinishQty(0.0);
            }

            if(moduleProcessStatics.getPipeLengthFinish()==null){
                moduleProcessStatics.setPipeLengthFinish(0.0);
            }
            if(moduleProcessStatics.getPipeLengthTotal()==null){
                moduleProcessStatics.setPipeLengthTotal(0.0);
            }

            if(moduleProcessStatics.getComponentQtyFinish()==null){
                moduleProcessStatics.setComponentQtyFinish(0.0);
            }

            if(moduleProcessStatics.getComponentQtyTotal()==null){
                moduleProcessStatics.setComponentQtyTotal(0.0);
            }

            if(moduleProcessStatics.getCableLadderQtyFinish()==null){
                moduleProcessStatics.setCableLadderQtyFinish(0.0);
            }
            if(moduleProcessStatics.getCableLadderQtyTotal()==null){
                moduleProcessStatics.setCableTotalQty(0.0);
            }

            if(moduleProcessStatics.getCableLadderOtherQtyFinish()==null){
                moduleProcessStatics.setCableLadderOtherQtyFinish(0.0);
            }
            if(moduleProcessStatics.getCableLadderOtherQtyTotal()==null){
                moduleProcessStatics.setCableLadderOtherQtyTotal(0.0);
            }

            if(moduleProcessStatics.getConduitQtyFinish()==null){
                moduleProcessStatics.setConduitQtyFinish(0.0);
            }
            if(moduleProcessStatics.getConduitQtyTotal()==null){
                moduleProcessStatics.setConduitQtyTotal(0.0);
            }
            if(moduleProcessStatics.getUnistrutQtyFinish()==null){
                moduleProcessStatics.setUnistrutQtyFinish(0.0);
            }
            if(moduleProcessStatics.getUnistrutQtyTotal()==null){
                moduleProcessStatics.setUnistrutQtyTotal(0.0);
            }

            moduleProcessStatics.setArchiveYear(DateUtils.getYear(beforeDate));
            moduleProcessStatics.setArchiveMonth(DateUtils.monthOfYear(beforeDate));
            moduleProcessStatics.setArchiveDay(DateUtils.dayOfYear(beforeDate));
            moduleProcessStatics.setArchiveWeek(DateUtils.weekOfYear(beforeDate));
            moduleProcessStatics.setOrgId(orgId);
            moduleProcessStatics.setFuncPart(moduleProcessStaticItemDTO.getFuncPart());
            moduleProcessStatics.setProjectId(projectId);
            moduleProcessStatics.setCreatedAt(new Date());
            moduleProcessStatics.setLastModifiedAt(new Date());
            moduleProcessStatics.setArchiveDate(fixedTime);
            moduleProcessStatics.setFuncPart("ELECTRICAL");
            moduleProcessStatics.setStatus(EntityStatus.ACTIVE);
            moduleProcessStaticsRepository.save(moduleProcessStatics);
        }
    }

    @Override
    public Page<ModuleProcessStatics> search(
        Long orgId,
        Long projectId,
        PageDTO page
    ) {
        return null;
    }

    @Override
    public ModuleRelationDTO searchModuleRelationNos(
        Long orgId,
        Long projectId,
        String funcPart
    ) {
        Date beforeDate = DateUtils.getBeforeDate();
        Date fixedTime = DateUtils.getFixedTime(beforeDate);

        ModuleRelationDTO moduleRelationDTO = new ModuleRelationDTO();
        List<String> moduleRelations = moduleProcessStaticsRepository.findModuleRelationsNoByOrgIdAndProjectId(
            orgId,
            projectId,
            fixedTime,
            funcPart
        );
        moduleRelationDTO.setModuleRelationNos(moduleRelations);
        return moduleRelationDTO;
    }

    @Override
    public ModuleRelationDTO searchModuleNos(
        Long orgId,
        Long projectId,
        String shipmentNo,
        String funcPart
    ) {
        Date beforeDate = DateUtils.getBeforeDate();
        Date fixedTime = DateUtils.getFixedTime(beforeDate);
        ModuleRelationDTO moduleRelationDTO = new ModuleRelationDTO();
        List<String> moduleNos = moduleProcessStaticsRepository.findModuleNoByOrgIdAndProjectId(
            orgId,
            projectId,
            shipmentNo,
            fixedTime,
            funcPart
        );
        moduleRelationDTO.setModuleNos(moduleNos);
        return moduleRelationDTO;
    }

    @Override
    public ModuleShipmentDetailDTO searchModuleDetail(
        Long orgId,
        Long projectId,
        String shipmentNo,
        ModuleRelationSearchDTO moduleRelationSearchDTO
    ) {
        Date beforeDate = DateUtils.getBeforeDate();
        Date fixedTime = DateUtils.getFixedTime(beforeDate);

        List<ModuleProcessStatics> moduleProcessStatics = new ArrayList<>();
        if (moduleRelationSearchDTO.getModuleNos() == null || moduleRelationSearchDTO.getModuleNos().size() == 0) {
            moduleProcessStatics = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndShipmentNo(
                orgId,
                projectId,
                shipmentNo,
                fixedTime,
                moduleRelationSearchDTO.getFuncPart()
            );
        } else {
            moduleProcessStatics = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndShipmentNoAndModules(
                orgId,
                projectId,
                shipmentNo,
                moduleRelationSearchDTO.getModuleNos(),
                fixedTime,
                moduleRelationSearchDTO.getFuncPart()
            );
        }

        ModuleShipmentDetailDTO moduleShipmentDetailDTO = new ModuleShipmentDetailDTO();
        Integer cuttingFinishQty = 0;
        Integer cuttingTotalQty = 0;
        Integer fitUpFinishQty = 0;
        Integer fitUpTotalQty = 0;
        Integer weldFinishQty = 0;
        Integer weldTotalQty = 0;
        Integer paintedFinishQty = 0;
        Integer paintedTotalQty = 0;
        Double moduleAssembledFinishQty = 0.0;
        Double moduleAssembledTotalQty = 0.0;
        Double weightTotal = 0.0;
        Double lengthTotal = 0.0;
        Integer qtyTotal = 0;

        Double outFittingFinish = 0.0;
        Double outFittingTotal = 0.0;
        Double cableFinish = 0.0;
        Double cableTotal = 0.0;
        Double equipmentFinish = 0.0;
        Double equipmentTotal = 0.0;

        List<ModuleShipmentDetailItemDTO> moduleShipmentDetailItemDTOs = new ArrayList<>();
        for (ModuleProcessStatics moduleProcessStatic : moduleProcessStatics) {
            ModuleShipmentDetailItemDTO moduleShipmentDetailItemDTO = new ModuleShipmentDetailItemDTO();
            moduleShipmentDetailItemDTO.setModuleNo(moduleProcessStatic.getModuleNo());

            if (moduleProcessStatic.getWeightTotal() != null) {
                moduleShipmentDetailItemDTO.setWeight(moduleProcessStatic.getWeightTotal());
            } else {
                moduleShipmentDetailItemDTO.setWeight(0.0);
            }
            BigDecimal weight1 = new java.math.BigDecimal(String.valueOf(weightTotal));
            BigDecimal weight2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getWeightTotal()));
            if (moduleProcessStatic.getWeightTotal() != null) {
                weightTotal = weight1.add(weight2).doubleValue();
            } else {
                weightTotal = weight1.doubleValue();
            }

            BigDecimal length1 = new java.math.BigDecimal(String.valueOf(lengthTotal));
            BigDecimal length2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getLengthTotal()));
            if (moduleProcessStatic.getLengthTotal() != null) {
                lengthTotal = length2.add(length1).doubleValue();
            } else {
                lengthTotal = length1.doubleValue();
            }


            Integer qty1 = qtyTotal;
            Integer qty2 = moduleProcessStatic.getQtyTotal();
            if (moduleProcessStatic.getQtyTotal() != null) {
                qtyTotal = qty2 + qty1;
            } else {
                qtyTotal = qty1;
            }

            if (moduleProcessStatic.getOutFittingFinishQty() != null) {
                BigDecimal outFittingFinish1 = new java.math.BigDecimal(String.valueOf(outFittingFinish));
                BigDecimal outFittingFinish2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getOutFittingFinishQty()));
                outFittingFinish = outFittingFinish1.add(outFittingFinish2).doubleValue();
            }

            if (moduleProcessStatic.getOutFittingTotalQty() != null) {
                BigDecimal outFittingTotal1 = new java.math.BigDecimal(String.valueOf(outFittingTotal));
                BigDecimal outFittingTotal2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getOutFittingTotalQty()));
                outFittingTotal = outFittingTotal1.add(outFittingTotal2).doubleValue();
            }
            if (moduleProcessStatic.getCableFinishQty() != null) {
                BigDecimal cableFinish1 = new java.math.BigDecimal(String.valueOf(cableFinish));
                BigDecimal cableFinish2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getCableFinishQty()));
                cableFinish = cableFinish2.add(cableFinish1).doubleValue();
            }

            if (moduleProcessStatic.getCableTotalQty() != null) {
                BigDecimal cableTotal1 = new java.math.BigDecimal(String.valueOf(cableTotal));
                BigDecimal cableTotal2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getCableTotalQty()));
                cableTotal = cableTotal1.add(cableTotal2).doubleValue();
            }

            if (moduleProcessStatic.getEquipmentFinishQty() != null) {
                BigDecimal equipmentFinish1 = new java.math.BigDecimal(String.valueOf(equipmentFinish));
                BigDecimal equipmentFinish2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getEquipmentFinishQty()));
                equipmentFinish = equipmentFinish1.add(equipmentFinish2).doubleValue();
            }
            if (moduleProcessStatic.getEquipmentTotalQty() != null) {
                BigDecimal equipmentTotal1 = new java.math.BigDecimal(String.valueOf(equipmentTotal));
                BigDecimal equipmentTotal2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getEquipmentTotalQty()));
                equipmentTotal = equipmentTotal1.add(equipmentTotal2).doubleValue();
            }

            cuttingFinishQty = cuttingFinishQty + moduleProcessStatic.getCuttingFinishQty();
            cuttingTotalQty = cuttingTotalQty + moduleProcessStatic.getCuttingTotalQty();

            fitUpFinishQty = fitUpFinishQty + moduleProcessStatic.getFitUpFinishQty();
            fitUpTotalQty = fitUpTotalQty + moduleProcessStatic.getFitUpTotalQty();

            weldFinishQty = weldFinishQty + moduleProcessStatic.getWeldFinishQty();
            weldTotalQty = weldTotalQty + moduleProcessStatic.getWeldTotalQty();
            paintedFinishQty = paintedFinishQty + moduleProcessStatic.getPaintedFinishQty();
            paintedTotalQty = paintedTotalQty + moduleProcessStatic.getPaintedTotalQty();

            BigDecimal d1 = new java.math.BigDecimal(String.valueOf(moduleAssembledFinishQty));
            BigDecimal d2 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getModuleAssembledFinishQty()));
            moduleAssembledFinishQty = d1.add(d2).doubleValue();

            BigDecimal d3 = new java.math.BigDecimal(String.valueOf(moduleAssembledTotalQty));
            BigDecimal d4 = new java.math.BigDecimal(String.valueOf(moduleProcessStatic.getModuleAssembledTotalQty()));
            moduleAssembledTotalQty = d3.add(d4).doubleValue();

            if (moduleProcessStatic.getCuttingTotalQty() == 0) {
                moduleShipmentDetailItemDTO.setCuttingPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format((float) moduleProcessStatic.getCuttingFinishQty() / (float) moduleProcessStatic.getCuttingTotalQty() * 100);
                moduleShipmentDetailItemDTO.setCuttingPercentage(result);
            }

            if (moduleProcessStatic.getFitUpTotalQty() == 0) {
                moduleShipmentDetailItemDTO.setFitupPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format((float) moduleProcessStatic.getFitUpFinishQty() / (float) moduleProcessStatic.getFitUpTotalQty() * 100);
                moduleShipmentDetailItemDTO.setFitupPercentage(result);
            }

            if (moduleProcessStatic.getWeldTotalQty() == 0) {
                moduleShipmentDetailItemDTO.setWeldPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format((float) moduleProcessStatic.getWeldFinishQty() / (float) moduleProcessStatic.getWeldTotalQty() * 100);
                moduleShipmentDetailItemDTO.setWeldPercentage(result);
            }
            if (moduleProcessStatic.getPaintedTotalQty() == 0) {
                moduleShipmentDetailItemDTO.setPaintedPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format((float) moduleProcessStatic.getPaintedFinishQty() / (float) moduleProcessStatic.getPaintedTotalQty() * 100);
                moduleShipmentDetailItemDTO.setPaintedPercentage(result);
            }
            if (moduleProcessStatic.getModuleAssembledTotalQty() == 0.0) {
                moduleShipmentDetailItemDTO.setAssembledPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format(moduleProcessStatic.getModuleAssembledFinishQty() / moduleProcessStatic.getModuleAssembledTotalQty() * 100);
                moduleShipmentDetailItemDTO.setAssembledPercentage(result);
            }

            if (moduleProcessStatic.getCableTotalQty() == null || moduleProcessStatic.getCableTotalQty() == 0.0) {
                moduleShipmentDetailItemDTO.setCablePercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format(moduleProcessStatic.getCableFinishQty() / moduleProcessStatic.getCableTotalQty() * 100);
                moduleShipmentDetailItemDTO.setCablePercentage(result);
            }
            if (moduleProcessStatic.getOutFittingTotalQty() == null || moduleProcessStatic.getOutFittingTotalQty() == 0.0) {
                moduleShipmentDetailItemDTO.setOutFittingPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format(moduleProcessStatic.getOutFittingFinishQty() / moduleProcessStatic.getOutFittingTotalQty() * 100);
                moduleShipmentDetailItemDTO.setOutFittingPercentage(result);
            }
            if (moduleProcessStatic.getEquipmentTotalQty() == null || moduleProcessStatic.getEquipmentTotalQty() == 0.0) {
                moduleShipmentDetailItemDTO.setEquipmentPercentage("0");
            } else {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                String result = numberFormat.format(moduleProcessStatic.getEquipmentFinishQty() / moduleProcessStatic.getEquipmentTotalQty() * 100);
                moduleShipmentDetailItemDTO.setEquipmentPercentage(result);
            }

            moduleShipmentDetailItemDTOs.add(moduleShipmentDetailItemDTO);
        }

        if (cuttingTotalQty == 0) {
            moduleShipmentDetailDTO.setCuttingPercentage("0");
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format((float) cuttingFinishQty / (float) cuttingTotalQty * 100);
            moduleShipmentDetailDTO.setCuttingPercentage(result1);
        }
        if (fitUpTotalQty == 0) {
            moduleShipmentDetailDTO.setFitupPercentage("0");
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format((float) fitUpFinishQty / (float) fitUpTotalQty * 100);
            moduleShipmentDetailDTO.setFitupPercentage(result1);
        }
        if (weldTotalQty == 0) {
            moduleShipmentDetailDTO.setWeldPercentage("0");
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format((float) weldFinishQty / (float) weldTotalQty * 100);
            moduleShipmentDetailDTO.setWeldPercentage(result1);
        }
        if (paintedTotalQty == 0) {
            moduleShipmentDetailDTO.setPaintedPercentage("0");
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format((float) paintedFinishQty / (float) paintedTotalQty * 100);
            moduleShipmentDetailDTO.setPaintedPercentage(result1);
        }
        if (moduleAssembledTotalQty == 0.0) {
            moduleShipmentDetailDTO.setAssembledPercentage("0");
        } else {
            double d = new BigDecimal(moduleAssembledFinishQty / moduleAssembledTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format(d);
            moduleShipmentDetailDTO.setAssembledPercentage(result1);
        }

        if (outFittingTotal == 0.0) {
            moduleShipmentDetailDTO.setOutFittingPercentage("0");
            moduleShipmentDetailDTO.setOutFittingTotal(0.0);
        } else {
            double d = new BigDecimal(outFittingFinish / outFittingTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format(d);
            moduleShipmentDetailDTO.setOutFittingPercentage(result1);
            moduleShipmentDetailDTO.setOutFittingTotal(outFittingTotal);
        }

        if (cableTotal == 0.0) {
            moduleShipmentDetailDTO.setCablePercentage("0");
            moduleShipmentDetailDTO.setCableTotal(0.0);
            moduleShipmentDetailDTO.setCableFinish(0.0);
        } else {
            double d = new BigDecimal(cableFinish / cableTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format(d);
            moduleShipmentDetailDTO.setCablePercentage(result1);
            moduleShipmentDetailDTO.setCableTotal(cableTotal);
            moduleShipmentDetailDTO.setCableFinish(cableFinish);
        }

        if (equipmentTotal == 0.0) {
            moduleShipmentDetailDTO.setEquipmentPercentage("0");
            moduleShipmentDetailDTO.setEquipmentTotal(0.0);
            moduleShipmentDetailDTO.setEquipmentFinish(0.0);
        } else {
            double d = new BigDecimal(equipmentFinish / equipmentTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(1);
            String result1 = numberFormat.format(d);
            moduleShipmentDetailDTO.setEquipmentPercentage(result1);
            moduleShipmentDetailDTO.setEquipmentTotal(equipmentTotal);
            moduleShipmentDetailDTO.setEquipmentFinish(equipmentFinish);
        }

        List<ModuleProcessModuleAssembledStaticItemDTO> moduleProcessModuleAssembledStaticItemDTOS = new ArrayList<>();
        if (moduleRelationSearchDTO.getModuleNos() != null) {
            moduleProcessModuleAssembledStaticItemDTOS = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndShipmentNoModuleAssembled(
                orgId,
                projectId,
                shipmentNo,
                moduleRelationSearchDTO.getModuleNos(),
                fixedTime
            );
        } else {
            moduleProcessModuleAssembledStaticItemDTOS = moduleProcessStaticsRepository.findByOrgIdAndProjectIdAndShipmentNoModuleAssembledAll(
                orgId,
                projectId,
                shipmentNo,
                fixedTime
            );
        }

        if (moduleProcessModuleAssembledStaticItemDTOS.size() > 0) {

            List<ModuleProcessModuleAssembledDetailItemDTO> moduleProcessModuleAssembledStaticItemDTOs = new ArrayList<>();

            // 栏杆-Handrail
            Double handrailFinishQty = 0.0;
            Double handrailTotalQty = 0.0;
            String handrailUnit = "m";
            // 格栅-Grating
            Double gratingFinishQty = 0.0;
            Double gratingTotalQty = 0.0;
            String gratingUnit = "m2";
            // 花钢板-Floor Plate
            Double floorPlateFinishQty = 0.0;
            Double floorPlateTotalQty = 0.0;
            String floorPlateUnit = "m2";
            // 踏步-Tread
            Double threadFinishQty = 0.0;
            Double threadTotalQty = 0.0;
            String threadUnit = "个";

            // 管系管段完成长度
            Double pipeLengthFinish = 0.0;
            // 管系管段总长度长度
            Double pipeLengthTotal = 0.0;
            // 管系管段长度单位
            String pipeLengthUnit = "M";

            // 管系管附件完成数量
            Double componentQtyFinish = 0.0;
            // 管系管附件总数量
            Double componentQtyTotal = 0.0;
            // 管系管附件数量单位
            String componentQtyUnit = "EA";

            // 电气 电缆
            Double cableLadderQtyFinish = 0.0;

            Double cableLadderQtyTotal = 0.0;

            String cableLadderQtyUnit = "m";

            Double cableLadderOtherQtyFinish = 0.0;

            Double cableLadderOtherQtyTotal = 0.0;

            String cableLadderOtherQtyUnit = "ea";

            // 电气 管系
            Double conduitQtyFinish = 0.0;

            Double conduitQtyTotal = 0.0;

            String conduitQtyUnit = "m";

            // 电气 焊件
            Double unistrutQtyFinish = 0.0;

            Double unistrutQtyTotal = 0.0;

            String unistrutQtyUnit = "m";

            for (ModuleProcessModuleAssembledStaticItemDTO moduleProcessModuleAssembledStaticItemDTO : moduleProcessModuleAssembledStaticItemDTOS) {
                BigDecimal d1 = new java.math.BigDecimal(String.valueOf(handrailFinishQty));
                BigDecimal d2 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getHandrailFinishQty()));
                handrailFinishQty = d1.add(d2).doubleValue();

                BigDecimal d3 = new java.math.BigDecimal(String.valueOf(handrailTotalQty));
                BigDecimal d4 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getHandrailTotalQty()));
                handrailTotalQty = d3.add(d4).doubleValue();
//                handrailUnit = moduleProcessModuleAssembledStaticItemDTO.getHandrailUnit();


                BigDecimal d5 = new java.math.BigDecimal(String.valueOf(gratingFinishQty));
                BigDecimal d6 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getGratingFinishQty()));
                gratingFinishQty = d5.add(d6).doubleValue();

                BigDecimal d7 = new java.math.BigDecimal(String.valueOf(gratingTotalQty));
                BigDecimal d8 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getGratingTotalQty()));
                gratingTotalQty = d7.add(d8).doubleValue();
//                gratingUnit = moduleProcessModuleAssembledStaticItemDTO.getGratingUnit();

                BigDecimal d9 = new java.math.BigDecimal(String.valueOf(floorPlateFinishQty));
                BigDecimal d10 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getFloorPlateFinishQty()));
                floorPlateFinishQty = d9.add(d10).doubleValue();

                BigDecimal d11 = new java.math.BigDecimal(String.valueOf(floorPlateTotalQty));
                BigDecimal d12 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getFloorPlateTotalQty()));
                floorPlateTotalQty = d11.add(d12).doubleValue();
//                floorPlateUnit = moduleProcessModuleAssembledStaticItemDTO.getFloorPlateUnit();

                BigDecimal d13 = new java.math.BigDecimal(String.valueOf(threadFinishQty));
                BigDecimal d14 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getThreadFinishQty()));
                threadFinishQty = d13.add(d14).doubleValue();

                BigDecimal d15 = new java.math.BigDecimal(String.valueOf(threadTotalQty));
                BigDecimal d16 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getThreadTotalQty()));
                threadTotalQty = d15.add(d16).doubleValue();
//                threadUnit = moduleProcessModuleAssembledStaticItemDTO.getThreadUnit();

                BigDecimal d17 = new java.math.BigDecimal(String.valueOf(pipeLengthFinish));
                BigDecimal d18 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getPipeLengthFinish()));
                pipeLengthFinish = d17.add(d18).doubleValue();

                BigDecimal d19 = new java.math.BigDecimal(String.valueOf(pipeLengthTotal));
                BigDecimal d20 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getPipeLengthTotal()));
                pipeLengthTotal = d19.add(d20).doubleValue();

                BigDecimal d21 = new java.math.BigDecimal(String.valueOf(componentQtyFinish));
                BigDecimal d22 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getComponentQtyFinish()));
                componentQtyFinish = d21.add(d22).doubleValue();

                BigDecimal d23 = new java.math.BigDecimal(String.valueOf(componentQtyTotal));
                BigDecimal d24 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getComponentQtyTotal()));
                componentQtyTotal = d23.add(d24).doubleValue();



                BigDecimal d25 = new java.math.BigDecimal(String.valueOf(cableLadderQtyTotal));
                BigDecimal d26 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getCableLadderQtyTotal()));
                cableLadderQtyTotal = d25.add(d26).doubleValue();

                BigDecimal d27 = new java.math.BigDecimal(String.valueOf(cableLadderQtyFinish));
                BigDecimal d28 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getCableLadderQtyFinish()));
                cableLadderQtyFinish = d27.add(d28).doubleValue();

                BigDecimal d37 = new java.math.BigDecimal(String.valueOf(cableLadderOtherQtyFinish));
                BigDecimal d38 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getCableLadderOtherQtyFinish()));
                cableLadderOtherQtyFinish = d37.add(d38).doubleValue();

                BigDecimal d39 = new java.math.BigDecimal(String.valueOf(cableLadderOtherQtyTotal));
                BigDecimal d40 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getCableLadderOtherQtyTotal()));
                cableLadderOtherQtyTotal = d39.add(d40).doubleValue();


                BigDecimal d29 = new java.math.BigDecimal(String.valueOf(conduitQtyFinish));
                BigDecimal d30 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getConduitQtyFinish()));
                conduitQtyFinish = d29.add(d30).doubleValue();

                BigDecimal d31 = new java.math.BigDecimal(String.valueOf(conduitQtyTotal));
                BigDecimal d32 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getConduitQtyTotal()));
                conduitQtyTotal = d31.add(d32).doubleValue();

                BigDecimal d33 = new java.math.BigDecimal(String.valueOf(unistrutQtyTotal));
                BigDecimal d34 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getUnistrutQtyTotal()));
                unistrutQtyTotal = d33.add(d34).doubleValue();

                BigDecimal d35 = new java.math.BigDecimal(String.valueOf(unistrutQtyFinish));
                BigDecimal d36 = new java.math.BigDecimal(String.valueOf(moduleProcessModuleAssembledStaticItemDTO.getUnistrutQtyFinish()));
                unistrutQtyFinish = d35.add(d36).doubleValue();

            }
            if(moduleRelationSearchDTO.getFuncPart()!=null && moduleRelationSearchDTO.getFuncPart().equals("STRUCTURE")){
                if (handrailTotalQty.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("栏杆-Handrail");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(handrailFinishQty / handrailTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("栏杆-Handrail");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(handrailUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(handrailTotalQty);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (gratingTotalQty.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("格栅-Grating");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(gratingTotalQty / gratingTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("格栅-Grating");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(gratingUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(gratingTotalQty);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (floorPlateTotalQty.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("花钢板-Floor Plate");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {

                    double d = new BigDecimal(floorPlateFinishQty / floorPlateTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("花钢板-Floor Plate");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(floorPlateUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(floorPlateTotalQty);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (threadTotalQty.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("踏步-Tread");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(threadFinishQty / threadTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("踏步-Tread");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(threadUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(threadTotalQty);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }
            }

            if(moduleRelationSearchDTO.getFuncPart()!=null && moduleRelationSearchDTO.getFuncPart().equals("PIPING")){
                if (pipeLengthTotal.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("Pipe");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(threadFinishQty / threadTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("Pipe");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(pipeLengthUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(pipeLengthTotal);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (componentQtyTotal.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("Component");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(threadFinishQty / threadTotalQty * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("Component");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(componentQtyUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(componentQtyTotal);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }
            }

            if(moduleRelationSearchDTO.getFuncPart()!=null && moduleRelationSearchDTO.getFuncPart().equals("ELECTRICAL")){
                if (cableLadderQtyTotal.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("CABLE_LADDER_STRAIGHT");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(cableLadderQtyFinish / cableLadderQtyTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("CABLE_LADDER_STRAIGHT");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(cableLadderQtyUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(cableLadderQtyTotal);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (cableLadderOtherQtyTotal.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("CABLE_LADDER_OTHER");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(cableLadderOtherQtyFinish / cableLadderOtherQtyTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("CABLE_LADDER_OTHER");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(cableLadderOtherQtyUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(cableLadderOtherQtyTotal);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (conduitQtyTotal.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("CONDUIT GALVANISED");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(conduitQtyFinish / conduitQtyTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("CONDUIT GALVANISED");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(conduitQtyUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(conduitQtyTotal);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }

                if (unistrutQtyTotal.equals(0.0)) {
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("Unistrut");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit("");
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(0.0);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage("0");
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                } else {
                    double d = new BigDecimal(unistrutQtyFinish / unistrutQtyTotal * 100).setScale(2, BigDecimal.ROUND_UP).doubleValue();

                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(1);
                    String result1 = numberFormat.format(d);
                    ModuleProcessModuleAssembledDetailItemDTO moduleProcessModuleAssembledDetailItemDTO = new ModuleProcessModuleAssembledDetailItemDTO();
                    moduleProcessModuleAssembledDetailItemDTO.setMaterialType("Unistrut");
                    moduleProcessModuleAssembledDetailItemDTO.setUnit(unistrutQtyUnit);
                    moduleProcessModuleAssembledDetailItemDTO.setIffWeight(unistrutQtyTotal);
                    moduleProcessModuleAssembledDetailItemDTO.setPercentage(result1);
                    moduleProcessModuleAssembledStaticItemDTOs.add(moduleProcessModuleAssembledDetailItemDTO);
                }
            }


            moduleShipmentDetailDTO.setModuleProcessModuleAssembledDetailItemDTOS(
                moduleProcessModuleAssembledStaticItemDTOs
            );
        }

        moduleShipmentDetailDTO.setWeightTotal(weightTotal);
        moduleShipmentDetailDTO.setLengthTotal(lengthTotal);
        moduleShipmentDetailDTO.setQtyTotal(qtyTotal);
        moduleShipmentDetailDTO.setModuleShipmentDetailItemDTO(moduleShipmentDetailItemDTOs);
        return moduleShipmentDetailDTO;

    }
}
