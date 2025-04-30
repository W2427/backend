package com.ose.tasks.domain.model.service.wbs.piping;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entity.ISOEntityBase;

import java.util.List;

/**
 * WBS 实体操作服务接口。
 */
public interface ISOEntityInterface extends BaseWBSEntityInterface<ISOEntityBase, ISOEntryCriteriaDTO> {


    /**
     * 更新ISO上bom_ln_node的材料匹配率
     *
     * @param batchTask        批处理任务信息
     * @param operator         操作者信息
     * @param project          项目信息
     * @param initialMatchFlag 是否是第一次匹配，如果为true则是第一次
     * @return 批处理执行结果
     */
    BatchResultDTO updateBomLnMatch(BatchTask batchTask,
                                    OperatorDTO operator,
                                    Project project,
                                    boolean initialMatchFlag);

    List<ISOEntity> searchModules(
        Long orgId,
        Long projectId
    );

}
