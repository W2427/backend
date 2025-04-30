package com.ose.tasks.domain.model.service.bpm;

import org.springframework.data.domain.Page;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ProcessCategoryDTO;
import com.ose.tasks.entity.bpm.BpmProcessCategory;

/**
 * service接口
 */
public interface ProcessCategoryInterface {

    /**
     * 创建工序分类
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param categoryDTO
     * @return
     */
    BpmProcessCategory create(Long orgId, Long projectId, ProcessCategoryDTO categoryDTO);

    /**
     * 查询工序分类列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param page
     * @return
     */
    Page<BpmProcessCategory> getList(Long orgId, Long projectId, PageDTO page);

    /**
     * 查询工序分类详细信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    BpmProcessCategory getEntitySubType(Long orgId, Long projectId, Long id);

    /**
     * 删除工序分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    boolean delete(Long orgId, Long projectId, Long id);

    /**
     * 修改工序分类
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param id
     * @param categoryDTO
     * @return
     */
    BpmProcessCategory modify(Long orgId, Long projectId, Long id, ProcessCategoryDTO categoryDTO);

    /**
     * 根据工序分类名称查询工序分类
     *
     * @param nameCn
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    BpmProcessCategory findByName(Long orgId, Long projectId, String nameCn);


}
