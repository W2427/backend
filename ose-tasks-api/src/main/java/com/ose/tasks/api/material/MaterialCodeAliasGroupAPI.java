package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.MaterialCodeAliasGroupCriteriaDTO;
import com.ose.tasks.dto.material.MaterialCodeAliasGroupDTO;
import com.ose.tasks.dto.material.MaterialCodeAliasGroupImportDTO;
import com.ose.tasks.entity.material.MaterialCodeAliasGroup;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;


public interface MaterialCodeAliasGroupAPI {

    /**
     * 查询材料代码别称与材料分组的对应关系。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 材料代码别称与材料分组的对应关系 列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-code-alias-group-relation",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<MaterialCodeAliasGroup> searchMaterialCodeAliasGroupRelations(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        MaterialCodeAliasGroupCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得材料代码别称与材料分组的对应关系。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param id        实体 ID
     * @return 材料代码别称与材料分组的对应关系信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-code-alias-group-relation/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<MaterialCodeAliasGroup> getMaterialCodeAliasGroupRelation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 根据材料代码别称取得对应关系。
     *
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param materialCode 材料代码别称
     * @return 材料代码别称与材料分组的对应关系信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-code-alias-group-relation/material-code/{materialCode}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<MaterialCodeAliasGroup> getMaterialCodeAliasGroupRelationByMaterialCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "材料代码别称") String materialCode);

    /**
     * 删除材料代码别称与材料分组的对应关系。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param id        材料代码别称与材料分组的对应关系ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/material-code-alias-group-relation/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteMaterialCodeAliasGroupRelation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestParam @Parameter(description = "对应关系版本号") long version
    );

    /**
     * 插入材料代码别称与材料分组的对应关系。
     *
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param materialCodeAliasGroupDTO 对应关系传输对象
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/material-code-alias-group-relation",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addMaterialCodeAliasGroupRelation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody MaterialCodeAliasGroupDTO materialCodeAliasGroupDTO
    );

    /**
     * 更新材料代码别称与材料分组的对应关系。
     *
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param id                        对应关系ID
     * @param version                   对应关系的版本号
     * @param materialCodeAliasGroupDTO 对应关系传输对象
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/material-code-alias-group-relation/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateMaterialCodeAliasGroupRelation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody MaterialCodeAliasGroupDTO materialCodeAliasGroupDTO
    );

    /**
     * 导出excel。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/export-material-code-alias-group",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void exportMaterialCodeAliasGroup(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) throws IOException;

    /**
     * 导入excel。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-material-code-alias-group",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importMaterialCodeAliasGroup(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody MaterialCodeAliasGroupImportDTO groupImportDTO
    );
}
