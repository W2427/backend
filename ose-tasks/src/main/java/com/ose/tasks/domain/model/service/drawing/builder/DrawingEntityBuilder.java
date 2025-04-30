package com.ose.tasks.domain.model.service.drawing.builder;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DrawingEntityBuilder implements WBSEntityBuilder<Drawing> {
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

//        Object entity;



        Map<String, Object> entityMap = new HashMap<>();
        for(ColumnImportConfig cic : columnConfigs){
            String columnName = cic.getColumnName();

            if (columnName.equals("drawingType")) {
                entityMap.put(columnName, cic.getFixedValue().replaceAll("^\\[+|\\]+$", ""));
            } else {
                Object cellVal = WorkbookUtils.readXlsCell(row, cic.getColumnNo(), cic.getDataType());
                cellVal = cellVal.toString().replaceAll("\\t","");
                entityMap.put(columnName, cellVal);
            }

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


///------------------------
        String strParentElStr = config.getParentElFormula();// "${#function +\"_\"+ #docType}";
        String keyNoElStr = config.getKeyNoElFormula();// "${#dwgNo}";

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(keyNoElStr, new TemplateParserContext("${","}"));
//        Expression expression1 = parser.parseExpression(str, new TemplateParserContext());

        EvaluationContext context = new StandardEvaluationContext();

        for (Map.Entry<String, Object> entry : entityMap.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }



        String keyNo = expression.getValue(context, String.class);
        if(StringUtils.isEmpty(keyNo)) {
            return null;
        }
        expression = parser.parseExpression(strParentElStr, new TemplateParserContext("${","}"));
        String parentNo = expression.getValue(context, String.class);
//        entityMap.put("parentNo",parentNo);
//        if (errors.size() > 0) {
//
//            WorkbookUtils
//                .setCellValue(sheet, rowNo, errorColNo, String.join("；", errors))
//                .setCellStyle(errorStyle);
//
//            failedCount++;
//        }

        Object entity = config.getRepository().findByProjectIdAndNoAndDeletedIsFalse(project.getId(), keyNo).orElse(null);
        if(entity == null) {
            try {
                entity = config.getEntityTypeClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        BeanUtils.copyProperties(entityMap, entity);

        Drawing dwg = (Drawing) config.getRepository().save(entity);
        dwg.setParentNo(parentNo);
        dwg.setPreparePerson(((Drawing)entity).getPreparePerson());
        dwg.setReviewPerson(((Drawing)entity).getReviewPerson());
        dwg.setApprovePerson(((Drawing)entity).getApprovePerson());
        dwg.setCoSignPerson(((Drawing)entity).getCoSignPerson());
        dwg.setQcPerson(((Drawing)entity).getQcPerson());
        dwg.setDocPerson(((Drawing)entity).getDocPerson());
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
    public EntitySubTypeRule getRule(Long orgId, Long projectId, WBSEntityImportSheetConfigBuilder.SheetConfig config, Row row, Set<EntitySubTypeRule> entityCategoryRules) {
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
