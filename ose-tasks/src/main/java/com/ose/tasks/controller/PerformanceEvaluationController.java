package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.PerformanceEvaluationAPI;
import com.ose.tasks.domain.model.service.PerformanceEvaluationInterface;
import com.ose.tasks.dto.PerformanceEvaluationDataDTO;
import com.ose.tasks.dto.PerformanceEvaluationSearchDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "工时评估")
@RestController
@RequestMapping(value = "/performanceEvaluation")
public class PerformanceEvaluationController extends BaseController implements PerformanceEvaluationAPI {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceEvaluationController.class);

    private final PerformanceEvaluationInterface performanceEvaluationService;

    @Autowired
    public PerformanceEvaluationController(PerformanceEvaluationInterface performanceEvaluationService) {
        this.performanceEvaluationService = performanceEvaluationService;
    }

    @Override
    @Operation(
        summary = "search",
        description = "search。"
    )
    @RequestMapping(
        method = GET,
        value = "/search"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<PerformanceEvaluationDataDTO> getList(
        PerformanceEvaluationSearchDTO dto) {

        return new JsonListResponseBody<>(
            performanceEvaluationService
                .getList(dto, getContext().getOperator().getId())
        );
    }

    /**
     *
     * @param criteriaDTO
     * @throws IOException
     */
    @RequestMapping(
        method = GET,
        value = "/download"
    )
    @Operation(description = "按条件下载")
    @WithPrivilege
    @Override
    public void attendanceDownload(
        PerformanceEvaluationSearchDTO criteriaDTO
    ) throws IOException, ParseException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = performanceEvaluationService.saveDownloadFile(criteriaDTO, operator.getId());
        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-timesheet-summary.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }

}
