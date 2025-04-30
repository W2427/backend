package com.ose.tasks.api;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.WeldMaterialDTO;
import com.ose.tasks.entity.WeldMaterial;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface WeldMaterialAPI {

    /**
     * 创建韩祠信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param weldMaterialDTO 焊材信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/weld-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WeldMaterialDTO weldMaterialDTO
    );

    /**
     * 获取焊材详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchNo  批次号
     * @return 焊材详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/weld-material/{batchNo}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WeldMaterial> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("batchNo") String batchNo
    );

}
