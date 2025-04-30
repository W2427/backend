package com.ose.docs.api;

import com.ose.docs.entity.FileViewES;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 文件接口。
 */
@FeignClient(name = "ose-docs", contextId = "fileFeign")
public interface FileFeignAPI {

    /**
     * 取得文件详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FileViewES> getFileInfo(
        @PathVariable("orgId") String orgId,
        @PathVariable("fileId") String fileId
    );

    /**
     * 下载文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}"
    )
    void getFile(
        @PathVariable("orgId") String orgId,
        @PathVariable("fileId") String fileId
    ) throws IOException;

    /**
     * 下载项目文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/files/{fileId}"
    )
    void getFile(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        @PathVariable("fileId") String fileId
    ) throws IOException;

    /**
     * 下载项目文件名为报告号。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/files/{fileId}"
    )
    void getReportFile(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        @PathVariable("fileId") String fileId
    ) throws IOException;

    /**
     * 下载文件缩略图。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/thumbnail"
    )
    void getFileThumbnail(
        @PathVariable("orgId") String orgId,
        @PathVariable("fileId") String fileId
    ) throws IOException;

    /**
     * 下载原文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files/{fileId}/original"
    )
    void getOriginalFile(
        @PathVariable("orgId") String orgId,
        @PathVariable("fileId") String fileId
    ) throws IOException;

}
