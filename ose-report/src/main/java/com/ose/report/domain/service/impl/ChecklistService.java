package com.ose.report.domain.service.impl;

import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.report.domain.repository.ChecklistItemRepository;
import com.ose.report.domain.repository.ChecklistRepository;
import com.ose.report.domain.repository.ReportTemplateRepository;
import com.ose.report.domain.service.ChecklistInterface;
import com.ose.report.dto.ChecklistDTO;
import com.ose.report.dto.ChecklistInfoDTO;
import com.ose.report.dto.ChecklistItemDTO;
import com.ose.report.dto.ChecklistItemImportDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.entity.Checklist;
import com.ose.report.entity.ChecklistItem;
import com.ose.report.entity.ChecklistSimulation;
import com.ose.report.entity.Template;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 检查单服务
 */
@Component
public class ChecklistService extends BaseChecklistService implements ChecklistInterface {

    /**
     * 检查单 操作仓库
     */
    private final ChecklistRepository checklistRepository;

    /**
     * 检查项 操作仓库
     */
    private final ChecklistItemRepository checklistItemRepository;

    /**
     * 报表模板 操作仓库
     */
    private final ReportTemplateRepository reportTemplateRepository;

    /**
     * 构造方法
     *
     * @param checklistRepository      检查单操作仓库
     * @param checklistItemRepository  检查项操作仓库
     * @param reportTemplateRepository 报表模板操作仓库
     */
    @Autowired
    public ChecklistService(ChecklistRepository checklistRepository,
                            ChecklistItemRepository checklistItemRepository,
                            ReportTemplateRepository reportTemplateRepository) {

        this.checklistRepository = checklistRepository;
        this.checklistItemRepository = checklistItemRepository;
        this.reportTemplateRepository = reportTemplateRepository;
    }

    /**
     * 查询检查单（按ID）
     *
     * @param checklistId 检查单ID
     * @return 检查单分页数据
     */
    @Override
    public Checklist search(Long checklistId) {

        Optional<Checklist> optionalChecklist = checklistRepository.findById(checklistId);

        return optionalChecklist.orElse(null);
    }

    /**
     * 查询检查单（按条件）（分页）
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param condition  检索条件
     * @param pagination 分页参数
     * @return 检查单分页数据
     */
    @Override
    public Page<Checklist> search(Long orgId, Long projectId, String condition, PageDTO pagination) {

        Page<Checklist> searchResult;

        // 无检索条件，查询全部。
        // 有检索条件，根据检索条件进行查询。
        if (StringUtils.isEmpty(condition)) {
            searchResult = checklistRepository.findAllByOrgIdAndProjectId(orgId, projectId, pagination.toPageable());

        } else {
            searchResult =
                checklistRepository.findChecklistWithOrgIdAndProjectIdAndCondition(
                    orgId, projectId, condition, pagination.toPageable());
        }

        return searchResult;
    }

    /**
     * 创建检查单
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param checklistDTO 检查单数据
     * @return 创建完成的检查单
     */
    @Override
    public Checklist create(Long orgId, Long projectId, ChecklistDTO checklistDTO) {

        Checklist checklist = BeanUtils.copyProperties(checklistDTO, new Checklist());
        checklist.setOrgId(orgId);
        checklist.setProjectId(projectId);

        return checklistRepository.save(checklist);
    }

    /**
     * 更新检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param checklistId  检查单ID
     * @param checklistDTO 检查单数据
     * @return 更新完成的检查单
     */
    @Override
    public Checklist update(Long orgId, Long projectId, Long checklistId, ChecklistDTO checklistDTO) {

        Checklist checklist = BeanUtils.copyProperties(checklistDTO, new Checklist());
        checklist.setId(checklistId);
        checklist.setOrgId(orgId);
        checklist.setProjectId(projectId);

        return checklistRepository.save(checklist);
    }

    /**
     * 更新检查单预览文件
     *
     * @param checklistId 检查单ID
     * @param previewFile 检查单预览文件路径, fileId
     * @return 更新完成的检查单
     */
    @Override
    public Checklist updatePreviewFile(Long checklistId, Long previewFile) {

        // 查询检查单
        Checklist checklist = checklistRepository.findById(checklistId).orElse(null);

        // 更新检查单预览文件
        checklist.setPreviewFile(previewFile);

        // 更新并返回
        return checklistRepository.save(checklist);
    }

    /**
     * 删除检查单
     *
     * @param checklistId 检查单ID
     */
    @Override
    public void delete(Long checklistId) {

        // 删除检查单
        checklistRepository.deleteById(checklistId);

        // 删除关联的检查项
        checklistItemRepository.deleteByChecklistId(checklistId);
    }

    /**
     * 查询检查项
     *
     * @param checklistId 检查单ID
     * @param pagination  分页参数
     * @return 检查项分页数据
     */
    @Override
    public Page<ChecklistItem> searchItem(Long checklistId, PageDTO pagination) {

        return checklistItemRepository.findAllByChecklistId(checklistId, pagination.toPageable());
    }

