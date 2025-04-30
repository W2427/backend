package com.ose.tasks.api.drawing;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.dto.drawing.DetailDesignDrawingCriteriaDTO;
import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface DetailDesignDrawingAPI {

    /**
     * 查找详细设计图纸列表。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param page        分页参数
     * @param criteriaDTO 查询参数
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "detail-design",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DetailDesignDrawing> searchDetailDesignList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        DetailDesignDrawingCriteriaDTO criteriaDTO
    );

    /**
     * 查看详细图纸详细。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        详细设计图纸id
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "detail-design/{id}/detail",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DetailDesignDrawingDetail> searchDetailDesignListDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id);

}
