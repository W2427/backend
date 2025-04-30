package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.bpm.HierarchyBaseDTO;
import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 工序阶段 CRUD 操作接口。
 */
@Transactional
public interface BpmProcessStageRepository extends PagingAndSortingWithCrudRepository<BpmProcessStage, Long> {

    Page<BpmProcessStage> findByStatusAndProjectIdAndOrgId(EntityStatus active, Long projectId, Long orgId, Pageable pageable);

    List<BpmProcessStage> findByProjectIdAndStatus(Long projectId, EntityStatus status);

    Optional<BpmProcessStage> findByOrgIdAndProjectIdAndNameEn(Long orgId, Long projectId, String nameEn);

    Optional<BpmProcessStage> findByOrgIdAndProjectIdAndNameEnAndStatus(Long orgId, Long projectId, String nameEn, EntityStatus status);

    /**
     * 根据工序阶段ID列表获取工序阶段列表。
     *
     * @param processStageIDs 工序阶段ID列表
     * @return 工序阶段列表
     */
    List<BpmProcessStage> findByIdIn(List<Long> processStageIDs);

    /**
     * 根据工序阶段名称、所属组织 ID、所属项目 ID 取得工序阶段。
     *
     * @param nameCn    工序阶段名称
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @param status    状态
     * @return 工序阶段
     */
    Optional<BpmProcessStage> findByOrgIdAndProjectIdAndNameCnAndStatus(
        Long orgId,
        Long projectId,
        String nameCn,
        EntityStatus status
    );

    @Query("SELECT new com.ose.tasks.dto.process.EntityProcessDTO(s.nameEn, p.nameEn) FROM BpmProcessStage s LEFT OUTER JOIN BpmProcess p ON p.processStage.id = s.id AND p.status = com.ose.vo.EntityStatus.ACTIVE WHERE s.projectId = :projectId AND s.status = com.ose.vo.EntityStatus.ACTIVE")
    List<EntityProcessDTO> findProcesses(@Param("projectId") Long projectId);


    List<BpmProcessStage> findByOrgIdAndProjectIdAndNameEnInAndStatus(
        Long orgId,
        Long projectId,
        List<String> stages,
        EntityStatus status
    );

    List<BpmProcessStage> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bpm_process_stage WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);

    @Query("SELECT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(ps.id, ps.nameCn, ps.nameEn) FROM BpmProcessStage ps WHERE id = :processStageId")
    HierarchyBaseDTO findDTOById(@Param("processStageId") Long processStageId);
}
