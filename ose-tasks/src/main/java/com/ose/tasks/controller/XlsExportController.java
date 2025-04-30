package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.XlsExportAPI;
import com.ose.tasks.domain.model.service.XlsExportInterface;
import com.ose.tasks.entity.ExportExcel;
import com.ose.util.CollectionUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "通用Excel导出接口")
@RestController
@RequestMapping("/orgs")
public class XlsExportController extends BaseController implements XlsExportAPI {

    private final static Logger logger = LoggerFactory.getLogger(XlsExportController.class);


    private XlsExportInterface xlsExportService;


    @Value("${application.files.protected}")
    private String protectedDir;


    /**
     * 构造方法。
     */
    @Autowired
    public XlsExportController(XlsExportInterface xlsExportService) {
        this.xlsExportService = xlsExportService;
    }

    /**
     * 取得所有通用XLS 导出 列表信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Operation(
        summary = "获取通用导出的Excel列表"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/exported-xls",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<ExportExcel> list(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        PageDTO pageDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            xlsExportService.getList(orgId, projectId, pageDTO)
        );
    }

    /**
     * 导出excel。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @Param excelView 导出的excel视图名称
     */
    @Operation(
        summary = "导出选定到excel"
    )
    @RequestMapping(
        method = GET,
        value = "/{orgId}/projects/{projectId}/export-excel",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public void exportExcel(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestParam("excel_view") String excelView) throws FileNotFoundException {

        /*  1. 检查文件生成的时间和同步、异步。如果为异步直接生成
            2. 如果文件已经不在2小时内，则生成线程生成xlsx文件
            3. 长传保存文件
            4. 更新export_excel中的文件生成时间
            5. 如果export_excel表中生成文件的时间在2小时内，则直接下载文件
         */
        ContextDTO context = getContext();

        if (excelView.contains("3000-D-EC-000-CN-ITP-0818-00_05U-08-WHS")) {
            List<ExportExcel> eel = xlsExportService.getViewName(orgId, projectId, excelView);

            logger.info(excelView + " is exporting");

            if (CollectionUtils.isEmpty(eel) || eel.size() != 1) {

                throw new BusinessError("NO SUCH VIEW 没有这样的下载文件");
            } else {
                File excel = xlsExportService.saveWhsDownloadFile(orgId, projectId, excelView);
                if (excel == null) throw new BusinessError("FILE is EMPTY");

                HttpServletResponse response = getContext().getResponse();
                try {
                    response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

                    response.setHeader(
                        CONTENT_DISPOSITION,
                        "attachment; filename=" + excelView + ".xlsx"
                    );

                    IOUtils.copy(
                        new FileInputStream(excel), response.getOutputStream()
                    );

                } catch (FileNotFoundException e) {
                    throw new NotFoundError();
                } catch (UnsupportedEncodingException e) {
                    throw new BusinessError("", "文件编码不支持");
                } catch (IOException e) {
                    throw new BusinessError("", "下载文件出错");
                }

                try {
                    response.flushBuffer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } else {
            List<ExportExcel> eel = xlsExportService.getViewName(orgId, projectId, excelView);

            logger.info(excelView + " is exporting");

            if (CollectionUtils.isEmpty(eel) || eel.size() != 1) {

                throw new BusinessError("NO SUCH VIEW 没有这样的下载文件");
            } else {
                ExportExcel exportExcel = eel.get(0);

                if (!exportExcel.isAsync()) {


                    File excel = xlsExportService.saveDownloadFile(orgId, projectId, excelView);
                    if (excel == null) throw new BusinessError("FILE is EMPTY");

                    HttpServletResponse response = getContext().getResponse();
                    try {
                        response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

                        response.setHeader(
                            CONTENT_DISPOSITION,
                            "attachment; filename=" + excelView + ".xlsx"
                        );

                        IOUtils.copy(
                            new FileInputStream(excel), response.getOutputStream()
                        );

                    } catch (FileNotFoundException e) {
                        throw new NotFoundError();
                    } catch (UnsupportedEncodingException e) {
                        throw new BusinessError("", "文件编码不支持");
                    } catch (IOException e) {
                        throw new BusinessError("", "下载文件出错");
                    }

                    try {
                        response.flushBuffer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (exportExcel.getExcelGenDate() == null || (System.currentTimeMillis() - exportExcel.getExcelGenDate()) > 12 * 3600000L) {

                    xlsExportService.saveAsyncDownloadFile(context, orgId, projectId, exportExcel);

                    HttpServletResponse response = getContext().getResponse();

                    if (exportExcel.getFilePath() != null) {
                        File excel = new File(protectedDir, exportExcel.getFilePath());
                        try {
                            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

                            response.setHeader(
                                CONTENT_DISPOSITION,
                                "attachment; filename=" + excelView + ".xlsx"
                            );

                            IOUtils.copy(
                                new FileInputStream(excel), response.getOutputStream()
                            );

                        } catch (FileNotFoundException e) {
                            throw new NotFoundError();
                        } catch (UnsupportedEncodingException e) {
                            throw new BusinessError("", "文件编码不支持");
                        } catch (IOException e) {
                            throw new BusinessError("", "下载文件出错");
                        }

                        try {
                            response.flushBuffer();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            response.setContentType(APPLICATION_JSON_UTF8_VALUE);

                            PrintWriter writer = response.getWriter();
                            writer.write("文件生成中，请过几分钟下载");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (exportExcel.isGenerating()) {
                    HttpServletResponse response = getContext().getResponse();
                    try {
                        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

                        PrintWriter writer = response.getWriter();
                        writer.write("文件生成中，请再等几分钟下载");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    File excel = new File(protectedDir, exportExcel.getFilePath());
                    if (excel == null) throw new BusinessError("FILE is EMPTY");

                    HttpServletResponse response = getContext().getResponse();
                    try {
                        response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

                        response.setHeader(
                            CONTENT_DISPOSITION,
                            "attachment; filename=" + excelView + ".xlsx"
                        );

                        IOUtils.copy(
                            new FileInputStream(excel), response.getOutputStream()
                        );

                    } catch (FileNotFoundException e) {
                        throw new NotFoundError();
                    } catch (UnsupportedEncodingException e) {
                        throw new BusinessError("", "文件编码不支持");
                    } catch (IOException e) {
                        throw new BusinessError("", "下载文件出错");
                    }

                    try {
                        response.flushBuffer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }
        }




    }
}
