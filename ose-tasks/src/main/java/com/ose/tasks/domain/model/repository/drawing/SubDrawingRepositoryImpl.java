package com.ose.tasks.domain.model.repository.drawing;

import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.ose.tasks.dto.drawing.ProofreadDrawingListCriteriaDTO;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.SubDrawingCriteriaDTO;
import com.ose.vo.EntityStatus;

/**
 * 用户查询。
 */
public class SubDrawingRepositoryImpl extends BaseRepository implements SubDrawingRepositoryCustom {

    @Override
    public List<SubDrawing> findByDrawingIdAndStatusAndFilePathNotNull(Long drawingId, EntityStatus status) {
        SQLQueryBuilder<SubDrawing> builder = getSQLQueryBuilder(SubDrawing.class)
            .is("drawingId", drawingId).is("status", EntityStatus.ACTIVE).isNotNull("filePath");

        builder.sort(new Sort.Order(Sort.Direction.ASC, "subDrawingNo"));
        builder.sort(new Sort.Order(Sort.Direction.ASC, "pageNo"));

        List<SubDrawing> subPipings = builder.limit(Integer.MAX_VALUE).exec().list();
        return subPipings;
    }

    @Override
    public Page<SubDrawing> search(Long orgId, Long projectId, Long drawingId, PageDTO page,
                                   SubDrawingCriteriaDTO criteriaDTO) {
        SQLQueryBuilder<SubDrawing> builder = getSQLQueryBuilder(SubDrawing.class).is("orgId", orgId)
            .is("projectId", projectId).is("drawingId", drawingId);
        if (criteriaDTO.getKeyword() != null &&
            !criteriaDTO.getKeyword().equals("")) {
            builder.like("subNo", criteriaDTO.getKeyword());
        }
        if (criteriaDTO.getStatus() != null) {
            builder.is("status", criteriaDTO.getStatus());
        }

        builder.sort(new Sort.Order(Sort.Direction.ASC, "seq"));


        return builder.paginate(page.toPageable()).exec().page();
    }

    /**
     * 查找图纸版本下的运行中的子图纸。
     *
     * @param id
     * @param status
     * @param revOrder
     * @return
     */
    @Override
    public List<SubDrawing> findByDrawingIdAndStatusAndRevOrderLE(Long id, EntityStatus status,
                                                                  Integer revOrder) {

        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append(" SELECT ds.* FROM sub_drawing ds")
            .append(" LEFT JOIN drawing_detail dd ON dd.id = ds.drawing_detail_id")
            .append(" WHERE ds.drawing_id = :drawingId")
            .append(" AND dd.rev_order <= :revOrder");

        if (status != null) {
            sql.append(" AND ds.status = :status");
        }

        sql.append(" ORDER BY dd.rev_order, ds.seq ASC");

        Query query = entityManager.createNativeQuery(sql.toString(), SubDrawing.class);
        query.setParameter("drawingId", id);
        query.setParameter("revOrder", revOrder);
        if (status != null) {
            query.setParameter("status", "ACTIVE");
        }

        List<SubDrawing> result = query.getResultList();
        return result;
    }

    /**
     * 查询图纸下的最新子图纸。
     *
     * @param id
     * @param revOrder
     * @return
     */
    @Override
    public List<SubDrawing> findByDrawingIdAndRevOrderLE(Long id, Integer revOrder) {

        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append(" SELECT ds.* FROM sub_drawing ds")
            .append(" LEFT JOIN drawing_detail dd ON dd.id = ds.drawing_detail_id")
            .append(" WHERE ds.drawing_id = :drawingId")
            .append(" AND dd.rev_order <= :revOrder");
        sql.append(" AND ( ds.status = 'ACTIVE' || ds.status = 'APPROVED' )");
        sql.append(" ORDER BY dd.rev_order, ds.seq ASC");

        Query query = entityManager.createNativeQuery(sql.toString(), SubDrawing.class);
        query.setParameter("drawingId", id);
        query.setParameter("revOrder", revOrder);

        List<SubDrawing> result = query.getResultList();
        return result;
    }

