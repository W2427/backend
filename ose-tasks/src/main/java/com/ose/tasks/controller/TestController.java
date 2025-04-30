package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.TestAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.StageVersionDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.StageVersion;
import com.ose.tasks.vo.setting.BatchTaskCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "测试线程池接口")
@RestController
public class TestController extends BaseController implements TestAPI {


    private final BatchTaskInterface batchTaskService;

    private ProjectInterface projectService;

    @Resource
    private ThreadPoolTaskScheduler taskExecutor;

    /**
     * 构造方法。
     */
    @Autowired
    public TestController(
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService
    ) {
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;

    }

    @Override
    @Operation(description = "测试线程池")
    @RequestMapping(
        method = GET,
        value = "/test",
        consumes = ALL_VALUE
    )
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonResponseBody test() {
        Long projectId = 1595407955858982139L;
        Long orgId = 1595407955817113231L;
        Project project = projectService.get(orgId, projectId);
        int list1 = 5;
        int list2 = 5;
        Map<String, String> map = new HashMap<>();
        map.put("key", "1");
        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService.run(
            contextDTO,
            project,
            BatchTaskCode.TEST,
            batchTask -> {
                Thread t = Thread.currentThread();

                ThreadLocal<Map<String, String>> tMap = new ThreadLocal<>();
                tMap.set(map);

                taskExecutor.getScheduledThreadPoolExecutor();
                System.out.println("当前线程池中有效数量：" + taskExecutor.getScheduledThreadPoolExecutor().getActiveCount());
                for (int i = 0; i < list1; i++) {
                    System.out.println("线程1->数量：" + list1 + ";当前计数值-> " + i);
                    try {
                        t.sleep(10000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
                Map<String, String> newMap = tMap.get();
                newMap.put("key", "2");
                System.out.println("线程1->MAP：" + newMap.get("key"));
                System.out.println("初始1->MAP：" + map.get("key"));
                return new BatchResultDTO();
            }
        );

        batchTaskService.run(
            contextDTO,
            project,
            BatchTaskCode.TEST,
            batchTask -> {
                ThreadLocal<Map<String, String>> tMap = new ThreadLocal<>();
                tMap.set(map);

                System.out.println("当前线程池中有效数量：" + taskExecutor.getScheduledThreadPoolExecutor().getActiveCount());
                for (int i = 0; i < list2; i++) {
                    System.out.println("线程2->数量：" + list2 + ";当前计数值-> " + i);
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
                Map<String, String> newMap = tMap.get();
                newMap.put("key", "3");
                System.out.println("线程2->MAP：" + newMap.get("key"));
                System.out.println("初始2->MAP：" + map.get("key"));
                return new BatchResultDTO();
            }
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "测试线程池2")
    @RequestMapping(
        method = GET,
        value = "/test2",
        consumes = ALL_VALUE
    )
    @ResponseStatus(OK)
    @WithPrivilege
    public JsonResponseBody test2() {
        Long projectId = 1595407955858982139L;
        Long orgId = 1595407955817113231L;
        Project project = projectService.get(orgId, projectId);
        int list2 = 200;

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        Map<String, String> map = new HashMap<>();
        batchTaskService.run(
            contextDTO,
            project,
            BatchTaskCode.TEST,
            batchTask -> {
                if (!map.isEmpty()) {
                    System.out.println(map.get("key"));
                }
                map.put("key", "200");
                System.out.println("当前线程池中有效数量：" + taskExecutor.getScheduledThreadPoolExecutor().getActiveCount());
                for (int i = 0; i < list2; i++) {
                    System.out.println("线程22->数量：" + list2 + ";当前计数值-> " + i);
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }

                return new BatchResultDTO();
            }
        );


        return new JsonResponseBody();
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/stage-version-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<StageVersion> getStageVersionList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
        ) {
        return new JsonListResponseBody<>(
            getContext(),
            projectService.getStageVersionList(orgId, projectId, pageDTO)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/stages/{stage}/stage-version-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<StageVersion> getStageVersionList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("stage") String stage
    ) {
        return new JsonListResponseBody<>(projectService.getStageVersionList(orgId, projectId,stage));
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/stageVersions/{stageVersionId}/stage-version",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<StageVersion> getStageVersion(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("stageVersionId") Long stageVersionId
    ) {
        return new JsonObjectResponseBody<>(projectService.getStageVersion(orgId, projectId, stageVersionId));
    }

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/stage-version",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonResponseBody createStageVersionList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody StageVersionDTO stageVersionDTO
    ) {
        projectService.creatStageVersionList(orgId, projectId, stageVersionDTO, getContext());
        return new JsonResponseBody();
    }

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/process-stages-version/{stageVersionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonResponseBody deleteStageVersionList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("stageVersionId") Long stageVersionId
    ) {
        projectService.deleteStageVersionList(orgId, projectId, stageVersionId, getContext());
        return new JsonResponseBody();
    }


}
