package com.ose.docs.controller;

import com.ose.annotation.InternalAccessOnly;
import com.ose.annotation.MultipartFileMaxSize;
import com.ose.annotation.MultipartFileMimeType;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadAPI;
import com.ose.docs.constant.upload.*;
import com.ose.docs.domain.model.service.FileStorageInterface;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.docs.util.RedisToES8;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "文件上传接口")
@RestController
public class UploadController extends BaseController implements UploadAPI {

    // 文件存储服务
    private FileStorageInterface fileStorageService;

    /**
     * 构造方法。
     */
    @Autowired
    public UploadController(
        FileStorageInterface fileStorageService
    ) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * 保存上传的文件。
     *
     * @param orgId            所属组织 ID
     * @param fileUploadConfig 文件上传配置
     * @param multipartFile    通过 HTTP 传输的文件数据
     * @return 响应实例
     */
    private JsonObjectResponseBody<TemporaryFileDTO> saveMultipartFile(
        String orgId,
        FileMetadataDTO.FileUploadConfig fileUploadConfig,
        MultipartFile multipartFile
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            new TemporaryFileDTO(
                fileStorageService.saveAsTemporaryFile(
                    getContext().getOperator(),
                    orgId,
                    fileUploadConfig,
                    multipartFile
                ).getName(),
                multipartFile.getOriginalFilename()
            )
        );
    }

    @Override
    @Operation(
        summary = "上传项目模块流程定义文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/project-module-process-definition-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(ProjectModuleProcessDefinitionFile.MAX_SIZE)
    @MultipartFileMimeType(ProjectModuleProcessDefinitionFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadProjectModuleProcessDefinitionFile(
        @PathVariable @Parameter(description = "组织 ID") String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, ProjectModuleProcessDefinitionFile.CONFIG, file);
    }

    @Override
    @Operation(
        summary = "上传项目层级结构导入文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/project-hierarchy-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(ProjectHierarchyImportFile.MAX_SIZE)
    @MultipartFileMimeType(ProjectHierarchyImportFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadProjectHierarchyImportFile(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, ProjectHierarchyImportFile.CONFIG, file);
    }

    @Override
    @Operation(
        summary = "上传 WBS 实体导入文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wbs-entity-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(ProjectEntitiesImportFile.MAX_SIZE)
    @MultipartFileMimeType(ProjectEntitiesImportFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWBSEntityImportFile(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, ProjectEntitiesImportFile.CONFIG, file);
    }

    /**
     * 上传 材料代码别称与材料分组对应关系导入文件。
     *
     * @param orgId 组织ID
     * @param file  材料代码别称与材料分组对应关系导入文件
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/material-code-alias-group-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "上传 材料代码别称与材料分组对应关系导入文件"
    )
    @Override
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(MaterialCodeAliasGroupExportFile.MAX_SIZE)
    @MultipartFileMimeType(MaterialCodeAliasGroupExportFile.MIME_TYPE)
    @WithPrivilege
    public JsonObjectResponseBody<TemporaryFileDTO> uploadMaterialCodeAliasGroupImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file) {
        return saveMultipartFile(orgId, MaterialCodeAliasGroupExportFile.CONFIG, file);
    }

    /**
     * 上传额定工时导入文件。
     *
     * @param orgId 组织ID
     * @param file  额定工时导入文件
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/rated-time-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "上传额定工时导入文件"
    )
    @Override
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(RatedTimeImportFile.MAX_SIZE)
    @MultipartFileMimeType(RatedTimeImportFile.MIME_TYPE)
    @WithPrivilege
    public JsonObjectResponseBody<TemporaryFileDTO> uploadRatedTimeImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file) {
        return saveMultipartFile(orgId, RatedTimeImportFile.CONFIG, file);
    }

    /**
     * 上传WPS导入文件。
     *
     * @param orgId 组织ID
     * @param file  WPS导入文件
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
    @Override
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(WpsImportFile.MAX_SIZE)
    @MultipartFileMimeType(WpsImportFile.MIME_TYPE)
    @WithPrivilege
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWpsImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file) {
        return saveMultipartFile(orgId, WpsImportFile.CONFIG, file);
    }

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
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWpsImportUpdateFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file) {
        return saveMultipartFile(orgId, WpsImportUpdateFile.CONFIG, file);
    }

    /**
     * 上传WELDER导入文件。
     *
     * @param orgId 组织ID
     * @param file  WELDER导入文件
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/welder-file",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "上传WELDER导入文件"
    )
    @Override
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(WelderImportFile.MAX_SIZE)
    @MultipartFileMimeType(WelderImportFile.MIME_TYPE)
    @WithPrivilege
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWelderImportFile(
        @PathVariable("orgId") String orgId,
        @RequestPart("file") MultipartFile file) {
        return saveMultipartFile(orgId, WelderImportFile.CONFIG, file);
    }

    @Override
    @Operation(
        summary = "上传项目计划导入文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/plan-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(ProjectPlanImportFile.MAX_SIZE)
    @MultipartFileMimeType(ProjectPlanImportFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadPlanImportFile(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, ProjectPlanImportFile.CONFIG, file);
    }

    @Override
    @Operation(
        summary = "上传项目文档文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/project-document-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(ProjectDocumentFile.MAX_SIZE)
    @MultipartFileMimeType(ProjectDocumentFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadProjectDocumentFile(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, ProjectDocumentFile.CONFIG, file);
    }

    @Override
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
    @MultipartFileMaxSize(IssueAttachment.MAX_SIZE)
    @MultipartFileMimeType(IssueAttachment.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadIssueAttachment(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, IssueAttachment.CONFIG, file);
    }

    @Override
    @Operation(
        summary = "上传遗留问题导入文件",
        description = "需要用户在指定组织中拥有操作项目的权限（<code>PROJECT</code>）。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/issue-import-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @MultipartFileMaxSize(IssueImportFile.MAX_SIZE)
    @MultipartFileMimeType(IssueImportFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadIssueImportFile(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, IssueAttachment.CONFIG, file);
    }

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
    @MultipartFileMaxSize(ExperienceAttachment.MAX_SIZE)
    @MultipartFileMimeType(ExperienceAttachment.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadExperienceAttachment(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, ExperienceAttachment.CONFIG, file);
    }

    /**
     * 上传WPS附件。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Override
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
    @MultipartFileMaxSize(WpsAttachment.MAX_SIZE)
    @MultipartFileMimeType(WpsAttachment.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWpsAttachment(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, WpsAttachment.CONFIG, file);
    }

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
    @MultipartFileMaxSize(WpqrAttachment.MAX_SIZE)
    @MultipartFileMimeType(WpqrAttachment.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWpqrAttachment(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, WpqrAttachment.CONFIG, file);
    }

    /**
     * 上传分包商Logo。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Override
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
    @MultipartFileMaxSize(SubConAttachment.MAX_SIZE)
    @MultipartFileMimeType(SubConAttachment.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadSubConAttachment(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, SubConAttachment.CONFIG, file);
    }

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
    @MultipartFileMaxSize(SubConAttachment.MAX_SIZE)
    @MultipartFileMimeType(SubConAttachment.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWelderAttachment(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, SubConAttachment.CONFIG, file);
    }

    /**
     * 上传NDT检查人员证书。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Override
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
    @MultipartFileMaxSize(NdtInspectorCertificateFile.MAX_SIZE)
    @MultipartFileMimeType(NdtInspectorCertificateFile.MIME_TYPE)
    @WithPrivilege // TODO @WithPrivilege(privileges = PROJECT)
    public JsonObjectResponseBody<TemporaryFileDTO> uploadNdtInspectorCertificate(
        @PathVariable String orgId,
        @RequestParam MultipartFile file
    ) {
        return saveMultipartFile(orgId, NdtInspectorCertificateFile.CONFIG, file);
    }


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
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWBSStructureEntityImportFile(
        @PathVariable("orgId") String orgId,
        @RequestBody MultipartFile file
    ) {

        return saveMultipartFile(orgId, ProjectEntitiesImportFile.CONFIG, file);
    }

    /**
     * 上传电气实体文件 XLS。
     *
     * @param orgId 组织ID
     * @param file  文件
     * @return 文件信息
     */
    @Operation(
        summary = "上传结构实体文件 import-project-electrical-entities",
        description = "上传结构实体文件 import-project-electrical-entities"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/wbs-entity-import-electrical-files",
        consumes = MULTIPART_FORM_DATA_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadWBSElectricalEntityImportFile(
        @PathVariable("orgId") String orgId,
        @RequestBody MultipartFile file
    ) {

        return saveMultipartFile(orgId, ProjectEntitiesImportFile.CONFIG, file);
    }

    /**
     * 上传结构套料文件 XLS。
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
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<TemporaryFileDTO> uploadStructureNestImportFile(
        @PathVariable("orgId") String orgId,
        @RequestBody MultipartFile file
    ) {

        return saveMultipartFile(orgId, MaterialStructureNestImportFile.CONFIG, file);
    }

    @Override
    @Operation(description = "保存上传的文件（仅供其他服务调用）")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/uploaded-files/{temporaryFileName}/persist",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @InternalAccessOnly
    @WithPrivilege // TODO
    public JsonObjectResponseBody<FileES> save(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @PathVariable @Parameter(description = "临时文件名") String temporaryFileName,
        @RequestBody @Parameter(description = "文档信息") FilePostDTO filePostDTO
    ) {
        return save(orgId, null, temporaryFileName, filePostDTO);
    }


    @Override
    @Operation(description = "保存上传的文件（仅供其他服务调用）")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/uploaded-files/{temporaryFileName}/persist",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @InternalAccessOnly
    @WithPrivilege // TODO
    public JsonObjectResponseBody<FileES> save(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @PathVariable @Parameter(description = "临时文件名") String temporaryFileName,
        @RequestBody @Parameter(description = "文档信息") FilePostDTO filePostDTO
    ) {
        return new JsonObjectResponseBody<>(
            fileStorageService.resolveTemporaryFile(
                getContext().getOperator(),
                projectId,
                temporaryFileName,
                filePostDTO
            )
        );
    }

    /**
     * 触发redis中数据写入es8。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/es-repair",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody esRepair(
        @PathVariable("orgId") Long orgId
    ) throws Exception {
        String[] args = {};
        RedisToES8.main(args);

        return new JsonResponseBody();
    }

}
