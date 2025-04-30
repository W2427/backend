package com.ose.tasks.domain.model.service.mail;

import com.ose.dto.BaseDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmHiTaskinstRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.GOEMailConfigRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.setting.DisciplineRepository;
import com.ose.tasks.domain.model.repository.setting.FuncPartRepository;
import com.ose.tasks.domain.model.repository.setting.HierarchyTypeRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageBasicRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryProcessRelationBasicRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryProcessRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.taskpackage.TaskPackageCategoryInterface;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.GOEMailConfig;
import com.ose.tasks.entity.setting.Discipline;
import com.ose.tasks.entity.setting.FuncPart;
import com.ose.tasks.entity.setting.HierarchyType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.taskpackage.*;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.util.ValueUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GOE邮件发送服务。
 */
@Component
public class GOEMailConfigService implements GOEMailConfigInterface {

    private final GOEMailConfigRepository goeMailConfigRepository;

    private final BpmHiTaskinstRepository bpmHiTaskinstRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public GOEMailConfigService(
        GOEMailConfigRepository goeMailConfigRepository,
        BpmHiTaskinstRepository bpmHiTaskinstRepository) {
        this.goeMailConfigRepository = goeMailConfigRepository;
        this.bpmHiTaskinstRepository = bpmHiTaskinstRepository;
    }

    /**
     * 查询。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param actInstId   流程 ID
     * @param taskDefKey  任务节点
     * @return 分页数据
     */
    @Override
    public List<GOEMailConfig> searchList(
        final Long orgId,
        final Long projectId,
        final Long actInstId,
        final String taskDefKey
    ) {
//        return goeMailConfigRepository.findByOrgIdAndProjectIdAndTaskDefKey(orgId, projectId, taskDefKey);

        List<GOEMailConfig> mails = goeMailConfigRepository.findByOrgIdAndProjectIdAndTaskDefKey(orgId, projectId, taskDefKey);
        List<String> emails = bpmHiTaskinstRepository.findEmailByHiAssignee(actInstId);
        if (emails.size() > 0) {
            for (String email : emails) {
                GOEMailConfig config = new GOEMailConfig();
                config.setToMail(email);
                mails.add(config);
            }
        }

        if (mails.size() > 0) {
            List<GOEMailConfig> goeMailConfigs = mails.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(GOEMailConfig::getToMail, mail -> mail, (mail1, mail2) -> mail1),
                    map -> new ArrayList<>(map.values())
                ));

            return goeMailConfigs;
        }


        return mails;

    }
}
