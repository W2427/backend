package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.MmImportBatchTask;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-import")
public interface MmMaterialImportHistoryAPI {

    /**
     * 导入记录。
     */
    @RequestMapping(
        method = GET,
        value = "/{entityId}"
    )
    JsonListResponseBody<MmImportBatchTask> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        MmMaterialImportHistorySearchDTO mmMaterialImportHistorySearchDTO
    );

}
