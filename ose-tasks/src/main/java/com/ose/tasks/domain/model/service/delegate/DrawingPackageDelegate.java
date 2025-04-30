package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.BatchTaskDrawingRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 图纸 工序 代理。
 */
@Component
public class DrawingPackageDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final BatchTaskDrawingRepository batchTaskDrawingRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingPackageDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                  BpmRuTaskRepository ruTaskRepository,
                                  StringRedisTemplate stringRedisTemplate,
                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                  BatchTaskDrawingRepository batchTaskDrawingRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.batchTaskDrawingRepository = batchTaskDrawingRepository;
    }

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        String process = execResult.getActInst().getProcess();
        String taskKey = execResult.getRuTask().getTaskDefKey();
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        if (!process.equals("DRAWING-REDMARK")) {
            boolean exitedFlag = batchTaskDrawingRepository.existsByOrgIdAndProjectIdAndRunningIsTrue(orgId, projectId);
            if (exitedFlag) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("本项目中正在打包的图集已经达到上限，请稍后再试");
                return execResult;
            }
        } else {
            if (taskKey.equals("usertask-REDMARK-DESIGN") || taskKey.equals("usertask-REDMARK-CHECK") || taskKey.equals("usertask-REDMARK-MODIFY")) {
                boolean exitedFlag = batchTaskDrawingRepository.existsByOrgIdAndProjectIdAndRunningIsTrue(orgId, projectId);
                if (exitedFlag) {
                    execResult.setExecResult(false);
                    execResult.setErrorDesc("该图集中还有其他流程正在打包中，请稍后再试");
                    return execResult;
                }
            }
        }
        return execResult;
    }

}
