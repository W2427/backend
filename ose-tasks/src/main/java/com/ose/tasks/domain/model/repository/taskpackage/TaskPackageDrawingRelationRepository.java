package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.taskpackage.TaskPackageDrawingRelation;
import com.ose.tasks.entity.taskpackage.TaskPackageEntity;
import com.ose.tasks.vo.drawing.DrawingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 任务包-图纸数据仓库。
 */
public interface TaskPackageDrawingRelationRepository extends PagingAndSortingWithCrudRepository<TaskPackageDrawingRelation, Long> {

    Page<TaskPackageDrawingRelation> findByOrgIdAndProjectIdAndTaskPackageId(Long orgId, Long projectId, Long taskPackageId, Pageable pageable);

    Page<TaskPackageDrawingRelation> findByOrgIdAndProjectIdAndTaskPackageIdAndDrawingId(Long orgId, Long projectId, Long taskPackageId, Long drawingId, Pageable pageable);

    Page<TaskPackageDrawingRelation> findByOrgIdAndProjectIdAndTaskPackageIdAndDrawingType(Long orgId, Long projectId, Long taskPackageId, DrawingType drawingType, Pageable pageable);

    Optional<TaskPackageDrawingRelation> findByOrgIdAndProjectIdAndTaskPackageIdAndId(Long orgId, Long projectId, Long taskPackageId, Long id);

    boolean existsByOrgIdAndProjectIdAndTaskPackageId(Long orgId, Long projectId, Long taskPackageId);

    @Query(
        value = "SELECT DISTINCT"
            + "   wp.id      AS task_package_id,"
            + "   spool.iso_no,"
            + "   dwg.id     AS drawing_id,"
            + "   dwg.dwg_no AS drawing_no,"
            + "   sdwg.id    AS sub_drawing_id,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no,"
            + "   sdwg.sub_drawing_version,"
            + "   sdwg.file_id"
            + " FROM"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_entity_relation AS wpe"
            + "     ON wpe.task_package_id = wp.id"
            + "     AND wpe.entity_type = 'SPOOL'"
            + "   INNER JOIN entity_spool AS spool"
            + "     ON spool.id = wpe.entity_id"
            + "     AND spool.deleted = 0"
            + "   INNER JOIN sub_drawing AS sdwg"
            + "     ON sdwg.project_id = wp.project_id"
            + "     AND sdwg.sub_drawing_no = REPLACE(REPLACE(spool.iso_no, '\"', '_'), '/', '_')"
            + "     AND sdwg.page_no = spool.sheet_no"
            + "     AND sdwg.status = 'ACTIVE'"
            + "   INNER JOIN drawing AS dwg"
            + "     ON dwg.id = sdwg.drawing_id"
            + "     AND dwg.status = 'ACTIVE'"
            + " WHERE"
            + "   wp.project_id = :#{#taskPackage.projectId}"
            + "   AND wp.id = :#{#taskPackage.id}"
            + " ORDER BY"
            + "   dwg.dwg_no,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no",
        nativeQuery = true
    )
    List<Object> findSpoolDrawings(@Param("taskPackage") TaskPackageEntity taskPackage );

    @Query(
        value = "SELECT DISTINCT"
            + "   wp.id      AS task_package_id,"
            + "   spool.iso_no,"
            + "   dwg.id     AS drawing_id,"
            + "   dwg.dwg_no AS drawing_no,"
            + "   sdwg.id    AS sub_drawing_id,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no,"
            + "   sdwg.sub_drawing_version,"
            + "   sdwg.file_id"
            + " FROM"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_entity_relation AS wpe"
            + "     ON wpe.task_package_id = wp.id"
            + "     AND wpe.entity_type = 'SPOOL'"
            + "   INNER JOIN entity_spool AS spool"
            + "     ON spool.id = wpe.entity_id"
            + "     AND spool.deleted = 0"
            + "   INNER JOIN sub_drawing AS sdwg"
            + "     ON sdwg.project_id = wp.project_id"
            + "     AND sdwg.sub_drawing_no = REPLACE(REPLACE(spool.iso_no, '\"', '_'), '/', '_')"
            + "     AND sdwg.page_no = spool.sheet_no"
            + "     AND sdwg.status = 'ACTIVE'"
            + "   INNER JOIN drawing AS dwg"
            + "     ON dwg.id = sdwg.drawing_id"
            + "     AND dwg.status = 'ACTIVE'"
            + " WHERE"
            + "   wp.project_id = :#{#taskPackage.projectId}"
            + "   AND wp.id = :#{#taskPackage.id}"
            + " ORDER BY"
            + "   dwg.dwg_no,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no"
            + " LIMIT :#{#start}, :#{#offset}",
        nativeQuery = true
    )
    List<Object> findSpoolDrawings(@Param("taskPackage") TaskPackageEntity taskPackage, @Param("start")int start, @Param("offset")int offset);

