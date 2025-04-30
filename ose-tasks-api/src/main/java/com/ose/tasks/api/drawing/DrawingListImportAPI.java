package com.ose.tasks.api.drawing;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTaskBasic;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface DrawingListImportAPI {

    /**
     * 导入{管道}生产图纸清单。
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/{discipline}/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importDrawingList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("discipline") String discipline,
        @RequestParam("version") long version,
        @RequestBody DrawingImportDTO importDTO
    );


    /**
     * 导入图纸记录。
     */
    @RequestMapping(
        method = GET,
        value = "drawing-list/import-records",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BatchTaskBasic> importRecords(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO page
    );

    /**
     * 导入图纸详细设计清单。
     */
    @RequestMapping(
        method = POST,
        value = "detail-design/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importDetailDesign(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody DrawingImportDTO importDTO
    );
}
