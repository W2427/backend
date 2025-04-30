package com.ose.material.vo.easyExcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ose.material.vo.QrCodeType;
import org.springframework.stereotype.Component;

/**
 * qrCodeType类型转换器
 */
@Component
public class QrCodeTypeConverter implements Converter<QrCodeType> {

    @Override
    public Class supportJavaTypeKey() {
        return QrCodeType.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

//    @Override
//    public QrCodeType convertToJavaData(WriteCellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }

    @Override
    public WriteCellData convertToExcelData(QrCodeType qrCodeType, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData(qrCodeType.toString());
    }
}