    @Query(
        value = "SELECT DISTINCT"
            + "   wp.id      AS task_package_id,"
            + "   iso.no     AS iso_no,"
            + "   dwg.id     AS drawing_id,"
            + "   dwg.dwg_no AS drawing_no,"
            + "   sdwg.id    AS sub_drawing_id,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no,"
            + "   sdwg.sub_drawing_version,"
            + "   sdwg.file_id"
            + " FROM"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_entity_relation AS wpe"
            + "     ON wpe.task_package_id = wp.id"
            + "     AND wpe.entity_type = 'ISO'"
            + "   INNER JOIN entity_iso AS iso"
            + "     ON iso.id = wpe.entity_id"
            + "     AND iso.deleted = 0"
            + "   INNER JOIN entity_spool AS spool"
            + "     ON spool.iso_entity_id = iso.id"
            + "     AND spool.deleted = 0"
            + "   INNER JOIN sub_drawing AS sdwg"
            + "     ON sdwg.project_id = wp.project_id"
            + "     AND sdwg.sub_drawing_no = REPLACE(REPLACE(iso.no, '\"', '_'), '/', '_')"
            + "     AND sdwg.page_no = spool.sheet_no"
            + "     AND sdwg.status = 'ACTIVE'"
            + "   INNER JOIN drawing AS dwg"
            + "     ON dwg.id = sdwg.drawing_id"
            + "     AND dwg.status = 'ACTIVE'"
            + " WHERE"
            + "   wp.project_id = :#{#taskPackage.projectId}"
            + "   AND wp.id = :#{#taskPackage.id}"
            + " ORDER BY"
            + "   dwg.dwg_no,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no",
        nativeQuery = true
    )
    List<Object> findISODrawings(@Param("taskPackage") TaskPackageEntity taskPackage);

    @Query(
        value = "SELECT DISTINCT"
            + "   wp.id      AS task_package_id,"
            + "   iso.no     AS iso_no,"
            + "   dwg.id     AS drawing_id,"
            + "   dwg.dwg_no AS drawing_no,"
            + "   sdwg.id    AS sub_drawing_id,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no,"
            + "   sdwg.sub_drawing_version,"
            + "   sdwg.file_id"
            + " FROM"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_entity_relation AS wpe"
            + "     ON wpe.task_package_id = wp.id"
            + "     AND wpe.entity_type = 'ISO'"
            + "   INNER JOIN entity_iso AS iso"
            + "     ON iso.id = wpe.entity_id"
            + "     AND iso.deleted = 0"
            + "   INNER JOIN entity_spool AS spool"
            + "     ON spool.iso_entity_id = iso.id"
            + "     AND spool.deleted = 0"
            + "   INNER JOIN sub_drawing AS sdwg"
            + "     ON sdwg.project_id = wp.project_id"
            + "     AND sdwg.sub_drawing_no = REPLACE(REPLACE(iso.no, '\"', '_'), '/', '_')"
            + "     AND sdwg.page_no = spool.sheet_no"
            + "     AND sdwg.status = 'ACTIVE'"
            + "   INNER JOIN drawing AS dwg"
            + "     ON dwg.id = sdwg.drawing_id"
            + "     AND dwg.status = 'ACTIVE'"
            + " WHERE"
            + "   wp.project_id = :#{#taskPackage.projectId}"
            + "   AND wp.id = :#{#taskPackage.id}"
            + " ORDER BY"
            + "   dwg.dwg_no,"
            + "   sdwg.sub_drawing_no,"
            + "   sdwg.page_no"
            + " LIMIT :#{#start}, :#{#offset}",
        nativeQuery = true
    )
    List<Object> findISODrawings(@Param("taskPackage") TaskPackageEntity taskPackage, @Param("start")int start, @Param("offset")int offset);

