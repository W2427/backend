package com.ose.tasks.domain.model.service.material;

import com.ose.dto.ContextDTO;
import com.ose.feign.RequestWrapper;
import com.ose.tasks.domain.model.repository.material.StructureEntityQrCodeRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.material.StructureEntityQrCodeCreateDTO;
import com.ose.tasks.dto.material.StructureEntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * 结构实体二维码查询接口。
 */
@Component
public class StructureEntityQrCodeService implements StructureEntityQrCodeInterface {

    private final StructureEntityQrCodeRepository structureEntityQrCodeRepository;
    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    /**
     * 构造方法
     */
    @Autowired
    public StructureEntityQrCodeService(
        StructureEntityQrCodeRepository structureEntityQrCodeRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService
    ) {
        this.structureEntityQrCodeRepository = structureEntityQrCodeRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    /**
     * 查询结构零件二维码列表。
     *
     * @param orgId
     * @param projectId
     * @param criteriaDTO
     * @return
     */
    @Override
    public Page<StructureEntityQrCode> search(
        Long orgId,
        Long projectId,
        StructureEntityQrCodeCriteriaDTO criteriaDTO) {
        return structureEntityQrCodeRepository.search(orgId, projectId, criteriaDTO);
    }

    /**
     * 创建结构零件二维码。
     *
     * @param orgId
     * @param projectId
     * @param structureEntityQrCodeCreateDTO
     * @param context
     * @return
     */
    @Override
    public StructureEntityQrCode create(
        Long orgId,
        Long projectId,
        StructureEntityQrCodeCreateDTO structureEntityQrCodeCreateDTO,
        ContextDTO context) {
        StructureEntityQrCode structureEntityQrCode = new StructureEntityQrCode();
        BeanUtils.copyProperties(structureEntityQrCodeCreateDTO, structureEntityQrCode);
        structureEntityQrCode.setOrgId(orgId);
        structureEntityQrCode.setProjectId(projectId);
        structureEntityQrCode.setCreatedAt(new Date());
        structureEntityQrCode.setCreatedBy(context.getOperator().getId());
        structureEntityQrCode.setLastModifiedAt(new Date());
        structureEntityQrCode.setLastModifiedBy(context.getOperator().getId());
        structureEntityQrCode.setStatus(EntityStatus.ACTIVE);
        structureEntityQrCodeRepository.save(structureEntityQrCode);
        return structureEntityQrCode;
    }

    /**
     * 更新结构两件二维码旧数据。
     *
     * @param orgId
     * @param projectId
     * @param context
     */
    @Override
    public void generate(
        Long orgId,
        Long projectId,
        ContextDTO context) {

        String authorization = context.getAuthorization();
        if(!context.isContextSet()) {
            String userAgent = context.getUserAgent();
            RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                null
            );

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }

        Project project = projectService.get(orgId, projectId);

        batchTaskService.run(
            context,
            project,
            BatchTaskCode.STRUCT_ENTITY,
            batchTask -> {
                System.out.println("开始生成旧的结构实体数据");

                structureEntityQrCodeRepository.deleteByOrgIdAndProjectId(
                    orgId,
                    projectId
                );

//                List<BpmStructureCuttingTaskProgramEntity> bpmStructureCuttingTaskProgramEntityList = bpmStructureCuttingTaskProgramEntityRepository.findByOrgIdAndProjectIdAndStatus(
//                    orgId,
//                    projectId,
//                    EntityStatus.ACTIVE
//                );

                System.out.println("找到结构实体旧数据");
//                if (bpmStructureCuttingTaskProgramEntityList.size() > 0) {
//
//                    Map<Long, Object> resultMap = new HashMap<>();
//
//                    for (BpmStructureCuttingTaskProgramEntity bpmStructureCuttingTaskProgramEntity : bpmStructureCuttingTaskProgramEntityList) {
//                        if (resultMap.get(bpmStructureCuttingTaskProgramEntity.getEntityId()) != null) {
//                            BpmStructureCuttingTaskProgramEntity bpmStructureCuttingTaskProgramFindEntity = (BpmStructureCuttingTaskProgramEntity) resultMap.get(bpmStructureCuttingTaskProgramEntity.getEntityId());
//
//
//                            if (bpmStructureCuttingTaskProgramFindEntity.getId() > bpmStructureCuttingTaskProgramEntity.getId()) {
//                                resultMap.put(bpmStructureCuttingTaskProgramEntity.getEntityId(), bpmStructureCuttingTaskProgramFindEntity);
//                            } else {
//                                resultMap.put(bpmStructureCuttingTaskProgramEntity.getEntityId(), bpmStructureCuttingTaskProgramEntity);
//                            }
//
//                        } else {
//                            resultMap.put(bpmStructureCuttingTaskProgramEntity.getEntityId(), bpmStructureCuttingTaskProgramEntity);
//                        }
//                    }
//
//                    System.out.println("结构实体旧数据去重");
//                    Iterator<Map.Entry<Long, Object>> iterator = resultMap.entrySet().iterator();
//                    while (iterator.hasNext()) {
//                        Map.Entry<Long, Object> entry = iterator.next();
//                        BpmStructureCuttingTaskProgramEntity bpmStructureCuttingTaskProgramEntity = (BpmStructureCuttingTaskProgramEntity) entry.getValue();
//                        if (bpmStructureCuttingTaskProgramEntity != null) {
//
//                            BpmStructureCuttingTask cutting = bpmStructureCuttingTasksRepository.findByOrgIdAndProjectIdAndIdAndStatus(
//                                orgId,
//                                projectId,
//                                bpmStructureCuttingTaskProgramEntity.getCuttingId(),
//                                EntityStatus.ACTIVE);
//                            System.out.println("结构下料");
//                            if (cutting == null) {
//                                continue;
//                            }
//
//
//                            Wp05Entity wp05 = wp05EntityRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(bpmStructureCuttingTaskProgramEntity.getEntityId(), orgId, projectId).orElse(null);
//                            if (wp05 == null) {
//                                continue;
//                            }
//                            System.out.println("查询wp05");
//
//
//                            StructureEntityQrCode newStructureEntityQrCode = new StructureEntityQrCode();
//
//                            newStructureEntityQrCode.setOrgId(orgId);
//                            newStructureEntityQrCode.setProjectId(projectId);
//                            newStructureEntityQrCode.setEntityId(wp05.getId());
//                            newStructureEntityQrCode.setEntityNo(wp05.getNo());
//                            newStructureEntityQrCode.setEntityType(wp05.getWbsEntityType());
//                            newStructureEntityQrCode.setRevision(wp05.getRevision());
//                            newStructureEntityQrCode.setWorkClass(wp05.getWorkClass());
//                            newStructureEntityQrCode.setCostCode(wp05.getCostCode());
//                            newStructureEntityQrCode.setMemberSize(wp05.getMemberSize());
//                            newStructureEntityQrCode.setMaterial(wp05.getMaterial());
//                            newStructureEntityQrCode.setLengthText(wp05.getLengthText());
//                            newStructureEntityQrCode.setLengthUnit(wp05.getLengthUnit());
//                            newStructureEntityQrCode.setLength(wp05.getLength());
//                            newStructureEntityQrCode.setWidthText(wp05.getWidthText());
//                            newStructureEntityQrCode.setWidthUnit(wp05.getWidthUnit());
//                            newStructureEntityQrCode.setWidth(wp05.getWidth());
//                            newStructureEntityQrCode.setHeightText(wp05.getHeightText());
//                            newStructureEntityQrCode.setHeightUnit(wp05.getHeightUnit());
//                            newStructureEntityQrCode.setHeight(wp05.getHeight());
//                            newStructureEntityQrCode.setWeightText(wp05.getWeightText());
//                            newStructureEntityQrCode.setWeightUnit(wp05.getWeightUnit());
//                            newStructureEntityQrCode.setWeight(wp05.getWeight());
//                            newStructureEntityQrCode.setPaintCode(wp05.getPaintCode());
//                            newStructureEntityQrCode.setBusinessType(wp05.getBusinessType());
//                            newStructureEntityQrCode.setBusinessTypeId(wp05.getBusinessTypeId());
//                            newStructureEntityQrCode.setNew(wp05.isNew());
//                            newStructureEntityQrCode.setQrCode(wp05.getQrCode());
//                            newStructureEntityQrCode.setNestedFlag(wp05.getNestedFlag());
//
//                            newStructureEntityQrCode.setTagNumberId(bpmStructureCuttingTaskProgramEntity.getTagNumberId());
//                            newStructureEntityQrCode.setTagNumber(bpmStructureCuttingTaskProgramEntity.getTagNumber());
//                            newStructureEntityQrCode.setHeatNo(bpmStructureCuttingTaskProgramEntity.getHeatNo());
//                            newStructureEntityQrCode.setHeatNoId(bpmStructureCuttingTaskProgramEntity.getHeatNoId());
//                            newStructureEntityQrCode.setMaterialQrCode(bpmStructureCuttingTaskProgramEntity.getMaterialQrCode());
//                            newStructureEntityQrCode.setIdent(bpmStructureCuttingTaskProgramEntity.getIdent());
//                            newStructureEntityQrCode.setShortDesc(bpmStructureCuttingTaskProgramEntity.getShortDesc());
//
//                            newStructureEntityQrCode.setProgramId(bpmStructureCuttingTaskProgramEntity.getProgramId());
//                            newStructureEntityQrCode.setProgramNo(bpmStructureCuttingTaskProgramEntity.getProgramNo());
//                            newStructureEntityQrCode.setCuttingId(cutting.getId());
//                            newStructureEntityQrCode.setCuttingNo(cutting.getNo());
//                            newStructureEntityQrCode.setPrintFlg(false);
//                            newStructureEntityQrCode.setManuallyCreated(true);
//
//                            newStructureEntityQrCode.setCreatedAt(new Date());
//                            newStructureEntityQrCode.setCreatedBy(context.getOperator().getId());
//                            newStructureEntityQrCode.setLastModifiedAt(new Date());
//                            newStructureEntityQrCode.setLastModifiedBy(context.getOperator().getId());
//                            newStructureEntityQrCode.setStatus(EntityStatus.ACTIVE);
//                            structureEntityQrCodeRepository.save(newStructureEntityQrCode);
//                            System.out.println("创建数据");
//                        }
//                    }
//                }
                return new BatchResultDTO();
            });
    }

}
