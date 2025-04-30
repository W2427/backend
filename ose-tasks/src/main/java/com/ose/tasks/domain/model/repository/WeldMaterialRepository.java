package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.WeldMaterial;
import com.ose.tasks.vo.WeldMaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WeldMaterialRepository extends PagingAndSortingWithCrudRepository<WeldMaterial, Long>, WpsWeldMaterialBatchNoRepositoryCustom {

    /**
     * 获取焊材列表。
     *
     * @return 焊材列表
     */
    List<WeldMaterial> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);

    /**
     * 获取焊材分页列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageable  分页参数
     * @return 焊材列表
     */
    Page<WeldMaterial> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId, Pageable pageable);

    /**
     * 获取焊材分页列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageable  分页参数
     * @return 焊材列表
     */
    Page<WeldMaterial> findByOrgIdAndProjectIdAndBatchNoAndDeletedIsFalse(Long orgId, Long projectId, String batchNo, Pageable pageable);
    /**
     * 获取焊材详情。
     *
     * @param batchNo 批次号
     * @return 焊材详情
     */
    WeldMaterial findByProjectIdAndBatchNoAndDeletedIsFalse(Long projectId ,String batchNo);
    /**
     * 获取焊材批次号。
     *
     * @param weldMaterialType 焊材类型
     * @return 焊材详情
     */
    List<WeldMaterial> findByProjectIdAndWeldMaterialTypeAndDeletedIsFalse(Long projectId, WeldMaterialType weldMaterialType);

}
