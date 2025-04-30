package com.ose.util;

import com.ose.exception.ValidationError;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Microsoft Office Excel 工作表工具。
 */
public class WorkbookUtils {

    /**
     * 将列编号转为索引。
     *
     * @param columnNo 列编号（A~Z, AA~ZZ, AAA~XFD）
     * @return 列索引（0~16383）
     */
    private static int toColumnIndex(String columnNo) {

        columnNo = columnNo.toUpperCase();

        int length = columnNo.length();
        int integer;
        int value = 0;

        for (int index = 0; index < length; index++) {
            integer = Character.codePointAt(columnNo, index) - 64;
            value += integer * Math.pow(26, length - index - 1);
        }

        return value - 1;
    }

    // 数字格式
    private static Pattern NUMBER = Pattern.compile("^([\\-+]?[0-9]+(\\.[0-9]+)?)(.+)$");

    /**
     * 将字符串格式化为数字格式。
     *
     * @param string 字符串
     * @return 格式化后的字符串
     */
    private static String toNumberFormat(String string) {

        if (!StringUtils.isEmpty(string, true)) {
            string = string
                .replaceAll("[^.\\-+0-9]+", ".")
                .replaceAll("^\\+", "")
                .replaceAll("\\.$", "");
        }

        Matcher matcher = NUMBER.matcher(string);

        if (matcher.matches()) {
            string = matcher.group(1);
        } else {
            string = "0";
        }

        return string;
    }

