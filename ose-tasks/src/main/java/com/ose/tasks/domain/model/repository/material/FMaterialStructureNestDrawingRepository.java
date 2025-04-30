package com.ose.tasks.domain.model.repository.material;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.material.FMaterialStructureNestDrawing;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结构套料零件库
 */
@Transactional
public interface FMaterialStructureNestDrawingRepository extends PagingAndSortingWithCrudRepository<FMaterialStructureNestDrawing, Long> {

    /**
     * 结构套料零件列表。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 套料结构方案id
     * @param pageable                 分页参数
     * @return
     */
    Page<FMaterialStructureNestDrawing> findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(Long orgId, Long projectId, Long fMaterialStructureNestId, EntityStatus entityStatus, Pageable pageable);

    /**
     * 通过组织id，项目id，结构套料方案id，查找套料零件列表。
     *
     * @param orgId
     * @param projectId
     * @param fMaterialStructureNestId
     * @param entityStatus
     * @return
     */
    List<FMaterialStructureNestDrawing> findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(Long orgId, Long projectId, Long fMaterialStructureNestId, EntityStatus entityStatus);

    /**
     * 通过程序编号查找结构套料零件列表。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param nestingProgramNo 结构套料程序编号
     * @param pageable         分页参数
     * @return
     */
    Page<FMaterialStructureNestDrawing> findByOrgIdAndProjectIdAndNestingProgramNoAndStatus(Long orgId, Long projectId, String nestingProgramNo, EntityStatus entityStatus, Pageable pageable);

    /**
     * 结构套料零件列表。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param wp04No       套料结构方案id
     * @param wp05No       分页参数
     * @param entityStatus 状态
     * @return
     */
    List<FMaterialStructureNestDrawing> findByOrgIdAndProjectIdAndWp04NoAndWp05NoAndStatus(Long orgId, Long projectId, String wp04No, String wp05No, EntityStatus entityStatus);

    /**
     * 结构套料零件列表。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param wp05No       分页参数
     * @param entityStatus 状态
     * @return
     */
    List<FMaterialStructureNestDrawing> findByOrgIdAndProjectIdAndWp05NoAndStatus(Long orgId, Long projectId, String wp05No, EntityStatus entityStatus);

    /**
     * 结构套料零件列表。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param wp05No       分页参数
     * @param entityStatus 状态
     * @return
     */
    List<FMaterialStructureNestDrawing> findByOrgIdAndProjectIdAndWp05NoAndRevAndFinishAndStatus(
        Long orgId,
        Long projectId,
        String wp05No,
        String rev,
        Boolean finish,
        EntityStatus entityStatus
    );
}
