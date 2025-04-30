package com.ose.tasks.domain.model.service.sheet;

import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.tasks.domain.model.service.builder.*;
import com.ose.util.CryptoUtils;
import com.ose.util.SpringContextUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WBSEntityImportSheetConfigBuilder {


//    private final WBSEntityBuilder<ISOEntity> isoEntityBuilder;


//    private final WBSEntityBuilder<SpoolEntity> spoolEntityBuilder;

//    private final WBSEntityBuilder<PipePieceEntity> pipePieceEntityBuilder;


//    private final WBSEntityBuilder<WeldEntity> weldEntityBuilder;


//    private final WBSEntityBuilder<ComponentEntity> componentEntityWBSEntityBuilder;


//    private final WBSEntityBuilder<PressureTestPackageEntityBase> pressureTestPackageEntityWBSEntityBuilder;


//    private final WBSEntityBuilder<CleanPackageEntityBase> cleanPackageEntityWBSEntityBuilder;


//    private final WBSEntityBuilder<SubSystemEntityBase> subSystemEntityWBSEntityBuilder;


    private final SubSystemEntityRepository subSystemEntityRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public WBSEntityImportSheetConfigBuilder(
//        WBSEntityBuilder<ISOEntity> isoEntityBuilder,
//        WBSEntityBuilder<SpoolEntity> spoolEntityBuilder,
//        WBSEntityBuilder<PipePieceEntity> pipePieceEntityBuilder,
//        WBSEntityBuilder<WeldEntity> weldEntityBuilder,
//        WBSEntityBuilder<ComponentEntity> componentEntityWBSEntityBuilder,
//        WBSEntityBuilder<PressureTestPackageEntityBase> pressureTestPackageEntityWBSEntityBuilder,
//        WBSEntityBuilder<CleanPackageEntityBase> cleanPackageEntityWBSEntityBuilder,
//        WBSEntityBuilder<SubSystemEntityBase> subSystemEntityWBSEntityBuilder,
        SubSystemEntityRepository subSystemEntityRepository
    ) {
//        this.isoEntityBuilder = isoEntityBuilder;
//        this.spoolEntityBuilder = spoolEntityBuilder;
//        this.pipePieceEntityBuilder = pipePieceEntityBuilder;
//        this.weldEntityBuilder = weldEntityBuilder;
//        this.componentEntityWBSEntityBuilder = componentEntityWBSEntityBuilder;
//        this.pressureTestPackageEntityWBSEntityBuilder = pressureTestPackageEntityWBSEntityBuilder;
//        this.cleanPackageEntityWBSEntityBuilder = cleanPackageEntityWBSEntityBuilder;
//        this.subSystemEntityWBSEntityBuilder = subSystemEntityWBSEntityBuilder;
        this.subSystemEntityRepository = subSystemEntityRepository;
    }

    /**
     * 读取实体数据导入工作表配置列表。
     *
     * @param settings 设置工作表
     * @return 配置列表
     */
    public List<SheetConfig> readConfigs(Sheet settings) throws ClassNotFoundException {

        if (settings == null) {
            throw new ValidationError("no settings sheet found");
        }

        Workbook workbook = settings.getWorkbook();
        Sheet sheet;
        List<String> values = new ArrayList<>();
        String checkSum = settings.getRow(1).getCell(0).getStringCellValue();
        int rowCount = settings.getPhysicalNumberOfRows();
        Row row;
        List<SheetConfig> configs = new ArrayList<>();
        SheetConfig config;

        for (int rowIndex = 3; rowIndex < rowCount; rowIndex++) {

            row = settings.getRow(rowIndex);

            config = new SheetConfig();

            config.sheetName = WorkbookUtils.readAsString(row, 0);

            if (StringUtils.isEmpty(config.sheetName)) {
                continue;
            }

            String entityTypeClassStr = StringUtils.trim(WorkbookUtils.readAsString(row, 2));
            config.entityType =
                WorkbookUtils.readAsString(row, 1)
            ;
            Class clazz = Class.forName(entityTypeClassStr);
//            String className = StringUtils.lowerFirst(entityTypeClassStr.substring(entityTypeClassStr.lastIndexOf(".") + 1));
            config.entityTypeClass = clazz;

            String builderClassStr = StringUtils.trim(WorkbookUtils.readAsString(row, 3));
            Class builderClazz = Class.forName(builderClassStr);
            String builderClassNameStr = builderClassStr.substring(builderClassStr.lastIndexOf(".") + 1);
            String builderClassName = StringUtils.lowerFirst(builderClassNameStr);//


            config.builder = (WBSEntityBuilder) SpringContextUtils.getBean(builderClassName, builderClazz);

            String repositoryClassStr = StringUtils.trim(WorkbookUtils.readAsString(row, 4));
            if(!StringUtils.isEmpty(repositoryClassStr)) {
                String repositoryClassNameStr = repositoryClassStr.substring(repositoryClassStr.lastIndexOf(".") + 1);
                String repositoryClassName = StringUtils.lowerFirst(repositoryClassNameStr);
                config.repository = (WBSEntityBaseRepository) SpringContextUtils.getBean(repositoryClassName, Class.forName(repositoryClassStr));
            }
            config.headerRows = WorkbookUtils.readAsInteger(row, 5);
            config.maxParents = WorkbookUtils.readAsInteger(row, 6);
//            config.columns = WorkbookUtils.readAsInteger(row, 7);
            config.columnSettingSheetName = WorkbookUtils.readAsString(row, 7);
            String dbTableStr = WorkbookUtils.readAsString(row, 8);
            config.db = dbTableStr.split("\\.")[0];
            config.table = dbTableStr.split("\\.")[1];
            config.parentHierarchyType = WorkbookUtils.readAsString(row, 11);
            config.keyNoElFormula = WorkbookUtils.readAsString(row, 10);
            config.parentElFormula = WorkbookUtils.readAsString(row, 9);

//            config.parent1HierarchyType = WorkbookUtils.readAsString(row, 9);
//            config.parent1ElFormula = WorkbookUtils.readAsString(row, 10);
//
//            config.parent2HierarchyType = WorkbookUtils.readAsString(row, 11);
//            config.parent2ElFormula = WorkbookUtils.readAsString(row, 12);
//
//            config.parent3HierarchyType = WorkbookUtils.readAsString(row, 13);
//            config.parent3ElFormula = WorkbookUtils.readAsString(row, 14);
//
//            config.parent4HierarchyType = WorkbookUtils.readAsString(row, 15);
//            config.parent4ElFormula = WorkbookUtils.readAsString(row, 16);

            config.deleteElFormula = WorkbookUtils.readAsString(row, 12);
            config.contentHeaderRows = WorkbookUtils.readAsInteger(row, 13);
            config.columns = WorkbookUtils.readAsInteger(row, 14);

//            config.entityType = WorkbookUtils.readAsString(row, 21);
//            config.entitySubType = WorkbookUtils.readAsString(row, 22);

            if ((sheet = workbook.getSheet(config.sheetName)) == null) {
                continue;
            }

            config.sheet = sheet;

            values.add(config.toString());

            configs.add(config);
        }

//        if (!checkSum.equals(CryptoUtils.md5(String.join("\r\n", values)))) {
//            throw new ValidationError("settings sheet has been modified");
//        }

        return configs;
    }

    /**
     * WBS 实体导入工作表配置。
     */
    public static class SheetConfig<B extends WBSEntityBuilder, R extends WBSEntityBaseRepository> {


        private String sheetName;

        private String columnSettingSheetName;

        private String entityType;


        private int headerRows;


        private int maxParents;


        private int columns;


        private Sheet sheet;

        private Class entityTypeClass;

        private String db;

        private String table;

        private String parentHierarchyType;

        private String parentElFormula;

        private String keyNoElFormula;

//        private String parent1HierarchyType;//第一个父级的层级类型
//        private String parent1ElFormula;
//
//        private String parent2HierarchyType;//第二个父级的层级类型
//        private String parent2ElFormula;
//
//        private String parent3HierarchyType;//第三个父级的层级类型
//        private String parent3ElFormula;
//
//        private String parent4HierarchyType;//第四个父级的层级类型
//        private String parent4ElFormula;

        private String deleteElFormula;

        private String entitySubType;

        private int contentHeaderRows;

        private B builder;


        private R repository;

        public String getSheetName() {
            return sheetName;
        }

        public String getEntityType() {
            return entityType;
        }

        public int getHeaderRows() {
            return headerRows;
        }

        public int getMaxParents() {
            return maxParents;
        }

        public int getColumns() {
            return columns;
        }

        public Sheet getSheet() {
            return sheet;
        }

        public B getBuilder() {
            return builder;
        }

        public R getRepository() {
            return repository;
        }



        @Override
        public String toString() {
            return this.sheetName
                + "," + this.entityType
                + "," + this.headerRows
                + "," + this.maxParents
                + "," + this.columns
                ;
        }

        public Class getEntityTypeClass() {
            return entityTypeClass;
        }

        public void setEntityTypeClass(Class entityTypeClass) {
            this.entityTypeClass = entityTypeClass;
        }

        public String getColumnSettingSheetName() {
            return columnSettingSheetName;
        }

        public void setColumnSettingSheetName(String columnSettingSheetName) {
            this.columnSettingSheetName = columnSettingSheetName;
        }

        public String getDb() {
            return db;
        }

        public void setDb(String db) {
            this.db = db;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getParentElFormula() {
            return parentElFormula;
        }

        public void setParentElFormula(String parentElFormula) {
            this.parentElFormula = parentElFormula;
        }

        public String getKeyNoElFormula() {
            return keyNoElFormula;
        }

        public String getParentHierarchyType() {
            return parentHierarchyType;
        }

        public void setParentHierarchyType(String parentHierarchyType) {
            this.parentHierarchyType = parentHierarchyType;
        }

        public void setKeyNoElFormula(String keyNoElFormula) {
            this.keyNoElFormula = keyNoElFormula;
        }

        public String getDeleteElFormula() {
            return deleteElFormula;
        }

        public void setDeleteElFormula(String deleteElFormula) {
            this.deleteElFormula = deleteElFormula;
        }

        public String getEntitySubType() {
            return entitySubType;
        }

        public void setEntitySubType(String entitySubType) {
            this.entitySubType = entitySubType;
        }

        public int getContentHeaderRows() {
            return contentHeaderRows;
        }

        public void setContentHeaderRows(int contentHeaderRows) {
            this.contentHeaderRows = contentHeaderRows;
        }
    }

}
