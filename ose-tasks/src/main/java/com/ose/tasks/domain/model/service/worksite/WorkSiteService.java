package com.ose.tasks.domain.model.service.worksite;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.repository.worksite.WorkSiteRepository;
import com.ose.tasks.dto.worksite.WorkSiteMoveDTO;
import com.ose.tasks.dto.worksite.WorkSitePatchDTO;
import com.ose.tasks.dto.worksite.WorkSitePostDTO;
import com.ose.tasks.entity.worksite.WorkSite;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.util.ValueUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作场地管理服务。
 */
@Component
public class WorkSiteService implements WorkSiteInterface {


    private final WorkSiteRepository workSiteRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public WorkSiteService(
        WorkSiteRepository workSiteRepository,
        WBSEntryStateRepository wbsEntryStateRepository
    ) {
        this.workSiteRepository = workSiteRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }

    /**
     * 查询工作场地信息。
     *
     * @param companyId 公司 ID
     * @param parentId  上级 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 工作场地分页数据
     */
    @Override
    public Page<WorkSite> search(
        final Long companyId,
        final Long parentId,
        final Long projectId,
        final Pageable pageable
    ) {

        if (parentId != null && projectId != null) {
            return workSiteRepository
                .findByCompanyIdAndProjectIdAndParentIdAndDeletedIsFalse(companyId, projectId, parentId, pageable);
        } else if (parentId != null) {
            return workSiteRepository
                .findByCompanyIdAndParentIdAndDeletedIsFalse(companyId, parentId, pageable);
        } else if (projectId != null) {
            return workSiteRepository
                .findByCompanyIdAndProjectIdAndDepthAndDeletedIsFalse(companyId, projectId, 0, pageable);
        } else {
            return workSiteRepository
                .findByCompanyIdAndDepthAndDeletedIsFalse(companyId, 0, pageable);
        }

    }

    /**
     * 取得工作场地详细信息。
     *
     * @param companyId  公司 ID
     * @param projectId  项目 ID
     * @param workSiteId 工作场地 ID
     * @return 工作场地详细信息
     */
    @Override
    public WorkSite get(Long companyId, Long projectId, Long workSiteId) {
        return (
            projectId == null
                ? workSiteRepository.findByCompanyIdAndIdAndDeletedIsFalse(companyId, workSiteId)
                : workSiteRepository.findByCompanyIdAndProjectIdAndIdAndDeletedIsFalse(companyId, projectId, workSiteId)
        ).orElse(null);
    }

    /**
     * 取得工作场地信息。
     *
     * @param companyId  公司 ID
     * @param projectId  项目 ID
     * @param workSiteId 工作场地 ID
     * @param version    更新版本号
     * @return 工作场地信息
     */
    private WorkSite get(Long companyId, Long projectId, Long workSiteId, long version) {

        WorkSite workSite = get(companyId, projectId, workSiteId);

        if (workSite == null) {
            throw new NotFoundError();
        }

        if (workSite.getVersion() != version) {
            throw new ConflictError();
        }

        return workSite;
    }

    /**
     * 保存工作场地信息。
     *
     * @param operator 操作者信息
     * @param workSite 工作场地信息
     * @return 工作场地信息
     */
    private WorkSite save(OperatorDTO operator, WorkSite workSite) {

        if (workSiteRepository.existsByCompanyIdAndParentIdAndProjectIdAndNameAndIdNotAndDeletedIsFalse(
            workSite.getCompanyId(),
            workSite.getParentId(),
            workSite.getProjectId(),
            workSite.getName(),
            workSite.getId()
        )) {
            throw new DuplicatedError();
        }

        workSite.setLastModifiedAt();
        workSite.setLastModifiedBy(operator.getId());

        workSite = workSiteRepository.save(workSite);

        wbsEntryStateRepository.updateWorkSiteName(
            workSite.getId(),
            workSite.getName()
        );

        return workSite;
    }

    /**
     * 取得上级场地信息。
     *
     * @param companyId 公司 ID
     * @param parentId  上级 ID
     * @param projectId 项目 ID
     * @return 上级场地信息
     */
    private WorkSite getParent(Long companyId, Long parentId, Long projectId) {

        if (parentId == null) {
            return null;
        }

        WorkSite parent = get(companyId, null, parentId);

        if (parent == null) {
            throw new BusinessError("未找到上级场地");
        }

        if (parent.getProjectId() != null && !parent.getProjectId().equals(projectId)) {
            throw new BusinessError("必须与上级场地属于相同的项目");
        }

        return parent;
    }

