package com.ose.tasks.domain.model.service;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.SubconRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.dto.SubconDTO;
import com.ose.tasks.entity.Subcon;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class SubconService implements SubconInterface {
    private final static Logger logger = LoggerFactory.getLogger(SubconService.class);
    private final SubconRepository subconRepository;
    private final UploadFeignAPI uploadFeignAPI;

    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;
    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    @Autowired
    public SubconService(
        UploadFeignAPI uploadFeignAPI,
        SubconRepository subconRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository) {
        this.subconRepository = subconRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }

    /**
     * 创建分包商信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param subconDTO  分包商信息
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        SubconDTO subconDTO
    ) {

        Subcon subcon = new Subcon();
        Date now = new Date();
        BeanUtils.copyProperties(subconDTO, subcon);

        if (subconDTO.getSubContractorLogo() != null) {
            logger.error("供货商1 保存docs服务->开始");
            JsonObjectResponseBody<FileES> attachment =
                uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    subconDTO.getSubContractorLogo(),
                    new FilePostDTO());

            logger.error("供货商1 保存docs服务->结束");
            subcon.setSubContractorLogo(LongUtils.parseLong(attachment.getData().getId()));
        }

        subcon.setOrgId(orgId);
        subcon.setProjectId(projectId);
        subcon.setCreatedAt(now);
        subcon.setCreatedBy(operatorId);
        subcon.setLastModifiedAt(now);
        subcon.setLastModifiedBy(operatorId);
        subcon.setStatus(EntityStatus.ACTIVE);

        subconRepository.save(subcon);

    }

    /**
     * 获取分包商列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 分包商列表
     */
    @Override
    public List<Subcon> list(
        Long orgId,
        Long projectId
    ) {

        return subconRepository.findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
    }

    /**
     * 获取分包商列表（分页）。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页参数
     * @return 带分页的分包商列表
     */
    @Override
    public Page<Subcon> search(Long orgId, Long projectId, PageDTO pageDTO) {
        return subconRepository
            .findByOrgIdAndProjectIdAndDeletedIsFalseOrderByCreatedAtDesc(orgId, projectId, pageDTO.toPageable());
    }

    /**
     * 获取分包商详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconId  分包商ID
     * @return 分包商详情
     */
    @Override
    public Subcon get(
        Long orgId,
        Long projectId,
        Long subconId
    ) {

        return subconRepository.findByIdAndDeletedIsFalse(subconId);
    }

    /**
     * 更新分包商信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param subconDTO  更新信息
     */
    @Override
    public void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long subconId,
        SubconDTO subconDTO
    ) {

        Subcon subcon = subconRepository.findByIdAndDeletedIsFalse(subconId);

        if (subcon == null) {
            throw new NotFoundError("subcon is not found");
        }

        if (subconDTO.getName() != null) {
            subcon.setName(subconDTO.getName());
        }
        if (subconDTO.getFullName() != null) {
            subcon.setFullName(subconDTO.getFullName());
        }

        if (subconDTO.getWebsite() != null) {
            subcon.setWebsite(subconDTO.getWebsite());
        }

        if (subconDTO.getAddress() != null) {
            subcon.setAddress(subconDTO.getAddress());
        }

        if (subconDTO.getIntelligence() != null) {
            subcon.setIntelligence(subconDTO.getIntelligence());
        }

        if (subconDTO.getSubContractorLogo() != null) {
            logger.error("供货商2 保存docs服务->开始");
            JsonObjectResponseBody<FileES> attachment =
                uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    subconDTO.getSubContractorLogo(),
                    new FilePostDTO());
            logger.error("供货商2 保存docs服务->结束");
            subcon.setSubContractorLogo(LongUtils.parseLong(attachment.getData().getId()));
        }

        subcon.setLastModifiedAt(new Date());
        subcon.setLastModifiedBy(operatorId);

        subconRepository.save(subcon);
    }

    /**
     * 删除分包商。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconId  分包商ID
     */
    @Override
    public void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long subconId
    ) {

        Subcon subcon = subconRepository.findByIdAndDeletedIsFalse(subconId);

        if (subcon == null) {
            throw new NotFoundError("subcon is not found");
        }

        Date now = new Date();

        subcon.setLastModifiedAt(now);
        subcon.setLastModifiedBy(operatorId);
        subcon.setDeletedAt(now);
        subcon.setDeletedBy(operatorId);
        subcon.setDeleted(true);
        subcon.setStatus(EntityStatus.DELETED);

        subconRepository.save(subcon);
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param included 引用数据映射表
     * @param entity   返回结果
     * @param <T>      数据实体泛型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        T entity
    ) {

        if (entity == null) {
            return included;
        }

        WelderSimplified welder = null;
        if (entity instanceof WelderSimplified) {
            welder = (WelderSimplified) entity;
        }

        if (welder != null && welder.getSubConId() != null) {
            Subcon subcon = subconRepository.findByIdAndDeletedIsFalse(welder.getSubConId());
            if (subcon != null) {
                included.put(subcon.getId(), subcon);
            }
        }

        return included;
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param included 引用数据映射表
     * @param entities 返回结果列表
     * @param <T>      数据实体泛型
     * @return 应用数据
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {

        if (entities == null || entities.size() == 0) {
            return included;
        }

        List<Long> subconIds = new ArrayList<>();

        for (T entity : entities) {
            if (entity instanceof WelderSimplified && ((WelderSimplified) entity).getSubConId() != null) {
                subconIds.add(((WelderSimplified) entity).getSubConId());
            }
        }

        if (subconIds.size() == 0) {
            return included;
        }

        List<Subcon> subcons = subconRepository.findByIdInAndDeletedIsFalse(subconIds);

        for (Subcon subcon : subcons) {
            included.put(subcon.getId(), subcon);
        }

        return included;
    }

    @Override
    public Iterable<Subcon> findAllByIds(List<Long> ids) {
        return subconRepository.findAllById(ids);
    }

    /**
     * 获取工作班组列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 工作班组列表
     */
    @Override
    public List<BpmActivityInstanceState> teamNameList(
        Long orgId,
        Long projectId
    ) {
        List<BpmActivityInstanceState> bpmActivityInstancesList = bpmActivityInstanceStateRepository.findTeamNameByProjectId(projectId);

        return bpmActivityInstancesList;
    }

    /**
     * 获取工作场地列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 工作场地列表
     */
    @Override
    public List<BpmActivityInstanceState> workSiteNameList(
        Long orgId,
        Long projectId
    ) {
        List<BpmActivityInstanceState> bpmActivityInstancesList = bpmActivityInstanceStateRepository.findByProjectIdAndStatus(projectId);

        return bpmActivityInstancesList;
    }
}
