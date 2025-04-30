package com.ose.issues.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.issues.domain.model.repository.*;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Experience;
import com.ose.issues.entity.ExperienceMail;
import com.ose.issues.dto.ExperienceMailListDTO;
import com.ose.issues.vo.IssueType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ExperienceService extends IssueBusiness implements ExperienceInterface {

    private final ExperienceRepository experienceRepository;
    private final ExperienceMailRepository experienceMailRepository;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    /**
     * 构造方法。
     */
    @Autowired
    public ExperienceService(
        IssueRepository issueRepository,
        ExperienceRepository experienceRepository,
        ExperienceMailRepository experienceMailRepository,
        IssuePropertyRepository issuePropertyRepository,
        IssueRecordRepository issueRecordRepository,
        PropertyDefinitionRepository propertyDefinitionRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(
            issueRepository,
            issuePropertyRepository,
            issueRecordRepository,
            propertyDefinitionRepository,
            uploadFeignAPI
        );
        this.experienceRepository = experienceRepository;
        this.experienceMailRepository = experienceMailRepository;
    }

    /**
     * 创建经验教训。
     *
     * @param operatorId          操作人ID
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param experienceCreateDTO 问题信息
     * @return 经验教训信息
     */
    @Transactional
    public Experience create(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final ExperienceCreateDTO experienceCreateDTO
    ) {

        Experience experience = BeanUtils
            .copyProperties(experienceCreateDTO, new Experience(), "attachment");

        Long parentId = experienceCreateDTO.getParentId();

        // 如果前端传递parentId过来，说明此操作为历史升级操作
        // 1.将parentId赋给实体    2.将其他拥有相同父级的实体的currentExperience改成false
        if (!LongUtils.isEmpty(parentId)) {
            String parentNo = experienceRepository.findParentNo(parentId);
            Integer parentSeriesNo = experienceRepository.findParentSeriesNo(parentId);
            experience.setParentId(parentId);
            experience.setNo(parentNo);
            experience.setSeriesNo(parentSeriesNo);
            experienceRepository.updateCurrentStatus(orgId, projectId, parentId);
        }

        experience.setType(IssueType.EXPERIENCE);
        experience.setOrgId(orgId);
        experience.setProjectId(projectId);
        experience.setCreatedAt();
        experience.setCreatedBy(operatorId);
        experience.setLastModifiedAt();
        experience.setLastModifiedBy(operatorId);
        experience.setStatus(EntityStatus.ACTIVE);
        experience.setCurrentExperience(true);

        //如果是单纯的新增，需要校验编号是否重复
        if (LongUtils.isEmpty(parentId)) {
            checkNoAvailability(projectId, IssueType.EXPERIENCE, experience.getNo());
        }

        saveAttachmentAndProperties(
            orgId, projectId, experience,
            experienceCreateDTO.getAttachment(),
            experienceCreateDTO.getProperties()
        );


        experience = experienceRepository.save(experience);

        // 如果前端没有传递parentId过来，说明当前操作只是单纯的新增操作
        // 需要将自己的id作为parentId
        if (LongUtils.isEmpty(parentId)) {

            experience.setSeriesNo(getNewIssueSeriesNo(orgId, projectId));
            experience.setNo(getNewNo(experienceCreateDTO.getProjectName(), experience.getSeriesNo()));
            experience.setParentId(experience.getId());

            return experienceRepository.save(experience);
        }
        return experience;
    }


    /**
     * 获得新编号
     *
     * @param projectName 项目名
     * @param seriesNo    流水号
     * @return
     */
    private String getNewNo(String projectName, Integer seriesNo) {

        String newNo = projectName + "-" + "L" + "-";

        String indexStr = "" + seriesNo;
        if (indexStr.length() < 4) {
            for (int i = 0; i < 4 - indexStr.length(); i++) {
                newNo += "0";
            }
        }
        return newNo + seriesNo;
    }

    /**
     * 获得新流水号
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    private Integer getNewIssueSeriesNo(Long orgId, Long projectId) {

        Integer seriesNo = experienceRepository.findMaxSeriesNo(orgId, projectId);

        if (seriesNo == null) {
            return 1;
        }
        return seriesNo + 1;
    }

    /**
     * 更新经验教训。
     *
     * @param operatorId          操作人ID
     * @param orgId               组织 ID
     * @param projectId           项目 ID
     * @param experienceId        问题 ID
     * @param experienceUpdateDTO 更新信息
     * @return 经验教训信息
     */
    @Transactional
    public Experience update(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final Long experienceId,
        final ExperienceUpdateDTO experienceUpdateDTO
    ) {

        Experience experience = experienceRepository
            .findByIdAndDeletedIsFalse(experienceId);

        if (experience == null) {
            throw new NotFoundError();
        }

        if (experienceUpdateDTO.getNo() != null) {
            experience.setNo(experienceUpdateDTO.getNo());
            checkNoAvailability(projectId, IssueType.EXPERIENCE, experience.getNo(), experienceId);
        }

        if (experienceUpdateDTO.getTitle() != null) {
            experience.setTitle(experienceUpdateDTO.getTitle());
        }

        if (experienceUpdateDTO.getDescription() != null) {
            experience.setDescription(experienceUpdateDTO.getDescription());
        }

        experience.setLastModifiedAt();
        experience.setLastModifiedBy(operatorId);

        saveAttachmentAndProperties(
            orgId, projectId, experience,
            experienceUpdateDTO.getAttachment(),
            experienceUpdateDTO.getProperties()
        );

        return experienceRepository.save(experience);
    }

    /**
     * 删除经验教训。
     *
     * @param operatorId   操作人 ID
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param experienceId 属性 ID
     */
    public void delete(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final Long experienceId
    ) {

        Experience experience = experienceRepository
            .findByIdAndDeletedIsFalse(experienceId);

        if (experience == null) {
            throw new NotFoundError();
        }

        experience.setDeletedAt();
        experience.setDeletedBy(operatorId);
        experience.setCurrentExperience(false);

        experienceRepository.save(experience);

        // 因新增历史版本需求，删除的时候要对历史版本就行相应操作。
        // 1.删除之后将上一个版本的currentExperience置true
        // 2.如果当前就是第一个版本，直接删除即可
        if (!LongUtils.isEmpty(experience.getParentId())
            && !experience.getId().equals(experience.getParentId())
            ) {

            List<Experience> historyTree = experienceRepository.findByOrgIdAndProjectIdAndParentIdAndDeletedIsFalseOrderByCreatedAtDesc(orgId, projectId, experience.getParentId());

            if (historyTree.size() > 0) {

                Experience lastHistoryVersion = historyTree.get(0);

                lastHistoryVersion.setCurrentExperience(true);
                lastHistoryVersion.setLastModifiedAt();
                lastHistoryVersion.setLastModifiedBy(operatorId);

                experienceRepository.save(lastHistoryVersion);
            }
        }

    }

    /**
     * 经验教训列表。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param experienceCriteriaDTO 查询参数
     * @param pageable              分页参数
     * @return 问题列表
     */
    public Page<Experience> search(
        final Long orgId,
        final Long projectId,
        final IssueCriteriaDTO experienceCriteriaDTO,
        final Pageable pageable
    ) {
        return setProperties(projectId,
            experienceRepository
                .search(orgId, projectId, experienceCriteriaDTO, pageable, Experience.class)
        );
    }

    @Override
    public List<Experience> getExperienceList(Long orgId, Long projectId, Long experienceParentId) {
        return experienceRepository.findByOrgIdAndProjectIdAndParentIdAndDeletedIsFalseOrderByCreatedAtDesc(orgId, projectId, experienceParentId);
    }

    /**
     * 经验教训详情。
     *
     * @param experienceId 问题ID
     * @return 问题信息
     */
    @Override
    public Experience get(Long projectId, Long experienceId) {
        return setProperties(projectId,
            experienceRepository
                .findByIdAndDeletedIsFalse(experienceId)
        );
    }

    /**
     * 经验教训打开关闭。
     *
     * @param experienceId 问题ID
     * @return 问题信息
     */
    @Transactional
    public Experience operate(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final Long experienceId,
        final ExperienceStatusDTO experienceStatusDTO
    ) {
        Experience experience = experienceRepository
            .findByIdAndDeletedIsFalse(experienceId);
        experience.setStatus(experienceStatusDTO.getSuspendStatus());
        return experienceRepository.save(experience);
    }

    /**
     * 发送经验教训邮件。
     *
     * @param experienceId 经验ID
     * @return 经验教训信息
     */
    @Transactional
    public ExperienceMail mail(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final Long experienceId,
        final ExperienceMailDTO experienceMailDTO
    ) {
        ExperienceMail experienceMail = new ExperienceMail();
        Experience experience = experienceRepository.findByIdAndDeletedIsFalse(experienceId);
        BeanUtils.copyProperties(experienceMailDTO, experienceMail);
        experienceMail.setOrgId(orgId);
        experienceMail.setProjectId(projectId);
        experienceMail.setExperienceId(experienceId);
        Date now = new Date();
        experienceMail.setCreatedBy(operatorId);
        experienceMail.setCreatedAt(now);
        experienceMail.setLastModifiedBy(operatorId);
        experienceMail.setLastModifiedAt(now);
        experienceMail.setStatus(EntityStatus.ACTIVE);

        return experienceMailRepository.save(experienceMail);
    }

    /**
     * 经验教训邮件列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 邮件列表
     */
    public Page<ExperienceMailListDTO> getList(
        Long orgId,
        Long projectId,
        PageDTO pageDTO
    ) {
        List<ExperienceMailListDTO> mailList = new ArrayList<>();
        Page<ExperienceMail> mailList2 = experienceMailRepository.findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId, pageDTO.toPageable());

        for (ExperienceMail dto : mailList2.getContent()) {
            ExperienceMailListDTO item = BeanUtils.copyProperties(dto, new ExperienceMailListDTO());
            mailList.add(item);
        }

        Page pageList = new PageImpl<>(
            mailList,
            pageDTO.toPageable(),
            mailList2.getTotalElements());
        return pageList;
    }
}
