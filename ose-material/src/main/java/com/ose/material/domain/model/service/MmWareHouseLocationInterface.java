package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmWareHouseLocationCreateDTO;
import com.ose.material.dto.MmWareHouseLocationSearchDTO;
import com.ose.material.entity.MmWareHouseLocationEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 仓库货位接口
 */
public interface MmWareHouseLocationInterface {

    /**
     * 获取仓库货位。
     */
    Page<MmWareHouseLocationEntity> search(
        Long orgId,
        Long projectId,
        MmWareHouseLocationSearchDTO mmWareHouseLocationSearchDTO
    );

    /**
     * 创建仓库货位。
     *
     * @param orgId
     * @param projectId
     * @param mmWareHouseLocationCreateDTO
     * @return
     */
    MmWareHouseLocationEntity create(
        Long orgId,
        Long projectId,
        MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新仓库货位。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseLocationId
     * @param mmWareHouseLocationCreateDTO
     * @return
     */
    MmWareHouseLocationEntity update(
        Long orgId,
        Long projectId,
        Long wareHouseLocationId,
        MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 仓库货位详情。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseLocationId
     * @return
     */
    MmWareHouseLocationEntity detail(
        Long orgId,
        Long projectId,
        Long wareHouseLocationId
    );

    /**
     * 删除仓库货位。
     *
     * @param orgId
     * @param projectId
     * @param wareHouseLocationId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long wareHouseLocationId,
        ContextDTO contextDTO
    );

    /**
     * 获取某类仓库货位。
     */
    List<MmWareHouseLocationEntity> searchType(
        Long orgId,
        Long projectId,
        String wareHouseType
    );
}
