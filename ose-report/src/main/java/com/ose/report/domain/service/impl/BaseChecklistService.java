package com.ose.report.domain.service.impl;

import com.ose.report.dto.ChecklistInfoDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.dto.subreport.DynamicColumnSubReport;
import com.ose.report.dto.subreport.DynamicReportItem;
import com.ose.report.dto.subreport.TemplateSubReport;
import com.ose.report.entity.Checklist;
import com.ose.report.entity.ChecklistItem;
import com.ose.report.entity.Template;
import com.ose.report.vo.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查单服务 通用处理
 */
public class BaseChecklistService {

    /**
     * 默认构造方法
     */
    public BaseChecklistService() {
    }

    /**
     * 组装检查单报表数据
     *
     * @param checklist        检查单基本信息
     * @param templates        模板信息
     * @param checklistItems   检查单项目
     * @param checklistInfoDTO 检查单信息
     * @return 组装检查单报表数据
     */
    ChecklistReportDTO assembleReportData(Checklist checklist,
                                          List<Template> templates,
                                          List<ChecklistItem> checklistItems,
                                          ChecklistInfoDTO checklistInfoDTO) {

        Template templateTitle = null;
        Template templateSignature = null;

        for (Template template : templates) {

            if (Position.TITLE.equals(template.getPosition())) {
                templateTitle = template;
            } else if (Position.SIGNATURE.equals(template.getPosition())) {
                templateSignature = template;
            }

            if (templateTitle != null && templateSignature != null) {
                break;
            }
        }

        // 组装数据
        ChecklistReportDTO reportData = new ChecklistReportDTO();

        reportData.setSerial(checklist.getSerial()); // 检查单序号
        reportData.setNeedStore(false); // 不需要存储

        // 设置表头
        Map<String, Object> headerMappingData = new HashMap<>();
        headerMappingData.put("project", checklist.getTitle());
        headerMappingData.put("reportNo", checklist.getSerial());
        headerMappingData.put("reportName", checklist.getName());

        headerMappingData.put("tagNo", checklistInfoDTO.getTagNo());
        headerMappingData.put("location", checklistInfoDTO.getLocation());
        headerMappingData.put("tagDesc", checklistInfoDTO.getTagDesc());
        headerMappingData.put("sysNo", checklistInfoDTO.getSysNo());
        headerMappingData.put("cpNo", checklistInfoDTO.getCpNo());
        headerMappingData.put("dwgNo", checklistInfoDTO.getDwgNo());
        headerMappingData.put("subSysDesc", checklistInfoDTO.getSubSysDesc());

        reportData.setHeader(new TemplateSubReport(templateTitle, headerMappingData));

        // 设置明细
        DynamicReportItem[] columnItems = {
            new DynamicReportItem("item", "Item (项目)", "string", 37),
            new DynamicReportItem("description", "Description (描述)", "string", 373),
            new DynamicReportItem("yes", "YES", "string", 35),
            new DynamicReportItem("no", "NO", "string", 35),
            new DynamicReportItem("na", "N/A", "string", 35),
        };

        List<Map<String, Object>> itemsMappingData = new ArrayList<>();
        Map<String, Object> itemMappingData;

        for (ChecklistItem checklistItem : checklistItems) {

            itemMappingData = new HashMap<>();
            itemMappingData.put("item", checklistItem.getItemNo());
            itemMappingData.put("description", checklistItem.getDescription());

            itemsMappingData.add(itemMappingData);
        }

        DynamicColumnSubReport[] dynamicColumnSubReports = {
            new DynamicColumnSubReport(columnItems, itemsMappingData)
        };
        reportData.setDetail(dynamicColumnSubReports);

        // 设置备注
        reportData.setSummary(new TemplateSubReport());

        // 设置签字栏
        reportData.setFooter(new TemplateSubReport(templateSignature, new HashMap<>()));

        // 返回报表数据
        return reportData;
    }
}
