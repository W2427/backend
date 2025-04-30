package com.ose.tasks.api.wbsStructure;

import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 实体管理接口。
 */
public interface WBSStructureEntityAPI {

    /**
     * 导入结构实体。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-structure-entities",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    );

}
