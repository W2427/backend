package com.ose.tasks.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.SubconDTO;
import com.ose.tasks.entity.Subcon;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubconInterface extends EntityInterface {

    void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        SubconDTO subconDTO
    );

    List<Subcon> list(
        Long orgId,
        Long projectId
    );

    /**
     * 获取分包商列表（分页）。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页参数
     * @return 带分页的分包商列表
     */
    Page<Subcon> search(Long orgId, Long projectId, PageDTO pageDTO);

    Subcon get(
        Long orgId,
        Long projectId,
        Long subconId
    );

    /**
     * 删除分包商。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param subconId   分包商ID
     */
    void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long subconId
    );

    /**
     * 更新包商信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param subconId   项目ID
     * @param subconDTO  更新信息
     */
    void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long subconId,
        SubconDTO subconDTO
    );

     Iterable<Subcon> findAllByIds(List<Long> ids);

    /**
     * 获取工作班组列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 工作班组列表
     */
    List<BpmActivityInstanceState> teamNameList(
        Long orgId,
        Long projectId
    );

    /**
     * 获取工作场地列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 工作场地列表
     */
    List<BpmActivityInstanceState> workSiteNameList(
        Long orgId,
        Long projectId
    );

}
