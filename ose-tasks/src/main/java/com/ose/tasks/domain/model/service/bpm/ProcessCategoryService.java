package com.ose.tasks.domain.model.service.bpm;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessCategoryRepository;
import com.ose.tasks.dto.bpm.ProcessCategoryDTO;
import com.ose.tasks.entity.bpm.BpmProcessCategory;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;

@Component
public class ProcessCategoryService implements ProcessCategoryInterface {


    /**
     * 工序阶段 操作仓库
     */
    private final BpmProcessCategoryRepository processCategoryRepository;

    /**
     * 构造方法
     *
     * @param processCategoryRepository 工序阶段 操作仓库
     */
    @Autowired
    public ProcessCategoryService(BpmProcessCategoryRepository processCategoryRepository) {
        this.processCategoryRepository = processCategoryRepository;
    }

    /**
     * 创建工序分类
     */
    @Override
    public BpmProcessCategory create(Long orgId, Long projectId, ProcessCategoryDTO categoryDTO) {
        BpmProcessCategory category = BeanUtils.copyProperties(categoryDTO, new BpmProcessCategory());
        category.setProjectId(projectId);
        category.setOrgId(orgId);
        category.setCreatedAt();
        category.setStatus(EntityStatus.ACTIVE);
        return processCategoryRepository.save(category);
    }

    /**
     * 查询工序分类列表
     */
    @Override
    public Page<BpmProcessCategory> getList(Long orgId, Long projectId, PageDTO page) {
        return processCategoryRepository.findByStatusAndProjectIdAndOrgId(EntityStatus.ACTIVE, projectId, orgId, page.toPageable());
    }

    /**
     * 查询工序分类详细信息
     */
    @Override
    public BpmProcessCategory getEntitySubType(Long orgId, Long projectId, Long id) {
        Optional<BpmProcessCategory> op = processCategoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 删除工序分类
     */
    @Override
    public boolean delete(Long orgId, Long projectId, Long id) {
        Optional<BpmProcessCategory> op = processCategoryRepository.findById(id);
        if (op.isPresent()) {
            BpmProcessCategory category = op.get();
            category.setStatus(EntityStatus.DELETED);
            category.setLastModifiedAt();
            processCategoryRepository.save(category);
        }
        return false;
    }

    /**
     * 修改工序分类
     */
    @Override
    public BpmProcessCategory modify(Long orgId, Long projectId, Long id, ProcessCategoryDTO categoryDTO) {
        Optional<BpmProcessCategory> result = processCategoryRepository.findById(id);
        if (result.isPresent()) {
            BpmProcessCategory category = BeanUtils.copyProperties(categoryDTO, result.get());
            category.setLastModifiedAt();
            return processCategoryRepository.save(category);
        }
        return null;
    }

    /**
     * 根据工序分类名称查询工序分类
     */
    @Override
    public BpmProcessCategory findByName(Long orgId, Long projectId, String nameCn) {
        return processCategoryRepository.findByOrgIdAndProjectIdAndNameCnAndStatus(orgId, projectId, nameCn, EntityStatus.ACTIVE);
    }


}
