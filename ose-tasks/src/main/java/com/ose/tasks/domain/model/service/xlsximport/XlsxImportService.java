package com.ose.tasks.domain.model.service.xlsximport;

import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.xlsximport.CommonRepository;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class XlsxImportService implements XlsxImportInterface {
    private final static Logger logger = LoggerFactory.getLogger(XlsxImportService.class);


    private final CommonRepository columnImportConfigRepository;

    private final CommonRepository commonRepository;

    @Autowired
    public XlsxImportService(CommonRepository columnImportConfigRepository,
                             CommonRepository commonRepository) {
        this.columnImportConfigRepository = columnImportConfigRepository;
        this.commonRepository = commonRepository;
    }

    /**
     * 取得所有自定义属性信息。
     *
     * @param sheet     工作表信息 数据类型，TEXT,"INTEGER,DOUBLE,FLOAT,BOOLEAN,DATE
     * @return 列号-自定义属性映射表
     */
    @Override
    public List<ColumnImportConfig> getColumnDefinitions(
        final Sheet sheet,
        final String dbName,
        final String tableName,
        final Long orgId,
        final Long projectId,
        final String clazzName,
        final int columnHeaderRowNo// COLUMN_HEADER_ROW_NO
    ) {
        List<List<Object>> columnInfos = commonRepository.getColumnInfos(dbName, tableName);

        Map<String, String> columnDefinitionMap = new HashMap<>();

        columnInfos.forEach(columnInfo ->{
            String cName = (String) columnInfo.get(0);
            String cType = "";
            if (columnInfo.get(1) instanceof byte[]) {
                byte[] tmpArr = (byte[]) columnInfo.get(1);
                cType = new String(tmpArr);
            } else {
                cType = (String) columnInfo.get(1);
            }


            switch (cType) {
                case "varchar":
                case "longtext":
                case "char":
                case "longblob":
                case "mediumtext":
                case "mediumblob":
                case "text":
                case "blob":
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "TEXT");
                    break;

                case "bigint":
                case "int":
                case "smallint":
                case "tinyint":
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "INTEGER");
                    break;

                case "double":
                case "decimal":
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "DOUBLE");
                    break;

                case "float":
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "FLOAT");
                    break;

                case "bit":
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "BOOLEAN");
                    break;

                case "datetime":
                case "timestamp":
                case "time":
                case "date":
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "DATE");
                    break;

                default:
                    columnDefinitionMap.put(StringUtils.underscoreToCamelCase(cName), "TEXT");

            }

        });
//
//        Set<String> cableAlngFieldNames = Arrays.stream(CableAlng.class.getDeclaredFields()).
//            map(fld -> fld.getName()).collect(Collectors.toSet());
        List<ColumnImportConfig> result = new ArrayList<>();

        int rowNo = columnHeaderRowNo;
        Row row;

        do {
            if(rowNo > sheet.getLastRowNum()) break;
            row = sheet.getRow(rowNo);
            String columnName = WorkbookUtils.readAsString(row, 0);
            String isDisplayedStr = WorkbookUtils.readAsString(row, 4);
            Integer columnNo = null;//
            List<Integer> columnNos = new ArrayList<>();

            String topFixedCellCoorStr = null;
            String fixedVal = null;

            switch (row.getCell(1).getCellTypeEnum()) {
//                case STRING:
//                    String[] cnos = StringUtils.trim(WorkbookUtils.readAsString(row, 1)).split(",");
//                    for(String cno : cnos) {
//                        columnNos.add(Integer.parseInt(cno));
//                    }
//                    break;
                case STRING:
                    String cellStr = WorkbookUtils.readAsString(row, 1);
                    if(StringUtils.isEmpty(cellStr)) break;
                    if(cellStr.startsWith("[[") && cellStr.endsWith("]]")) { // fixed value
                        fixedVal = cellStr;
                    } else if(cellStr.startsWith("[") && cellStr.endsWith("]")) { // topcell
                        topFixedCellCoorStr = cellStr;
                    } else {
                        String[] cnos = StringUtils.trim(WorkbookUtils.readAsString(row, 1)).split(",");
                        for (String cno : cnos) {
                            columnNos.add(Integer.parseInt(cno));
                        }
                    }
                    break;
                case NUMERIC:
                    columnNo = WorkbookUtils.readAsInteger(row, 1);
                    break;
                default:
                    throw new ValidationError("Column No is Invalid");
            }
            String shortDesc = WorkbookUtils.readAsString(row, 3);
            if(StringUtils.isEmpty(columnName)) break;

            ColumnImportConfig columnImportConfig = new ColumnImportConfig();

            columnImportConfig.setColumnName(columnName);
            columnImportConfig.setColumnNo(columnNo);
            columnImportConfig.setJsonColumns(columnNos);
            columnImportConfig.setColumnDesc(shortDesc);
            columnImportConfig.setFixedValue(fixedVal);
            columnImportConfig.setTopFixedCellCoor(topFixedCellCoorStr);
            String dataType = columnDefinitionMap.get(columnName);
            if("Y".equalsIgnoreCase(isDisplayedStr) || "YES".equalsIgnoreCase(isDisplayedStr)) {
                columnImportConfig.setDisplayed(true);
            } else {
                columnImportConfig.setDisplayed(false);
            }
            columnImportConfig.setDataType(dataType == null ? "TEXT" : dataType);
            columnImportConfig.setClazzName(clazzName);

            result.add(columnImportConfig);

            rowNo += 1;

        } while(true);

        result.forEach(cic ->{
            String columnName = cic.getColumnName();
            ColumnImportConfig ccic = columnImportConfigRepository.findByProjectIdAndClazzNameAndColumnNameAndStatus(
                projectId, clazzName, columnName, EntityStatus.ACTIVE);
            if(ccic == null) {
                cic.setProjectId(projectId);
                cic.setOrgId(orgId);
                cic.setStatus(EntityStatus.ACTIVE);
                columnImportConfigRepository.save(cic);
            } else {
                BeanUtils.copyProperties(cic, ccic, "id", "status");
                ccic.setOrgId(orgId);
                ccic.setProjectId(projectId);
                columnImportConfigRepository.save(ccic);
            }
        });

        return result;
    }

}
