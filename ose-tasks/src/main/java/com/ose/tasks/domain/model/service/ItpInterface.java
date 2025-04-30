package com.ose.tasks.domain.model.service;

import com.ose.tasks.dto.ItpCriteriaDTO;
import com.ose.tasks.dto.ItpCreateDTO;
import com.ose.tasks.dto.ItpUpdateDTO;
import com.ose.tasks.entity.Itp;
import org.springframework.data.domain.Page;

public interface ItpInterface {

    /**
     * 创建ITP。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpCreateDTO 创建ITP信息
     */
    void create(Long operatorId, Long orgId, Long projectId, ItpCreateDTO itpCreateDTO);

    /**
     * 获取ITP列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param itpCriteriaDTO 查询条件
     * @return ITP列表
     */
    Page<Itp> search(Long orgId, Long projectId, ItpCriteriaDTO itpCriteriaDTO);

    /**
     * 获取ITP详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param itpId     ITPID
     * @return ITP详情
     */
    Itp get(Long orgId, Long projectId, Long itpId);

    /**
     * 更新ITP详情。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpId        ITPID
     * @param itpUpdateDTO 更新ITP信息
     */
    void update(Long operatorId, Long orgId, Long projectId, Long itpId, ItpUpdateDTO itpUpdateDTO);

    /**
     * 删除ITP详情。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param itpId      ITPID
     */
    void delete(Long operatorId, Long orgId, Long projectId, Long itpId);

}
