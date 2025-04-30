package com.ose.tasks.util.easyExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.*;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * Timestamp类型转换器
 */
@Component
public class LocalDateConverter implements Converter<Timestamp>{

    @Override
    public Class supportJavaTypeKey() {
        return Timestamp.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

//    @Override
//    public Timestamp convertToJavaData(WriteCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }

    @Override
    public WriteCellData convertToExcelData(Timestamp timestamp, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData(timestamp.toLocalDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}



/**
 * Date类型转换器
 */
//@Component
//public class LocalDateConverter implements Converter<Date>{
//
//    @Override
//    public Class supportJavaTypeKey() {
//        return Date.class;
//    }
//
//    @Override
//    public CellDataTypeEnum supportExcelTypeKey() {
//        return CellDataTypeEnum.STRING;
//    }
//
//    @Override
//    public Date convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }
//
//    @Override
//    public CellData convertToExcelData(Date date, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return new CellData(date.toString());
//    }
//}


/**
 * LocalDate类型转换器
 */
//@Component
//public class LocalDateConverter implements Converter<LocalDate> {
//    @Override
//    public Class<LocalDate> supportJavaTypeKey() {
//        return LocalDate.class;
//    }
//
//    @Override
//    public CellDataTypeEnum supportExcelTypeKey() {
//        return CellDataTypeEnum.STRING;
//    }
//
//    @Override
//    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty
//        , GlobalConfiguration globalConfiguration) throws Exception {
//        return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    }
//
//    @Override
//    public CellData convertToExcelData(LocalDate localDate, ExcelContentProperty excelContentProperty
//        , GlobalConfiguration globalConfiguration) throws Exception {
//        return new CellData<>(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//    }
//}


/**
 * 转换EntityStatus
 */
//@Component
//public class LocalDateConverter implements Converter<EntityStatus> {
//
//    @Override
//    public Class<EntityStatus> supportJavaTypeKey() {
//        return EntityStatus.class;
//    }
//
//    @Override
//    public CellDataTypeEnum supportExcelTypeKey() {
//        return CellDataTypeEnum.STRING;
//    }
//
//    @Override
//    public EntityStatus convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }
//
//    @Override
//    public CellData convertToExcelData(EntityStatus entityStatus, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return new CellData(entityStatus.toString());
//    }
//}
