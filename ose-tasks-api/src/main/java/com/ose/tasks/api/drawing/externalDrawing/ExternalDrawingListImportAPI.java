package com.ose.tasks.api.drawing.externalDrawing;

import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/external-drawing")
public interface ExternalDrawingListImportAPI {

    /**
     * 导入{管道}生产图纸清单。
     */
    @RequestMapping(
        method = POST,
        value = "{discipline}/import",
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
}
