package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * WBS 实体 CRUD 操作接口。
 */
@NoRepositoryBean
public interface WBSEntityBaseRepository<T extends WBSEntityBase> extends CrudRepository<T, Long> {

    /**
     * 根据节点编号及项目 ID 取得实体信息。
     *
     * @param no        节点编号
     * @param projectId 项目 ID
     * @return 实体信息
     */
    Optional<T> findByProjectIdAndNoAndDeletedIsFalse(Long projectId, String no);

    /**
     * 根据实体 ID、所属组织 ID、所属项目 ID 取得实体信息。
     *
     * @param id        实体 ID
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @return 实体信息
     */
    Optional<T> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(Long id, Long orgId, Long projectId);

    /**
     * 根据实体 ID、所属组织 ID、所属项目 ID 取得实体信息。
     *
     * @param id        实体 ID
     * @param projectId 项目 ID
     * @return 实体信息
     */
    T findByProjectIdAndIdAndDeletedIsFalse(Long projectId, Long id);

    /**
     * 判断节点编号是否存在于项目ID。
     *
     * @param no        节点编号
     * @param projectId 项目 ID
     * @return 存在：true；不存在：false；
     */
    boolean existsByNoAndProjectIdAndDeletedIsFalse(String no, Long projectId);

    /**
     * 判断节点编号是否存在于项目。
     *
     * @param no        节点编号
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 存在：true；不存在：false；
     */
    Optional<T> findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(Long orgId, Long projectId, String no);
}
