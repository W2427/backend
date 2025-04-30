package com.ose.issues.domain.model.repository;

import com.ose.issues.dto.IssueCriteriaDTO;
import com.ose.issues.entity.IssueBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IssueCustomRepository {

    /**
     * 查询问题。
     *
     * @param <T>       问题范型
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param pageable  分页参数
     * @param clazz     问题类型
     * @return 用户查询结果分页数据
     */
    <T extends IssueBase> Page<T> search(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO criteria,
        Pageable pageable,
        Class<T> clazz
    );

    /**
     * 查询问题。
     *
     * @param <T>       问题范型
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param clazz     问题类型
     * @return 用户查询结果列表数据
     */
    <T extends IssueBase> List<T> search(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO criteria,
        Class<T> clazz
    );

    /**
     * 返回xls导出需要的数据
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param sql
     * @return
     */
    List<List<Object>> getXlsList(Long orgId, Long projectId, String sql);

    List<Map<String, Object>> getColumnItems(Long projectId, IssueCriteriaDTO issueCriteria, String columnName, boolean isIdRequired);

}
