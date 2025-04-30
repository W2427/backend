package com.ose.tasks.controller.taskpackage;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.mail.GOEMailConfigAPI;
import com.ose.tasks.domain.model.service.mail.GOEMailConfigInterface;
import com.ose.tasks.entity.bpm.GOEMailConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GOE人员查询接口")
@RestController
public class GOEMailConfigController extends BaseController implements GOEMailConfigAPI {


    private final GOEMailConfigInterface goeMailConfigService;

    /**
     * 构造方法。
     */
    @Autowired
    public GOEMailConfigController(
        GOEMailConfigInterface goeMailConfigService) {
        this.goeMailConfigService = goeMailConfigService;
    }

    @Override
    @Operation(description = "查询任务包")
    @WithPrivilege
    public JsonListResponseBody<GOEMailConfig> searchList(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "流程 ID") Long actInstId,
        @PathVariable @Parameter(description = "任务节点") String taskDefKey
    ) {
        return new JsonListResponseBody<>(
            goeMailConfigService
                .searchList(orgId, projectId, actInstId, taskDefKey)
        );
    }
}
