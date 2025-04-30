package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ExInspEntityInfoDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.bpm.BpmExInspScheduleDetail;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.vo.InspectParty;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExInspTaskBaseInterface {


    Page<BpmRuTask> getExternalInspectionRuTaskList(Long orgId, Long projectId, Long assignee,
                                                    List<Long> actInstIds, PageDTO pageDTO);


    /**
     * 查询外检实体信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param seriesNo
     * @return
     */
    List<ExInspEntityInfoDTO> getExternalInspectionEntityInfo(Long orgId, Long projectId,
                                                              String seriesNo);


    /**
     * 取得工作场地
     *
     * @param projectId
     * @param actInstList
     * @return
     */
    String getWorkSiteName(Long projectId, List<BpmActivityInstanceBase> actInstList);



    BpmExInspSchedule getExInspScheduleByActInst(Long orgId,
                                                 Long projectId,
                                                 BpmActivityInstanceBase actInst);


    /**
     * 生成 报检详情，对应于 不同的检验方
     *
     * @param orgId                            组织ID
     * @param projectId                        项目ID
     * @param exInspSchedule                   报检安排
     * @param inspectParty                     外检方
     * @param operator                         用户
     * @param externalInspectionScheduleDetail 外检安排详情
     * @return
     */
    BpmExInspScheduleDetail generateExInspScheduleDetail(Long orgId,
                                                         Long projectId,
                                                         BpmExInspSchedule exInspSchedule,
                                                         InspectParty inspectParty,
                                                         OperatorDTO operator,
                                                         BpmExInspScheduleDetail externalInspectionScheduleDetail);

}
