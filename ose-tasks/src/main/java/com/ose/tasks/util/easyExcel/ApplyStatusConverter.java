package com.ose.tasks.util.easyExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ose.tasks.vo.ApplyStatus;
import org.springframework.stereotype.Component;

/**
 * Timestamp类型转换器
 */
@Component
public class ApplyStatusConverter implements Converter<ApplyStatus>{

    @Override
    public Class supportJavaTypeKey() {
        return ApplyStatus.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

//    @Override
//    public ApplyStatus convertToJavaData(WriteCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }

    @Override
    public WriteCellData convertToExcelData(ApplyStatus entityStatus, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        return new WriteCellData(entityStatus.toString());
    }
}
