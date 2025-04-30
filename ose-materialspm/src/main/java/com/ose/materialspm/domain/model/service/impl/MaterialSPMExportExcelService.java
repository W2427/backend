package com.ose.materialspm.domain.model.service.impl;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.exception.NotFoundError;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.ExportFileInputDataDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.util.LongUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component
public class MaterialSPMExportExcelService {
    private final static Logger logger = LoggerFactory.getLogger(MaterialSPMExportExcelService.class);
    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Autowired
    public MaterialSPMExportExcelService() {
    }

    public ExportFileDTO exportExcelXlsx(
        Long orgId,
        Long projectId,
        UploadFeignAPI uploadFeignAPI,
        LinkedHashMap<String, String> title,
        List<Map<String, Object>> exportDataList,
        String fileName
    ) {

        ExportFileDTO result = new ExportFileDTO();

        Workbook workbook;
        File excel;

        try {
            excel = new File(temporaryDir, fileName + ".xlsx");
            excel.createNewFile();
            workbook = new XSSFWorkbook();
        } catch (IOException e) {
            throw new NotFoundError();
        }
        Sheet sheet = workbook.createSheet();

        List<String> keyList = new ArrayList<>();

        Row row = sheet.createRow(0);
        int col = 0;
        Iterator<String> iterator = title.keySet().iterator();
        while (iterator.hasNext()) {
            String titleKey = iterator.next();
            keyList.add(titleKey);
            Cell cell = row.createCell(col++);
            cell.setCellValue(title.get(titleKey));
        }

        int i = 1;
        for (Map<String, Object> data : exportDataList) {
            row = sheet.createRow(i++);
            col = 0;
            for (String key : keyList) {
                Object value = data.get(key);
                if (value == null) {
                    col++;
                    continue;
                } else if (value instanceof String) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((String) value);
                } else if (value instanceof RichTextString) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((RichTextString) value);
                } else if (value instanceof Double) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((Double) value);
                } else if (value instanceof Date) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((Date) value);
                } else if (value instanceof Calendar) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((Calendar) value);
                } else if (value instanceof Boolean) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((Boolean) value);
                } else if (value instanceof Integer) { // 整形数据
                    Cell cell = row.createCell(col++);
                    cell.setCellValue((Integer) value);
                } else if (value instanceof BigDecimal) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue(((BigDecimal) value).doubleValue());
                }
            }
        }

        FileOutputStream stream;
        try {
            stream = new FileOutputStream(excel);
            workbook.write(stream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
            MediaType.APPLICATION_PDF_VALUE, true, excel.getName());

        try {
            IOUtils.copy(new FileInputStream(excel), fileItem.getOutputStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        // 将文件上传到文档服务器
        logger.error("材料1 上传docs服务->开始");
        MockMultipartFile fileItem1 = null;
        try {
            fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
            .uploadProjectDocumentFile(orgId.toString(), fileItem1);


            logger.error("材料1 上传docs服务->结束");
        logger.error("材料1 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            tempFileResBody.getData().getName(), new FilePostDTO());
        logger.error("材料1 保存docs服务->结束");
        result.setFileId(LongUtils.parseLong(
            fileESResBody.getData().getId()
        ));
        return result;
    }

    public ExportFileDTO exportExcelXlsxForMultipleSheet(
        Long orgId,
        Long projectId,
        UploadFeignAPI uploadFeignAPI,
        List<ExportFileInputDataDTO> eportFileInputDataDTOList,
        String fileName
    ) {

        ExportFileDTO result = new ExportFileDTO();

        Workbook workbook;
        File excel;

        try {
            excel = new File(temporaryDir, fileName + ".xlsx");
            excel.createNewFile();
            workbook = new XSSFWorkbook();
        } catch (IOException e) {
            throw new NotFoundError();
        }

        for (ExportFileInputDataDTO exportFileInputDataDTO : eportFileInputDataDTOList) {

            String sheetName = exportFileInputDataDTO.getSheetName();

            LinkedHashMap<String, String> title = exportFileInputDataDTO.getTitle();

            List<Map<String, Object>> exportDataList = exportFileInputDataDTO.getExportDataList();

            Sheet sheet = workbook.createSheet(sheetName);

            List<String> keyList = new ArrayList<>();

            Row row = sheet.createRow(0);
            int col = 0;
            Iterator<String> iterator = title.keySet().iterator();
            while (iterator.hasNext()) {
                String titleKey = iterator.next();
                keyList.add(titleKey);
                Cell cell = row.createCell(col++);
                cell.setCellValue(title.get(titleKey));
            }

            int i = 1;
            for (Map<String, Object> data : exportDataList) {
                row = sheet.createRow(i++);
                col = 0;
                for (String key : keyList) {
                    Object value = data.get(key);
                    if (value == null) {
                        col++;
                        continue;
                    } else if (value instanceof String) {
                        Cell cell = row.createCell(col++);
                        cell.setCellValue((String) value);
                    } else if (value instanceof RichTextString) {
                        Cell cell = row.createCell(col++);
                        cell.setCellValue((RichTextString) value);
                    } else if (value instanceof Double) {
                        Cell cell = row.createCell(col++);
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Date) {
                        Cell cell = row.createCell(col++);
                        cell.setCellValue((Date) value);
                    } else if (value instanceof Calendar) {
                        Cell cell = row.createCell(col++);
                        cell.setCellValue((Calendar) value);
                    } else if (value instanceof Boolean) {
                        Cell cell = row.createCell(col++);
                        cell.setCellValue((Boolean) value);
                    }
                }
            }
        }

        FileOutputStream stream;
        try {
            stream = new FileOutputStream(excel);
            workbook.write(stream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
            MediaType.APPLICATION_PDF_VALUE, true, excel.getName());

        try {
            IOUtils.copy(new FileInputStream(excel), fileItem.getOutputStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        // 将文件上传到文档服务器
        logger.error("材料2 上传docs服务->开始");

        MockMultipartFile fileItem1 = null;
        try {
            fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
            .uploadProjectDocumentFile(orgId.toString(), fileItem1);
        logger.error("材料2 上传docs服务->结束");
        logger.error("材料2 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            tempFileResBody.getData().getName(), new FilePostDTO());
        logger.error("材料2 保存docs服务->结束");
        result.setFileId(LongUtils.parseLong(
            fileESResBody.getData().getId()
        ));
        return result;
    }
}
