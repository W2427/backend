package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.QualityControlAPI;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.service.constructlog.BaseConstructLog;
import com.ose.tasks.domain.model.service.constructlog.ConstructLogInterface;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.qc.BaseConstructionLog;
import com.ose.util.SpringContextUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name = "实体质量检测")
@RequestMapping("/orgs")
public class QualityControlController extends BaseController implements QualityControlAPI {

    private final BaseConstructLog baseConstructLog;
    private final BpmProcessRepository bpmProcessRepository;

    @Autowired
    public QualityControlController(
        BaseConstructLog baseConstructLog, BpmProcessRepository bpmProcessRepository) {

        this.baseConstructLog = baseConstructLog;
        this.bpmProcessRepository = bpmProcessRepository;
    }

    /**
     * 获取 工序 质量检测结果。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 结果列表
     */
    @Operation(
        summary = "获取 工序 质量检测结果"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/{processId}/{entityId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<? extends BaseConstructionLog> getProcessTestResult(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "工序Id") Long processId,
        @PathVariable @Parameter(description = "工序Id") Long entityId,
        PageDTO pageDTO
    ) {

        ContextDTO context = getContext();
        BpmProcess bpmProcess = bpmProcessRepository.findById(processId).orElse(null);
        if (bpmProcess == null) throw new NotFoundError("there is no this process");
        if (!StringUtils.isEmpty(bpmProcess.getConstructionLogClass())) {


            Class clazz = null;
            try {
                clazz = Class.forName(bpmProcess.getConstructionLogClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ConstructLogInterface delegate = (ConstructLogInterface) SpringContextUtils.getBean(clazz);

            return new JsonListResponseBody<>(
                context,
                delegate.getTestResult(orgId, projectId, entityId, bpmProcess.getNameEn(), pageDTO.toPageable())
            );
        }
        throw new NotFoundError("there is no this process");
    }
}
