package com.ose.tasks.domain.model.service.plan.builder;

import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntityEntryDateRepository;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.entry.WBSEntityEntryDate;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class WBSEntityEntryDateBuilder implements WBSEntityBuilder<Drawing> {
    private static String NO_PARENT = "NO_PARENT";

    private static int header = 2;

    private final ProcessInterface processService;

    private final ProjectNodeRepository projectNodeRepository;
    private final WBSEntityEntryDateRepository wbsEntityEntryDateRepository;
    private final DrawingRepository drawingRepository;

    @Autowired
    public WBSEntityEntryDateBuilder(ProcessInterface processService, ProjectNodeRepository projectNodeRepository, WBSEntityEntryDateRepository wbsEntityEntryDateRepository, DrawingRepository drawingRepository) {
        this.processService = processService;
        this.projectNodeRepository = projectNodeRepository;
        this.wbsEntityEntryDateRepository = wbsEntityEntryDateRepository;
        this.drawingRepository = drawingRepository;
    }

    @Override
    public Drawing build(SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public Drawing build(SheetConfig config, Project project, Row row, Drawing entity, EntitySubTypeRule rule) {
        return null;
    }


    @Override
    public Drawing build(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        String drawingNo = "";

        if(CollectionUtils.isEmpty(columnConfigs)) {
            return null;
        }
        List<Map<String, Object>> entityMaps = new ArrayList<>();
        if(CollectionUtils.isEmpty(columnConfigs.get(0).getJsonColumns())) {
            Map<String, Object> entityMap = new HashMap<>();

            for (ColumnImportConfig cic : columnConfigs) {
                String columnName = cic.getColumnName();
                Object cellVal = WorkbookUtils.readXlsCell(row, cic.getColumnNo(), cic.getDataType());

                if ("entityNo".equalsIgnoreCase(columnName)){
                    drawingNo = cellVal.toString();
                }

                entityMap.put(columnName, cellVal);
            }
            entityMap.put("projectId", project.getId());
            entityMap.put("orgId", project.getOrgId());
            entityMap.put("lastModifiedAt", new Date());
            entityMap.put("createdAt", new Date());
            entityMap.put("createdBy", operator.getId());
            entityMap.put("lastModifiedBy", operator.getId());
            entityMap.put("status", EntityStatus.ACTIVE);
            entityMap.put("deleted", false);
            entityMap.put("companyId", project.getCompanyId());
            entityMaps.add(entityMap);
        } else {
            for(int i=0; i< columnConfigs.get(0).getJsonColumns().size(); i++) {
                Map<String, Object> entityMap = new HashMap<>();
                for (ColumnImportConfig cic : columnConfigs) {
                    String columnName = cic.getColumnName();
                    Object cellVal = null;

                    if("processNo".equalsIgnoreCase(columnName)) {
                        cellVal = WorkbookUtils.readXlsCell(
                            config.getSheet().getRow(header),
                            cic.getColumnNo()==null?cic.getJsonColumns().get(i):cic.getColumnNo(),
                            cic.getDataType()
                        );
                    } else if (columnName.contains("Date")) {
                        cellVal = WorkbookUtils.readAsDate(
                            row,
                            cic.getColumnNo()==null?cic.getJsonColumns().get(i):cic.getColumnNo(),
                            dateFormat
                        );
                    } else if ("entityNo".equalsIgnoreCase(columnName)){
                        if (cic.getColumnNo() != null) {
                            cellVal = WorkbookUtils.readXlsCell(
                                row,
                                cic.getColumnNo(),
                                cic.getDataType());

                            drawingNo = cellVal.toString();
                        }

                    } else {

                        if (cic.getColumnNo() != null) {
                            cellVal = WorkbookUtils.readXlsCell(
                                row,
                                cic.getColumnNo(),
                                cic.getDataType());
                        }
                    }
                    entityMap.put(columnName, cellVal);

                }
                entityMap.put("projectId", project.getId());
                entityMap.put("orgId", project.getOrgId());
                entityMap.put("lastModifiedAt", new Date());
                entityMap.put("createdAt", new Date());
                entityMap.put("createdBy", operator.getId());
                entityMap.put("lastModifiedBy", operator.getId());
                entityMap.put("status", EntityStatus.ACTIVE);
                entityMap.put("deleted", false);
                entityMap.put("companyId", project.getCompanyId());
                entityMaps.add(entityMap);
            }
        }

///------------------------
        Date engineerStartDate = null;
        Date engineerEndDate = null;
        for(Map<String, Object> entityMap: entityMaps) {
            String entityNo = (String)entityMap.get("entityNo");
            String processNo = (String) entityMap.get("processNo");
            if(StringUtils.isEmpty(entityNo) || StringUtils.isEmpty(processNo)) continue;
            String[] tmp = processNo.split("-");
            if(tmp.length < 2) {
                throw new ValidationError("Process No "+processNo + " is invalid");
            }
            String processStage = tmp[0];
            String processStr = tmp[1];

            //新增操作设置，engineerStartDate & engineerEndDate 读取entityMaps中processNo不为IAB的时间，取map其中的最小值为开始时间，最大时间为结束时间
            if (!processStr.contains("IAB")){
                Date startDate = (Date) entityMap.get("planStartDate");
                Date endDate = (Date) entityMap.get("planEndDate");
                // 取最小值逻辑
                if (engineerStartDate == null && startDate != null) {
                    engineerStartDate = startDate;
                }else if(engineerStartDate != null && startDate != null){
                    if (startDate.before(engineerStartDate)) {
                        engineerStartDate = startDate;
                    }
                }
                // 取最大值逻辑
                if (engineerEndDate == null && endDate != null) {
                    engineerEndDate = endDate;
                }else if (engineerEndDate != null && endDate != null){
                    if (endDate.after(engineerEndDate)) {
                        engineerEndDate = endDate;
                    }
                }
            }

            ProjectNode pn = projectNodeRepository.findByProjectIdAndNoAndDeletedIsFalse(project.getId(), entityNo).orElse(null);
            if (pn == null) {
                throw new ValidationError("Process No " + processNo + " is invalid");
            }
            BpmProcess bpmProcess = processService.getBpmProcess(project.getId(), processStage, processStr);
            if(bpmProcess == null) {
                throw new ValidationError("Process No " + processNo + " is invalid");
            }
            String parentNo = NO_PARENT;
            Long entityId = pn.getEntityId();
            WBSEntityEntryDate wbsEntityEntryDate =
                wbsEntityEntryDateRepository.findByProjectIdAndEntityIdAndProcessId(project.getId(), entityId, bpmProcess.getId());

            if(wbsEntityEntryDate == null) {
                wbsEntityEntryDate = new WBSEntityEntryDate();
            }

            BeanUtils.copyProperties(entityMap, wbsEntityEntryDate);
            wbsEntityEntryDate.setEntityId(entityId);
            wbsEntityEntryDate.setProcessId(bpmProcess.getId());
            wbsEntityEntryDateRepository.save(wbsEntityEntryDate);

        }

        Drawing dwg  = drawingRepository.findByProjectIdAndNoAndDeletedIsFalse(project.getId(), drawingNo).get();

        dwg.setEngineeringStartDate(engineerStartDate);
        dwg.setEngineeringFinishDate(engineerEndDate);

        drawingRepository.save(dwg);

        dwg.setParentNo(NO_PARENT);

        return dwg;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {

    }

    @Override
    public <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes, Set<EntitySubTypeRule> entityCategoryRules) {
        return null;
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {

    }

    @Override
    public EntitySubTypeRule getRule(Long orgId, Long projectId, SheetConfig config, Row row, Set<EntitySubTypeRule> entityCategoryRules) {
        return null;
    }

    @Override
    public WBSImportLogDTO<Drawing> buildWbs(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator) {
        return null;
    }

    @Override
    public String checkParent(Project project, Row row, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public Drawing checkRule(Long orgId, Long projectId, Drawing entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public Drawing generateQrCode(Drawing entity) {
        return null;
    }
}
