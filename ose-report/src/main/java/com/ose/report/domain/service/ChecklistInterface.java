package com.ose.report.domain.service;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ChecklistDTO;
import com.ose.report.dto.ChecklistItemDTO;
import com.ose.report.dto.ChecklistItemImportDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.entity.Checklist;
import com.ose.report.entity.ChecklistItem;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 检查单服务接口
 */
public interface ChecklistInterface extends EntityInterface {

    /**
     * 查询检查单（按ID）
     *
     * @param checklistId 检查单ID
     * @return 检查单分页数据
     */
    Checklist search(Long checklistId);

    /**
     * 查询检查单（按条件）（分页）
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param condition  检索条件
     * @param pagination 分页参数
     * @return 检查单分页数据
     */
    Page<Checklist> search(Long orgId, Long projectId, String condition, PageDTO pagination);

    /**
     * 创建检查单
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param checklistDTO 检查单数据
     * @return 创建完成的检查单
     */
    Checklist create(Long orgId, Long projectId, ChecklistDTO checklistDTO);

    /**
     * 更新检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param checklistId  检查单ID
     * @param checklistDTO 检查单数据
     * @return 更新完成的检查单
     */
    Checklist update(Long orgId, Long projectId, Long checklistId, ChecklistDTO checklistDTO);

    /**
     * 更新检查单预览文件
     *
     * @param checklistId 检查单ID
     * @param previewFile 检查单预览文件路径, 文件 ID
     * @return 更新完成的检查单
     */
    Checklist updatePreviewFile(Long checklistId, Long previewFile);

    /**
     * 删除检查单
     *
     * @param checklistId 检查单ID
     */
    void delete(Long checklistId);

    /**
     * 查询检查项（分页）
     *
     * @param checklistId 检查单ID
     * @param pagination  分页参数
     * @return 检查项分页数据
     */
    Page<ChecklistItem> searchItem(Long checklistId, PageDTO pagination);

    /**
     * 创建检查项
     *
     * @param checklistId      检查单ID
     * @param checklistItemDTO 检查项数据
     * @return 创建完成的检查项
     */
    ChecklistItem createItem(Long checklistId, ChecklistItemDTO checklistItemDTO);

    /**
     * 更新检查项
     *
     * @param checklistItemId  检查项ID
     * @param checklistItemDTO 检查项数据
     * @return 更新完成的检查项
     */
    ChecklistItem updateItem(Long checklistItemId, ChecklistItemDTO checklistItemDTO);

    /**
     * 删除检查项
     *
     * @param checklistItemId 检查项ID
     */
    void deleteItem(Long checklistItemId);

    /**
     * 导入检查项
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param checklistItems 检查单检查项数组
     * @return 创建完成的检查项
     */
    List<ChecklistItemImportDTO> saveImportItem(Long orgId,
                                                Long projectId,
                                                List<ChecklistItemImportDTO> checklistItems);

    /**
     * 组装空白/预览检查单报表数据
     *
     * @param checklistId 检查单ID
     * @return 空白/预览检查单报表数据
     */
    ChecklistReportDTO assemblePreviewReportData(Long checklistId);
}