    /**
     * 设置排序权重。
     *
     * @param workSite       工作场地信息
     * @param nextWorkSiteId 下一个工作场地 ID
     */
    private void setSortWeight(final WorkSite workSite, final Long nextWorkSiteId) {


        if (nextWorkSiteId == null) {

            workSiteRepository
                .findFirstByCompanyIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                    workSite.getCompanyId(),
                    workSite.getParentId()
                )
                .ifPresent(previousWorkSite -> workSite.setSort(previousWorkSite.getSort() + 1));


        } else {

            WorkSite nextWorkSite = get(workSite.getCompanyId(), workSite.getParentId(), nextWorkSiteId);

            if (nextWorkSite == null) {
                throw new BusinessError("指定的下一个场地不存在");
            }

            workSite.setSort(nextWorkSite.getSort());

            workSiteRepository.increaseSortScore(
                workSite.getCompanyId(),
                workSite.getParentId(),
                nextWorkSite.getSort()
            );

        }

    }

    /**
     * 创建工作场地信息。
     *
     * @param operator    操作者信息
     * @param companyId   公司 ID
     * @param projectId   项目 ID
     * @param parentId    上级 ID
     * @param workSiteDTO 工作场地数据传输对象
     * @return 工作场地信息
     */
    @Override
    @Transactional
    public WorkSite create(
        final OperatorDTO operator,
        final Long companyId,
        final Long projectId,
        final Long parentId,
        final WorkSitePostDTO workSiteDTO
    ) {

        final WorkSite parent = getParent(companyId, parentId, projectId);

        WorkSite workSite = new WorkSite();

        workSiteDTO.setName(StringUtils.trim(workSiteDTO.getName()));

        BeanUtils.copyProperties(workSiteDTO, workSite);

        workSite.setCompanyId(companyId);
        workSite.setProjectId(projectId);
        workSite.setParentId(parentId);
        workSite.setPath(parentId == null ? "/" : String.format("%s%s/", parent.getPath(), parentId));
        workSite.setDepth(parentId == null ? 0 : (parent.getDepth() + 1));
        setSortWeight(workSite, workSiteDTO.getNextWorkSiteId());
        workSite.setCreatedAt();
        workSite.setCreatedBy(operator.getId());
        workSite.setStatus(EntityStatus.ACTIVE);

        return save(operator, workSite);
    }

    /**
     * 更新工作场地信息。
     *
     * @param operator    操作者信息
     * @param companyId   公司 ID
     * @param projectId   项目 ID
     * @param workSiteId  工作场地 ID
     * @param version     更新版本号
     * @param workSiteDTO 工作场地数据传输对象
     * @return 工作场地信息
     */
    @Override
    @Transactional
    public WorkSite update(
        final OperatorDTO operator,
        final Long companyId,
        final Long projectId,
        final Long workSiteId,
        final long version,
        final WorkSitePatchDTO workSiteDTO
    ) {

        if (!StringUtils.isEmpty(workSiteDTO.getName())) {
            workSiteDTO.setName(StringUtils.trim(workSiteDTO.getName()));
        }

        WorkSite workSite = get(companyId, projectId, workSiteId, version);
        workSite.setName(ValueUtils.notEmpty(workSiteDTO.getName(), workSite.getName()));
        workSite.setAddress(ValueUtils.notEmpty(workSiteDTO.getAddress(), workSite.getAddress()));
        workSite.setRemarks(ValueUtils.notEmpty(workSiteDTO.getRemarks(), workSite.getRemarks()));
        setSortWeight(workSite, workSiteDTO.getNextWorkSiteId());

        return save(operator, workSite);
    }

    /**
     * 移动工作场地。
     *
     * @param operator    操作者信息
     * @param companyId   公司 ID
     * @param projectId   项目 ID
     * @param workSiteId  工作场地 ID
     * @param version     更新版本号
     * @param workSiteDTO 工作场地数据传输对象
     * @return 工作场地信息
     */
    @Override
    @Transactional
    public WorkSite move(
        final OperatorDTO operator,
        final Long companyId,
        final Long projectId,
        final Long workSiteId,
        final long version,
        final WorkSiteMoveDTO workSiteDTO
    ) {

        final Long parentId = workSiteDTO.getParentId();

        final WorkSite parent = getParent(companyId, parentId, projectId);

        WorkSite workSite = get(companyId, projectId, workSiteId, version);

        if ((parentId == null && workSite.getParentId() == null)
            || (parentId != null && parentId.equals(workSite.getParentId()))) {
            return workSite;
        }

        if (parent == null) {
            workSite.setParentId(null);
            workSite.setPath("/");
            workSite.setDepth(0);
        } else {
            workSite.setParentId(parentId);
            workSite.setPath(String.format("%s%s/", parent.getPath(), parentId));
            workSite.setDepth(parent.getDepth() + 1);
        }

        setSortWeight(workSite, workSiteDTO.getNextWorkSiteId());
        workSite.setLastModifiedAt();
        workSite.setLastModifiedBy(operator.getId());

        return workSiteRepository.save(workSite);
    }

    /**
     * 删除工作场地信息。
     *
     * @param operator   操作者信息
     * @param companyId  公司 ID
     * @param projectId  项目 ID
     * @param workSiteId 工作场地 ID
     * @param version    更新版本号
     */
    @Override
    @Transactional
    public void delete(
        final OperatorDTO operator,
        final Long companyId,
        final Long projectId,
        final Long workSiteId,
        final long version
    ) {

        WorkSite workSite = get(companyId, projectId, workSiteId, version);

        workSite.setDeletedAt();
        workSite.setDeletedBy(operator.getId());

        workSiteRepository.save(workSite);
    }

}