    /**
     * 读取单元格的字符串值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static String readAsString(Row row, String columnNo) {
        return readAsString(row, toColumnIndex(columnNo));
    }

    /**
     * 读取单元格的字符串值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static String readAsString(Row row, int columnNo) {
        return readAsString(row.getCell(columnNo));
    }

    /**
     * 读取单元格的字符串值。
     *
     * @param cell 单元格
     * @return 值
     */
    public static String readAsString(Cell cell) {

        if (cell == null) {
            return null;
        }

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                NumberFormat nf = NumberFormat.getInstance();
                String s = nf.format(cell.getNumericCellValue());
                if (s.indexOf(",") >= 0) {
                    s = s.replace(",", "");
                    return ("" + s).replaceAll("\\.0*$", "");
                } else {
                    return ("" + cell.getNumericCellValue()).replaceAll("\\.0*$", "");
                }

            case BLANK:
                return "";
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook()
                    .getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                String value;
                switch (cellValue.getCellTypeEnum()) {
                    case STRING:
                        value = cellValue.getStringValue();
                        break;
                    case NUMERIC:
                        value = String.valueOf(cellValue.getNumberValue());
                        break;
                    case FORMULA:
                        value = "";
                        break;
                    case BLANK:
                        value = "";
                        break;
                    default:
                        throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
                }
                return value;
            default:
                // TODO throw validation error
                throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
        }

    }

    /**
     * 读取单元格的整数值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static int readAsInteger(Row row, String columnNo) {
        return readAsInteger(row, toColumnIndex(columnNo));
    }

    /**
     * 读取单元格的整数值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static int readAsInteger(Row row, int columnNo) {
        return readAsInteger(row.getCell(columnNo));
    }

    /**
     * 读取单元格的整数值。
     *
     * @param cell 单元格
     * @return 值
     */
    public static int readAsInteger(Cell cell) {

        if (cell == null) {
            return 0;
        }

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return Integer.parseInt(toNumberFormat(cell.getStringCellValue()));
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case BLANK:
                return 0;
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook()
                    .getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                Integer value;
                switch (cellValue.getCellTypeEnum()) {
                    case STRING:
                        value = Integer.valueOf(cellValue.getStringValue());
                        break;
                    case NUMERIC:
                        value = Double.valueOf(cellValue.getNumberValue()).intValue();
                        break;
                    case FORMULA:
                        value = 0;
                        break;
                    case BLANK:
                        value = 0;
                        break;
                    default:
                        throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
                }
                return value;
            default:
                // TODO throw validation error
                throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
        }

    }

    /**
     * 读取单元格的浮点数值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static Float readAsFloat(Row row, String columnNo) {
        return readAsFloat(row, toColumnIndex(columnNo));
    }

    /**
     * 读取单元格的浮点数值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static float readAsFloat(Row row, int columnNo) {
        return readAsFloat(row.getCell(columnNo));
    }

    /**
     * 读取单元格的浮点数值。
     *
     * @param cell 单元格
     * @return 值
     */
    public static float readAsFloat(Cell cell) {

        if (cell == null) {
            return 0f;
        }

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return Float.valueOf(toNumberFormat(cell.getStringCellValue()));
            case NUMERIC:
                return (float) cell.getNumericCellValue();
            case BLANK:
                return 0f;
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook()
                    .getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                float value;
                switch (cellValue.getCellTypeEnum()) {
                    case STRING:
                        value = Float.valueOf(cellValue.getStringValue());
                        break;
                    case NUMERIC:
                        value = Double.valueOf(cellValue.getNumberValue()).floatValue();
                        break;
                    case FORMULA:
                        value = 0f;
                        break;
                    case BLANK:
                        value = 0f;
                        break;
                    default:
                        throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
                }
                return value;
            default:
                // TODO throw validation error
                throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
        }

    }

    /**
     * 读取单元格的双精度值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static double readAsDouble(Row row, String columnNo) {
        return readAsDouble(row, toColumnIndex(columnNo));
    }

    /**
     * 读取单元格的双精度值。
     *
     * @param row      工作表行实例
     * @param columnNo 列索引
     * @return 值
     */
    public static double readAsDouble(Row row, int columnNo) {
        return readAsDouble(row.getCell(columnNo));
    }

    /**
     * 读取单元格的双精度值。
     *
     * @param cell 单元格
     * @return 值
     */
    public static double readAsDouble(Cell cell) {

        if (cell == null) {
            return 0.0;
        }

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return Double.valueOf(toNumberFormat(cell.getStringCellValue()));
            case NUMERIC:
                return cell.getNumericCellValue();
            case BLANK:
                return 0.0;
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook()
                    .getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                double value;
                switch (cellValue.getCellTypeEnum()) {
                    case STRING:
                        value = Double.valueOf(cellValue.getStringValue());
                        break;
                    case NUMERIC:
                        value = cellValue.getNumberValue();
                        break;
                    case FORMULA:
                        value = 0.0;
                        break;
                    case BLANK:
                        value = 0.0;
                        break;
                    default:
                        throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
                }
                return value;
            default:
                // TODO throw validation error
                throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
        }

    }

    /**
     * 读取单元格的布尔值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static boolean readAsBoolean(Row row, String columnNo) {
        return readAsBoolean(row, toColumnIndex(columnNo));
    }

    /**
     * 读取单元格的布尔值。
     *
     * @param row      工作表行实例
     * @param columnNo 列索引
     * @return 值
     */
    public static boolean readAsBoolean(Row row, int columnNo) {
        return readAsBoolean(row.getCell(columnNo));
    }

    /**
     * 读取单元格的布尔值。
     *
     * @param cell 单元格
     * @return 值
     */
    public static boolean readAsBoolean(Cell cell) {

        if (cell == null) {
            return false;
        }

        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                String string = cell.getStringCellValue().toLowerCase();
                return "true".equals(string) || "yes".equals(string) || "y".equals(string);
            case NUMERIC:
                return cell.getNumericCellValue() != 0;
            case BLANK:
                return false;
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook()
                    .getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                boolean value;
                switch (cellValue.getCellTypeEnum()) {
                    case STRING:
                        value = Boolean.valueOf(cellValue.getStringValue());
                        break;
                    case NUMERIC:
                        if (Double.compare(cellValue.getNumberValue(), 0.0) == 0) {
                            value = false;
                        } else {
                            value = true;
                        }
                        break;
                    case FORMULA:
                        value = false;
                        break;
                    case BLANK:
                        value = false;
                        break;
                    default:
                        throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
                }

                return value;
            default:
                // TODO throw validation error
                throw new ValidationError("cell(" + (cell.getRowIndex() + 1) + "," + (cell.getColumnIndex() + 1) + ") error.validation");
        }

    }

    /**
     * 读取单元格的日期值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @return 值
     */
    public static Date readAsDate(Row row, String columnNo) {
        return readAsDate(row, toColumnIndex(columnNo));
    }

    /**
     * 读取单元格的日期值。
     *
     * @param row      工作表行实例
     * @param columnNo 列名
     * @param sdf      日期格式
     * @return 值
     */
    public static Date readAsDate(Row row, String columnNo, SimpleDateFormat sdf) {
        return readAsDate(row, toColumnIndex(columnNo), sdf);
    }

    /**
     * 读取单元格的日期值。
     *
     * @param row      工作表行实例
     * @param columnNo 列索引
     * @return 值
     */
    public static Date readAsDate(Row row, int columnNo) {
        return readAsDate(row.getCell(columnNo), null);
    }

    /**
     * 读取单元格的日期值。
     *
     * @param row      工作表行实例
     * @param columnNo 列索引
     * @param sdf      日期格式
     * @return 值
     */
    public static Date readAsDate(Row row, int columnNo, SimpleDateFormat sdf) {
        return readAsDate(row.getCell(columnNo), sdf);
    }

    /**
     * 读取单元格的日期值。
     *
     * @param cell 单元格
     * @param sdf  日期格式
     * @return 值
     */
    public static Date readAsDate(Cell cell, SimpleDateFormat sdf) {

        if (cell == null || CellType.BLANK == cell.getCellTypeEnum()) {
            return null;
        }

        if (CellType.STRING == cell.getCellTypeEnum()) {
            return toDate(cell.getStringCellValue(), sdf);
        }

        if (CellType.FORMULA == cell.getCellTypeEnum()) {

            FormulaEvaluator formulaEvaluator = cell
                .getSheet()
                .getWorkbook()
                .getCreationHelper()
                .createFormulaEvaluator();

            CellValue cellValue = formulaEvaluator.evaluate(cell);

            switch (cellValue.getCellTypeEnum()) {
                case STRING:
                    return toDate(cellValue.getStringValue(), sdf);
                case NUMERIC:
                    return new Date(Double.valueOf(cellValue.getNumberValue()).longValue());
                default:
                    throw new ValidationError("日期格式无效");
            }

        }

        return cell.getDateCellValue();
    }

    /**
     * 字符串转日期。
     *
     * @param date 日期字符串
     * @param sdf  日期格式
     * @return 日期
     */
    private static Date toDate(String date, SimpleDateFormat sdf) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        if (sdf == null) {
            return new Date(date);
        }
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new ValidationError("日期格式无效");
        }
    }

    /**
     * 复制单元格。
     *
     * @param cell    单元格
     * @param newCell 目标单元格
     * @return 复制的单元格
     */
    public static Cell cloneCell(Cell cell, Cell newCell) {

        newCell.setCellType(cell.getCellTypeEnum());
        newCell.setCellComment(cell.getCellComment());
        newCell.setCellStyle(cell.getCellStyle());

        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                newCell.setCellValue(cell.getNumericCellValue());
                break;
            case STRING:
                newCell.setCellValue(cell.getStringCellValue());
                break;
            case FORMULA:
                newCell.setCellValue(cell.getCellFormula());
                break;
            case BLANK:
                break;
            case BOOLEAN:
                newCell.setCellValue(cell.getBooleanCellValue());
                break;
            case ERROR:
                newCell.setCellValue(cell.getErrorCellValue());
                break;
            default:
                break;
        }

        return newCell;
    }

    /**
     * 复制列。
     *
     * @param sheet    工作表
     * @param sourceNo 复制对象列索引
     * @param targetNo 复制目标列索引
     */
    public static void copyColumn(
        final Sheet sheet,
        final int sourceNo,
        final int targetNo
    ) {
        copyColumn(sheet, sourceNo, targetNo, null);
    }

    /**
     * 复制列。
     *
     * @param sheet     工作表
     * @param sourceNo  复制对象列索引
     * @param targetNo  复制目标列索引
     * @param valueList 有效值列表
     */
    public static void copyColumn(
        final Sheet sheet,
        final int sourceNo,
        final int targetNo,
        final List<String> valueList
    ) {
        copyColumn(sheet, sourceNo, targetNo, 0, valueList);
    }

    /**
     * 复制列。
     *
     * @param sheet      工作表
     * @param sourceNo   复制对象列索引
     * @param targetNo   复制目标列索引
     * @param headerRows 表头行数
     * @param valueList  有效值列表
     */
    public static void copyColumn(
        final Sheet sheet,
        final int sourceNo,
        final int targetNo,
        final int headerRows,
        final List<String> valueList
    ) {

        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        Cell source;
        Cell target;

        // 将复制对象列的每行的单元格复制到目标列对应行的单元格
        while (rows.hasNext()) {

            row = rows.next();
            source = row.getCell(sourceNo);
            target = row.getCell(targetNo);

            if (source == null) {

                if (target != null) {
                    row.removeCell(target);
                }

                continue;
            }

            if (target == null) {
                target = row.createCell(targetNo);
            }

            cloneCell(source, target);
        }

        // 设置有效值列表
        if (valueList != null && valueList.size() > 0) {

            CellRangeAddressList range = new CellRangeAddressList(
                headerRows,
                sheet.getPhysicalNumberOfRows(),
                targetNo,
                targetNo
            );

            String[] values = valueList.toArray(new String[0]);

            DataValidation dataValidation = null;

            if (sheet instanceof XSSFSheet) {
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
                dataValidation = dvHelper.createValidation(
                    dvHelper.createExplicitListConstraint(values),
                    range
                );
            } else if (sheet instanceof HSSFSheet) {
                dataValidation = new HSSFDataValidation(
                    range,
                    DVConstraint.createExplicitListConstraint(values)
                );
            }

            if (dataValidation != null) {
                dataValidation.setSuppressDropDownArrow(true);
                sheet.addValidationData(dataValidation);
            }

        }

    }

    /**
     * 删除列。
     *
     * @param sheet 工作表
     * @param from  开始列
     * @param to    截止列
     */
    public static void removeColumns(Sheet sheet, short from, short to) {

        short colNum;
        short lastColNum = getLastColNum(sheet);

        for (colNum = from; colNum < to; colNum++) {
            removeColumnCells(sheet, colNum);
        }

        moveColumns(sheet, to, lastColNum, from);
    }



        /**
         * 删除所有合并单元格
         */
    public static void removeAllMergeCells(Sheet sheet) {
        int i = sheet.getNumMergedRegions();
        for(int k =0; k<i;k++){
            sheet.removeMergedRegion(k);

        }

    }
    /**
     * 取得总列数。
     *
     * @param sheet 工作表
     * @return 总列数
     */
    private static short getLastColNum(Sheet sheet) {

        short lastColNum = 0;

        Row row;

        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {

            row = sheet.getRow(rowNum);

            lastColNum = (short) Math.max(
                lastColNum,
                row == null ? 0 : row.getLastCellNum()
            );

        }

        return lastColNum;
    }

    /**
     * 取得指定单元格所在的合并区域。
     *
     * @param sheet  工作表
     * @param rowNum 行号
     * @param colNum 列号
     * @return 合并区域
     */
    private static CellRangeAddress removeMergedRegion(
        Sheet sheet,
        int rowNum,
        short colNum
    ) {

        CellRangeAddress rangeAddress;

        for (
            int rangeAddressIndex = 0;
            rangeAddressIndex < sheet.getNumMergedRegions();
            rangeAddressIndex++
            ) {

            rangeAddress = sheet.getMergedRegion(rangeAddressIndex);

            if (rangeAddress.getFirstRow() <= rowNum
                && rangeAddress.getLastRow() >= rowNum
                && rangeAddress.getFirstColumn() <= colNum
                && rangeAddress.getLastColumn() >= colNum) {
                sheet.removeMergedRegion(rangeAddressIndex);
                return rangeAddress;
            }

        }

        return null;
    }

    /**
     * 删除指定列的所有单元格。
     *
     * @param sheet  工作表
     * @param colNum 列编号
     */
    private static void removeColumnCells(Sheet sheet, short colNum) {

        int rowNum;
        int lastRowNum = sheet.getLastRowNum();
        Row row;
        Cell cell;

        for (rowNum = 0; rowNum <= lastRowNum; rowNum++) {

            removeMergedRegion(sheet, rowNum, colNum);

            if ((row = sheet.getRow(rowNum)) == null
                || (cell = row.getCell(colNum)) == null) {
                continue;
            }

            row.removeCell(cell);
        }

    }

    /**
     * 移动指定列的单元格到目标列。
     *
     * @param sheet 工作表
     * @param from  起始列
     * @param to    截止列
     * @param dest  目标列
     */
    private static void moveColumns(
        Sheet sheet,
        short from,
        short to,
        short dest
    ) {

        int rowNum;
        int lastRowNum = sheet.getLastRowNum();
        short cellNum;
        short lastCellNum;
        int maxCellNum = 0;
        Row row;
        Cell cell;
        CellRangeAddress rangeAddress;
        short destColNum;

        // 移动单元格
        for (rowNum = 0; rowNum <= lastRowNum; rowNum++) {

            if ((row = sheet.getRow(rowNum)) == null
                || (lastCellNum = row.getLastCellNum()) < from) {
                continue;
            }

            lastCellNum = (short) Math.min(lastCellNum, to);
            maxCellNum = (short) Math.max(maxCellNum, lastCellNum);

            for (cellNum = from; cellNum <= lastCellNum; cellNum++) {

                if ((cell = row.getCell(cellNum)) == null) {
                    continue;
                }


                rangeAddress = removeMergedRegion(sheet, rowNum, cellNum);

                destColNum = (short) (dest + (cellNum - from));

                cloneCell(cell, row.createCell(destColNum));


                if (rangeAddress != null) {
                    removeMergedRegion(sheet, rowNum, destColNum);
                    sheet.addMergedRegion(new CellRangeAddress(
                        rangeAddress.getFirstRow(),
                        rangeAddress.getLastRow(),
                        destColNum + (rangeAddress.getFirstColumn() - cellNum),
                        destColNum + (rangeAddress.getLastColumn() - cellNum)
                    ));
                }



                row.removeCell(cell);
            }

        }

        // 更新列宽
        for (cellNum = dest; cellNum < (dest + (to - from)); cellNum++) {
            sheet.setColumnWidth(
                cellNum,
                sheet.getColumnWidth(from + (cellNum - dest))
            );
        }

        for (; cellNum <= maxCellNum; cellNum++) {
            sheet.setColumnWidth(
                cellNum,
                2230
            );
        }

    }

    /**
     * 若指定的单元格不存在则根据前一行同列的单元格创建新的单元格。
     *
     * @param sheet    工作表对象
     * @param rowIndex 行索引
     * @param colIndex 列索引
     * @return 单元格
     */
    private static Cell createCellIfNotExists(Sheet sheet, int rowIndex, int colIndex) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(colIndex);

        if (cell == null) {

            cell = row.createCell(colIndex);

            Row styleSourceRow;
            Cell styleSourceCell;

            if ((styleSourceRow = sheet.getRow(rowIndex - 1)) != null
                && (styleSourceCell = styleSourceRow.getCell(colIndex)) != null) {
                cloneCell(styleSourceCell, cell);
            }

        }

        return cell;
    }

    /**
     * 设置单元格的值。
     *
     * @param sheet  工作表对象
     * @param row    行索引
     * @param col    列索引
     * @param string 值
     * @return 单元格
     */
    public static Cell setCellValue(Sheet sheet, int row, int col, String string) {

        Cell cell = createCellIfNotExists(sheet, row, col);

        if (string == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(string);
        }

        return cell;
    }

    /**
     * 设置单元格的值。
     *
     * @param sheet 工作表对象
     * @param row   行索引
     * @param col   列索引
     * @param date  值
     * @return 单元格
     */
    public static Cell setCellValue(Sheet sheet, int row, int col, Date date) {

        Cell cell = createCellIfNotExists(sheet, row, col);

        if (date == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(date);
        }

        return cell;
    }

    /**
     * 设置单元格的值。
     *
     * @param sheet  工作表对象
     * @param row    行索引
     * @param col    列索引
     * @param number 值
     * @return 单元格
     */
    public static Cell setCellValue(Sheet sheet, int row, int col, Double number) {

        Cell cell = createCellIfNotExists(sheet, row, col);

        if (number == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(number);
        }

        return cell;
    }

    /**
     * 设置单元格的值。
     *
     * @param sheet  工作表对象
     * @param row    行索引
     * @param col    列索引
     * @param number 值
     * @return 单元格
     */
    public static Cell setCellValue(Sheet sheet, int row, int col, Integer number) {

        Cell cell = createCellIfNotExists(sheet, row, col);

        if (number == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(number);
        }

        return cell;
    }

    /**
     * 将单元格设置为空。
     *
     * @param sheet 工作表对象
     * @param row   行索引
     * @param col   列索引
     * @return 单元格
     */
    public static Cell setCellAsBlank(Sheet sheet, int row, int col) {
        Cell cell = createCellIfNotExists(sheet, row, col);
        cell.setCellType(CellType.BLANK);
        return cell;
    }

    /**
     * 设置单元格的值。
     *
     * @param sheet 工作表对象
     * @param row   行索引
     * @param col   列索引
     * @param value 值
     * @return 单元格
     */
    public static Cell setCellValue(Sheet sheet, int row, int col, Enum value) {

        Cell cell = createCellIfNotExists(sheet, row, col);

        if (value == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(value.name());
        }

        return cell;
    }

    /**
     * 保存 Microsoft Excel 工作簿。
     *
     * @param workbook 工作表
     * @param path     保存路径
     */
    public static void save(
        Workbook workbook,
        String path
    ) throws IOException {

        File tempFile = new File(path + ".temp");

        workbook.write(new FileOutputStream(tempFile));
        workbook.close();

        File targetFile = new File(path);
        targetFile.delete();
        FileUtils.rename(tempFile, targetFile.getName());
    }

    /**
     * 获取excel行。
     *
     * @param sheet  页
     * @param rowNum 行数
     */
    public static Row getRow(Sheet sheet, int rowNum) {

        if (sheet == null) {
            return null;
        }

        return sheet.getRow(rowNum) != null ? sheet.getRow(rowNum)
            : sheet.createRow(rowNum);
    }

    /**
     * 获取excel单元格。
     *
     * @param row     行
     * @param cellNum 单元格数
     */
    public static Cell getCell(Row row, int cellNum) {

        if (row == null) {
            return null;
        }

        return row.getCell(cellNum) != null ? row.getCell(cellNum)
            : row.createCell(cellNum);
    }

    /**
     * 行复制，只复制格式，不复制值。
     *
     * @param templateRow 模版行
     * @param targetRow   目标行
     */
    public static void copyRow(Row templateRow, Row targetRow) {

        if (templateRow == null || targetRow == null) {
            return;
        }
        for (Iterator cellIterator = templateRow.cellIterator(); cellIterator.hasNext(); ) {
            Cell templateCell = (Cell) cellIterator.next();
            Cell newCell = targetRow.createCell(templateCell.getColumnIndex());
            copyCellStyle(templateCell, newCell);
        }

    }

    /**
     * 复制单元格式
     *
     * @param srcCell 拷贝源单元格
     * @param dstCell 拷贝目的单元格
     */
    private static void copyCellStyle(Cell srcCell, Cell dstCell) {
        dstCell.setCellStyle(srcCell.getCellStyle());
        // Comment
        if (srcCell.getCellComment() != null) {
            dstCell.setCellComment(srcCell.getCellComment());
        }
    }

    /**
     * 获取Excel2007图片
     * @param sheetNum 当前sheet编号
     * @param sheet 当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（0_1_1）String，value:图片流PictureData
     */
    public static Map<String, PictureData> getSheetPictrues07(int sheetNum,
                                                              XSSFSheet sheet, XSSFWorkbook workbook) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();

        List<XSSFShape> shapes = sheet.getDrawingPatriarch().getShapes();


        List<POIXMLDocumentPart> drs = sheet.getRelations();

        for (POIXMLDocumentPart dr : drs) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
               /* List<XSSFShape> */shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getPreferredSize();
                    CTMarker ctMarker = anchor.getFrom();
                    String picIndex = String.valueOf(sheetNum) + "_"
                        + ctMarker.getRow() + "_" + ctMarker.getCol();

                    sheetIndexPicMap.put(picIndex, pic.getPictureData());
                    dr = null;
