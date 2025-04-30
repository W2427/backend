package com.ose.tasks.domain.model.repository.drawing.externalDrawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawing;
import com.ose.vo.EntityStatus;
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
public interface ExternalDrawingRepository extends PagingAndSortingWithCrudRepository<ExternalDrawing, Long>, ExternalDrawingRepositoryCustom {

    List<ExternalDrawing> findByBatchTaskId(Long batchTaskId);

    boolean existsByDwgNoAndStatus(String dwgNo, EntityStatus status);

    ExternalDrawing findByQrCode(String qrCode);

    @Modifying
    @Query("update ExternalDrawing pi set pi.importFileId =:fileId where pi in(:list)")
    void updateFileIdIn(@Param("fileId") Long fileId, @Param("list") List<ExternalDrawing> list);

    Optional<ExternalDrawing> findByOrgIdAndProjectIdAndDwgNoAndStatus(Long orgId, Long projectId, String dwgNo,
                                                                       EntityStatus status);

    List<ExternalDrawing> findByOrgIdAndProjectId(Long orgId, Long projectId);

    List<ExternalDrawing> findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus entityStatus);

    @Query("SELECT distinct m.areaName FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.moduleName FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findByOrgId(Long orgId, Long projectId);

    @Query("SELECT distinct m.workNet FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findWorkNetByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.section FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findSectionByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.block FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findBlockByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.smallArea FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findSmallAreaByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.designDiscipline FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findDesignDisciplineByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.latestRev FROM ExternalDrawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<ExternalDrawing> findLatestRevByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    ExternalDrawing findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long drawingId);

    @Query("SELECT distinct best FROM ExternalDrawing m JOIN BpmEntitySubType best ON best.projectId = m.projectId AND best.nameEn = m.entitySubType " +
        " WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<BpmEntitySubType> findDrawingCategoryList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

//    /**
//     * 取得子图纸并返回与任务包的关系数据。
//     *
//     * @param projectId     项目 ID
//     * @param taskPackageId 任务包 ID
//     * @param drawings      图纸 ID 的集合
//     * @return 任务包-图纸关系列表
//     */
//    @Query("SELECT"
//        + "   new TaskPackageDrawingRelation(wp, d)"
//        + " FROM"
//        + "   TaskPackage wp,"
//        + "   ExternalDrawing AS d"
//        + "   LEFT OUTER JOIN TaskPackageDrawingRelation AS r"
//        + "     ON r.taskPackageId = :taskPackageId"
//        + "     AND r.drawingId = d.id"
//        + " WHERE wp.projectId = :projectId"
//        + "   AND wp.id = :taskPackageId"
//        + "   AND d.projectId = :projectId"
//        + "   AND d.id IN :drawings"
//        + "   AND d.status = 'ACTIVE'"
//        + "   AND r.id IS NULL"
//    )
//    List<TaskPackageDrawingRelation> findAndCreateTaskPackageRelations(
//        @Param("projectId") Long projectId,
//        @Param("taskPackageId") Long taskPackageId,
//        @Param("drawings") Collection<Long> drawings
//    );


    /**
     * 导出图纸目录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */

    @Query(value = "select " +
        "c.id as id, " +
        "        e.created_at as created_at, " +
        "        e.last_modified_at as last_modified_at, " +
        "        e.status as status, " +
        "        e.actural_drawing_issue_date as actural_drawing_issue_date, " +
        "        e.audit_no as audit_no, " +
        "        c.batch_task_id as batch_task_id, " +
        "        e.change_notice_no as change_notice_no, " +
        "        e.delivery_date as delivery_date, " +
        "        e.design_change_review_form as design_change_review_form, " +
        "        c.drawing_title as drawing_title, " +
        "        c.dwg_no as dwg_no, " +
        "        c.import_file_id as importFileId, " +
        "        e.issue_card_no as issue_card_no, " +
        "        c.latest_rev as latest_rev, " +
        "        c.org_id as org_id, " +
        "        ifnull(e.paper_amount,0) as paper_amount, " +
        "        ifnull(e.paper_use,0) as paper_use, " +
        "        ifnull(e.printing,0) as printing, " +
        "        e.production_receiving_date as production_receiving_date, " +
        "        c.project_id as project_id, " +
        "        ifnull(e.quantity,0) as quantity, " +
        "        e.return_record as return_record, " +
        "        e.rev as rev, " +
        "        c.sequence_no as sequence_no, " +
        "        e.file_id as file_id, " +
        "        c.file_last_version as file_last_version, " +
        "        e.file_name as file_name, " +
        "        c.file_page_count as file_page_count, " +
        "        e.file_path as file_path, " +
        "        '' as import_file_id, " +
        "        e.qr_code as qr_code, " +
        "        c.drawing_category_id as drawing_category_id, " +
        "        c.operator as operator, " +
        "        c.locked as locked, " +
        "        c.cover_id as cover_id, " +
        "        c.cover_path as cover_path, " +
        "        c.cover_name as cover_name, " +
        "        c.drawer as drawer, " +
        "        c.drawer_id as drawer_id, " +
        "        c.engineering_finish_date as engineering_finish_date, " +
        "        c.engineering_start_date as engineering_start_date " +
        "from external_drawing c " +
        "left join drawing_detail e on e.drawing_id = c.id " +
        "where project_id = :projectId and org_id = :orgId and c.status = 'ACTIVE' " +
        " order by c.dwg_no",
        nativeQuery = true
    )
    List<ExternalDrawing> getXlsxList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Modifying
    @Query("update ExternalDrawing dwg set dwg.isRedMarkOnGoing =:redMarkStatus where dwg.orgId = :orgId AND dwg.projectId = :projectId AND dwg.id = :dwgId")
    void updateRedMarkStatus(@Param("orgId") Long orgId,
                             @Param("projectId") Long projectId,
                             @Param("dwgId") Long dwgId,
                             @Param("redMarkStatus") boolean redMarkStatus);
}
