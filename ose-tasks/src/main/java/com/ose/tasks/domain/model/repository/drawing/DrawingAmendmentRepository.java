package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendment;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingAmendmentRepository extends PagingAndSortingWithCrudRepository<DrawingAmendment, Long>, DrawingAmendmentRepositoryCustom {



    Optional<DrawingAmendment> findByOrgIdAndProjectIdAndNoAndStatus(Long orgId, Long projectId, String no,
                                                                     EntityStatus status);

    List<DrawingAmendment> findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id,
                                                                 EntityStatus status);

//    List<DrawingAmendment> findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus entityStatus);

    @Query("SELECT distinct m.areaName FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findAreaNameByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.moduleName FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findModuleNameByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.workNet FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findWorkNetByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.section FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findSectionByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.block FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findBlockByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.smallArea FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findSmallAreaByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.designDiscipline FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findDesignDisciplineByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);

    @Query("SELECT distinct m.latestRev FROM DrawingAmendment m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
    List<DrawingAmendment> findLatestRevByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId);
//    /**
//     * 导出图纸目录
//     *
//     * @param orgId     组织ID
//     * @param projectId 项目ID
//     * @return
//     */

//    @Query(value = "select " +
//        "c.id as id, " +
//        "        e.created_at as created_at, " +
//        "        e.last_modified_at as last_modified_at, " +
//        "        e.status as status, " +
//        "        e.actural_drawing_issue_date as actural_drawing_issue_date, " +
//        "        e.audit_no as audit_no, " +
//        "        c.batch_task_id as batch_task_id, " +
//        "        e.change_notice_no as change_notice_no, " +
//        "        e.delivery_date as delivery_date, " +
//        "        e.design_change_review_form as design_change_review_form, " +
//        "        c.drawing_title as drawing_title, " +
//        "        c.dwg_no as dwg_no, " +
//        "        c.import_file_id as importFileId, " +
//        "        e.issue_card_no as issue_card_no, " +
//        "        c.latest_rev as latest_rev, " +
//        "        c.org_id as org_id, " +
//        "        ifnull(e.paper_amount,0) as paper_amount, " +
//        "        ifnull(e.paper_use,0) as paper_use, " +
//        "        ifnull(e.printing,0) as printing, " +
//        "        e.production_receiving_date as production_receiving_date, " +
//        "        c.project_id as project_id, " +
//        "        ifnull(e.quantity,0) as quantity, " +
//        "        e.return_record as return_record, " +
//        "        e.rev as rev, " +
//        "        c.sequence_no as sequence_no, " +
//        "        e.file_id as file_id, " +
//        "        c.file_last_version as file_last_version, " +
//        "        e.file_name as file_name, " +
//        "        c.file_page_count as file_page_count, " +
//        "        e.file_path as file_path, " +
//        "        '' as import_file_id, " +
//        "        e.qr_code as qr_code, " +
//        "        c.drawing_category_id as drawing_category_id, " +
//        "        c.operator as operator, " +
//        "        c.locked as locked, " +
//        "        c.cover_id as cover_id, " +
//        "        c.cover_path as cover_path, " +
//        "        c.cover_name as cover_name, " +
//        "        c.drawer as drawer, " +
//        "        c.drawer_id as drawer_id, " +
//        "        c.engineering_finish_date as engineering_finish_date, " +
//        "        c.engineering_start_date as engineering_start_date " +
//        "from drawing c " +
//        "left join drawing_detail e on e.drawing_id = c.id " +
//        "where project_id = :projectId and org_id = :orgId and c.status = 'ACTIVE' " +
//        " order by c.dwg_no",
//        nativeQuery = true
//    )
//    List<Drawing> getXlsxList(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

//    @Query("SELECT distinct m.moduleName FROM Drawing m WHERE m.projectId = :projectId and m.orgId = :orgId and m.status = 'ACTIVE'")
//    List<Drawing> findByOrgId(Long orgId, Long projectId);

}
