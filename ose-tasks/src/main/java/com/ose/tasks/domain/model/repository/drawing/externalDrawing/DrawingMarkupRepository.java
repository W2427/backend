package com.ose.tasks.domain.model.repository.drawing.externalDrawing;

import com.ose.tasks.entity.drawing.DrawingMarkup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Set;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingMarkupRepository extends PagingAndSortingRepository<DrawingMarkup, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM DrawingMarkup dm WHERE dm.projectId = :projectId AND dm.dwgNo = :dwgName AND dm.revision = :revision")
    void deleteByProjectIdAndDwgNameAndRevision(@Param("projectId") Long projectId,
                                                         @Param("dwgName") String dwgName,
                                                         @Param("revision") String dwgRev);

    @Transactional
    @Modifying
    @Query("DELETE FROM DrawingMarkup dm WHERE dm.projectId = :projectId AND dm.dwgNo = :dwgName")
    void deleteByProjectIdAndDwgName(@Param("projectId") Long projectId,
                                     @Param("dwgName") String dwgName);

    List<DrawingMarkup> findByProjectIdAndDwgNo(Long projectId, String dwgNo);

    @Query("SELECT pi FROM HierarchyNodeRelation hnr JOIN ProjectNode pn ON pn.id = hnr.nodeId " +
        "   JOIN DrawingMarkup pi ON pi.projectId = pn.projectId AND pi.entityNo = pn.no" +
        " WHERE hnr.projectId = :projectId AND hnr.ancestorEntityId = :subSystemId")
    Set<DrawingMarkup> getPdfPageIndexesBySubSys(@Param("projectId") Long projectId, @Param("subSystemId") Long subSystemId);


    @Query("SELECT pi FROM DrawingMarkup pi " +
        " WHERE pi.projectId = :projectId AND pi.parentNo = :subSystemNo")
    Set<DrawingMarkup> getMarkupDwgBySubSysNo(@Param("projectId") Long projectId, @Param("subSystemNo") String subSystemNo);

    @Query("SELECT pi FROM DrawingMarkup pi " +
        " WHERE pi.projectId = :projectId AND pi.entityNo = :entityNo")
    Set<DrawingMarkup> findByProjectIdAndEntityNo(@Param("projectId") Long projectId, @Param("entityNo") String pkgNo);

    @Query("SELECT pi.entityNo as tag, hnr.ancestorEntityId as ssId FROM HierarchyNodeRelation hnr JOIN ProjectNode pn ON pn.id = hnr.nodeId " +
        "   JOIN DrawingMarkup pi ON pi.projectId = pn.projectId AND pi.entityNo = pn.no" +
        " WHERE hnr.projectId = :projectId AND pi.drawingId = :drawingId AND hnr.ancestorEntityType = 'SUB_SYSTEM'" )
    Set<Tuple> getSubSysByProjectIdAndDrawingId(@Param("projectId") Long projectId, @Param("drawingId") Long drawingId);
}

