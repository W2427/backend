package com.ose.tasks.domain.model.service.builder;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.bpm.CategoryRuleType;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.tasks.vo.wbs.WBSImportLogStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.unit.*;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import com.ose.util.BeanUtils;

import java.util.*;

import static com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;

/**
 * WBS 实体导入逻辑接口。
 *
 * @param <T> WBS 实体范型
 */
public interface WBSEntityBuilder<T extends WBSEntityBase> {

    String HYPHEN = "-";

    /**
     * WBS 实体构建逻辑。
     *
     * @param config  工作表读取配置
     * @param project 项目信息
     * @param row     工作表中数据行实例
     * @return WBS 实体实例
     */
    T build(SheetConfig config, Project project, Row row);

    /**
     * WBS 实体构建逻辑。
     *
     * @param columnConfigs  工作表读取配置
     * @param project 项目信息
     * @param row     工作表中数据行实例
     * @return WBS 实体实例
     */
    default T build(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){
        return null;
    }

    /**
     * WBS 实体构建逻辑。
     *
     * @param config  工作表读取配置
     * @param project 项目信息
     * @param row     工作表中数据行实例
     * @param entity  数据实体
     */
    T build(SheetConfig config, Project project, Row row, T entity, EntitySubTypeRule rule);


    /**
     * 取得父级 Map<String, String>
     *
     * @return
     */
    default Map<String, String> getParentNo(WBSEntityImportSheetConfigBuilder.SheetConfig config, Row row) {
        Map<String, String> parentNoMap = new HashMap<>();

        // 模块以外的父级信息
        for (int colIndex = 2; colIndex < config.getMaxParents(); colIndex++) {
            String parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, colIndex));
            if (!StringUtils.isEmpty(parentNodeNo)) {
//                parentNoMap.put(isoHierarchyTypes[colIndex], parentNodeNo);
            }
        }
        return parentNoMap;
    }


    /**
     * 取得父级 Map<String, String>
     *
     * @param project
     * @return
     */
    default Map<String, String> getParentNo(SheetConfig config, Project project, Row row, Row titleRow, EntitySubTypeRule rule) {
        Map<String, String> parentNoMap = new HashMap<>();
        String content1 = WorkbookUtils.readAsString(row, 0);
        String content2 = WorkbookUtils.readAsString(row, 1);

        // 读取上级节点编号
        String parentNodeNo = null;

        if (rule != null) {
            if (config.getSheetName().equals("Components")) {
                // 父级
                if (rule.getParentType() != null) {
//                    if ("ISO" == rule.getParentType()) {
//                        parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, 0));
//                    }
//                    if ("SPOOL" == rule.getParentType()) {
//                        parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, 1));
//                    }
                } else {
                    if ((content1 != null && !content1.equals("")) && (content2 != null && !content2.equals(""))) {
                        parentNodeNo = StringUtils.trim(content2);
                    } else if ((content2 != null && !content2.equals(""))) {
                        parentNodeNo = StringUtils.trim(content2);
                    } else if ((content1 != null && !content1.equals(""))) {
                        parentNodeNo = StringUtils.trim(content1);
                    }
                }
            } else {
                // 父级
//                if ("ISO" == rule.getParentType()) {
//                    parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, 1));
//                } else if ("SPOOL" == rule.getParentType()) {
//                    parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, 0));
//                }
            }
        }

        if (!StringUtils.isEmpty(parentNodeNo)) {
//            parentNoMap.put("PIPING", parentNodeNo);
        }

        return parentNoMap;
    }

    /**
     * 取得 NodeNo
     *
     * @return
     */
    default String getNodeNo(WBSEntityImportSheetConfigBuilder.SheetConfig config,
                             String displayName, Row row, Long projectId, EntitySubTypeRule rule) {


        return displayName;
    }


    /**
     * 删除 实体及关联信息
     *
     * @param entity
     * @param operator
     * @param project
     * @param <T>
     */
    <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project);


    /**
     * 检查各个实体类型是否符合 属性要求，如是否满足规则，已存在 实体类型等
     *
     * @return
     */
    <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes, Set<EntitySubTypeRule> entityCategoryRules);


    void deleteNoneHierarchyEntity(Long noneHierarchyEntityId);

    /**
     * 设置 ISO PN ID号，更新WBS-ENTRY
     */
    default void setIsoIdsAndWbs(Long projectId, Long entityId) {
    }

    EntitySubTypeRule getRule(
        Long orgId,
        Long projectId,
        WBSEntityImportSheetConfigBuilder.SheetConfig config,
        Row row,
        Set<EntitySubTypeRule> entityCategoryRules
    );

    /**
     * 取得父级 Map<String, String>
     *
     * @param categoryRuleType
     * @return
     */
    default Map<String, String> getParentNo(
        CategoryRuleType categoryRuleType,
        Row row,
        EntitySubTypeRule rule
    ) {
        Map<String, String> parentNoMap = new HashMap<>();
        // 读取上级节点编号
        String parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, 0));
        if (!StringUtils.isEmpty(parentNodeNo)) {
//            parentNoMap.put("PIPING", parentNodeNo);
        }
        return parentNoMap;
    }

    /**
     * WBS 实体构建逻辑。
     *
     * @param columnConfigs  工作表读取配置
     * @param project 项目信息
     * @param row     工作表中数据行实例
     * @return WBS 实体实例
     */
    WBSImportLogDTO<T> buildWbs(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator);

    /**
     * 检查实体父级信息是否存在
     *
     * @return
     */
    String checkParent(Project project, Row row, EntitySubTypeRule rule);

    default WBSImportLogDTO<T> buildDefault(SheetConfig config, Project project, Row row,
                                            List<ColumnImportConfig> columnConfigs,
                                            OperatorDTO operator){
        WBSImportLogDTO<T> wbsImportLogDTO = new WBSImportLogDTO<T>();
        Map<String, String> parentNodeNoMap = new HashMap<>();
        if(CollectionUtils.isEmpty(columnConfigs)) {
            wbsImportLogDTO.setStatus(WBSImportLogStatus.SKIP);
            wbsImportLogDTO.setSkipCount(1);
            wbsImportLogDTO.setErrorStr("Column Setting is EMPTY, SKIPPED");
            return wbsImportLogDTO;
        }

        Boolean isDeleteMark = false;
        Map<String, Object> entityMap = new HashMap<>();
        for(ColumnImportConfig cic : columnConfigs){
            String columnName = cic.getColumnName();
            Object cellVal = null;
            if (cic.getFixedValue() != null) {
                cellVal = cic.getFixedValue().replaceAll("\\[\\[","").replaceAll("\\]\\]","");
            } else if(cic.getColumnNo()!=null) {
                cellVal = WorkbookUtils.readXlsCell(row, cic.getColumnNo(), cic.getDataType());
            } else if(cic.getTopFixedCellCoor() != null) {
                String[] coorArr = cic.getTopFixedCellCoor().replaceAll("\\[","").replaceAll("\\]","").split(",");

                cellVal = WorkbookUtils.readXlsCell(row.getSheet().getRow(Integer.parseInt(coorArr[0])),Integer.parseInt(coorArr[1]),cic.getDataType());
            }
            if(cellVal != null && cellVal instanceof String) {

                cellVal = cellVal==null?null:cellVal.toString().replaceAll("\\t","");
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
        entityMap.put("deleted",false);
        entityMap.put("companyId", project.getCompanyId());

        String strParentElStr = config.getParentElFormula();
        String parentHierarchyType = config.getParentHierarchyType();


///------------------------
        /*--------
        获取 最多4个 父级 层级类型 和 对应的 父级 计算的值
         */
//        String strParent1ElStr = config.getParent1ElFormula();// "${#function +\"_\"+ #docType}";
//        String parent1HierarchyType = config.getParent1HierarchyType();
//        String strParent2ElStr = config.getParent2ElFormula();// "${#function +\"_\"+ #docType}";
//        String parent2HierarchyType = config.getParent2HierarchyType();
//
//        String strParent3ElStr = config.getParent3ElFormula();// "${#function +\"_\"+ #docType}";
//        String parent3HierarchyType = config.getParent2HierarchyType();
//
//        String strParent4ElStr = config.getParent4ElFormula();// "${#function +\"_\"+ #docType}";
//        String parent4HierarchyType = config.getParent4HierarchyType();

        String keyNoElStr = config.getKeyNoElFormula();// "${#dwgNo}";
        String deleteElStr = config.getDeleteElFormula();// "${#remark}"

        String entityTypeStr = config.getEntityType();
        String entitySubTypeStr = config.getEntitySubType();

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(keyNoElStr, new TemplateParserContext("${","}"));

        EvaluationContext context = new StandardEvaluationContext();

        for (Map.Entry<String, Object> entry : entityMap.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        String keyNo = expression.getValue(context, String.class);
        if(StringUtils.isEmpty(keyNo) || keyNo.equals("")) {
            wbsImportLogDTO.setStatus(WBSImportLogStatus.SKIP);
            wbsImportLogDTO.setSkipCount(1);
            wbsImportLogDTO.setErrorStr("Key Word is EMPTY, SKIPPED");
            return wbsImportLogDTO;
        }

        if(!StringUtils.isEmpty(parentHierarchyType) && !StringUtils.isEmpty(strParentElStr)) {
            expression = parser.parseExpression(strParentElStr, new TemplateParserContext("${", "}"));
            String parentNo = expression.getValue(context, String.class);
            if(!StringUtils.isEmpty(parentNo)) {
                parentNodeNoMap.put(parentHierarchyType, parentNo);
            }
        }

//        //ParentNo1
//        if(!StringUtils.isEmpty(parent1HierarchyType) && !StringUtils.isEmpty(strParent1ElStr)) {
//            expression = parser.parseExpression(strParent1ElStr, new TemplateParserContext("${", "}"));
//            String parent1No = expression.getValue(context, String.class);
//            if(!StringUtils.isEmpty(parent1No)) {
//                parentNodeNoMap.put(parent1HierarchyType, parent1No);
//            }
//        }
//
//        //ParentNo2
//        if(!StringUtils.isEmpty(parent2HierarchyType) && !StringUtils.isEmpty(strParent2ElStr)) {
//            expression = parser.parseExpression(strParent2ElStr, new TemplateParserContext("${", "}"));
//            String parent2No = expression.getValue(context, String.class);
//            if(!StringUtils.isEmpty(parent2No)) {
//                parentNodeNoMap.put(parent2HierarchyType, parent2No);
//            }
//        }
//
//        //ParentNo3
//        if(!StringUtils.isEmpty(parent3HierarchyType) && !StringUtils.isEmpty(strParent3ElStr)) {
//            expression = parser.parseExpression(strParent3ElStr, new TemplateParserContext("${", "}"));
//            String parent3No = expression.getValue(context, String.class);
//            if(!StringUtils.isEmpty(parent3No)) {
//                parentNodeNoMap.put(parent3HierarchyType, parent3No);
//            }
//        }
//
//        //ParentNo4
//        if(!StringUtils.isEmpty(parent4HierarchyType) && !StringUtils.isEmpty(strParent4ElStr)) {
//            expression = parser.parseExpression(strParent4ElStr, new TemplateParserContext("${", "}"));
//            String parent4No = expression.getValue(context, String.class);
//            if(!StringUtils.isEmpty(parent4No)) {
//                parentNodeNoMap.put(parent4HierarchyType, parent4No);
//            }
//        }
        if(MapUtils.isEmpty(parentNodeNoMap)) {
            wbsImportLogDTO.setStatus(WBSImportLogStatus.SKIP);
            wbsImportLogDTO.setSkipCount(1);
            wbsImportLogDTO.setErrorStr("Parent No is EMPTY, SKIPPED");
            return wbsImportLogDTO;
        }

        if(!StringUtils.isEmpty(deleteElStr)) {
            expression = parser.parseExpression(deleteElStr, new TemplateParserContext("${","}"));
            String deleteStr = expression.getValue(context, String.class);
            if(!StringUtils.isEmpty(deleteStr) && deleteStr.toUpperCase().startsWith("DELETE")) {
                isDeleteMark = true;
            }
        }

        if(entityMap.containsKey("entityType") && !StringUtils.isEmpty(entityTypeStr)) {
            expression = parser.parseExpression(entityTypeStr, new TemplateParserContext("${","}"));
            String entityType = expression.getValue(context, String.class);
            if(!StringUtils.isEmpty(entityType)) {
                wbsImportLogDTO.setEntityType(entityType);
            } else {
                wbsImportLogDTO.setStatus(WBSImportLogStatus.SKIP);
                wbsImportLogDTO.setSkipCount(1);
                wbsImportLogDTO.setErrorStr("Entity Type is EMPTY, SKIPPED");
                return wbsImportLogDTO;
            }
        }

        if(entityMap.containsKey("entitySubType") && !StringUtils.isEmpty(entitySubTypeStr)) {
            expression = parser.parseExpression(entitySubTypeStr, new TemplateParserContext("${","}"));
            String entitySubType = expression.getValue(context, String.class);
            if(!StringUtils.isEmpty(entitySubType)) {
                wbsImportLogDTO.setEntitySubType(entitySubType);
            } else {
                wbsImportLogDTO.setStatus(WBSImportLogStatus.SKIP);
                wbsImportLogDTO.setSkipCount(1);
                wbsImportLogDTO.setErrorStr("Entity Sub Type is EMPTY, SKIPPED");
                return wbsImportLogDTO;
            }
        }

        Object entity = config.getRepository().findByProjectIdAndNoAndDeletedIsFalse(project.getId(), keyNo).orElse(null);
        if(entity == null) {
            try {
                entity = config.getEntityTypeClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if(isDeleteMark) {
            config.getBuilder().delete((T) entity, operator, project);
            wbsImportLogDTO.setStatus(WBSImportLogStatus.DELETED);
            wbsImportLogDTO.setDeletedCount(1);
            wbsImportLogDTO.setDoneCount(1);
            wbsImportLogDTO.setWbsEntityBase((T)entity);
            return wbsImportLogDTO;
        }
        if (entityMap.get("lengthUnit") != null) {
            entityMap.put("lengthUnit", LengthUnit.getByName(entityMap.get("lengthUnit").toString()));
        }
        if (entityMap.get("widthUnit") != null) {
            entityMap.put("widthUnit", LengthUnit.getByName(entityMap.get("widthUnit").toString()));
        }
        if (entityMap.get("heightUnit") != null) {
            entityMap.put("heightUnit", LengthUnit.getByName(entityMap.get("heightUnit").toString()));
        }
        if (entityMap.get("weightUnit") != null) {
            entityMap.put("weightUnit", WeightUnit.getByName(entityMap.get("weightUnit").toString()));
        }
        if (entityMap.get("npsUnit") != null) {
            entityMap.put("npsUnit", LengthUnit.getByName(entityMap.get("npsUnit").toString()));
        }
        if (entityMap.get("designPressureUnit") != null) {
            entityMap.put("designPressureUnit", PressureUnit.getByName(entityMap.get("designPressureUnit").toString()));
        }
        if (entityMap.get("designTemperatureUnit") != null) {
            entityMap.put("designTemperatureUnit", TemperatureUnit.getByName(entityMap.get("designTemperatureUnit").toString()));
        }
        if (entityMap.get("operatePressureUnit") != null) {
            entityMap.put("operatePressureUnit", PressureUnit.getByName(entityMap.get("operatePressureUnit").toString()));
        }
        if (entityMap.get("operateTemperatureUnit") != null) {
            entityMap.put("operateTemperatureUnit", TemperatureUnit.getByName(entityMap.get("operateTemperatureUnit").toString()));
        }
        if (entityMap.get("insulationThicknessUnit") != null) {
            entityMap.put("insulationThicknessUnit", LengthUnit.getByName(entityMap.get("insulationThicknessUnit").toString()));
        }
        if (entityMap.get("testPressureUnit") != null) {
            entityMap.put("testPressureUnit", PressureUnit.getByName(entityMap.get("testPressureUnit").toString()));
        }
        if (entityMap.get("nde") != null) {
            entityMap.put("nde", NDEType.valueOf(entityMap.get("nde").toString()));
        }

        if (entityMap.get("paintingAreaUnit") != null) {
            entityMap.put("paintingAreaUnit", AreaUnit.getByName(entityMap.get("paintingAreaUnit").toString()));
        }

        if (entityMap.get("thickness1Unit") != null) {
            entityMap.put("thickness1Unit", LengthUnit.getByName(entityMap.get("thickness1Unit").toString()));
        }
        if (entityMap.get("thickness2Unit") != null) {
            entityMap.put("thickness2Unit", LengthUnit.getByName(entityMap.get("thickness2Unit").toString()));
        }

        BeanUtils.copyProperties(entityMap, entity);//, ignoredFields);
        wbsImportLogDTO.setStatus(WBSImportLogStatus.DONE);
        wbsImportLogDTO.setDoneCount(1);
        wbsImportLogDTO.setWbsEntityBase((T)entity);
        wbsImportLogDTO.setParentNodeNoMap(parentNodeNoMap);
        return wbsImportLogDTO;

    }

    T checkRule(Long orgId, Long projectId, T entity, EntitySubTypeRule rule);

    T generateQrCode(T entity);


}
