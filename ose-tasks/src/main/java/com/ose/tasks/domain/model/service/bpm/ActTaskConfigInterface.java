package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.BpmActTaskConfigCreateDTO;
import com.ose.tasks.dto.bpm.BpmActTaskConfigDTO;
import com.ose.tasks.entity.bpm.BpmActTaskConfig;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateType;
import com.ose.tasks.vo.bpm.ProcessType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ActTaskConfigInterface {

    /**
     * 根据工序id和任务id获取流程节点配置
     */
    List<BpmActTaskConfig> getConfigByProcDefIdAndActTaskId(BpmProcess bpmProcess, BpmRuTask ruTask,
                                                            BpmActTaskConfigDelegateType delegateType);

    /**
     * 根据流程定义id及任务id获取流程节点配置
     *
     * @param ruTask    运行任务
     * @return List
     */
    List<BpmActTaskConfig> getConfigByProcDefIdAndActTaskId(BpmProcess bpmProcess, BpmRuTask ruTask, BpmActTaskConfigDelegateStage delegateStage);


    /**
     * 根据 任务节点的名称 取得 对饮前置 后置 代理设置的清单list Map
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param taskDefKey      任务定义
     * @param taskType        任务类型
     * @param processType     工序类型
     * @param processCategory 工序分类
     * @return Map
     */
    Map<String, List<String>> getConfigByTaskDefKey(Long orgId, Long projectId, String taskDefKey, String taskType,
                                                    String processKey, ProcessType processType, String processCategory);


    /**
     * 根据 任务节点的名称 取得 批处理任务  对应前置 后置 代理设置的清单list Map
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param taskDefKey      任务定义
     * @param taskType        任务类型
     * @param processType     工序类型
     * @param processCategory 工序分类
     * @return Map
     */
    Map<String, List<String>> getBatchTaskConfigByTaskDefKey(Long orgId, Long projectId, String taskDefKey, String taskType,
                                                             String processKey, ProcessType processType, String processCategory);


    /**
     * 根据 任务节点的名称 取得 挂起任务  对应前置 后置 代理设置的清单list Map
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param taskDefKey      任务定义
     * @param taskType        任务类型
     * @param processType     工序类型
     * @param processCategory 工序分类
     * @return Map
     */
    Map<String, List<String>> getSuspendConfigByTaskDefKey(Long orgId, Long projectId, String taskDefKey, String taskType,
                                                             String processKey, ProcessType processType, String processCategory);

    /**
     * 根据 任务节点的名称 取得 创建 代理设置的清单list Map
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param processType     工序类型
     * @param processKey      工序阶段+工序
     * @param processCategory 工序分类
     * @return Map
     */
    Map<String, List<String>> getConfigForCreate(Long orgId, Long projectId, String processKey, ProcessType processType, String processCategory);


    /**
     * 创建任务代理设置。
     *
     * @param orgId                     组织ID 组织id
     * @param projectId                 项目id
     * @param bpmActTaskConfigCreateDTO 传输对象
     * @return BpmActTaskConfig
     */
    BpmActTaskConfig add(Long orgId, Long projectId, BpmActTaskConfigCreateDTO bpmActTaskConfigCreateDTO, OperatorDTO operator);

    /**
     * 查询任务代理设置列表。
     *
     * @param orgId               组织ID 组织id
     * @param projectId           项目id
     * @param bpmActTaskConfigDTO 查询参数
     * @param pageDTO             分页参数
     * @return Page
     */
    Page<BpmActTaskConfig> search(Long orgId, Long projectId, BpmActTaskConfigDTO bpmActTaskConfigDTO, PageDTO pageDTO);

    /**
     * 编辑任务代理设置。
     *
     * @param orgId               组织ID 组织id
     * @param projectId           项目id
     * @param bpmActTaskConfigId  任务代理设置ID
     * @param bpmActTaskConfigDTO 传输对象
     * @return BpmActTaskConfig
     */
    BpmActTaskConfig edit(Long orgId, Long projectId, Long bpmActTaskConfigId, BpmActTaskConfigDTO bpmActTaskConfigDTO, OperatorDTO operator);

    /**
     * 任务代理设置详情。
     *
     * @param orgId              组织ID 组织id
     * @param projectId          项目id
     * @param bpmActTaskConfigId 任务代理设置ID
     * @return BpmActTaskConfig
     */
    BpmActTaskConfig detail(Long orgId, Long projectId, Long bpmActTaskConfigId);

    /**
     * 删除任务代理设置。
     *
     * @param orgId              组织ID 组织id
     * @param projectId          项目id
     * @param bpmActTaskConfigId 任务代理设置ID
     */
    void delete(Long orgId, Long projectId, Long bpmActTaskConfigId, OperatorDTO operator);


}
