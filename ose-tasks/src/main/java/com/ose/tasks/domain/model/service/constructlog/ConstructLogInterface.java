package com.ose.tasks.domain.model.service.constructlog;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.qc.TestResultDTO;
import com.ose.tasks.entity.qc.BaseConstructionLog;
import com.ose.tasks.entity.qc.PipingTestLog;
import com.ose.tasks.entity.qc.StructureTestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public interface ConstructLogInterface {

    /**
     * 搭建检测结果实例。
     *
     * @param operatorDTO          操作人信息
     * @param wbsEntityType        实体类型
     * @param entityId             实体ID
     * @param processNameEn        工序名称
     * @param testResultDTO        工序结果DTO
     */
    default <T extends TestResultDTO> BaseConstructionLog buildConstructLog(OperatorDTO operatorDTO,
                                                              String wbsEntityType,
                                                              Long entityId,
                                                              String processNameEn,
                                                              Long processId,
                                                              String processStage,
                                                              T testResultDTO){
        return null;
    }

    default <T extends TestResultDTO> StructureTestLog buildStructureConstructLog(OperatorDTO operatorDTO,
                                                                          String wbsEntityType,
                                                                          Long entityId,
                                                                          String processNameEn,
                                                                          Long processId,
                                                                          String processStage,
                                                                          T testResultDTO){
        return null;
    }



        /**
         * 设置流程角色。
         *
         */
    default <T extends TestResultDTO> PipingTestLog setRolePerformers(PipingTestLog pipingTestLog,
                                                                      Long actInstId, Long entityId,
                                                                      T testResultDTO,
                                                                      Map<String, Set<String>> roleTaskDefKeyMap){
        return null;
    }


    default <T extends TestResultDTO> StructureTestLog setStructureRolePerformers(StructureTestLog structureTestLog,
                                                                                  Long actInstId, Long entityId,
                                                                                  T testResultDTO,
                                                                                  Map<String, Set<String>> roleTaskDefKeyMap){
        return null;
    }

    default StructureTestLog setStructureEntitySubType(StructureTestLog structureTestLog, String wbsEntityType){
        return null;
    }

        /**
         * 设置子类型
         * @param pipingTestLog
         * @param wbsEntityType
         * @return
         */
    default PipingTestLog setEntitySubType(PipingTestLog pipingTestLog, String wbsEntityType) {
        return null;
    }

        /**
         * 创建检测结果。
         *
         * @param operatorDTO          操作人信息
         * @param wbsEntityType        实体类型
         * @param entityId             实体ID
         * @param processNameEn        工序名称
         * @param testResultDTO        工序结果DTO
         */
    <T extends TestResultDTO> void createConstructLog(
        OperatorDTO operatorDTO,
        String wbsEntityType,
        Long entityId,
        String processNameEn,
        Long processId,
        String processStage,
        T testResultDTO);

    /**
     * 取得焊工IDs
     */
    default String getWelderIds(Long orgId, Long projectId, Long weldEntityId) {
        return null;
    }

    /**
     * 取得WPS IDs
     */
    String getWpsIds(Long orgId, Long projectId, Long weldEntityId);

    /**
     * 取得建造日志
     *
     * @param orgId
     * @param projectId
     * @param entityId
     * @return
     */
    Page<? extends BaseConstructionLog> getTestResult(Long orgId, Long projectId, Long entityId, String processNameEn, Pageable pageable);


}
