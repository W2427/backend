package com.ose.vo;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.stereotype.Component;

/**
 * Timestamp类型转换器
 */
@Component
public class StatusConverter implements Converter<EntityStatus> {

    @Override
    public Class supportJavaTypeKey() {
        return EntityStatus.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

//    @Override
//    public EntityStatus convertToJavaData(WriteCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }

    @Override
    public WriteCellData convertToExcelData(EntityStatus entityStatus, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData(entityStatus.toString());
    }
}
