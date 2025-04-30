package com.ose.tasks.domain.model.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.PrintConfigDTO;
import com.ose.tasks.entity.PrintConfig;

public interface PrintConfigInterface {

    /**
     * 添加打印机配置信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param configDTO
     * @return
     */
    PrintConfig create(Long orgId, Long projectId, PrintConfigDTO configDTO);

    /**
     * 查询打印机配置信息列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param page
     * @return
     */
    Page<PrintConfig> getList(Long orgId, Long projectId, PageDTO page);

    /**
     * 查询打印机配置信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    PrintConfig get(Long orgId, Long projectId, Long id);

    /**
     * 删除打印机配置信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    boolean delete(Long orgId, Long projectId, Long id);

    /**
     * 修改打印机配置信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param configDTO
     * @return
     */
    PrintConfig modify(Long orgId, Long projectId, Long id, PrintConfigDTO configDTO);

    /**
     * 根据type查询打印机配置信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param type
     * @return
     */
    List<PrintConfig> searchByType(Long orgId, Long projectId, String type);

}
