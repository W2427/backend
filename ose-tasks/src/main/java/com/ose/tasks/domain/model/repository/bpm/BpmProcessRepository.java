package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessCategory;
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
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface BpmProcessRepository extends PagingAndSortingWithCrudRepository<BpmProcess, Long>, BpmProcessRepositoryCustom {

    @Query(
        value = "SELECT m FROM BpmProcess m WHERE m.status=:status and m.orgId=:orgId and m.projectId=:projectId "
            + " and (m.nameCn like %:name% or m.nameEn like %:name%) and m.processStage.id = :stageId and m.processCategory.id = :processCategoryId"
            + " order by m.orderNo",
        countQuery = "SELECT count(m) FROM BpmProcess m WHERE m.status=:status and m.orgId=:orgId and m.projectId=:projectId "
            + " and (m.nameCn like %:name% or m.nameEn like %:name%) and m.processStage.id =:stageId and m.processCategory.id = :processCategoryId"
    )
    Page<BpmProcess> findByStatusAndProjectIdAndOrgIdAndNameCnOrNameEnAndProcessStageId(
        @Param("status") EntityStatus active, @Param("projectId") Long projectId, @Param("orgId") Long orgId,
        @Param("name") String name, @Param("stageId") Long stageId, Pageable pageable, @Param("processCategoryId") Long processCategoryId);

    List<BpmProcess> findByStatusAndProjectIdAndOrgId(EntityStatus status, Long projectId, Long orgId);

    @Query("SELECT distinct m.processStage FROM BpmProcess m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<BpmProcessStage> findProcessStageByProjectIdAndOrgId(@Param("projectId") Long projectId, @Param("orgId") Long orgId);

    @Query("SELECT m FROM BpmProcess m WHERE m.status = 'ACTIVE' and m.processStage.id = :stageId")
    List<BpmProcess> findProcessesByStageId(@Param("stageId") Long stageId);

    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = :orgId AND p.projectId = :projectId AND p.processStage.id = :stageId AND (p.nameEn = :name OR p.nameCn = :name) AND p.status='ACTIVE'")

    Optional<BpmProcess> findByOrgIdAndProjectIdAndStageIdAndName(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("stageId") Long stageId, @Param("name") String name);

    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = :orgId AND p.projectId = :projectId AND p.processStage.nameEn = :stageNameEn AND p.nameEn = :nameEn AND p.status='ACTIVE' ")

    Optional<BpmProcess> findByOrgIdAndProjectIdAndStageNameAndName(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("stageNameEn") String stageNameEn, @Param("nameEn") String name);

    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = :orgId AND p.projectId = :projectId AND p.processStage.nameEn = :stageNameEn AND p.nameEn = :nameEn AND p.status='ACTIVE' AND p.funcPart=:funcPart")
    Optional<BpmProcess> findByOrgIdAndProjectIdAndStageNameAndNameAndFuncPart(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("stageNameEn") String stageNameEn, @Param("nameEn") String name, @Param("funcPart") String funcPart);

    @Query("SELECT p FROM BpmProcess p WHERE p.projectId = :projectId AND p.processStage.nameEn = :stageNameEn AND p.nameEn = :nameEn AND p.status='ACTIVE'")
    Optional<BpmProcess> findByProjectIdAndStageNameAndName(@Param("projectId") Long projectId, @Param("stageNameEn") String stageNameEn, @Param("nameEn") String name);

    @Query("SELECT distinct m.processCategory FROM BpmProcess m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<BpmProcessCategory> findProcessCategoryByProjectIdAndOrgId(@Param("projectId") Long projectId, @Param("orgId") Long orgId);

    /**
     * 根据ID列表获取工序列表。
     *
     * @param processIDs 工序ID列表
     * @return 工序列表
     */
    List<BpmProcess> findByIdIn(List<Long> processIDs);

    /**
     * 根据ID列表获取工序。
     *
     * @param processId 工序ID
     * @return 工序详情
     */
    BpmProcess findByIdIs(Long processId);



    List<BpmProcess> findByOrgIdAndProjectIdAndNameEnAndStatus(Long orgId, Long projectId, String nameEn,
                                                               EntityStatus status);


    /**
     * 根据阶段和工序代码获取工序信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param stage       阶段代码
     * @param processCode 工序代码
     * @return 工序信息
     */
    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = ?1 AND p.projectId = ?2 AND p.processStage.nameEn = ?3 AND p.nameEn = ?4")
    BpmProcess findByStageAndProcessCode(Long orgId, Long projectId, String stage, String processCode);

    /**
     * 根据工序名称、所属组织 ID、所属项目 ID 取得工序。
     *
     * @param processNameCn 工序名称
     * @param stageNameCn   工序阶段名称
     * @param orgId         所属组织 ID
     * @param projectId     项目 ID
     * @param status        状态
     * @return 工序
     */
    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = :orgId AND p.projectId = :projectId AND p.processStage.nameCn = :stageNameCn AND p.nameCn = :processNameCn AND p.status=:status")
    Optional<BpmProcess> findByOrgIdAndProjectIdAndNameCnAndStatus(
        @Param("projectId") Long projectId,
        @Param("orgId") Long orgId,
        @Param("processNameCn") String processNameCn,
        @Param("stageNameCn") String stageNameCn,
        @Param("status") EntityStatus status
    );

    /**
     * 根据 工序阶段Id 返回工序阶段列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param stageId
     * @param status
     * @return
     */
    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = :orgId AND p.projectId = :projectId AND p.processStage.id = :stageId AND p.status=:status")
    List<BpmProcess> findByOrgIdAndProjectIdAndStageIdAndStatus(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("stageId") Long stageId,
        @Param("status") EntityStatus status);

    /**
     * 返回工序阶段-工序列表
     *
     * @param orgId     组织IDw
     * @param projectId 项目ID
     * @return
     */
    @Query(value = "SELECT " +
        "CONCAT(ps.name_en,'-',p.name_en) " +
        "FROM bpm_process p " +
        "JOIN bpm_process_stage ps ON p.process_stage_id = ps.id " +
        "WHERE p.org_id = :orgId AND p.project_id = :projectId AND p.status=:status",
        nativeQuery = true)
    List<String> getProcessKeys(@Param("orgId") Long orgId,
                                @Param("projectId") Long projectId,
                                @Param("status") String status);

    /**
     * 查找当前项目的工序信息。
     *
     * @param orgId
     * @param projectId
     * @param stageId
     * @param status
     * @return
     */
    BpmProcess findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long stageId, EntityStatus status);

    List<BpmProcess> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bpm_process WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);






    BpmProcess findByOrgIdAndProjectIdAndNameEnAndFuncPartAndStatus(
        Long orgId,
        Long projectId,
        String nameEn,
        String funcPartStr,
        EntityStatus status);

    @Query("SELECT p FROM BpmProcess p WHERE p.orgId = :orgId AND p.projectId = :projectId " +
        "AND p.processStage.nameEn = :stageEn AND p.nameEn = :nameEn AND p.funcPart = :funcPart AND p.status = :status")
    BpmProcess findByOrgIdAndProjectIdAndNameEnAndFuncPartAndStatus(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("stageEn") String stageEn,
        @Param("nameEn") String nameEn,
        @Param("funcPart") String funcPart,
        @Param("status") EntityStatus status
    );

    List<BpmProcess> findByNameEnAndStatus(String ndt, EntityStatus status);


    @Query("SELECT bp FROM BpmProcess bp WHERE bp.projectId = :projectId " +
        "AND bp.processStage.nameEn = :stage AND bp.status = 'ACTIVE'")
    List<BpmProcess> findByProjectIdAndProcessStage(@Param("projectId") Long projectId,
                                                    @Param("stage") String stage);

}
