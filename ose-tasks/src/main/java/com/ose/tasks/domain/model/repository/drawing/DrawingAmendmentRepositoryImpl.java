package com.ose.tasks.domain.model.repository.drawing;


import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendment;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 主图纸
 */
public class DrawingAmendmentRepositoryImpl extends BaseRepository implements DrawingAmendmentRepositoryCustom {

    private static final String SD_DWG_BOM_FORECAST = "SD_DWG_BOM_FORECAST";//生产设计预估材料表
    private static final List<String> ENTITY_CATEGORY = new ArrayList<>(
        Arrays.asList(SD_DWG_BOM_FORECAST));
    @Override
    public Page<DrawingAmendment> getList(Long orgId, Long projectId, PageDTO page, DrawingCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        StringBuilder whereSql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("d.id as id, ");
        sql.append("d.created_at as createdAt, ");
        sql.append("d.last_modified_at as lastModifiedAt, ");
        sql.append("d.status as status, ");
        sql.append("d.batch_task_id as batchTaskId, ");
        sql.append("d.drawing_title as drawingTitle, ");
        sql.append("d.no as no, ");
        sql.append("d.dwg_no as dwgNo, ");
        sql.append("d.latest_rev as latestRev, ");
        sql.append("d.latest_approved_rev as latestApprovedRev, ");
        sql.append("d.org_id as orgId, ");
        sql.append("d.project_id as projectId, ");
        sql.append("d.file_id as fileId, ");
        sql.append("d.file_last_version as fileLastVersion, ");
        sql.append("d.file_name as fileName, ");
        sql.append("d.file_page_count as filePageCount, ");
        sql.append("d.file_path as filePath, ");
        sql.append("d.qr_code as qrCode, ");
//        sql.append("d.drawing_category_id as drawingCategoryId, ");
        sql.append("d.operator as operator, ");
        sql.append("d.cover_id as coverId, ");
        sql.append("d.cover_path as coverPath, ");
        sql.append("d.engineering_finish_date as engineeringFinishDate, ");
        sql.append("d.engineering_start_date as engineeringStartDate, ");
        sql.append("d.estimated_man_hours as estimatedManHours, ");
        sql.append("d.area_name as areaName, ");
        sql.append("d.module_name as moduleName, ");
        sql.append("d.block as block, ");
        sql.append("d.design_discipline as designDiscipline, ");
        sql.append("d.installation_drawing_no as installationDrawingNo, ");
        sql.append("d.section as section, ");
        sql.append("d.small_area as smallArea, ");
        sql.append("d.work_net as workNet, ");
        sql.append("d.drawer as drawer, ");
        sql.append("d.drawer_id as drawerId ");
        sql.append("FROM drawing_amendment AS d ");
        sql.append("left join bpm_entity_sub_type AS c ON d.`drawing_category_id` = c.`id` ");
        sql.append("WHERE 1 = 1 ");

        whereSql.append("AND d.project_id = :projectId ");
        whereSql.append("AND d.status = :status ");

//        if (criteriaDTO.getDrawingCategoryId() != null) {
//            whereSql.append("AND d.drawing_category_id = :drawingCategoryId ");
//        }

        if (criteriaDTO.getDwgNo() != null) {
            whereSql.append("AND d.dwg_no = :dwgNo ");
        }

        if (criteriaDTO.getInfoPut() != null) {
            whereSql.append("AND d.is_info_put = :isInfoPut ");
        }

        if (criteriaDTO.getAreaName() != null) {
            whereSql.append("AND d.area_name = :areaName ");
        }

        if (criteriaDTO.getModuleName() != null) {
            whereSql.append("AND d.module_name = :moduleName ");
        }

        if (criteriaDTO.getWorkNet() != null) {
            whereSql.append("AND d.work_net = :workNet ");
        }

        if (criteriaDTO.getSection() != null) {
            whereSql.append("AND d.section = :section ");
        }

        if (criteriaDTO.getBlock() != null) {
            whereSql.append("AND d.block = :block ");
        }

        if (criteriaDTO.getSmallArea() != null) {
            whereSql.append("AND d.small_area = :smallArea ");
        }

        if (criteriaDTO.getDesignDiscipline() != null) {
            whereSql.append("AND d.design_discipline = :designDiscipline ");
        }

        if (criteriaDTO.getLatestRev() != null) {
            whereSql.append("AND d.latest_rev = :latestRev ");
        }

        if (criteriaDTO.getKeyword() != null) {
            whereSql.append("AND (d.dwg_no like :dwgNo or d.drawing_title like :drawingTitle or d.no like :no)");
        }

        if (criteriaDTO.getBomFlag() != null
            && criteriaDTO.getBomFlag()) {
            whereSql.append("AND c.name_en in (:entityCategories)");
            sql.append(whereSql);
        } else if (criteriaDTO.getBomFlag() != null
            && !criteriaDTO.getBomFlag()) {
            StringBuilder whereSql2 = new StringBuilder(whereSql);
            whereSql.append("AND c.name_en not in (:entityCategories)");
//            whereSql2.append("AND d.drawing_category_id is null");
            sql.append("AND ( 1 = 1 ");
            sql.append(whereSql);
            sql.append(") OR ( 1 = 1 ");
            sql.append(whereSql2);
            sql.append(")");
        } else {
            sql.append(whereSql);
        }

        sql.append(" ORDER BY d.created_at desc ");

        if (!page.getFetchAll()) {
            sql.append("LIMIT :start , :offset ");
        }

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);