    /**
     * 创建检查项
     *
     * @param checklistId      检查单ID
     * @param checklistItemDTO 检查项数据
     * @return 创建完成的检查项
     */
    @Override
    public ChecklistItem createItem(Long checklistId, ChecklistItemDTO checklistItemDTO) {

        ChecklistItem checklistItem = BeanUtils.copyProperties(checklistItemDTO, new ChecklistItem());
        checklistItem.setChecklistId(checklistId);
        checklistItem.setStatus(EntityStatus.ACTIVE);

        return checklistItemRepository.save(checklistItem);
    }

    /**
     * 更新检查项
     *
     * @param checklistItemId  检查项ID
     * @param checklistItemDTO 检查项数据
     * @return 更新完成的检查项
     */
    @Override
    public ChecklistItem updateItem(Long checklistItemId, ChecklistItemDTO checklistItemDTO) {

        ChecklistItem checklistItem = BeanUtils.copyProperties(checklistItemDTO, new ChecklistItem());
        checklistItem.setId(checklistItemId);
        checklistItem.setStatus(EntityStatus.ACTIVE);

        return checklistItemRepository.save(checklistItem);
    }

    /**
     * 删除检查项
     *
     * @param checklistItemId 检查项ID
     */
    @Override
    public void deleteItem(Long checklistItemId) {

        checklistItemRepository.deleteById(checklistItemId);
    }

    /**
     * 导入检查项
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param checklistItemDTOs 检查单检查项数组
     * @return 创建完成的检查项
     */
    @Override
    public List<ChecklistItemImportDTO> saveImportItem(Long orgId,
                                                       Long projectId,
                                                       List<ChecklistItemImportDTO> checklistItemDTOs) {

        Map<String, Long> serialStore = new HashMap<>();

        // 处理所有检查项
        for (ChecklistItemImportDTO checklistItemDTO : checklistItemDTOs) {

            // 根据编号，查询检查单ID
            String serial = checklistItemDTO.getSerial();
            Long checklistId = serialStore.get(serial);

            // 临时存储中无对应的检查单ID时，进行按编号查询。
            if (checklistId == null) {

                Checklist checkList =
                    checklistRepository.findByOrgIdAndProjectIdAndSerial(orgId, projectId, serial);

                if (checkList == null || StringUtils.isEmpty(checkList.getSerial())) {
                    checklistId = 0L;
                } else {
                    checklistId = checkList.getId();
                }

                serialStore.put(serial, checklistId);
            }

            // 检查单ID为空字符串时，判断为数据异常。
            // 否则，创建检查项。
            if ("".equals(checklistId)) {

                Map<String, String> errors = new HashMap<>();
                errors.put("serial", "Not found.");
                checklistItemDTO.setErrors(errors);

            } else {
                ChecklistItem checklistItem = BeanUtils.copyProperties(checklistItemDTO, new ChecklistItem());
                checklistItem.setChecklistId(checklistId);
                checklistItem.setStatus(EntityStatus.ACTIVE);

                checklistItemRepository.save(checklistItem);
            }
        }

        return checklistItemDTOs;
    }

    /**
     * 组装空白/预览检查单报表数据
     *
     * @param checklistId 检查单ID
     * @return 空白/预览检查单报表数据
     */
    @Override
    public ChecklistReportDTO assemblePreviewReportData(Long checklistId) {

        // 获取检查单基本信息
        Checklist checklist = checklistRepository.findById(checklistId).orElse(null);

        if (checklist == null) {
            return null;
        }

        // 获取模板信息
        List<Template> templates = reportTemplateRepository.findByIdIn(new Long[]{
            checklist.getHeaderTemplateId(),
            checklist.getSignatureTemplateId()
        });

        // 模板未找到时处理结束
        if (templates == null || templates.size() == 0) {
            return null;
        }

        // 获取检查项获取数据
        List<ChecklistItem> checklistItems = checklistItemRepository.findAllByChecklistIdOrderByItemNo(checklistId);

        // 组装数据
        return this.assembleReportData(checklist, templates, checklistItems, new ChecklistInfoDTO());
    }

    /**
     * 引用数据处理
     *
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @param <T>      引用数据类型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(Map<Long, Object> included,
                                                             List<T> entities) {

        Set<Long> checklistIdSet = new HashSet<>();

        // 取得所有引用检查单的 ID
        for (T entity : entities) {

            // 来源模拟检查单
            if (entity instanceof ChecklistSimulation) {
                ChecklistSimulation checklistSimulation = (ChecklistSimulation) entity;
                checklistIdSet.add(checklistSimulation.getChecklistId());
            }
        }

        // 查询引用的检查单的基本信息
        List<Checklist> checklists =
            (List<Checklist>) checklistRepository.findAllById(new ArrayList<>(checklistIdSet));

        // 将应用目标用户信息设置到 included 字典中
        for (Checklist checklist : checklists) {
            included.put(checklist.getId(), checklist);
        }

        return included;
    }
}
