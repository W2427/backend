package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingRepository extends PagingAndSortingRepository<Drawing, Long>, DrawingRepositoryCustom,
    WBSEntityBaseRepository<Drawing> {

    List<Drawing> findByBatchTaskId(Long batchTaskId);

    Drawing findByQrCode(String qrCode);

    @Modifying
    @Transactional
    @Query("update Drawing pi set pi.importFileId =:fileId where pi in(:list)")
    void updateFileIdIn(@Param("fileId") Long fileId, @Param("list") List<Drawing> list);

    Optional<Drawing> findByOrgIdAndProjectIdAndDwgNoAndStatus(Long orgId, Long projectId, String dwgNo,
                                                               EntityStatus status);

//    Optional<Drawing> findByOrgIdAndProjectIdAndDwgNoAndStatus(Long orgId, Long projectId, String docNo,
//                                                                     EntityStatus status);

    List<Drawing> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus entityStatus);

    List<Drawing> findByOrgIdAndProjectId(Long orgId, Long projectId);

    List<Drawing> findByProjectId(Long projectId);

    @Query(value = " SELECT " +
        "d.*, MAX(dr.created_at) as last_created_at " +
        "FROM " +
        "drawing d " +
        "LEFT JOIN drawing_record dr on d.id= dr.drawing_id  and dr.engineer_id = :engineerId " +
        "WHERE " +
        "d.`status` = 'ACTIVE' " +
        "AND d.org_id = :orgId " +
        "AND d.project_id = :projectId " +
        "GROUP BY " +
        "d.id " +
        "ORDER BY " +
        "last_created_at DESC  ",
        nativeQuery = true
    )
    List<Drawing> findAllByOrgIdAndProjectIdAndDesc(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("engineerId") Long engineerId
    );

    @Query(value = " SELECT " +
        "  discipline " +
        "FROM " +
        "  `drawing` " +
        "WHERE " +
        "  org_id = :orgId " +
        "  AND project_id = :projectId " +
        "GROUP BY " +
        "  discipline " +
        "ORDER BY " +
        "  discipline ASC  ",
        nativeQuery = true
    )
    List<String> findDisciplinesByOrgIdAndProjectId(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId
    );

    @Query(value = "SELECT  " +
        "  doc_type  " +
        "FROM  " +
        "  `drawing`  " +
        "WHERE  " +
        "  doc_type IS NOT NULL  " +
        "  AND org_id = :orgId  " +
        "  AND project_id = :projectId  " +
        "GROUP BY  " +
        "  doc_type  " +
        "ORDER BY  " +
        "  doc_type ASC",
        nativeQuery = true
    )
    List<String> findDocTypesByOrgIdAndProjectId(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId
    );

    @Query(value = " SELECT " +
        "  d.* " +
        "FROM " +
        "  bpm_activity_instance_base bai " +
        "  LEFT JOIN bpm_activity_instance_state bais ON bais.bai_id = bai.id " +
        "  LEFT JOIN bpm_act_task_assignee bata ON bata.act_inst_id = bai.id  " +
        "  LEFT JOIN drawing d ON d.id = bai.entity_id  " +
        "WHERE " +
        "  bai.project_id = :projectId  " +
        "  AND bai.act_category = 'Design' " +
        "  AND bais.finish_state IS NOT NULL " +
        "  AND bata.assignee = :userId " +
        "GROUP BY bai.entity_id  ",
        nativeQuery = true
    )
    List<Drawing> findByProjectIdAndUserId(
        @Param("projectId") Long projectId,
        @Param("userId") Long userId
    );

    @Query(value = " SELECT " +
        "  *  " +
        "FROM " +
        "  `drawing` " +
        "WHERE " +
        "  deleted IS FALSE " +
        "  AND plan_hours IS NOT NULL " +
        "  AND engineering_start_date IS NOT NULL " +
        "  AND engineering_finish_date IS NOT NULL ",
        nativeQuery = true
    )
    List<Drawing> findAllPlanHourDrawing();

    Optional<Drawing> findAllByOrgIdAndProjectIdAndDocumentTitleAndDwgNoAndStatus(Long orgId, Long projectId, String documentTitle, String drawingNo, EntityStatus entityStatus);

    Drawing findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long drawingId);

    @Query("SELECT distinct best FROM Drawing m JOIN BpmEntitySubType best ON best.projectId = m.projectId " +
        "AND best.nameEn = m.entitySubType  WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<BpmEntitySubType> findDrawingCategoryList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    /**
     * 取得子图纸并返回与任务包的关系数据。
     *
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param drawings      图纸 ID 的集合
     * @return 任务包-图纸关系列表
     */
    @Query("SELECT"
        + "   new com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation(wp, d)"
        + " FROM"
        + "   com.ose.tasks.entity.taskpackage.TaskPackage wp,"
        + "   Drawing AS d"
        + "   LEFT OUTER JOIN com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation AS r"
        + "     ON r.taskPackageId = :taskPackageId"
        + "     AND r.drawingId = d.id"
        + " WHERE wp.projectId = :projectId"
        + "   AND wp.id = :taskPackageId"
        + "   AND d.projectId = :projectId"
        + "   AND d.id IN :drawings"
        + "   AND d.status = 'ACTIVE'"
        + "   AND r.id IS NULL"
    )
    List<TaskPackageDrawingRelation> findAndCreateTaskPackageRelations(
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("drawings") Collection<Long> drawings
    );


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
        "from drawing c " +
        "left join drawing_detail e on e.drawing_id = c.id " +
        "where project_id = :projectId and org_id = :orgId and c.status = 'ACTIVE' " +
        " order by c.dwg_no",
        nativeQuery = true
    )
    List<Drawing> getXlsxList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Modifying
    @Transactional
    @Query("update Drawing dwg set dwg.isRedMarkOnGoing =:redMarkStatus where dwg.orgId = :orgId AND dwg.projectId = :projectId AND dwg.id = :dwgId")
    void updateRedMarkStatus(@Param("orgId") Long orgId,
                             @Param("projectId") Long projectId,
                             @Param("dwgId") Long dwgId,
                             @Param("redMarkStatus") boolean redMarkStatus);


    @Query(value = "select " +
        " distinct c.discipline " +
        "from drawing c " +
        "where project_id = :projectId and c.status = 'ACTIVE' " +
        " order by c.discipline",
        nativeQuery = true
    )
    List<String> findByProjectIdGroupByDiscipline(Long projectId);

    List<Drawing> findByProjectIdAndDisciplineAndStatus(Long projectId, String discipline, EntityStatus entityStatus);


    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT"
            + "   o.id AS departmentId, "
            + "   o.name AS departmentName "
            + " FROM"
            + "   saint_whale_auth.organizations AS o "
            + " WHERE"
            + "   o.path like :orgId "
            + " AND  o.type = 'PROJECT' "
            + " AND  o.deleted is false "
            + " AND  o.idc is true ",
        nativeQuery = true
    )
    List<Map<String, Object>> findOrgInfoByOrgId(
        @Param("orgId") String orgId
    );

    @Query(
        value = "SELECT"
            + "   u.id, "
            + "   u.name "
            + " FROM"
            + "   saint_whale_auth.user_organization_relations AS uo "
            + " LEFT JOIN saint_whale_auth.users AS u ON uo.user_id = u.id "
            + " WHERE"
            + "   uo.organization_id = :orgId "
            + " AND  uo.deleted is false "
            + " AND  uo.idc is true ",
        nativeQuery = true
    )
    Map<String, Object> findUserInfoByOrgId(
        @Param("orgId") String orgId
    );

    Drawing findByOrgIdAndProjectIdAndDisplayNameAndStatus(Long orgId, Long projectId, String displayName, EntityStatus entityStatus);

}
