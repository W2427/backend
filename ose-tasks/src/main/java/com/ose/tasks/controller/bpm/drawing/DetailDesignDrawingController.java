package com.ose.tasks.controller.bpm.drawing;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.drawing.DetailDesignDrawingAPI;
import com.ose.tasks.domain.model.service.drawing.DetailDesignDrawingInterface;
import com.ose.tasks.dto.drawing.DetailDesignDrawingCriteriaDTO;
import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "详细设计图纸清单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class DetailDesignDrawingController extends BaseController implements DetailDesignDrawingAPI {

    private DetailDesignDrawingInterface detailDesignListService;

    /**
     * 构造方法
     */
    @Autowired
    public DetailDesignDrawingController(
        DetailDesignDrawingInterface detailDesignListService) {
        this.detailDesignListService = detailDesignListService;
    }

    /**
     * 详细设计图纸列表。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param page        分页参数
     * @param criteriaDTO 查询参数
     * @return 详细设计图纸列表
     */
    @Override
    @Operation(summary = "查询详细设计图纸清单列表", description = "查询详细设计图纸清单列表")
    @RequestMapping(method = GET, value = "detail-design", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DetailDesignDrawing> searchDetailDesignList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO page,
        DetailDesignDrawingCriteriaDTO criteriaDTO) {
        return new JsonListResponseBody<>(getContext(),
            detailDesignListService.searchDetailDesignList(orgId, projectId, page, criteriaDTO));
    }

    /**
     * 详细设计图纸详情。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        详细设计图纸id
     * @return 详细设计图纸详情
     */
    @Override
    @Operation(summary = "查询详细设计图纸清单明细列表", description = "查询详细设计图纸清单明细列表")
    @RequestMapping(method = GET, value = "detail-design/{id}/detail", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DetailDesignDrawingDetail> searchDetailDesignListDetail(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "DetailDesignListID") Long id) {
        return new JsonListResponseBody<>(getContext(),
            detailDesignListService.searchDetailDesignListDetail(orgId, projectId, id));
    }

}
