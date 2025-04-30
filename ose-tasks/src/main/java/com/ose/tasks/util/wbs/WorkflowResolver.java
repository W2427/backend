package com.ose.tasks.util.wbs;

import com.ose.controller.BaseController;
import com.ose.tasks.util.BPMNUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Activiti 工作流解析器。
 */
public abstract class WorkflowResolver {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    // 主流程定义 KEY
    public static final String PROCESS_DEFINITION_KEY = "MODULE_WORKFLOW";


    /**
     * 构造方法。
     *
     * @param funcPart      功能块
     * @param variables     工作流执行参数
     */
    WorkflowResolver(
        String funcPart,
        Map<String, Object> variables
    ) {


        if (funcPart == "PIPING") {
            BPMNUtils.EVALUATION_PARAMETERS_PIPING.keySet().forEach(entityType ->
                variables.computeIfAbsent(entityType, BPMNUtils.EVALUATION_PARAMETERS_PIPING::get)
            );
        } else if (funcPart == "STRUCTURE") {
            BPMNUtils.EVALUATION_PARAMETERS_STRUCTURE.keySet().forEach(entityType ->
                variables.computeIfAbsent(entityType, BPMNUtils.EVALUATION_PARAMETERS_STRUCTURE::get)
            );
        }


    }

}
