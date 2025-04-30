package com.ose.docs.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.FileAPI;
import com.ose.docs.domain.model.repository.FileESRepository;
import com.ose.docs.domain.model.service.FileInterface;
import com.ose.docs.dto.FileCriteriaDTO;
import com.ose.docs.entity.FileBaseES;
import com.ose.docs.entity.FileBasicViewES;
import com.ose.docs.entity.FileViewES;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.util.FileUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "文档接口")
@RestController
public class FileController extends BaseController implements FileAPI {

    // 受保护文件路径
    @Value("${application.files.protected}")
    private String protectedDir;

    // 文件服务
    private final FileInterface fileService;

    private final FileESRepository fileESRepository;

    /**
     * 构造方法。
     */
    public FileController(
        FileInterface fileService,
        FileESRepository fileESRepository) {
        this.fileService = fileService;
        this.fileESRepository = fileESRepository;
    }

    @Override
    @Operation(description = "查询文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    public JsonListResponseBody<FileBasicViewES> search(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @Parameter(description = "查询条件") FileCriteriaDTO criteria,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return search(orgId, null, criteria, pageDTO);
    }

    @Override
    @Operation(description = "查询项目文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/files",
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    public JsonListResponseBody<FileBasicViewES> search(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @Parameter(description = "查询条件") FileCriteriaDTO criteria,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            fileService.search(orgId, projectId, criteria, pageDTO.toPageable())
        );
    }

    @Override
    @Operation(description = "取得文档详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/info",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    @SetUserInfo
    public JsonObjectResponseBody<FileViewES> getFileInfo(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) {
        return new JsonObjectResponseBody<>(fileService.get(orgId, fileId));
    }

    /**
     * 下载文件。
     *
     * @param file         文件数据实体
     * @param relativePath 文件相对路径
     * @throws IOException IO 异常
     */
    private void download(
        FileBaseES file, String relativePath
    ) throws IOException {

        if (relativePath == null) {
            throw new NotFoundError();
        }

        HttpServletResponse response = getContext().getResponse();

        try {

            if (FileUtils.isImage(file.getMimeType())
                || FileUtils.isAudio(file.getMimeType())
                || FileUtils.isVideo(file.getMimeType())
                ) {
                response.setContentType(file.getMimeType());
            } else {
                response.setContentType(APPLICATION_OCTET_STREAM_VALUE);
            }

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"" + (new String(file.getName().getBytes(StandardCharsets.UTF_8), "ISO8859-1")) + "\""
            );

            IOUtils.copy(
                new FileInputStream(new File(protectedDir, relativePath)),
                response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        }

        response.flushBuffer();
    }

    @Override
    @Operation(description = "下载文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}"
    )
    @WithPrivilege // TODO
    public void getFile(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) throws IOException {
        FileBaseES file = fileService.paths(orgId, fileId);
        download(file, file.getPath());
    }

    @Override
    @Operation(description = "下载文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/report/{reportNo}"
    )
    @WithPrivilege // TODO
    public void getReportFile(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "文件 ID") String fileId,
        @PathVariable @Parameter(description = "报告号") String reportNo
    ) throws IOException {
        FileBaseES file = fileService.paths(orgId, fileId);
        int index = 0;
        for (int i = 0; i < file.getName().length(); i++) {
            char fileType = file.getName().toCharArray()[i];
            if (Character.toString(fileType).equals(".")) {
                index = i;
            }
        }
        String suffix = file.getName().substring(index);
        file.setName(reportNo + suffix);
        download(file, file.getPath());
    }

    @Override
    @Operation(description = "下载项目文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/files/{fileId}"
    )
    @WithPrivilege // TODO
    public void getFile(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) throws IOException {
        FileBaseES file = fileService.paths(orgId, projectId, fileId);
        download(file, file.getPath());
    }

    @Override
    @Operation(description = "下载文件缩略图")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/thumbnail"
    )
    @WithPrivilege // TODO
    public void getFileThumbnail(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) throws IOException {
        FileBaseES file = fileService.paths(orgId, fileId);
        download(file, file.getThumbnail());
    }

    @Override
    @Operation(description = "下载原文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/original"
    )
    @WithPrivilege // TODO
    public void getOriginalFile(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) throws IOException {
        FileBaseES file = fileService.paths(orgId, fileId);
        download(file, file.getOriginal());
    }

}
