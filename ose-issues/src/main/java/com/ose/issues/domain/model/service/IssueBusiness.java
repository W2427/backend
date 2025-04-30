package com.ose.issues.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.exception.DuplicatedError;
import com.ose.issues.domain.model.repository.IssuePropertyRepository;
import com.ose.issues.domain.model.repository.IssueRecordRepository;
import com.ose.issues.domain.model.repository.IssueRepository;
import com.ose.issues.domain.model.repository.PropertyDefinitionRepository;
import com.ose.issues.dto.PropertyDTO;
import com.ose.issues.entity.IssueBase;
import com.ose.issues.entity.IssueProperty;
import com.ose.issues.entity.IssueRecord;
import com.ose.issues.vo.IssueType;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

import java.util.*;

/**
 * 问题数据通用业务逻辑。
 */
public class IssueBusiness {

    // 数据仓库
    private final IssueRepository issueRepository;
    private final IssuePropertyRepository issuePropertyRepository;
    private final IssueRecordRepository issueRecordRepository;
    private final PropertyDefinitionRepository propertyDefinitionRepository;

    // Feign API
    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    public IssueBusiness(
        IssueRepository issueRepository,
        IssuePropertyRepository issuePropertyRepository,
        IssueRecordRepository issueRecordRepository,
        PropertyDefinitionRepository propertyDefinitionRepository,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.issueRepository = issueRepository;
        this.issuePropertyRepository = issuePropertyRepository;
        this.issueRecordRepository = issueRecordRepository;
        this.propertyDefinitionRepository = propertyDefinitionRepository;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 检查编号是否已被使用。
     *
     * @param projectId 项目 ID
     * @param issueType 问题类型
     * @param no        问题编号
     */
    void checkNoAvailability(Long projectId, IssueType issueType, String no) {
        checkNoAvailability(projectId, issueType, no, 0L);
    }

    /**
     * 检查编号是否已被使用。
     *
     * @param projectId 项目 ID
     * @param issueType 问题类型
     * @param no        问题编号
     * @param issueId   问题 ID
     */
    void checkNoAvailability(Long projectId, IssueType issueType, String no, Long issueId) {

        no = StringUtils.trim(no);

        if (StringUtils.isEmpty(no)) {
            return;
        }

        if (issueRepository
            .existsByProjectIdAndTypeAndNoAndIdIsNotAndDeletedIsFalse(projectId, issueType, no, issueId)) {
            throw new DuplicatedError("编号重复"); // TODO
        }

    }

    /**
     * 保存附件及自定义属性。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param issue      问题信息
     * @param attachment 附件临时文件名
     * @param properties 自定义属性列表
     */
    void saveAttachmentAndProperties(
        final Long orgId,
        final Long projectId,
        final IssueBase issue,
        final String attachment,
        final List<PropertyDTO> properties
    ) {

        final Long issueId = issue.getId();
        String issueAttachment = issue.getAttachment();

        // 保存附件
        if (attachment != null) {
            if (issueAttachment == null || !issueAttachment.equals(attachment)) {
                issue.setAttachment(
                    uploadFeignAPI
                        .save(orgId.toString(), projectId.toString(), attachment, new FilePostDTO())
                        .getData().getId().toString()
                );
            }
        }

        // 保存自定义属性
        if (properties != null) {

            Set<Long> propertyIDs = new HashSet<>();

            properties.forEach(property ->
                propertyIDs.add(property.getPropertyDefinitionId()));

            // 删除取消选择的自定义属性
            issuePropertyRepository
                .deleteByProjectIdAndIssueIdAndPropertyIdNotIn(projectId, issueId, propertyIDs);

            // 取得已保存的自定义属性
            List<IssueProperty> savedIssueProperties = issuePropertyRepository
                .findByProjectIdAndIssueIdAndPropertyIdIn(projectId, issueId, propertyIDs);

            // 声明问题的自定义属性列表
            List<IssueProperty> issueProperties = new ArrayList<>();

            // 将已保存的自定义属性设置到自定义属性列表中，当未保存时，构造新的自定义属性
            properties.forEach(property -> {

                final Long propertyDefinitionId = property.getPropertyDefinitionId();

                if (savedIssueProperties.size() == 0
                    || !savedIssueProperties.removeIf(issueProperty -> {
                    if (propertyDefinitionId.equals(issueProperty.getPropertyId())) {
                        issueProperty.setValues(property.getValues());
                        issueProperty.setLastModifiedAt();
                        issueProperties.add(issueProperty);
                        return true;
                    }
                    return false;
                })
                    ) {

                    IssueProperty issueProperty = new IssueProperty(
                        issueId,
                        propertyDefinitionRepository
                            .findByIdAndDeletedIsFalse(propertyDefinitionId),
                        property.getValues()
                    );

                    issueProperty.setCreatedAt();
                    issueProperty.setLastModifiedAt();
                    issueProperty.setStatus(EntityStatus.ACTIVE);

                    issueProperties.add(issueProperty);
                }

            });

            if (issueProperties.size() > 0) {
                issuePropertyRepository.saveAll(issueProperties);
            }

            issue.setProperties(issueProperties);
        }

    }

    /**
     * 设置问题的属性列表。
     *
     * @param issues 问题列表
     * @return 问题列表
     */
    <T extends IssueBase> List<T> setProperties(Long projectId, final List<T> issues) {

        if (issues == null || issues.size() == 0) {
            return issues;
        }

        Set<Long> issueIDs = new HashSet<>();

        issues.forEach(issue -> issueIDs.add(issue.getId()));

        List<IssueProperty> properties = issuePropertyRepository.findByProjectIdAndIssueIdIn(projectId, issueIDs);

        if (properties.size() == 0) {
            return issues;
        }

        Map<Long, List<IssueProperty>> issueMap = new HashMap<>();

        properties.forEach(property -> issueMap
            .computeIfAbsent(property.getIssueId(), issueId -> new ArrayList<>())
            .add(property)
        );

        issues.forEach(issue -> issue.setProperties(issueMap.get(issue.getId())));

        return issues;
    }

    /**
     * 设置问题的属性列表。
     *
     * @param issues 问题分页数据
     * @return 问题分页数据
     */
    <T extends IssueBase> Page<T> setProperties(Long projectId, final Page<T> issues) {
        setProperties(projectId, issues.getContent());
        return issues;
    }

    /**
     * 设置问题的属性列表。
     *
     * @param issue 问题信息
     * @return 问题信息
     */
    <T extends IssueBase> T setProperties(Long projectId, final T issue) {
        setProperties(projectId, Collections.singletonList(issue));
        return issue;
    }

    /**
     * 保存问题操作记录。
     *
     * @param operatorId 操作者
     * @param issueId    问题 ID
     * @param content    操作内容
     * @return 问题操作记录信息
     */
    IssueRecord saveIssueRecord(
        final Long operatorId,
        final Long issueId,
        final String content
    ) {

        if (StringUtils.isEmpty(content)) {
            return null;
        }

        Iterable<IssueRecord> records = saveIssueRecords(
            operatorId,
            Collections.singletonList(new IssueRecord(issueId, content))
        );

        return records == null ? null : records.iterator().next();
    }

    /**
     * 保存问题操作记录。
     *
     * @param operatorId 操作者
     * @param records    操作内容列表
     * @return 问题操作记录列表
     */
    Iterable<IssueRecord> saveIssueRecords(
        final Long operatorId,
        final List<IssueRecord> records
    ) {

        if (records == null || records.size() == 0) {
            return null;
        }

        Iterator<IssueRecord> iterator = records.iterator();

        IssueRecord record;

        while (iterator.hasNext()) {

            record = iterator.next();

            if (record == null || StringUtils.isEmpty(record.getContent())) {
                iterator.remove();
                continue;
            }

            record.setCreatedAt();
            record.setCreatedBy(operatorId);
            record.setLastModifiedBy(operatorId);
            record.setLastModifiedAt();
            record.setStatus(EntityStatus.ACTIVE);
        }

        return issueRecordRepository.saveAll(records);
    }

}