    @Override
    public Page<SubDrawing> search(Long orgId, Long projectId, Long drawingId,
                                   SubDrawingCriteriaDTO criteriaDTO, List<Long> subIds, Pageable pageable) {
        SQLQueryBuilder<SubDrawing> builder = getSQLQueryBuilder(SubDrawing.class).is("orgId", orgId)
            .is("projectId", projectId).is("drawingId", drawingId).in("id", subIds);
        if (criteriaDTO.getKeyword() != null
            && !criteriaDTO.getKeyword().equals("")) {
            builder.like("subNo", criteriaDTO.getKeyword());
        }
        if (criteriaDTO.getDrawingDetailId() != null
            && !criteriaDTO.getDrawingDetailId().equals("")) {
            builder.is("drawingDetailId", criteriaDTO.getDrawingDetailId());
        }

        if (criteriaDTO.getStatus() != null) {
            builder.is("status", criteriaDTO.getStatus());
        }

        if (criteriaDTO.getReviewStatus() != null) {
            builder.is("reviewStatus", criteriaDTO.getReviewStatus());
        }

        if (criteriaDTO.getProofreadType() != null) {

            Map<String, Object> operatorInit = new IdentityHashMap<>();
            operatorInit.put("$is", DrawingReviewStatus.INIT);
            Map<String, Object> operatorModify = new IdentityHashMap<>();
            operatorModify.put("$is", DrawingReviewStatus.MODIFY);
            Map<String, Object> operatorCheck = new IdentityHashMap<>();
            operatorCheck.put("$is", DrawingReviewStatus.CHECK);
            Map<String, Object> operatorReview = new IdentityHashMap<>();
            operatorReview.put("$is", DrawingReviewStatus.REVIEW);
            Map<String, Object> operatorCheckDone = new IdentityHashMap<>();
            operatorCheckDone.put("$is", DrawingReviewStatus.CHECK_DONE);
            Map<String, Object> operatorReviewDone = new IdentityHashMap<>();
            operatorReviewDone.put("$is", DrawingReviewStatus.REVIEW_DONE);

            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();

            if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.MODIFY) {
                keywordCriteria.put(new String("reviewStatus"), operatorInit);
                keywordCriteria.put(new String("reviewStatus"), operatorModify);
                keywordCriteria.put(new String("reviewStatus"), operatorCheckDone);
                keywordCriteria.put(new String("reviewStatus"), operatorReviewDone);
            } else if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.CHECK) {
                keywordCriteria.put(new String("reviewStatus"), operatorCheck);
                keywordCriteria.put(new String("reviewStatus"), operatorCheckDone);
            } else if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.REVIEW) {
                keywordCriteria.put(new String("reviewStatus"), operatorReview);
                keywordCriteria.put(new String("reviewStatus"), operatorReviewDone);
            }

            builder.orObj(keywordCriteria);
        }

        if (criteriaDTO.isOrderByNo()) {
            builder.sort(new Sort.Order(Sort.Direction.ASC, "subDrawingNo"));
            builder.sort(new Sort.Order(Sort.Direction.ASC, "pageNo"));
        } else {
            builder.sort(new Sort.Order(Sort.Direction.ASC, "seq"));
        }

        return builder.paginate(pageable).exec().page();
    }


    /**
     * 查找最新图集下子图纸。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param criteriaDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SubDrawing> searchLatestSubDrawing(Long orgId, Long projectId, Long drawingId,
                                                   SubDrawingCriteriaDTO criteriaDTO, Pageable pageable) {
        SQLQueryBuilder<SubDrawing> builder = getSQLQueryBuilder(SubDrawing.class).is("orgId", orgId)
            .is("projectId", projectId).is("drawingId", drawingId);

        if (criteriaDTO.getDrawingVersion() != null
            && !criteriaDTO.getDrawingVersion().equals("")) {
            builder.is("drawingVersion", criteriaDTO.getDrawingVersion());
        }

        if (criteriaDTO.getKeyword() != null
            && !criteriaDTO.getKeyword().equals("")) {
            builder.like("subNo", criteriaDTO.getKeyword());
        }
        if (criteriaDTO.getDrawingDetailId() != null
            && !criteriaDTO.getDrawingDetailId().equals(0L)) {
            builder.is("drawingDetailId", criteriaDTO.getDrawingDetailId());
        }

        if (criteriaDTO.getStatus() != null) {
            builder.is("status", criteriaDTO.getStatus());
        }

        if (criteriaDTO.getIsRedMark() != null) {
            builder.is("isRedMark", criteriaDTO.getIsRedMark());
        }

        if (criteriaDTO.getReviewStatus() != null) {
            builder.is("reviewStatus", criteriaDTO.getReviewStatus());
        }

        if(criteriaDTO.getActInstId() != null) {
            builder.is("actInstId", criteriaDTO.getActInstId());
        }

        if (criteriaDTO.getProofreadType() != null) {

            Map<String, Object> operatorInit = new IdentityHashMap<>();
            operatorInit.put("$is", DrawingReviewStatus.INIT);
            Map<String, Object> operatorModify = new IdentityHashMap<>();
            operatorModify.put("$is", DrawingReviewStatus.MODIFY);
            Map<String, Object> operatorCheck = new IdentityHashMap<>();
            operatorCheck.put("$is", DrawingReviewStatus.CHECK);
            Map<String, Object> operatorReview = new IdentityHashMap<>();
            operatorReview.put("$is", DrawingReviewStatus.REVIEW);
            Map<String, Object> operatorCheckDone = new IdentityHashMap<>();
            operatorCheckDone.put("$is", DrawingReviewStatus.CHECK_DONE);
            Map<String, Object> operatorReviewDone = new IdentityHashMap<>();
            operatorReviewDone.put("$is", DrawingReviewStatus.REVIEW_DONE);

            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();

            if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.MODIFY) {
                keywordCriteria.put(new String("reviewStatus"), operatorInit);
                keywordCriteria.put(new String("reviewStatus"), operatorModify);
                keywordCriteria.put(new String("reviewStatus"), operatorCheckDone);
                keywordCriteria.put(new String("reviewStatus"), operatorReviewDone);
            } else if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.CHECK) {
                keywordCriteria.put(new String("reviewStatus"), operatorCheck);
                keywordCriteria.put(new String("reviewStatus"), operatorCheckDone);
            } else if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.REVIEW) {
                keywordCriteria.put(new String("reviewStatus"), operatorReview);
                keywordCriteria.put(new String("reviewStatus"), operatorReviewDone);
            }

            builder.orObj(keywordCriteria);
        }

        if (criteriaDTO.isOrderByNo()) {
            builder.sort(new Sort.Order(Sort.Direction.ASC, "subDrawingNo"));
            builder.sort(new Sort.Order(Sort.Direction.ASC, "pageNo"));
        } else {
            builder.sort(new Sort.Order(Sort.Direction.ASC, "seq"));
        }

        return builder.paginate(pageable).exec().page();
    }

    /**
     * 查找所有图集下子图纸。
     *
     * @param orgId
     * @param projectId
     * @param criteriaDTO
     * @param pageable
     * @return
     */
    @Override
    public Page<SubDrawing> searchAllSubDrawing(Long orgId, Long projectId,
                                                   SubDrawingCriteriaDTO criteriaDTO, Pageable pageable) {
        SQLQueryBuilder<SubDrawing> builder = getSQLQueryBuilder(SubDrawing.class).is("orgId", orgId)
            .is("projectId", projectId);

        if (criteriaDTO.getDrawingVersion() != null
            && !criteriaDTO.getDrawingVersion().equals("")) {
            builder.is("drawingVersion", criteriaDTO.getDrawingVersion());
        }

        if (criteriaDTO.getKeyword() != null
            && !criteriaDTO.getKeyword().equals("")) {
            builder.like("subNo", criteriaDTO.getKeyword());
        }
        if (criteriaDTO.getDrawingDetailId() != null
            && !criteriaDTO.getDrawingDetailId().equals(0L)) {
            builder.is("drawingDetailId", criteriaDTO.getDrawingDetailId());
        }

        List<EntityStatus> statusList = new ArrayList<>();
        statusList.add(EntityStatus.DELETED);
        statusList.add(EntityStatus.APPROVED);
        statusList.add(EntityStatus.DISABLED);
        statusList.add(EntityStatus.REJECTED);
        statusList.add(EntityStatus.PENDING);
        statusList.add(EntityStatus.FINISHED);
        statusList.add(EntityStatus.SKIPPED);
        statusList.add(EntityStatus.CLOSED);
        statusList.add(EntityStatus.SUSPEND);
        statusList.add(EntityStatus.ACTIVE);
        if (criteriaDTO.getStatus() != null) {
            builder.is("status", criteriaDTO.getStatus());
        }else {
            builder.in("status", statusList);
        }

        if (criteriaDTO.getReviewStatus() != null) {
            builder.is("reviewStatus", criteriaDTO.getReviewStatus());
        }

        if(criteriaDTO.getActInstId() != null) {
            builder.is("actInstId", criteriaDTO.getActInstId());
        }

        if (criteriaDTO.getProofreadType() != null) {

            Map<String, Object> operatorInit = new IdentityHashMap<>();
            operatorInit.put("$is", DrawingReviewStatus.INIT);
            Map<String, Object> operatorModify = new IdentityHashMap<>();
            operatorModify.put("$is", DrawingReviewStatus.MODIFY);
            Map<String, Object> operatorCheck = new IdentityHashMap<>();
            operatorCheck.put("$is", DrawingReviewStatus.CHECK);
            Map<String, Object> operatorReview = new IdentityHashMap<>();
            operatorReview.put("$is", DrawingReviewStatus.REVIEW);
            Map<String, Object> operatorCheckDone = new IdentityHashMap<>();
            operatorCheckDone.put("$is", DrawingReviewStatus.CHECK_DONE);
            Map<String, Object> operatorReviewDone = new IdentityHashMap<>();
            operatorReviewDone.put("$is", DrawingReviewStatus.REVIEW_DONE);

            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();

            if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.MODIFY) {
                keywordCriteria.put(new String("reviewStatus"), operatorInit);
                keywordCriteria.put(new String("reviewStatus"), operatorModify);
                keywordCriteria.put(new String("reviewStatus"), operatorCheckDone);
                keywordCriteria.put(new String("reviewStatus"), operatorReviewDone);
            } else if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.CHECK) {
                keywordCriteria.put(new String("reviewStatus"), operatorCheck);
                keywordCriteria.put(new String("reviewStatus"), operatorCheckDone);
            } else if (criteriaDTO.getProofreadType() == ProofreadDrawingListCriteriaDTO.ProofreadType.REVIEW) {
                keywordCriteria.put(new String("reviewStatus"), operatorReview);
                keywordCriteria.put(new String("reviewStatus"), operatorReviewDone);
            }

            builder.orObj(keywordCriteria);
        }

        if (criteriaDTO.isOrderByNo()) {
            builder.sort(new Sort.Order(Sort.Direction.ASC, "subDrawingNo"));
            builder.sort(new Sort.Order(Sort.Direction.ASC, "pageNo"));
        } else {
            builder.sort(new Sort.Order(Sort.Direction.ASC, "seq"));
        }

        return builder.paginate(pageable).exec().page();
    }

    /**
     * 查找图纸版本下的运行中的子图纸。
     *
     * @param drawingId
     * @param drawingVersion
     * @param status
     * @return
     */
    @Override
    public List<SubDrawing> findByDrawingIdAndDrawingVersionAndStatus(
        Long orgId,
        Long projectId,
        Long drawingId,
        String drawingVersion,
        EntityStatus status) {

        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append(" SELECT sw.* FROM sub_drawing sw")
            .append(" WHERE sw.drawing_id = :drawingId")
            .append(" AND  sw.org_id = :orgId")
            .append(" AND  sw.project_id = :projectId");

        if (drawingVersion != null) {
            sql.append(" AND sw.drawing_version = :drawingVersion");
        }
        if (status != null) {
            sql.append(" AND sw.status = :status");
        }

        sql.append(" ORDER BY sw.sub_drawing_no ASC");

        Query query = entityManager.createNativeQuery(sql.toString(), SubDrawing.class);
        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("drawingId", drawingId);

        if (drawingVersion != null) {
            query.setParameter("drawingVersion", drawingVersion);
        }
        if (status != null) {
            query.setParameter("status", status.name());
        }

        List<SubDrawing> result = query.getResultList();
        return result;
    }

}
