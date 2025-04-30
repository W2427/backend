package com.ose.tasks.domain.model.service.drawing.builder;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SubDrawingEntityBuilder implements WBSEntityBuilder<SubDrawing> {
    @Override
    public SubDrawing build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public SubDrawing build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, SubDrawing entity, EntitySubTypeRule rule) {
        return null;
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
    public WBSImportLogDTO<SubDrawing> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator) {
        return null;
    }

    @Override
    public String checkParent(Project project, Row row, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public SubDrawing checkRule(Long orgId, Long projectId, SubDrawing entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public SubDrawing generateQrCode(SubDrawing entity) {
        return null;
    }
}