    @Query(
        value = "   SELECT"
            + "      IFNULL(wpd.sub_drawing_id, wpd.drawing_id) AS id"
            + "    FROM"
            + "      task_package AS wp"
            + "      INNER JOIN task_package_drawing_relation AS wpd"
            + "        ON wpd.task_package_id = wp.id"
            + "    WHERE"
            + "      wp.project_id = :projectId"
            + "      AND wp.id = :taskPackageId"
            + "  ",
        nativeQuery = true
    )
    List<Long> findAllDrawings(@Param("projectId") Long projectId, @Param("taskPackageId") Long taskPackageId);

    @Query(
        value = "   SELECT DISTINCT"
            + "      sdwg.id AS id"
            + "    FROM"
            + "      task_package AS wp"
            + "      INNER JOIN task_package_entity_relation AS wpe"
            + "        ON wpe.task_package_id = wp.id"
            + "        AND wpe.entity_type = 'SPOOL'"
            + "      INNER JOIN entity_spool AS spool"
            + "        ON spool.id = wpe.entity_id"
            + "        AND spool.deleted = 0"
            + "      INNER JOIN sub_drawing AS sdwg"
            + "        ON sdwg.project_id = wp.project_id"
            + "        AND sdwg.sub_drawing_no = REPLACE(REPLACE(spool.iso_no, '\"', '_'), '/', '_')"
            + "        AND sdwg.page_no = spool.sheet_no"
            + "        AND sdwg.status = 'ACTIVE'"
            + "      INNER JOIN drawing AS dwg"
            + "        ON dwg.id = sdwg.drawing_id"
            + "        AND dwg.status = 'ACTIVE'"
            + "    WHERE"
            + "      wp.project_id = :projectId"
            + "      AND wp.id = :taskPackageId",
        nativeQuery = true
    )
    List<Long> findSpoolDrawings(@Param("projectId") Long projectId, @Param("taskPackageId") Long taskPackageId);


    @Query(
        value = "   SELECT DISTINCT"
            + "      sdwg.id AS id"
            + "    FROM"
            + "      task_package AS wp"
            + "      INNER JOIN task_package_entity_relation AS wpe"
            + "        ON wpe.task_package_id = wp.id"
            + "        AND wpe.entity_type = 'ISO'"
            + "      INNER JOIN entity_iso AS iso"
            + "        ON iso.id = wpe.entity_id"
            + "        AND iso.deleted = 0"
            + "      INNER JOIN entity_spool AS spool"
            + "        ON spool.iso_entity_id = iso.id"
            + "        AND spool.deleted = 0"
            + "      INNER JOIN sub_drawing AS sdwg"
            + "        ON sdwg.project_id = wp.project_id"
            + "        AND sdwg.sub_drawing_no = REPLACE(REPLACE(iso.no, '\"', '_'), '/', '_')"
            + "        AND sdwg.page_no = spool.sheet_no"
            + "        AND sdwg.status = 'ACTIVE'"
            + "      INNER JOIN drawing AS dwg"
            + "        ON dwg.id = sdwg.drawing_id"
            + "        AND dwg.status = 'ACTIVE'"
            + "    WHERE"
            + "      wp.project_id = :projectId"
            + "      AND wp.id = :taskPackageId",
        nativeQuery = true
    )
    List<Long> findIsoDrawings(@Param("projectId") Long projectId, @Param("taskPackageId") Long taskPackageId);
}
