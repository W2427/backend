package com.ose.tasks.api.deliverydoc;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO;
import com.ose.tasks.dto.deliverydoc.GenerateDocDTO;
import com.ose.tasks.entity.deliverydoc.DeliveryDocument;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface DeliveryDocAPI {

    /**
     * 生成文档包
     */
    @RequestMapping(
        method = POST,
        value = "delivery-documents/generate-doc",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody generateDoc(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody GenerateDocDTO dto
    );

    /**
     * 生成文档包
     */
    @RequestMapping(
        method = POST,
        value = "delivery-documents/generate-module-doc/modules/{module}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody generateDoc(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("module") String module
    );

    /**
     * 生成文档包
     */
    @RequestMapping(
        method = POST,
        value = "delivery-documents/generate-all-doc",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody generateDoc(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 查询项目模块列表
     */
    @RequestMapping(
        method = GET,
        value = "delivery-documents/modules",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DeliveryDocModulesDTO> getModules(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        String keyword
    );

    /**
     * 查询模块下文档包
     */
    @RequestMapping(
        method = GET,
        value = "delivery-documents/modules/{module}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DeliveryDocument> getDeliveryDocs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("module") String module
    );

    /**
     * 下载模块文档包
     *
     * @throws IOException
     */
    @RequestMapping(
        method = GET,
        value = "delivery-documents/modules/{module}/download-zip",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void downloadModuleZipFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("module") String module
    ) throws IOException;
}
