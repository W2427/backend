package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface SubDrawingRepository extends PagingAndSortingWithCrudRepository<SubDrawing, Long>, SubDrawingRepositoryCustom {

    /**
     * 根据ISO图号查找子图纸。
     *
     * @param drawingId
     * @param subNo
     * @param status
     * @return
     */
    SubDrawing findByDrawingIdAndSubNoAndStatus(Long drawingId, String subNo, EntityStatus status);

    /**
     * 取得某流程下的REDMARK子图纸列表。
     *
     * @param projectId  项目 ID
     * @param orgId      工作包 ID
     * @param drawingId  子图纸 ID 集合
     * @param actInstId 子图纸 ID 集合
     * @return 图纸列表
     */
    @Query(
        value = "SELECT * FROM sub_drawing AS sd " +
            "WHERE sd.project_id = :projectId AND sd.org_id = :orgId " +
            "AND sd.drawing_id = :drawingId AND sd.actInstId=:actInstId",
        nativeQuery = true
    )
    List<SubDrawing> findByDrawingIdAndCommentAndActInstIdAndStatus(
        @Param("projectId") Long projectId,
        @Param("orgId") Long orgId,
        @Param("drawingId") Long drawingId,
        @Param("actInstId") Long actInstId
    );

    List<SubDrawing> findByDrawingIdAndSubDrawingNoAndPageNoAndStatus(Long drawingId, String drawingNo, Integer pageNo,
                                                                      EntityStatus status);

    List<SubDrawing> findByOrgIdAndProjectIdAndSubDrawingNoAndPageNoAndStatus(
        Long orgId,
        Long projectId,
        String drawingNo, Integer pageNo, EntityStatus status);

    SubDrawing findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        EntityStatus status);

    SubDrawing findByDrawingIdAndSubDrawingNoAndPageNoAndDrawingDetailIdAndStatus(Long drawingId,
                                                                                  String drawingNo, Integer pageNo, Long drawingDetailId, EntityStatus status);

    List<SubDrawing> findByProjectIdAndSubDrawingNoAndDrawingIdNot(Long projectId, String drawingNo, Long drawingId);


    SubDrawing findByDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersionAndDrawingDetailIdNotAndStatus(
        Long drawingId, String drawingNo, Integer pageNo, String drawingVersion, Long drawingDetailId,
        EntityStatus status);

    List<SubDrawing> findByProjectIdAndSubDrawingNoAndStatus(Long projectId,
                                                             String subDrawingNo, EntityStatus status);

    SubDrawing findByProjectIdAndSubDrawingNoAndPageNoAndSubDrawingVersionAndStatus(
        Long projectId, String drawingNo, Integer pageNo, String drawingVersion, EntityStatus status);

    List<SubDrawing> findByDrawingIdAndStatusAndDrawingDetailId(Long drawingId, EntityStatus status,
                                                                Long drawingDetailId);

    List<SubDrawing> findByOrgIdAndProjectIdAndDrawingIdAndSubDrawingNoAndDrawingDetailIdAndStatus(Long orgId, Long projectId, Long drawingId,
                                                                                                   String subDrawingNo, Long drawingDetailId, EntityStatus status);

    List<SubDrawing> findByOrgIdAndProjectIdAndStatusAndSubDrawingNoLike(Long orgId,
                                                                         Long projectId,
                                                                         EntityStatus status,
                                                                         String subDrawingNo);


    /**
     * 取得子图纸并返回与工作包的关系数据。
     *
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param subDrawings   子图纸 ID 集合
     * @return 任务包-图纸关系列表
     */
    @Query("SELECT"
        + "   new com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation(wp, d, sd)"
        + " FROM"
        + "   com.ose.tasks.entity.taskpackage.TaskPackage wp,"
        + "   SubDrawing AS sd"
        + "   INNER JOIN com.ose.tasks.entity.drawing.Drawing AS d"
        + "     ON d.id = sd.drawingId"
        + "     AND d.status = com.ose.vo.EntityStatus.ACTIVE"
        + "   LEFT OUTER JOIN com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation AS r"
        + "     ON r.taskPackageId = :taskPackageId"
        + "     AND r.subDrawingId = sd.id"
        + "     AND r.pageNo = sd.pageNo"
        + " WHERE wp.projectId = :projectId"
        + "   AND wp.id = :taskPackageId"
        + "   AND sd.projectId = :projectId"
        + "   AND sd.id IN :subDrawings"
        + "   AND sd.status = com.ose.vo.EntityStatus.ACTIVE"
        + "   AND r.id IS NULL"
    )
    List<TaskPackageDrawingRelation> findAndCreateTaskPackageRelations(
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("subDrawings") Collection<Long> subDrawings
    );

    Drawing findByOrgIdAndProjectIdAndDrawingId(Long orgId, Long projectId, Long drawingId);

    /**
     * 取得子图纸文件信息。
     *
     * @param projectId     项目 ID
     * @param subDrawingIDs 图纸 ID 集合
     * @return 子图纸信息
     */
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT sd.id, sd.file_id AS fileId, sd.sub_drawing_version AS subDrawingVersion FROM sub_drawing AS sd WHERE sd.project_id = :projectId AND sd.id IN :subDrawingIDs",
        nativeQuery = true
    )
    List<Map<String, Object>> findFileIdAndVersionByIDs(
        @Param("projectId") Long projectId,
        @Param("subDrawingIDs") Collection<Long> subDrawingIDs
    );

    SubDrawing findByProjectIdAndQrCode(Long projectId, String qrcode);

    List<SubDrawing> findByOrgIdAndProjectIdAndDrawingIdAndActInstId(
        Long orgId,
        Long projectId,
        Long entityId,
        Long actInstId);

    List<SubDrawing> findByProjectIdAndDrawingIdAndIsIssuedAndStatusAndActInstId(
        Long projectId, Long entityId, boolean isIssued, EntityStatus status, Long actInstId);

    /**
     * 查找【图号-页码-版本】相同的子图纸。
     *
     * @param projectId
     * @param drawingId
     * @param subNo
     * @param pageNo
     * @param latestRev
     * @return
     */
    List<SubDrawing> findByProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndLatestRev(
        Long projectId, Long drawingId, String subNo, Integer pageNo, String latestRev);

    /**
     * 找到当前图集中相同【图号-页码】。
     *
     * @param projectId
     * @param drawingId
     * @param subNo
     * @param sheetNo
     * @param status
     * @return
     */
    List<SubDrawing> findByProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndStatus(
        Long projectId, Long drawingId, String subNo, Integer sheetNo, EntityStatus status);

    List<SubDrawing> findByOrgIdAndProjectIdAndStatusAndSubDrawingNo(Long orgId, Long projectId, EntityStatus status, String dwgNo);

    List<SubDrawing> findByOrgIdAndProjectIdAndStatusAndSubDrawingNoAndPageNo(Long orgId,
                                                                              Long projectId,
                                                                              EntityStatus status,
                                                                              String isoName,
                                                                              Integer sheetNo);

    List<SubDrawing> findByOrgIdAndProjectIdAndDrawingIdAndReviewStatusInAndStatusIn(
        Long orgId, Long projectId, Long drawingId, Set<DrawingReviewStatus> reviewStatus, Set<EntityStatus> status);

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE review_status WHEN 'CHECK_DONE' THEN 1 ELSE 0 END) AS checkDoneCount, " +
        "SUM(CASE review_status WHEN 'CHECK' THEN 1 ELSE 0 END) AS checkCount, " +
        "SUM(CASE review_status WHEN 'MODIFY' THEN 1 ELSE 0 END) AS modifyCount " +
        "FROM " +
        "sub_drawing " +
        "WHERE act_inst_id = :actInstId AND org_id = :orgId AND project_id =:projectId AND drawing_id =:drawingId ",
        nativeQuery = true)
    Tuple findCheckStatusCount(@Param("orgId") Long orgId,
                               @Param("projectId") Long projectId,
                               @Param("actInstId") Long actInstId,
                               @Param("drawingId") Long drawingId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE sub_drawing SET status = :status WHERE org_id = :orgId AND project_id = :projectId AND drawing_id = :drawingId " +
        "AND act_inst_id = :actInstId",
        nativeQuery = true)
    void updateRedMarkSubDwgStatus(@Param("orgId") Long orgId,
                                   @Param("projectId") Long projectId,
                                   @Param("drawingId") Long drawingId,
                                   @Param("actInstId") Long actInstId,
                                   @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE sub_drawing SET review_status = :status WHERE org_id = :orgId AND project_id = :projectId AND drawing_id = :drawingId " +
        "AND act_inst_id = :actInstId",
        nativeQuery = true)
    void updateRedMarkSubDwgReviewStatus(@Param("orgId") Long orgId,
                                         @Param("projectId") Long projectId,
                                         @Param("drawingId") Long drawingId,
                                         @Param("actInstId") Long actInstId,
                                         @Param("status") String status);

    @Query(value = "SELECT sd FROM SubDrawing sd WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND " +
        "sd.drawingVersion = :latestRev AND sd.drawingId = :drawingId AND sd.fileId IS NULL AND sd.status=:status")
    List<SubDrawing> findByDrawingVersionAndDrawingIdAndFileIdIsNull(@Param("orgId") Long orgId,
                                                                     @Param("projectId") Long projectId,
                                                                     @Param("latestRev") String latestRev,
                                                                     @Param("drawingId") Long drawingId,
                                                                     @Param("status") EntityStatus status);

    @Query(value = "SELECT sd FROM SubDrawing sd WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND " +
        "sd.drawingVersion = :latestRev AND sd.drawingId = :drawingId  AND sd.status=:status")
    List<SubDrawing> findByDrawingVersionAndDrawingIdAndStatusActive(@Param("orgId") Long orgId,
                                                                     @Param("projectId") Long projectId,
                                                                     @Param("latestRev") String latestRev,
                                                                     @Param("drawingId") Long drawingId,
                                                                     @Param("status") EntityStatus status);


    @Query(value = "SELECT sd FROM SubDrawing sd WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND " +
        "sd.drawingVersion = :latestRev AND sd.drawingId = :drawingId AND sd.actInstId = :actInstId AND sd.fileId IS NULL")
    List<SubDrawing> findByDrawingVersionAndDrawingIdAndAndProcIdAndFileIdIsNull(@Param("orgId") Long orgId,
                                                                                 @Param("projectId") Long projectId,
                                                                                 @Param("actInstId") Long actInstId,
                                                                                 @Param("latestRev") String latestRev,
                                                                                 @Param("drawingId") Long drawingId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SubDrawing sd SET sd.status = :status WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND sd.drawingId = :drawingId AND sd.actInstId = :actInstId " +
        "AND sd.drawingVersion = :latestRev AND sd.status = com.ose.vo.EntityStatus.PENDING")
    void updateStatusByDrawingIdAndActInstIdAndDrawingVersion(@Param("orgId") Long orgId,
                                                               @Param("projectId") Long projectId,
                                                               @Param("status") EntityStatus status,
                                                               @Param("drawingId") Long drawingId,
                                                               @Param("actInstId") Long actInstId,
                                                               @Param("latestRev") String latestRev);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SubDrawing sd SET sd.reviewStatus = :reviewStatus WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND sd.drawingId = :drawingId AND sd.actInstId = :actInstId " +
        "AND sd.drawingVersion = :latestRev AND sd.status = com.ose.vo.EntityStatus.PENDING")
    void updateReviewStatusByDrawingIdAndActInstIdAndDrawingVersion(@Param("orgId") Long orgId,
                                                                     @Param("projectId") Long projectId,
                                                                     @Param("reviewStatus") DrawingReviewStatus reviewStatus,
                                                                     @Param("drawingId") Long drawingId,
                                                                     @Param("actInstId") Long actInstId,
                                                                     @Param("latestRev") String latestRev);

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE review_status WHEN 'CHECK_DONE' THEN 1 ELSE 0 END) AS checkDoneCount, " +
        "SUM(CASE review_status WHEN 'CHECK' THEN 1 ELSE 0 END) AS checkCount, " +
        "SUM(CASE review_status WHEN 'MODIFY' THEN 1 ELSE 0 END) AS modifyCount " +
        "FROM " +
        "sub_drawing " +
        "WHERE drawing_id = :drawingId AND org_id = :orgId AND project_id =:projectId AND drawing_version = :latestRev",
        nativeQuery = true)
    Tuple findNodeCheckStatusCount(@Param("orgId") Long orgId,
                                   @Param("projectId") Long projectId,
                                   @Param("drawingId") Long drawingId,
                                   @Param("latestRev") String latestRev);

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE review_status WHEN 'REVIEW_DONE' THEN 1 ELSE 0 END) AS reviewDoneCount, " +
        "SUM(CASE review_status WHEN 'REVIEW' THEN 1 ELSE 0 END) AS reviewCount " +
        "FROM " +
        "sub_drawing " +
        "WHERE drawing_id = :drawingId AND org_id = :orgId AND project_id =:projectId AND drawing_version = :latestRev",
        nativeQuery = true)
    Tuple findNodeReviewStatusCount(@Param("orgId") Long orgId,
                                    @Param("projectId") Long projectId,
                                    @Param("drawingId") Long drawingId,
                                    @Param("latestRev") String latestRev);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SubDrawing sd SET sd.reviewStatus = :reviewStatus WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND sd.drawingId = :drawingId " +
        "AND sd.drawingVersion = :latestRev AND sd.status = com.ose.vo.EntityStatus.PENDING AND sd.reviewStatus IN :inStatus")
    void updateReviewStatusByDrawingIdAndDrawingVersion(@Param("orgId") Long orgId,
                                                        @Param("projectId") Long projectId,
                                                        @Param("reviewStatus") DrawingReviewStatus reviewStatus,
                                                        @Param("drawingId") Long drawingId,
                                                        @Param("latestRev") String latestRev,
                                                        @Param("inStatus") Set<DrawingReviewStatus> inStatus);

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE review_status WHEN 'MODIFY' THEN 1 ELSE 0 END) AS modifyCount " +
        "FROM " +
        "sub_drawing " +
        "WHERE drawing_id = :drawingId AND org_id = :orgId AND project_id =:projectId AND drawing_version = :latestRev",
        nativeQuery = true)
    Tuple findNodeModifyStatusCount(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("drawingId") Long id,
        @Param("latestRev") String latestRev);

    @Query(value = "SELECT sub_no FROM sub_drawing WHERE org_id = :orgId AND project_id = :projectId AND act_inst_id = :actInstId  AND status = :status AND drawing_id = :drawingId",
        nativeQuery = true)
    Set<String> findByOrgIdAndProjectIdAndActInstIdAndStatusAndDrawingId(@Param("orgId") Long orgId,
                                                                          @Param("projectId") Long projectId,
                                                                          @Param("actInstId") Long actInstId,
                                                                          @Param("status") String status,
                                                                          @Param("drawingId") Long drawingId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SubDrawing sd SET sd.status = :updatedStatus " +
        "WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND sd.drawingId = :drawingId " +
        "AND sd.subNo IN :subNos AND sd.status = :oldStatus")
    void updateStatusDeletedByDrawingIdAndSubDrawingNoInAndStatus(@Param("orgId") Long orgId,
                                                                  @Param("projectId") Long projectId,
                                                                  @Param("updatedStatus") EntityStatus updatedStatus,
                                                                  @Param("drawingId") Long drawingId,
                                                                  @Param("subNos") Set<String> subNos,
                                                                  @Param("oldStatus") EntityStatus oldStatus);

    /**
     * 查找图纸下所有有效的子图纸。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param status
     * @return
     */
    List<SubDrawing> findByOrgIdAndProjectIdAndDrawingIdAndStatus(
        Long orgId,
        Long projectId,
        Long drawingId,
        EntityStatus status);

    SubDrawing findByOrgIdAndProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndStatus(
        Long orgId,
        Long projectId,
        Long drawingId,
        String subDrawingNo,
        Integer pageNo,
        EntityStatus status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE SubDrawing sd SET sd.status = :status WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND sd.drawingId = :drawingId " +
        " AND sd.status = com.ose.vo.EntityStatus.ACTIVE")
    void updateStatusByDrawingId(@Param("orgId") Long orgId,
                                 @Param("projectId") Long projectId,
                                 @Param("status") EntityStatus status,
                                 @Param("drawingId") Long drawingId);

    @Modifying
    @Transactional
    @Query("UPDATE SubDrawing sd SET sd.status = com.ose.vo.EntityStatus.DELETED, sd.reviewStatus = com.ose.tasks.vo.drawing.DrawingReviewStatus.INIT WHERE sd.orgId = :orgId AND sd.projectId = :projectId AND sd.drawingId = :drawingId AND sd.actInstId = :actInstId ")
    void deleteSubDrawing(@Param("orgId") Long orgId,
                          @Param("projectId") Long projectId,
                          @Param("drawingId") Long drawingId,
                          @Param("actInstId") Long actInstId);

    /**
     * 查找图纸下所有正在进行redmark的子图纸。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param status
     * @return
     */
    List<SubDrawing> findByOrgIdAndProjectIdAndDrawingIdAndIsRedMarkAndStatusNot(
        Long orgId,
        Long projectId,
        Long drawingId,
        EntityStatus isRedMark,
        EntityStatus status);

    List<SubDrawing> findByProjectIdAndStatus(Long projectId, EntityStatus status);

    List<SubDrawing> findByProjectIdAndQrCodeStatus(Long projectId, String qrCodeStatus);

    List<SubDrawing> findByProjectIdAndStatusAndQrCodeStatusIsNull(Long projectId, EntityStatus status);
}
