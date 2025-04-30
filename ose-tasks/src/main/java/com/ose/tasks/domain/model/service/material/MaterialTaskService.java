package com.ose.tasks.domain.model.service.material;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.api.MmPurchasePackageFeignAPI;
import com.ose.material.api.MmReleaseReceiveFeignAPI;
import com.ose.material.dto.MmPurchasePackageCreateDTO;
import com.ose.material.entity.MmPurchasePackageEntity;
import com.ose.material.entity.MmReleaseReceiveEntity;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.EntitySubTypeProcessRelationRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.material.MaterialTaskDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityTypeProcessRelation;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MaterialTaskService implements MaterialTaskInterface {

    private static final String PURCHASE_PACKAGE = "PURCHASE_PACKAGE";
    private final TodoTaskDispatchInterface todoTaskDispatchService;
    private final MmPurchasePackageFeignAPI mmPurchasePackageFeignAPI;
    private final MmReleaseReceiveFeignAPI mmReleaseReceiveFeignAPI;

    private final BpmEntitySubTypeRepository bpmEntitySubTypeRepository;

    private final EntitySubTypeProcessRelationRepository entitySubTypeProcessRelationRepository;

    @Autowired
    public MaterialTaskService(
        TodoTaskDispatchInterface todoTaskDispatchService,
        MmPurchasePackageFeignAPI mmPurchasePackageFeignAPI,
        MmReleaseReceiveFeignAPI mmReleaseReceiveFeignAPI,
        BpmEntitySubTypeRepository bpmEntitySubTypeRepository,
        EntitySubTypeProcessRelationRepository entitySubTypeProcessRelationRepository
    ) {
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.mmPurchasePackageFeignAPI = mmPurchasePackageFeignAPI;
        this.mmReleaseReceiveFeignAPI = mmReleaseReceiveFeignAPI;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.entitySubTypeProcessRelationRepository = entitySubTypeProcessRelationRepository;
    }

    /**
     * 创建采购包任务。
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param materialTaskDTO 材料任务创建信息
     */
    public void createPurchasePackageTask(
        Long orgId,
        Long projectId,
        ContextDTO context,
        MaterialTaskDTO materialTaskDTO
    ) {
        JsonObjectResponseBody<MmPurchasePackageEntity> mmPurchasePackageResponse = mmPurchasePackageFeignAPI.detail(
            orgId,
            projectId,
            materialTaskDTO.getEntityId()
        );
        if (null == mmPurchasePackageResponse.getData()) {
            throw new BusinessError("Purchase package does not exist!" + "采购包信息不存在！");
        } else {
            MmPurchasePackageEntity entity = mmPurchasePackageResponse.getData();
            // 判断采购包明细是否导入
//            JsonListResponseBody<MmPurchasePackageItemEntity> itemEntities = mmPurchasePackageFeignAPI.searchItem(orgId, projectId, entity.getId(), new MmPurchasePackageItemSearchDTO());
//            if (itemEntities.getData() == null || (itemEntities.getData() != null && itemEntities.getData().size() == 0)) {
//                throw new BusinessError("Purchase package item aren't imported!采购包明细未导入！");
//            }
            // 创建采购包任务
//            List<BpmProcess> processes = activityTaskService.getProcessByNameEN(orgId, projectId, BpmsProcessNameEnum.MATERIAL_PROCUREMENT.getType());
//            if (processes.isEmpty()) {
//                throw new ValidationError("please deploy the process CHANGE_LEAD_BY_CONSTRUCTION");
//            }
//            BpmProcess process = processes.get(0);
            List<BpmEntityTypeProcessRelation> relations = entitySubTypeProcessRelationRepository.findByEntitySubTypeIdAndStatus(entity.getEntitySubTypeId(), EntityStatus.ACTIVE);
            Long processId = relations.size() > 0 ? relations.get(0).getProcess().getId() : 0L;
//            BpmEntitySubType entitySubType = activityTaskService.getEntitiySubTypeByNameEN(orgId, projectId, PURCHASE_PACKAGE);
            Optional<BpmEntitySubType> entitySubTypeOptional = bpmEntitySubTypeRepository.findById(entity.getEntitySubTypeId());
            if (entitySubTypeOptional.isPresent()) {
                BpmEntitySubType entitySubType = entitySubTypeOptional.get();
                ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
                taskDTO.setAssignee(context.getOperator().getId());
                taskDTO.setAssigneeName(context.getOperator().getName());
//                taskDTO.setEntitySubType(PURCHASE_PACKAGE);
                taskDTO.setEntitySubType(entitySubType.getNameEn());
                taskDTO.setEntitySubTypeId(entitySubType.getId());
                taskDTO.setEntityId(materialTaskDTO.getEntityId());
                taskDTO.setEntityNo(entity.getPwpsNumber());
//                taskDTO.setProcess(BpmsProcessNameEnum.CHANGE_LEAD_BY_CONSTRUCTION.getType());
//                taskDTO.setProcessId(process.getId());
                taskDTO.setProcessId(processId);
                taskDTO.setVersion("0");
                CreateResultDTO createResult = todoTaskDispatchService.create(context, orgId, projectId, context.getOperator(), taskDTO);
//                BpmActivityInstanceBase bpmActivityInstance = createResult.getActInst();
            }

            // 更新采购包状态
            MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO = new MmPurchasePackageCreateDTO();
            mmPurchasePackageCreateDTO.setLocked(true);
            mmPurchasePackageFeignAPI.edit(
                orgId,
                projectId,
                materialTaskDTO.getEntityId(),
                mmPurchasePackageCreateDTO
            );
        }

    }


    /**
     * 创建入库单任务。
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param materialTaskDTO 材料任务创建信息
     */
    public void createReceiveTask(
        Long orgId,
        Long projectId,
        ContextDTO context,
        MaterialTaskDTO materialTaskDTO
    ) {
        JsonObjectResponseBody<MmReleaseReceiveEntity> mmReleaseReceiveResponse = mmReleaseReceiveFeignAPI.detail(
            orgId,
            projectId,
            materialTaskDTO.getEntityId()
        );
        if (null == mmReleaseReceiveResponse.getData()) {
            throw new BusinessError("Material Receive does not exist!" + "入库单信息不存在！");
        } else {
            MmReleaseReceiveEntity entity = mmReleaseReceiveResponse.getData();
            // 通过入库单类型查找实体子类ID
            BpmEntitySubType entitySubType = bpmEntitySubTypeRepository.findByProjectIdAndNameEn(projectId, entity.getType().toString());

            if (null == entitySubType) {
                throw new BusinessError("材料入库实体子类型" + entity.getType().toString() + "不存在于当前项目");
            }

            List<BpmEntityTypeProcessRelation> relations = entitySubTypeProcessRelationRepository.findByEntitySubTypeIdAndStatus(entitySubType.getId(), EntityStatus.ACTIVE);
            Long processId = relations.size() > 0 ? relations.get(0).getProcess().getId() : 0L;

            ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
            taskDTO.setAssignee(context.getOperator().getId());
            taskDTO.setAssigneeName(context.getOperator().getName());
            taskDTO.setEntitySubType(entitySubType.getNameEn());
            taskDTO.setEntitySubTypeId(entitySubType.getId());
            taskDTO.setEntityId(materialTaskDTO.getEntityId());
            taskDTO.setEntityNo(entity.getNo());
            taskDTO.setProcessId(processId);
            taskDTO.setVersion("0");
            CreateResultDTO createResult = todoTaskDispatchService.create(context, orgId, projectId, context.getOperator(), taskDTO);
        }
    }
}
