package com.ose.tasks.domain.model.repository.wbs.piping;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.wbs.entity.SpoolHierarchyInfoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 管线实体 CRUD 操作接口。
 */
public interface SpoolHierarchyInfoEntityRepository extends WBSEntityBaseRepository<SpoolHierarchyInfoEntity>,
    WBSEntityCustomRepository<SpoolHierarchyInfoEntity, SpoolEntryCriteriaDTO> {

    Optional<SpoolHierarchyInfoEntity> findByOrgIdAndProjectIdAndDeletedIsFalseAndQrCode(Long orgId, Long projectId,
                                                                                         String qrcode);

    @Query("SELECT pniso FROM "
        + "PressureTestPackageEntityBase eptp, ProjectNode pn, HierarchyNode hn, HierarchyNode hniso, ProjectNode pniso "
        + "WHERE eptp.id = pn.entityId "
        + "AND hn.node.id = pn.id "
        + "AND hn.id = hniso.parentId "
        + "AND hniso.node.id = pniso.id "
        + "AND eptp.projectId = :projectId "
        + "AND eptp.id = :entityId "
        + "AND pniso.deleted = false")
    List<ProjectNode> getIsoNoByPressureTestPackageId(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    Optional<SpoolHierarchyInfoEntity> findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(Long orgId, Long projectId, String no);

    Optional<SpoolHierarchyInfoEntity> findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

}
