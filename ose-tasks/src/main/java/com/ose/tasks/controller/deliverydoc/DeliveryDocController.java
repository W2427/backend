package com.ose.tasks.controller.deliverydoc;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.feign.RequestWrapper;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.deliverydoc.DeliveryDocAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.deliverydoc.DeliveryDocInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO;
import com.ose.tasks.dto.deliverydoc.GenerateDocDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.deliverydoc.DeliveryDocument;
import com.ose.tasks.vo.setting.BatchTaskCode;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

@Tag(name = "文档包接口")
@RestController
public class DeliveryDocController extends BaseController implements DeliveryDocAPI {

    private final DeliveryDocInterface deliveryDocService;
    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    /**
     * 构造方法
     */
    @Autowired
    public DeliveryDocController(
        DeliveryDocInterface deliveryDocService,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService
    ) {
        this.deliveryDocService = deliveryDocService;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    @Override
    @Operation(
        summary = "生成指定模块工序文档包",
        description = "生成指定模块工序文档包"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody generateDoc(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody GenerateDocDTO dto
    ) {
        ContextDTO context = getContext();
        final OperatorDTO operatorDTO = getContext().getOperator();
        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();
        @SuppressWarnings("static-access") final RequestAttributes attributes = new ServletRequestAttributes(
            new RequestWrapper(context.getRequest(), authorization, userAgent),
            null
        );

        if (!context.isContextSet()) {

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }

        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.DELIVERY,
            false,
            context,
            batchTask -> {
                deliveryDocService.generateDoc(
                    operatorDTO,
                    orgId,
                    projectId,
                    dto,
                    attributes
                );
                return new BatchResultDTO();
            });


















        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "生成指定模块文档包",
        description = "生成指定模块文档包"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody generateDoc(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "模块名") String module
    ) {
        ContextDTO context = getContext();
        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();

        @SuppressWarnings("static-access") final RequestAttributes attributes = new ServletRequestAttributes(
            new RequestWrapper(context.getRequest(), authorization, userAgent),
            null
        );
        final OperatorDTO operatorDTO = getContext().getOperator();


        if (!context.isContextSet()) {
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }











        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.DELIVERY,
            false,
            context,
            batchTask -> {
                deliveryDocService.generateDoc(
                    operatorDTO,
                    orgId,
                    projectId,
                    module,
                    attributes
                );
                return new BatchResultDTO();
            });
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "生成项目文档包",
        description = "生成项目文档包"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody generateDoc(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        ContextDTO context = getContext();
        String authorization = context.getAuthorization();
        String userAgent = context.getUserAgent();
        @SuppressWarnings("static-access") final RequestAttributes attributes = new ServletRequestAttributes(
            new RequestWrapper(context.getRequest(), authorization, userAgent),
            null
        );
        final OperatorDTO operatorDTO = getContext().getOperator();

        if (!context.isContextSet()) {
            RequestContextHolder.setRequestAttributes(attributes, true);

            context.setContextSet(true);
        }










        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            context,
            batchTask -> {
                deliveryDocService.generateDoc(
                    operatorDTO,
                    orgId,
                    projectId,
                    attributes
                );
                return new BatchResultDTO();
            });
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "项目模块列表",
        description = "项目模块列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DeliveryDocModulesDTO> getModules(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        String keyword
    ) {
        return new JsonListResponseBody<>(
            deliveryDocService.getModules(
                orgId,
                projectId,
                keyword
            )
        );
    }

    @Override
    @Operation(
        summary = "查询模块文档包",
        description = "查询模块文档包"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<DeliveryDocument> getDeliveryDocs(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "模块名") String module
    ) {
        return new JsonListResponseBody<>(
            deliveryDocService.getDeliveryDocs(
                getContext().getOperator(),
                orgId,
                projectId,
                module
            )
        );
    }

    @Override
    @Operation(
        summary = "查询模块文档包",
        description = "查询模块文档包"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public void downloadModuleZipFile(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "模块名") String module
    ) throws IOException {

        final OperatorDTO operator = getContext().getOperator();

        File zip = deliveryDocService.getModuleZipFile(operator, orgId, projectId, module);

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"" + zip.getName() + "\""
            );

            IOUtils.copy(
                new FileInputStream(zip), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        response.flushBuffer();
    }

}
