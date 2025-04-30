package com.ose.docs.api;

import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 文件上传接口。
 */
@FeignClient(name = "ose-docs", contextId = "uploadFeign", configuration = UploadFeignAPI.MultipartSupportConfig.class)
public interface UploadFeignAPI {

    @Component
    class MultipartSupportConfig {

        private final ObjectFactory<HttpMessageConverters> messageConverters;

        @Autowired
        public MultipartSupportConfig(
            ObjectFactory<HttpMessageConverters> messageConverters
        ) {
            this.messageConverters = messageConverters;
        }

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }

    }

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/project-module-process-definition-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadProjectModuleProcessDefinitionFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传项目层级结构导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/project-hierarchy-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadProjectHierarchyImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传 WBS 实体导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wbs-entity-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWBSEntityImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传 材料代码别称与材料分组对应关系导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/material-code-alias-group-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadMaterialCodeAliasGroupImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传额定工时导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/rated-time-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadRatedTimeImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传WPS导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wps-file",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "上传WPS导入文件"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWpsImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file);


    /**
     * 上传WPS更新的导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wps-update-file",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "上传WPS更新的导入文件"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWpsImportUpdateFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file);

    /**
     * 上传welder导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/welder-file",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "上传Welder导入文件"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWelderImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file);

    /**
     * 上传项目计划导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/plan-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadPlanImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传项目文档文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/project-document-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadProjectDocumentFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 持久化上传的文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/uploaded-files/{temporaryFileName}/persist",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<FileES> save(
        @PathVariable("orgId") String orgId,
        @PathVariable("temporaryFileName") String temporaryFileName,
        @RequestBody FilePostDTO filePostDTO
    );

    /**
     * 持久化上传的文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/uploaded-files/{temporaryFileName}/persist",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<FileES> save(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        @PathVariable("temporaryFileName") String temporaryFileName,
        @RequestBody FilePostDTO filePostDTO
    );


    @Operation(
        summary = "上传遗留问题附件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/issue-attachments",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadIssueAttachment(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    @Operation(
        summary = "上传遗留导入文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/issue-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadIssueImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    @Operation(
        summary = "上传经验教训附件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/experience-attachments",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadExperienceAttachment(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file
    );

    /**
     * 上传WPS附件。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传WPS附件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wps-attachments",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWpsAttachment(
        @PathVariable("orgId") String orgId,
        @RequestParam("projectId") MultipartFile file
    );

    /**
     * 上传WPQR附件。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传WPQR附件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wpqr-attachments",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWpqrAttachment(
        @PathVariable("orgId") String orgId,
        @RequestParam("projectId") MultipartFile file
    );


    /**
     * 上传分包商LOGO。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传分包商LOGO",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/subCon-attachments",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadSubConAttachment(
        @PathVariable("orgId") String orgId,
        @RequestParam("projectId") MultipartFile file
    );

    /**
     * 上传焊工照片。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传焊工照片",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/welder-attachments",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWelderAttachment(
        @PathVariable("orgId") String orgId,
        @RequestParam("projectId") MultipartFile file
    );

    /**
     * 上传NDT检查人员证书。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传NDT检查人员证书",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/ndt-inspector-certificates",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadNdtInspectorCertificate(
        @PathVariable("orgId") String orgId,
        @RequestParam("projectId") MultipartFile file
    );

    /**
     * 上传结构实体文件 XLS。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传结构实体文件 import-project-structure-entities",
        description = "上传结构实体文件 import-project-structure-entities"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wbs-entity-import-structure-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWBSStructureEntityImportFile(
        @PathVariable("orgId") String orgId,
        @RequestBody MultipartFile file
    );


    /**
     * 上传 电气 实体文件 XLS。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传电气实体文件 import-project-electrical-entities",
        description = "上传电气实体文件 import-project-electrical-entities"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wbs-entity-import-electrical-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadWBSElectricalEntityImportFile(
        @PathVariable("orgId") String orgId,
        @RequestBody MultipartFile file
    );

    /**
     * 上传结构套料excel。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传结构套料文件 import-project-structure-nest",
        description = "上传结构套料文件 import-project-structure-nest"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/import-project-structure-nest",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<TemporaryFileDTO> uploadStructureNestImportFile(
        @PathVariable("orgId") String orgId,
        @RequestBody MultipartFile file
    );

    /**
     * 触发redis中数据写入es8。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/es-repair",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody esRepair(
        @PathVariable("orgId") Long orgId
    ) throws Exception;

}