        if (criteriaDTO.getDrawingCategoryId() != null) {
            query.setParameter("drawingCategoryId", criteriaDTO.getDrawingCategoryId());
            countQuery.setParameter("drawingCategoryId", criteriaDTO.getDrawingCategoryId());
        }

        if (criteriaDTO.getDwgNo() != null) {
            query.setParameter("dwgNo", criteriaDTO.getDwgNo());
            countQuery.setParameter("dwgNo", criteriaDTO.getDwgNo());
        }

        if (criteriaDTO.getKeyword() != null) {
            query.setParameter("no", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("no", "%" + criteriaDTO.getKeyword() + "%");
            query.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("dwgNo", "%" + criteriaDTO.getKeyword() + "%");
            query.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
            countQuery.setParameter("drawingTitle", "%" + criteriaDTO.getKeyword() + "%");
        }

        if (criteriaDTO.getBomFlag() != null) {
            query.setParameter("entityCategories", ENTITY_CATEGORY);
            countQuery.setParameter("entityCategories", ENTITY_CATEGORY);
        }

        if (criteriaDTO.getAreaName() != null) {
            query.setParameter("areaName", criteriaDTO.getAreaName());
            countQuery.setParameter("areaName", criteriaDTO.getAreaName());
        }

        if (criteriaDTO.getModuleName() != null) {
            query.setParameter("moduleName", criteriaDTO.getModuleName());
            countQuery.setParameter("moduleName", criteriaDTO.getModuleName());
        }

        if (criteriaDTO.getWorkNet() != null) {
            query.setParameter("workNet", criteriaDTO.getWorkNet());
            countQuery.setParameter("workNet", criteriaDTO.getWorkNet());
        }

        if (criteriaDTO.getSection() != null) {
            query.setParameter("section", criteriaDTO.getSection());
            countQuery.setParameter("section", criteriaDTO.getSection());
        }

        if (criteriaDTO.getBlock() != null) {
            query.setParameter("block", criteriaDTO.getBlock());
            countQuery.setParameter("block", criteriaDTO.getBlock());
        }

        if (criteriaDTO.getSmallArea() != null) {
            query.setParameter("smallArea", criteriaDTO.getSmallArea());
            countQuery.setParameter("smallArea", criteriaDTO.getSmallArea());
        }

        if (criteriaDTO.getDesignDiscipline() != null) {
            query.setParameter("designDiscipline", criteriaDTO.getDesignDiscipline());
            countQuery.setParameter("designDiscipline", criteriaDTO.getDesignDiscipline());
        }

        if (criteriaDTO.getLatestRev() != null) {
            query.setParameter("latestRev", criteriaDTO.getLatestRev());
            countQuery.setParameter("latestRev", criteriaDTO.getLatestRev());
        }

        if (criteriaDTO.getInfoPut() != null) {
            if (criteriaDTO.getInfoPut().equals("true")) {
                query.setParameter("isInfoPut", 1);
                countQuery.setParameter("isInfoPut", 1);
            } else {
                query.setParameter("isInfoPut", 0);
                countQuery.setParameter("isInfoPut", 0);
            }

        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("status", EntityStatus.ACTIVE.name());
        countQuery.setParameter("status", EntityStatus.ACTIVE.name());

        // 分页参数
        if (!page.getFetchAll()) {

            int pageNo = page.getPage().getNo();
            int pageSize = page.getPage().getSize();
            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<DrawingAmendment> dwgList = new ArrayList<DrawingAmendment>();
//        Set<String> keys = new HashSet<String>();

        for (Map<String, Object> wpsMap : queryResultList) {
            DrawingAmendment dwg = new DrawingAmendment();
            dwg.setStatus(EntityStatus.ACTIVE);
            if (wpsMap.get("drawingCategoryId") != null) {
                SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
                    .is("id",wpsMap.get("drawingCategoryId").toString());
                List<BpmEntitySubType> category = builder.exec().list();
                if (!category.isEmpty()) {
                    dwg.setDrawingCategory(category.get(0));
                }
            }
            BeanUtils.copyProperties(wpsMap, dwg, "status");
            dwgList.add(dwg);
        }


        if (page.getFetchAll()) {
            return new PageImpl<>(
                dwgList
            );
        }

        return new PageImpl<>(dwgList, page.toPageable(), count.longValue());
    }
}
