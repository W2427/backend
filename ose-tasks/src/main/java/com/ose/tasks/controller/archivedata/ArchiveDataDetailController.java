package com.ose.tasks.controller.archivedata;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.tasks.api.archivedata.ArchiveDataDetailAPI;
import com.ose.tasks.domain.model.service.archivedata.ArchiveDataDetailInterface;
import com.ose.tasks.dto.archivedata.ArchiveDataCriteriaDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

@Tag(name = "归档数据明细接口")
@RestController
public class ArchiveDataDetailController extends BaseController implements ArchiveDataDetailAPI {

    private final ArchiveDataDetailInterface archiveDataDetailService;

    /**
     * 构造方法
     */
    @Autowired
    public ArchiveDataDetailController(
        ArchiveDataDetailInterface archiveDataDetailService
    ) {
        this.archiveDataDetailService = archiveDataDetailService;
    }

    @Override
    @Operation(
        summary = "查询模块文档包",
        description = "查询模块文档包"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public void downloadDataDetailsFile(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "归档数据业务类型") ArchiveDataType archiveType,
        ArchiveScheduleType scheduleType,
        ArchiveDataCriteriaDTO criteriaDTO
    ) throws IOException {

        final OperatorDTO operator = getContext().getOperator();

        File file = archiveDataDetailService
            .downloadDataDetailsFile(
                operator,
                orgId,
                projectId,
                archiveType,
                scheduleType,
                criteriaDTO
            );

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-archive-data-details.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(file), response.getOutputStream()
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
