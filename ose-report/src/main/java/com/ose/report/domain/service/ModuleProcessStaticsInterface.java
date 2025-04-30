package com.ose.report.domain.service;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ModuleRelationDTO;
import com.ose.report.dto.ModuleRelationSearchDTO;
import com.ose.report.dto.ModuleShipmentDetailDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessStaticDTO;
import com.ose.report.entity.moduleProcess.ModuleProcessStatics;
import org.springframework.data.domain.Page;

/**
 * 统计数据归档服务接口。
 */
public interface ModuleProcessStaticsInterface {

    /**
     * 创建模块完成进度统计。
     *
     * @param orgId
     * @param projectId
     * @param moduleProcessStaticDTO
     */

    void create(
        final Long orgId,
        final Long projectId,
        final ModuleProcessStaticDTO moduleProcessStaticDTO
    );

    Page<ModuleProcessStatics> search(
        Long orgId,
        Long projectId,
        PageDTO page
    );

    ModuleRelationDTO searchModuleRelationNos(
        Long orgId,
        Long projectId,
        String discipline
    );

    ModuleRelationDTO searchModuleNos(
        Long orgId,
        Long projectId,
        String shipmentNo,
        String discipline
    );

    ModuleShipmentDetailDTO searchModuleDetail(
        Long orgId,
        Long projectId,
        String shipmentNo,
        ModuleRelationSearchDTO moduleRelationSearchDTO
    );
}