//                    sheet.
                }

            }

        }

        // HSSFRow row = sheet1.createRow(2);

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        FileOutputStream fileOut = null;

        BufferedImage bufferImg = null;
        try {
            bufferImg = ImageIO.read(new File("/var/www/qr.png"));
            ImageIO.write(bufferImg, "png", byteArrayOut);

            XSSFDrawing patriarch = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(100, 100, 100, 5, (short) 1, 1, (short) 4, 4);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_DO_RESIZE);
            // 插入图片
            patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));

            fileOut = new FileOutputStream("/var/www/test_workbook.xlsx");
            // 写入excel文件
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return sheetIndexPicMap;
    }

    public static XSSFPicture addQrPic(XSSFSheet sheet, XSSFWorkbook workbook, BufferedImage bufferImg, ClientAnchor anchor) {

        Short col1 = anchor.getCol1();
        Short col2 = anchor.getCol2();

        int row1 = anchor.getRow1();
        int row2 = anchor.getRow2();

        double totalWidth = 0.0;
        double totalHeight = 0.0;

        for(int n = col1; n < col2 +1; n++) {
            totalWidth = totalWidth + sheet.getColumnWidthInPixels(n);
        }

        for(int n = row1; n < row2 + 1; n++) {
            totalHeight = totalHeight + sheet.getRow(n).getHeightInPoints()/72*96;
        }

        // 指定我想要的长宽
        double standardWidth = 60;
        double standardHeight = 60;

        // 计算需要的长宽比例的系数
        double a = standardWidth / totalWidth;
        double b = standardHeight / totalHeight;

        XSSFPicture pic = null;
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        // 获取文件后缀名
        String fileExt =  "png";
        try {
            ImageIO.write(bufferImg, fileExt, byteArrayOut);

            XSSFDrawing patriarch = sheet.createDrawingPatriarch();
//            XSSFClientAnchor anchor = new XSSFClientAnchor(100, 100, 100, 5, (short) 1, 1, (short) 4, 4);
//            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_DO_RESIZE);
            // 插入图片
            int format;
            if(fileExt.equalsIgnoreCase("png")) {
                format = XSSFWorkbook.PICTURE_TYPE_PNG;
            } else if(fileExt.equalsIgnoreCase("bmp")) {
                format = XSSFWorkbook.PICTURE_TYPE_BMP;
            } else if(fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("jpeg")) {
                format = XSSFWorkbook.PICTURE_TYPE_JPEG;
            } else {
                format = XSSFWorkbook.PICTURE_TYPE_PNG;
            }

            pic = patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), format));
            pic.resize(a,b);


        } catch (Exception e){
            e.printStackTrace();
        }

        return pic;
    }


    public static XSSFPicture addQrPic(XSSFSheet sheet, XSSFWorkbook workbook, String qrCode, ClientAnchor anchor) {
            XSSFPicture pic = null;

            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        // 获取文件后缀名
        String fileExt =  qrCode.substring(qrCode.lastIndexOf(".") + 1);
        BufferedImage bufferImg = null;
        try {
            bufferImg = ImageIO.read(new File(qrCode));
            pic = addQrPic(sheet, workbook, bufferImg, anchor);

        } catch (Exception e){
            e.printStackTrace();
        }

        return pic;
    }


    public static void printImg(List<Map<String, PictureData>> sheetList) throws IOException {

        for (Map<String, PictureData> map : sheetList) {
            Object key[] = map.keySet().toArray();
            for (int i = 0; i < map.size(); i++) {
                // 获取图片流
                PictureData pic = map.get(key[i]);
                // 获取图片索引
                String picName = key[i].toString();
                // 获取图片格式
                String ext = pic.suggestFileExtension();

                byte[] data = pic.getData();

                FileOutputStream out = new FileOutputStream("/var/www/" + picName + "." + ext);
                out.write(data);
                out.close();
            }
        }

    }


    /**
     * 获取Excel2003图片
     * @param sheetNum 当前sheet编号
     * @param sheet 当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（0_1_1）String，value:图片流PictureData
     * @throws IOException
     */
    public static Map<String, PictureData> getSheetPictrues03(int sheetNum,
                                                              HSSFSheet sheet, HSSFWorkbook workbook) {

        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        List<HSSFPictureData> pictures = workbook.getAllPictures();
        if (pictures.size() != 0) {
            for (HSSFShape shape : sheet.getDrawingPatriarch().getChildren()) {
                HSSFClientAnchor anchor = (HSSFClientAnchor) shape.getAnchor();
                if (shape instanceof HSSFPicture) {
                    HSSFPicture pic = (HSSFPicture) shape;
                    int pictureIndex = pic.getPictureIndex() - 1;
                    HSSFPictureData picData = pictures.get(pictureIndex);
                    String picIndex = String.valueOf(sheetNum) + "_"
                        + String.valueOf(anchor.getRow1()) + "_"
                        + String.valueOf(anchor.getCol1());
                    sheetIndexPicMap.put(picIndex, picData);
                }
            }
            return sheetIndexPicMap;
        } else {
            return null;
        }
    }





    /**
     * @param args
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void main(String[] args) throws InvalidFormatException, IOException {

        // 创建文件
        File file = new File("/var/www/test.xlsx");

        // 创建流
        InputStream input = new FileInputStream(file);

        // 获取文件后缀名
        String fileExt =  file.getName().substring(file.getName().lastIndexOf(".") + 1);

        // 创建Workbook
        Workbook wb = null;

        // 创建sheet
        Sheet sheet = null;

        //根据后缀判断excel 2003 or 2007+
        if (fileExt.equals("xls")) {
            wb = (HSSFWorkbook) WorkbookFactory.create(input);
        } else {
            wb = new XSSFWorkbook(input);
        }

        //获取excel sheet总数
        int sheetNumbers = wb.getNumberOfSheets();

        // sheet list
        List<Map<String, PictureData>> sheetList = new ArrayList<Map<String, PictureData>>();

        // 循环sheet
        for (int i = 0; i < sheetNumbers; i++) {

            sheet = wb.getSheetAt(i);
            // map等待存储excel图片
            Map<String, PictureData> sheetIndexPicMap;

            // 判断用07还是03的方法获取图片
            if (fileExt.equals("xls")) {
                sheetIndexPicMap = getSheetPictrues03(i, (HSSFSheet) sheet, (HSSFWorkbook) wb);
            } else {
                sheetIndexPicMap = getSheetPictrues07(i, (XSSFSheet) sheet, (XSSFWorkbook) wb);
            }
            // 将当前sheet图片map存入list
            sheetList.add(sheetIndexPicMap);
        }

        printImg(sheetList);


        WorkbookUtils.save(wb, "/var/www/abc.xlsx");
    }

    public static Object readXlsCell(Row row, Integer columNo, String dataType) {
        switch (dataType) {
            case "TEXT":
                return WorkbookUtils.readAsString(row, columNo);

            case "INTEGER":
                return WorkbookUtils.readAsInteger(row, columNo);

            case "DOUBLE":
                return WorkbookUtils.readAsDouble(row, columNo);

            case "FLOAT":
                return WorkbookUtils.readAsFloat(row, columNo);

            case "DATE":
                return WorkbookUtils.readAsDate(row, columNo);

            case "BOOLEAN":
                return WorkbookUtils.readAsBoolean(row, columNo);

            default:
                return WorkbookUtils.readAsString(row, columNo);

        }
    }
}
