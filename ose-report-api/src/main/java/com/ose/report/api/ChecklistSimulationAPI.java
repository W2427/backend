package com.ose.report.api;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ChecklistSimulationDTO;
import com.ose.report.entity.ChecklistSimulation;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 检查单报表接口
 */
public interface ChecklistSimulationAPI {

    /**
     * 查询模拟检查单
     *
     * @param orgId     组织
     * @param projectId 项目ID
     * @param page      分页信息
     * @return 模拟检查单列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ChecklistSimulation> searchSimulations(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        PageDTO page
    );

    /**
     * 查询单个模拟检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 模拟检查单信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ChecklistSimulation> searchSimulation(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @PathVariable Long simulationId
    );

    /**
     * 创建模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param checklistSimulationDTO 模拟检查单信息
     * @return 模拟检查单信息
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ChecklistSimulation> createSimulation(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @RequestBody ChecklistSimulationDTO checklistSimulationDTO
    );

    /**
     * 编辑模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param simulationId           模拟检查单ID
     * @param checklistSimulationDTO 模拟检查单内容
     * @return 模拟检查单内容
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody editSimulation(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @PathVariable Long simulationId,
        @RequestBody ChecklistSimulationDTO checklistSimulationDTO
    );

    /**
     * 删除模拟检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return No content
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteSimulation(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @PathVariable Long simulationId
    );

    /**
     * 模拟检查单制作
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}/generate"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ChecklistSimulation> simulationGenerate(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @PathVariable Long simulationId
    );

}
