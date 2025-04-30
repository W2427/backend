package com.ose.tasks.domain.model.repository.material;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.material.FMaterialStructureNestProgram;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结构套料排版库
 */
@Transactional
public interface FMaterialStructureNestProgramRepository extends PagingAndSortingWithCrudRepository<FMaterialStructureNestProgram, Long> {

    /**
     * 通过组织id，项目id，结构套料id,排版编号查找结构套料排版是否存在。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param no        套料结构排版名称
     * @return
     */
    FMaterialStructureNestProgram findByOrgIdAndProjectIdAndNoAndStatus(Long orgId, Long projectId, String no, EntityStatus entityStatus);

    /**
     * 通过组织id，项目id，结构套料id，产生余料号是否存在。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param remainderCreated 产生余料号
     * @return
     */
    FMaterialStructureNestProgram findByOrgIdAndProjectIdAndRemainderCreatedAndStatus(Long orgId, Long projectId, String remainderCreated, EntityStatus entityStatus);

    /**
     * 通过组织id，项目id，结构套料id，使用余料号是否存在。
     *
     * @param orgId         组织id
     * @param projectId     项目id
     * @param remainderUsed 使用余料号
     * @return
     */
    FMaterialStructureNestProgram findByOrgIdAndProjectIdAndRemainderUsedAndStatus(Long orgId, Long projectId, String remainderUsed, EntityStatus entityStatus);

    /**
     * 结构套料排版库列表。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 套料结构方案id
     * @param pageable                 分页参数
     * @return
     */
    Page<FMaterialStructureNestProgram> findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(Long orgId, Long projectId, Long fMaterialStructureNestId, EntityStatus entityStatus, Pageable pageable);

    /**
     * 通过组织id，项目id，结构套料id，查询套料方案下的所有排版程序。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 套料方案id
     * @return
     */
    List<FMaterialStructureNestProgram> findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(Long orgId, Long projectId, Long fMaterialStructureNestId, EntityStatus entityStatus);

    /**
     * 查找套料方案下的某个程序的详情。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param id           套料程序id
     * @param entityStatus 状态
     * @return
     */
    FMaterialStructureNestProgram findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus entityStatus);

}
