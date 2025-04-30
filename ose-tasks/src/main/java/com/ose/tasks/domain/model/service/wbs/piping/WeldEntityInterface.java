package com.ose.tasks.domain.model.service.wbs.piping;

import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WpsImportDTO;
import com.ose.tasks.dto.WpsImportResultDTO;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WeldWelderWPSDTO;
import com.ose.tasks.dto.wps.WpsMatchingDTO;
import com.ose.tasks.entity.wbs.entity.WeldEntityBase;
import com.ose.tasks.entity.wps.Wps;

import java.util.List;

public interface WeldEntityInterface extends BaseWBSEntityInterface<WeldEntityBase, WeldEntryCriteriaDTO> {
    /**
     * 取得焊口实体已经设定的WPS信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return WPS信息
     */
    List<Wps> getAlreadySetWPSInfo(
        Long id,
        Long orgId,
        Long projectId
    );

    /**
     * 根据上传的xls更新焊口的wps
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param userId
     * @param uploadDTO
     * @return
     */
    WpsImportResultDTO updateWeldEntityWps(Long orgId, Long projectId, Long userId, WpsImportDTO uploadDTO);


    /**
     * 取得焊口实体已经设定的WPS信息。
     *
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return WPS信息
     */
    List<WpsMatchingDTO> getWelderWps(
        Long orgId,
        Long projectId,
        WeldWelderWPSDTO weldWelderWPSDTO
    );
}
