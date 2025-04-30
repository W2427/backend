package com.ose.prints.domain.model.service;

import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.prints.common.ZplPrinter;
import com.ose.prints.dto.*;
import com.ose.util.QRCodeUtils;
import com.ose.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 批处理任务管理服务。
 */
@Component
public class PrintTaskService implements PrintTaskInterface {


    @Override
    public void printQrCode(String title, String printTs24Path, String printId, CuttingDetailDTO cuttingDTO,String type) {
        ZplPrinter p;
        try {
            // "ZDesigner ZT410-300dpi ZPL"
            p = new ZplPrinter(printId, printTs24Path);
        } catch (Exception e) {
            throw new NotFoundError();
        }
        int fontSize = 40;
        int qrcodeSize = 180;
        int leftStartPosition = 230;
        int topStartPosition = 30;
        try {
            //
//            p.setChar(cuttingDTO.getQrCode(), 40, 30, fontSize, fontSize);
//            p.setChar(title, 700, 20, 45, 45);
//            p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), qrcodeSize), 40, 60);
            //修改布局
            p.setChar(title, 700, 20, 45, 45);
            p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), qrcodeSize), 40, 30);
            p.setChar(cuttingDTO.getQrCode(), 40, qrcodeSize + 30 + 10, fontSize, fontSize);
            // p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), 150), 760,
            // 80);
            if (cuttingDTO.getProject() != null && !StringUtils.isBlank(cuttingDTO.getProject())) {
                p.setChar("PROJECT:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(cuttingDTO.getProject(), leftStartPosition + 180, topStartPosition, fontSize, fontSize);
            }
            if (cuttingDTO.getIdent() != null && !StringUtils.isBlank(cuttingDTO.getIdent())) {
                topStartPosition += 48;
                p.setChar("IDENT:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(cuttingDTO.getIdent(), leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                p.setChar("QTY:", leftStartPosition + 300, topStartPosition, fontSize, fontSize);
                p.setChar(cuttingDTO.getQty() + "(" + cuttingDTO.getUnit() + ")", leftStartPosition + 390,
                    topStartPosition, fontSize, fontSize);
            }
            if (cuttingDTO.getTagNumber() != null && !StringUtils.isBlank(cuttingDTO.getTagNumber())) {
                topStartPosition += 48;
                p.setChar("TAG:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(cuttingDTO.getTagNumber(), leftStartPosition + 100, topStartPosition, fontSize, fontSize);
            }
            if (cuttingDTO.getSpec() != null && !StringUtils.isBlank(cuttingDTO.getSpec())) {
                topStartPosition += 48;
                p.setChar("SPEC:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                if (isContainChinese(cuttingDTO.getSpec())) {
                    p.setText(cuttingDTO.getSpec(), leftStartPosition + 120, topStartPosition, fontSize, fontSize, 30,
                        2, 2, 24);
                } else {
                    p.setChar(cuttingDTO.getSpec(), leftStartPosition + 120, topStartPosition, fontSize, fontSize);
                }
            }
            if (cuttingDTO.getHeatNo() != null && !StringUtils.isBlank(cuttingDTO.getHeatNo())) {
                topStartPosition += 48;
                p.setChar("HEATNO:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
//                p.setChar(cuttingDTO.getHeatNo(), leftStartPosition + 160, topStartPosition, fontSize, fontSize);

                String heatNo = cuttingDTO.getHeatNo();
                int maxLength = 27;
                while (true) {
                    if (heatNo.getBytes().length > maxLength) {
                        String name1 = com.ose.util.StringUtils.substringByCount(heatNo, maxLength);
                        p.setChar(name1, leftStartPosition + 160, topStartPosition, fontSize, fontSize);
                        int name2Count = heatNo.getBytes().length - name1.getBytes().length;
                        heatNo = com.ose.util.StringUtils.substringByCount(heatNo, maxLength, name2Count);
                        topStartPosition += 48;
                    } else {
                        p.setChar(heatNo, leftStartPosition + 160, topStartPosition, fontSize, fontSize);
                        break;
                    }
                }
            }
            if (cuttingDTO.getMaterialBatchNumber() != null
                && !StringUtils.isBlank(cuttingDTO.getMaterialBatchNumber())) {
                topStartPosition += 48;
                p.setChar("BATCH:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(cuttingDTO.getMaterialBatchNumber(), leftStartPosition + 160, topStartPosition, fontSize,
                    fontSize);
            }

            // topStartPosition = 270;
            fontSize = 40;
            if (cuttingDTO.getName() != null && !StringUtils.isBlank(cuttingDTO.getName())) {
                int maxLength = 34;
                String name = cuttingDTO.getName();
                if (name.length() > maxLength) {
                    topStartPosition += 48;
                    String name1 = getStringNameLimitMaxlen(name, maxLength);
                    p.setChar("NAME: " + name1, 45, topStartPosition, fontSize, fontSize);
                    String name2 = name.substring(name1.length());
                    topStartPosition += 48;
                    p.setChar(name2, 150, topStartPosition, fontSize, fontSize);
                } else {
                    topStartPosition += 48;
                    p.setChar("NAME: " + cuttingDTO.getName(), 45, topStartPosition, fontSize, fontSize);
                }
            }

            if (cuttingDTO.getShortCode() == null) {
                cuttingDTO.setShortCode("");
            }

            if(type!=null && type.equals("cutting")){
                topStartPosition += 48;
//            p.setChar("CODE:", 45, topStartPosition, fontSize+60, fontSize+60);
                p.setChar(cuttingDTO.getShortCode(), leftStartPosition -30, topStartPosition, fontSize+60, fontSize+60);
            }

            fontSize = 35;
            if (!"SMALL".equals(cuttingDTO.getHeight()) && cuttingDTO.getDesc() != null
                && !StringUtils.isBlank(cuttingDTO.getDesc())) {
                topStartPosition += 48+40;
                p.setChar("DESC:", 45, topStartPosition, fontSize, fontSize);
                String desc = cuttingDTO.getDesc();
                boolean isch = isContainChinese(desc);
                if (isch) {
                    int maxLength = 26;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setText(name1, 150, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setText(desc, 150, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            break;
                        }
                    }
                } else {
                    int maxLength = 46;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setChar(name1, 150, topStartPosition, fontSize, fontSize);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setChar(desc, 150, topStartPosition, fontSize, fontSize);
                            break;
                        }
                    }
                }
            }
            String zpl = p.getZpl();
            // 如果没有设定printCount的时候，默认值是0，最少打印一次
            if (cuttingDTO.getPrintCount() == null || cuttingDTO.getPrintCount() == 0) {
                cuttingDTO.setPrintCount(1);
            }
            for (int i = 1; i <= cuttingDTO.getPrintCount(); i++) {
                boolean result = p.print(zpl);
                if (result == false) {
                    throw new BusinessError("print error");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new BusinessError("print error");
        }
    }

    @Override
    public void printQrCodeR(String title, String printTs24Path, String printId, CuttingDetailDTO cuttingDTO) {
        ZplPrinter p;
        try {
            // "ZDesigner ZT410-300dpi ZPL"
            p = new ZplPrinter(printId, printTs24Path);
        } catch (Exception e) {
            throw new NotFoundError();
        }
        int fontSize = 40;
        int qrcodeSize = 200;
        int topStartPositionR = 215;
        int leftStartPositionR = 400;
        try {
            // p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), qrcodeSize),
            // 80, 10);
            p.setCharR(cuttingDTO.getQrCode(), 360, 5, fontSize, fontSize);
            p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), 180), 180, 10);

            if (cuttingDTO.getProject() != null && !StringUtils.isBlank(cuttingDTO.getProject())) {
                leftStartPositionR -= 48;
                p.setCharR("PROJECT:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(cuttingDTO.getProject(), leftStartPositionR + 10, topStartPositionR + 180, fontSize,
                    fontSize);
            }

            if (cuttingDTO.getIdent() != null && !StringUtils.isBlank(cuttingDTO.getIdent())) {
                leftStartPositionR -= 48;
                p.setCharR("IDENT:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(cuttingDTO.getIdent(), leftStartPositionR + 10, topStartPositionR + 120, fontSize, fontSize);
                p.setCharR("QTY:", leftStartPositionR + 10, topStartPositionR + 260, fontSize, fontSize);
                p.setCharR(cuttingDTO.getQty() + "(" + cuttingDTO.getUnit() + ")", leftStartPositionR + 10,
                    topStartPositionR + 350, fontSize, fontSize);
            }

            if (cuttingDTO.getTagNumber() != null && !StringUtils.isBlank(cuttingDTO.getTagNumber())) {
                leftStartPositionR -= 48;
                p.setCharR("TAG:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(cuttingDTO.getTagNumber(), leftStartPositionR + 10, topStartPositionR + 85, fontSize,
                    fontSize);
            }

            if (cuttingDTO.getSpec() != null && !StringUtils.isBlank(cuttingDTO.getSpec())) {
                leftStartPositionR -= 48;
                p.setCharR("SEPC:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(cuttingDTO.getSpec(), leftStartPositionR + 10, topStartPositionR + 105, fontSize, fontSize);
            }

            if (cuttingDTO.getHeatNo() != null && !StringUtils.isBlank(cuttingDTO.getHeatNo())) {
                leftStartPositionR -= 48;
                p.setCharR("HEATNO:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
//                p.setCharR(cuttingDTO.getHeatNo(), leftStartPositionR + 10, topStartPositionR + 140, fontSize,
//                    fontSize);

                String heatNo = cuttingDTO.getHeatNo();
                int maxLength = 29;
                while (true) {
                    if (heatNo.getBytes().length > maxLength) {
                        String name1 = com.ose.util.StringUtils.substringByCount(heatNo, maxLength);
                        p.setCharR(name1, leftStartPositionR + 10, topStartPositionR + 150, fontSize, fontSize);
                        int name2Count = heatNo.getBytes().length - name1.getBytes().length;
                        heatNo = com.ose.util.StringUtils.substringByCount(heatNo, maxLength, name2Count);
                        leftStartPositionR -= 48;
                    } else {
                        p.setCharR(heatNo, leftStartPositionR + 10, topStartPositionR + 150, fontSize, fontSize);
                        break;
                    }
                }
            }

            if (cuttingDTO.getMaterialBatchNumber() != null
                && !StringUtils.isBlank(cuttingDTO.getMaterialBatchNumber())) {
                leftStartPositionR -= 48;
                p.setCharR("BATCH:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(cuttingDTO.getMaterialBatchNumber(), leftStartPositionR + 10, topStartPositionR + 125,
                    fontSize, fontSize);
            }

            if (cuttingDTO.getName() != null && !StringUtils.isBlank(cuttingDTO.getName())) {
                leftStartPositionR -= 48;
                p.setCharR(cuttingDTO.getName(), leftStartPositionR + 10, 5, fontSize, fontSize);
            }

            String zpl = p.getZpl();

            // 如果没有设定printCount的时候，默认值是0，最少打印一次
            if (cuttingDTO.getPrintCount() == null || cuttingDTO.getPrintCount() == 0) {
                cuttingDTO.setPrintCount(1);
            }
            for (int i = 1; i <= cuttingDTO.getPrintCount(); i++) {
                boolean result = p.print(zpl);
                if (result == false) {
                    throw new BusinessError("print error");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new BusinessError("print error");
        }
    }

    @Override
    public void printSpoolQrCode(String title, String printTs24Path, String printId,
                                 SpoolQrCodeDetailDTO spoolQrCodeDTO) {
        ZplPrinter p;
        try {
            // "ZDesigner ZT410-300dpi ZPL"
            p = new ZplPrinter(printId, printTs24Path);
        } catch (Exception e) {
            throw new NotFoundError();
        }
        int fontSize = 40;
        int qrcodeSize = 180;
        int leftStartPosition = 230;
        int topStartPosition = 50;
        try {
            //
            int fold = 0;
//            p.setChar(spoolQrCodeDTO.getQrCode(), 40, 60, fontSize, fontSize);
//            p.setChar(title, 700, 25, 45, 45);
//            p.setQRcodeImage(QRCodeUtils.createImage(spoolQrCodeDTO.getQrCode(), qrcodeSize), 40, 100);
            //修改布局
            p.setChar(title, 640, 25, 45, 45);
            p.setQRcodeImage(QRCodeUtils.createImage(spoolQrCodeDTO.getQrCode(), qrcodeSize), 40, 60);
            p.setChar(spoolQrCodeDTO.getQrCode(), 40, qrcodeSize + 60 + 10, fontSize, fontSize);
            // p.setQRcodeImage(QRCodeUtils.createImage(spoolQrCodeDTO.getQrCode(), 150),
            // 760, 80);
            if (spoolQrCodeDTO.getProject() != null && !StringUtils.isBlank(spoolQrCodeDTO.getProject())) {
                p.setChar("PROJECT:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(spoolQrCodeDTO.getProject(), leftStartPosition + 184, topStartPosition, fontSize, fontSize);
            }
            if (spoolQrCodeDTO.getMaterial() != null && !StringUtils.isBlank(spoolQrCodeDTO.getMaterial())) {
                int maxLength = 26;
                String material = spoolQrCodeDTO.getMaterial();
                if (material.length() > maxLength) {
                    fold++;
                    topStartPosition += 48;
                    String material1 = material.substring(0, maxLength);
                    p.setChar("MATERIAL:" + material1, leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                    String material2 = material.substring(material1.length());
                    topStartPosition += 48;
                    p.setChar(material2, leftStartPosition + 200, topStartPosition, fontSize, fontSize);
                } else {
                    topStartPosition += 48;
                    p.setChar("MATERIAL:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                    p.setChar(spoolQrCodeDTO.getMaterial(), leftStartPosition + 200, topStartPosition, fontSize, fontSize);
                }
            }
            if (spoolQrCodeDTO.getNps() != null && !StringUtils.isBlank(spoolQrCodeDTO.getNps())) {
                topStartPosition += 48;
                p.setChar("NPS:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(spoolQrCodeDTO.getNps(), leftStartPosition + 98, topStartPosition, fontSize, fontSize);
            }
            if (spoolQrCodeDTO.getPaintingCode() != null && !StringUtils.isBlank(spoolQrCodeDTO.getPaintingCode())) {
                topStartPosition += 48;
                p.setChar("PAINTING CODE:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(spoolQrCodeDTO.getPaintingCode(), leftStartPosition + 308, topStartPosition, fontSize,
                    fontSize);
            }
            if (spoolQrCodeDTO.getPaintingArea() != null && !StringUtils.isBlank(spoolQrCodeDTO.getPaintingArea())) {
                int maxLength = 22;
                String paintingArea = spoolQrCodeDTO.getPaintingArea();
                if (paintingArea.length() > maxLength) {
                    fold++;
                    topStartPosition += 48;
                    String paintingArea1 = paintingArea.substring(0, maxLength);
                    p.setChar("PAINTING AREA:" + paintingArea1, leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                    String paintingArea2 = paintingArea.substring(paintingArea1.length());
                    topStartPosition += 48;
                    p.setChar(paintingArea2, leftStartPosition + 308, topStartPosition, fontSize, fontSize);
                } else {
                    topStartPosition += 48;
                    p.setChar("PAINTING AREA:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                    p.setChar(paintingArea, leftStartPosition + 308, topStartPosition, fontSize,
                        fontSize);
                }
            }
            if (spoolQrCodeDTO.getWeight() != null && !StringUtils.isBlank(spoolQrCodeDTO.getWeight())) {
                topStartPosition += 48;
                p.setChar("WEIGHT:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                p.setChar(spoolQrCodeDTO.getWeight(), leftStartPosition + 160, topStartPosition, fontSize, fontSize);
            }
            topStartPosition = 285 + fold * 48;
            fontSize = 40;
            if (spoolQrCodeDTO.getName() != null && !StringUtils.isBlank(spoolQrCodeDTO.getName())) {
                int maxLength = 34;
                String name = spoolQrCodeDTO.getName();
                if (name.length() > maxLength) {
                    topStartPosition += 48;
                    String name1 = getStringNameLimitMaxlen(name, maxLength);
                    p.setChar("NAME: " + name1, 45, topStartPosition, fontSize, fontSize);
                    String name2 = name.substring(name1.length());
                    topStartPosition += 48;
                    p.setChar(name2, 150, topStartPosition, fontSize, fontSize);
                } else {
                    topStartPosition += 48;
                    p.setChar("NAME: " + spoolQrCodeDTO.getName(), 45, topStartPosition, fontSize, fontSize);
                }
            }

            String zpl = p.getZpl();

            // 如果没有设定printCount的时候，默认值是0，最少打印一次
            if (spoolQrCodeDTO.getPrintCount() == null || spoolQrCodeDTO.getPrintCount() == 0) {
                spoolQrCodeDTO.setPrintCount(1);
            }
            for (int i = 1; i <= spoolQrCodeDTO.getPrintCount(); i++) {
                boolean result = p.print(zpl);
                if (result == false) {
                    throw new BusinessError("print error");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new BusinessError("print error");
        }
    }

    @Override
    public void printSpoolQrCodeR(String title, String printTs24Path, String printId,
                                  SpoolQrCodeDetailDTO spoolQrCodeDTO) {
        ZplPrinter p;
        try {
            // "ZDesigner ZT410-300dpi ZPL"
            p = new ZplPrinter(printId, printTs24Path);
        } catch (Exception e) {
            throw new NotFoundError();
        }
        int fold = 0;
        int fontSize = 40;
        int qrcodeSize = 200;
        int topStartPositionR = 200;
        int leftStartPositionR = 500;
        try {
            // p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), qrcodeSize),
            // 80, 10);
            p.setCharR(spoolQrCodeDTO.getQrCode(), 460, 5, fontSize, fontSize);
            p.setQRcodeImage(QRCodeUtils.createImage(spoolQrCodeDTO.getQrCode(), 180), 280, 10);

            if (spoolQrCodeDTO.getProject() != null && !StringUtils.isBlank(spoolQrCodeDTO.getProject())) {
                leftStartPositionR -= 48;
                p.setCharR("PROJECT:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(spoolQrCodeDTO.getProject(), leftStartPositionR + 10, topStartPositionR + 180, fontSize,
                    fontSize);
            }

            if (spoolQrCodeDTO.getMaterial() != null && !StringUtils.isBlank(spoolQrCodeDTO.getMaterial())) {
                int maxLength = 26;
                String material = spoolQrCodeDTO.getMaterial();
                if (material.length() > maxLength) {
                    fold++;
                    leftStartPositionR -= 48;
                    String material1 = material.substring(0, maxLength);
                    p.setCharR("MATERIAL:" + material1, leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                    String material2 = material.substring(material1.length());
                    leftStartPositionR -= 48;
                    p.setCharR(material2, leftStartPositionR + 10, topStartPositionR + 190, fontSize, fontSize);
                } else {
                    leftStartPositionR -= 48;
                    p.setCharR("MATERIAL:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                    p.setCharR(spoolQrCodeDTO.getMaterial(), leftStartPositionR + 10, topStartPositionR + 190, fontSize,
                        fontSize);
                }
            }

            if (spoolQrCodeDTO.getNps() != null && !StringUtils.isBlank(spoolQrCodeDTO.getNps())) {
                leftStartPositionR -= 48;
                p.setCharR("NPS:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(spoolQrCodeDTO.getNps(), leftStartPositionR + 10, topStartPositionR + 80, fontSize,
                    fontSize);
            }

            if (spoolQrCodeDTO.getPaintingCode() != null && !StringUtils.isBlank(spoolQrCodeDTO.getPaintingCode())) {
                leftStartPositionR -= 48;
                p.setCharR("PAINTING CODE:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(spoolQrCodeDTO.getPaintingCode(), leftStartPositionR + 10, topStartPositionR + 280, fontSize,
                    fontSize);
            }

            if (spoolQrCodeDTO.getPaintingArea() != null && !StringUtils.isBlank(spoolQrCodeDTO.getPaintingArea())) {
                int maxLength = 22;
                String paintingArea = spoolQrCodeDTO.getPaintingArea();
                if (paintingArea.length() > maxLength) {
                    fold++;
                    leftStartPositionR -= 48;
                    String paintingArea1 = paintingArea.substring(0, maxLength);
                    p.setCharR("PAINTING AREA:" + paintingArea1, leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                    String paintingArea2 = paintingArea.substring(paintingArea1.length());
                    leftStartPositionR -= 48;
                    p.setCharR(paintingArea2, leftStartPositionR + 10, topStartPositionR + 280, fontSize, fontSize);
                } else {
                    leftStartPositionR -= 48;
                    p.setCharR("PAINTING AREA:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                    p.setCharR(spoolQrCodeDTO.getPaintingArea(), leftStartPositionR + 10, topStartPositionR + 280, fontSize,
                        fontSize);
                }
            }

            if (spoolQrCodeDTO.getWeight() != null && !StringUtils.isBlank(spoolQrCodeDTO.getWeight())) {
                leftStartPositionR -= 48;
                p.setCharR("WEIGHT:", leftStartPositionR + 10, topStartPositionR, fontSize, fontSize);
                p.setCharR(spoolQrCodeDTO.getWeight(), leftStartPositionR + 10, topStartPositionR + 160, fontSize,
                    fontSize);
            }

            if (spoolQrCodeDTO.getName() != null && !StringUtils.isBlank(spoolQrCodeDTO.getName())) {
                leftStartPositionR -= 48;
                // p.setCharR("NAME:", leftStartPositionR + 10, 5, fontSize, fontSize);
                p.setCharR(spoolQrCodeDTO.getName(), leftStartPositionR + 10, 20, fontSize, fontSize);
            }

            String zpl = p.getZpl();

            // 如果没有设定printCount的时候，默认值是0，最少打印一次
            if (spoolQrCodeDTO.getPrintCount() == null || spoolQrCodeDTO.getPrintCount() == 0) {
                spoolQrCodeDTO.setPrintCount(1);
            }
            for (int i = 1; i <= spoolQrCodeDTO.getPrintCount(); i++) {
                boolean result = p.print(zpl);
                if (result == false) {
                    throw new BusinessError("print error");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new BusinessError("print error");
        }
    }

    public static String getStringNameLimitMaxlen(String name, int maxLength) {
        String rgex = "^(.+)(-[^\\-]+)$";
        String name1 = getSubUtilSimple(name, rgex);
        if (name1.length() <= maxLength) {
            return name1;
        }
        rgex = "^(.+)(-[^\\-]+-[^\\-]+)$";
        name1 = getSubUtilSimple(name, rgex);
        if (name1.length() <= maxLength) {
            return name1;
        }
        rgex = "^(.+)(-[^\\-]+-[^\\-]+-[^\\-]+)$";
        name1 = getSubUtilSimple(name, rgex);
        return name1;
    }

    public static String getSubUtilSimple(String soap, String rgex) {
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    @Override
    public void printWLQrCode(String title, String printTs24Path, String printId,
                              WarehouseLoactionDetailDTO warehouseLoactionDetailDTO) {

        ZplPrinter p;
        try {
            // "ZDesigner ZT410-300dpi ZPL"
            p = new ZplPrinter(printId, printTs24Path);
        } catch (Exception e) {
            throw new NotFoundError();
        }
        int fontSize = 40;
        int qrcodeSize = 180;
        int leftStartPosition = 230;
        int topStartPosition = 30;

        try {
            //
//            p.setChar(warehouseLoactionDetailDTO.getQrCode(), 60, 30, fontSize, fontSize);
//            p.setChar(title, 670, 20, 45, 45);
//            p.setQRcodeImage(QRCodeUtils.createImage(warehouseLoactionDetailDTO.getQrCode(), qrcodeSize), 60, 60);
            //修改布局
            p.setChar(title, 670, 20, 45, 45);
            p.setQRcodeImage(QRCodeUtils.createImage(warehouseLoactionDetailDTO.getQrCode(), qrcodeSize), 60, 30);
            p.setChar(warehouseLoactionDetailDTO.getQrCode(), 60, qrcodeSize + 30, fontSize, fontSize);
            // p.setQRcodeImage(QRCodeUtils.createImage(cuttingDTO.getQrCode(), 150), 760,
            // 80);
            if (warehouseLoactionDetailDTO.getProject() != null && !StringUtils.isBlank(warehouseLoactionDetailDTO.getProject())) {
                p.setChar("PROJECT:", leftStartPosition + 20, topStartPosition, fontSize, fontSize);
                p.setChar(warehouseLoactionDetailDTO.getProject(), leftStartPosition + 190, topStartPosition, fontSize, fontSize);
            }

            fontSize = 40;
            if (warehouseLoactionDetailDTO.getWhCode() != null && !StringUtils.isBlank(warehouseLoactionDetailDTO.getWhCode())) {
                topStartPosition += 48;
                p.setChar("WH:", leftStartPosition + 20, topStartPosition, fontSize, fontSize);
                String desc = warehouseLoactionDetailDTO.getWhCode();
                boolean isch = isContainChinese(desc);
                if (isch) {
                    int maxLength = 30;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setText(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setText(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            break;
                        }
                    }
                } else {
                    int maxLength = 24;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setChar(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setChar(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            break;
                        }
                    }
                }
            }

            fontSize = 40;
            if (warehouseLoactionDetailDTO.getLocCode() != null && !StringUtils.isBlank(warehouseLoactionDetailDTO.getLocCode())) {
                topStartPosition += 48;
                p.setChar("LOC:", leftStartPosition + 20, topStartPosition, fontSize, fontSize);
                String desc = warehouseLoactionDetailDTO.getLocCode();
                boolean isch = isContainChinese(desc);
                if (isch) {
                    int maxLength = 30;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setText(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setText(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            break;
                        }
                    }
                } else {
                    int maxLength = 24;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setChar(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setChar(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            break;
                        }
                    }
                }
            }

            fontSize = 40;
            if (warehouseLoactionDetailDTO.getGoodsShelf() != null && !StringUtils.isBlank(warehouseLoactionDetailDTO.getGoodsShelf())) {
                topStartPosition += 48;
                p.setChar("G-S:", leftStartPosition + 20, topStartPosition, fontSize, fontSize);
                String desc = warehouseLoactionDetailDTO.getGoodsShelf();
                boolean isch = isContainChinese(desc);
                if (isch) {
                    int maxLength = 30;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setText(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setText(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            break;
                        }
                    }
                } else {
                    int maxLength = 24;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setChar(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setChar(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            break;
                        }
                    }
                }
            }

            fontSize = 40;
            if (warehouseLoactionDetailDTO.getMemo() != null && !StringUtils.isBlank(warehouseLoactionDetailDTO.getMemo())) {
                topStartPosition += 48;
                p.setChar("Memo:", leftStartPosition + 20, topStartPosition, fontSize, fontSize);
                String desc = warehouseLoactionDetailDTO.getMemo();
                boolean isch = isContainChinese(desc);
                if (isch) {
                    int maxLength = 30;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setText(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setText(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize, 30, 2, 2, 24);
                            break;
                        }
                    }
                } else {
                    int maxLength = 24;
                    while (true) {
                        if (desc.getBytes().length > maxLength) {
                            String name1 = com.ose.util.StringUtils.substringByCount(desc, maxLength);
                            p.setChar(name1, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            int name2Count = desc.getBytes().length - name1.getBytes().length;
                            desc = com.ose.util.StringUtils.substringByCount(desc, maxLength, name2Count);
                            topStartPosition += 48;
                        } else {
                            p.setChar(desc, leftStartPosition + 130, topStartPosition, fontSize, fontSize);
                            break;
                        }
                    }
                }
            }

            String zpl = p.getZpl();
            // 如果没有设定printCount的时候，默认值是0，最少打印一次
            if (warehouseLoactionDetailDTO.getPrintCount() == null || warehouseLoactionDetailDTO.getPrintCount() == 0) {
                warehouseLoactionDetailDTO.setPrintCount(1);
            }
            for (int i = 1; i <= warehouseLoactionDetailDTO.getPrintCount(); i++) {
                boolean result = p.print(zpl);
                if (result == false) {
                    throw new BusinessError("print error");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new BusinessError("print error");
        }

    }

    /**
     * 结构材料下料打印。
     *
     * @param title         标题
     * @param printTs24Path
     * @param printId       打印机id
     * @param cuttingDTO    打印信息
     */
    @Override
    public void structureCuttingPrintQrCode(String title, String printTs24Path, String printId, StructureCuttingDetailListDTO cuttingDTO) {
        if (cuttingDTO.getPrintList() != null && cuttingDTO.getPrintList().size() > 0) {
            for (StructureCuttingDetailDTO structureCuttingDetailDTO : cuttingDTO.getPrintList()) {
                ZplPrinter p;
                try {
                    // "ZDesigner ZT410-300dpi ZPL"
                    p = new ZplPrinter(printId, printTs24Path);
                } catch (Exception e) {
                    throw new NotFoundError();
                }
                int fontSize = 40;
                int qrcodeSize = 180;
                int leftStartPosition = 230;
                int topStartPosition = 40;
                try {

                    //修改布局
                    p.setChar(title, 800, 20, 45, 45);
                    p.setQRcodeImage(QRCodeUtils.createImage(structureCuttingDetailDTO.getQrCode(), qrcodeSize), 50, 30);
                    p.setChar(structureCuttingDetailDTO.getQrCode(), 60, qrcodeSize + 40 + 10, fontSize, fontSize);
                    if (structureCuttingDetailDTO.getProjectNo() != null && !StringUtils.isBlank(structureCuttingDetailDTO.getProjectNo())) {
                        p.setChar("PROJECT:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                        p.setChar(structureCuttingDetailDTO.getProjectNo(), leftStartPosition + 200, topStartPosition, fontSize, fontSize);
                    }
                    if (structureCuttingDetailDTO.getMaterial() != null && !StringUtils.isBlank(structureCuttingDetailDTO.getMaterial())) {
                        topStartPosition += 48;
                        p.setChar("MATERIAL:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                        p.setChar(structureCuttingDetailDTO.getMaterial(), leftStartPosition + 200, topStartPosition, fontSize, fontSize);
                        topStartPosition += 48;
                        p.setChar("QTY:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                        p.setChar(structureCuttingDetailDTO.getQty(), leftStartPosition + 200, topStartPosition, fontSize, fontSize);
                    }

                    if (structureCuttingDetailDTO.getPartNo() != null && !StringUtils.isBlank(structureCuttingDetailDTO.getPartNo())) {
                        int maxLength = 50;
                        String name = structureCuttingDetailDTO.getPartNo();
                        if (name.length() >= maxLength) {
                            System.out.println("1:" + name.length());
                            topStartPosition += 150;
                            String name1 = getStringNameLimitMaxlen(name, maxLength);
                            p.setChar("NO: " + name1, 60, topStartPosition, fontSize, fontSize);
                            String name2 = name.substring(name1.length());
                            topStartPosition += 48;
                            p.setChar(name2, 110, topStartPosition, fontSize, fontSize);
                        } else {
                            System.out.println("2:" + name.length());
                            topStartPosition += 150;
                            p.setChar("NAME: " + structureCuttingDetailDTO.getPartNo(), 60, topStartPosition, fontSize, fontSize);
                        }
                    }
                    String zpl = p.getZpl();
                    // 如果没有设定printCount的时候，默认值是0，最少打印一次
                    if (structureCuttingDetailDTO.getPrintCount() == null || structureCuttingDetailDTO.getPrintCount() == 0) {
                        structureCuttingDetailDTO.setPrintCount(1);
                    }
                    for (int i = 1; i <= structureCuttingDetailDTO.getPrintCount(); i++) {
                        boolean result = p.print(zpl);
                        if (result == false) {
                            throw new BusinessError("print error");
                        }
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    throw new BusinessError("print error");
                }
            }
        } else {
            throw new BusinessError("print error");
        }
    }

    /**
     * 结构配送单打印实体。
     *
     * @param printTs24Path
     * @param printId       打印机id
     * @param partDTO    打印信息
     */
    @Override
    public void structurePartPrintQrCode(String printTs24Path, String printId, StructurePartDetailListDTO partDTO) {
        if (partDTO.getPrintList() != null && partDTO.getPrintList().size() > 0) {
            for (StructurePartDetailDTO structurePartDetailDTO : partDTO.getPrintList()) {
                ZplPrinter p;
                try {
                    // "ZDesigner ZT410-300dpi ZPL"
                    p = new ZplPrinter(printId, printTs24Path);
                } catch (Exception e) {
                    throw new NotFoundError();
                }
                int fontSize = 40;
                int qrcodeSize = 180;
                int leftStartPosition = 230;
                int topStartPosition = 60;
                try {

                    //修改布局
                    p.setQRcodeImage(QRCodeUtils.createImage(structurePartDetailDTO.getQrCode(), qrcodeSize), 50, 30);
                    p.setChar(structurePartDetailDTO.getQrCode(), 60, qrcodeSize + 40 + 10, fontSize, fontSize);

                    if (structurePartDetailDTO.getEntityType() != null && !StringUtils.isBlank(structurePartDetailDTO.getEntityType())) {
                        p.setChar(structurePartDetailDTO.getEntityType(), 800, 40, 45, 45);
                    }

                    if (structurePartDetailDTO.getProjectNo() != null && !StringUtils.isBlank(structurePartDetailDTO.getProjectNo())) {
                        p.setChar("PROJECT:", leftStartPosition + 10, topStartPosition, fontSize, fontSize);
                        p.setChar(structurePartDetailDTO.getProjectNo(), leftStartPosition + 200, topStartPosition, fontSize, fontSize);
                    }

                    if (structurePartDetailDTO.getHeatNo() != null && !StringUtils.isBlank(structurePartDetailDTO.getHeatNo())) {
                        p.setChar("HEATNO:", leftStartPosition + 10, topStartPosition + 60, fontSize, fontSize);
                        p.setChar(structurePartDetailDTO.getHeatNo(), leftStartPosition + 200, topStartPosition + 60, fontSize, fontSize);
                    }

                    if (structurePartDetailDTO.getMaterial() != null && !StringUtils.isBlank(structurePartDetailDTO.getMaterial())) {
                        p.setChar("MATERIAL:", leftStartPosition + 10, topStartPosition + 120, fontSize, fontSize);
                        p.setChar(structurePartDetailDTO.getMaterial(), leftStartPosition + 200, topStartPosition + 120, fontSize, fontSize);
                    }

                    if (structurePartDetailDTO.getLength() != null && !StringUtils.isBlank(structurePartDetailDTO.getLength())) {
                        p.setChar("LENGTH:", leftStartPosition + 10, topStartPosition + 180, fontSize, fontSize);
                        p.setChar(structurePartDetailDTO.getLength() + "(mm)", leftStartPosition + 200, topStartPosition + 180, fontSize, fontSize);
                    }

                    if (structurePartDetailDTO.getPartNo() != null && !StringUtils.isBlank(structurePartDetailDTO.getPartNo())) {
                        p.setChar("NAME:", 50, 400, fontSize, fontSize);
                        p.setChar(structurePartDetailDTO.getPartNo(), 170, 400, fontSize, fontSize);
                    }

                    String zpl = p.getZpl();
                    // 如果没有设定printCount的时候，默认值是0，最少打印一次
                    if (structurePartDetailDTO.getPrintCount() == null || structurePartDetailDTO.getPrintCount() == 0) {
                        structurePartDetailDTO.setPrintCount(1);
                    }
                    for (int i = 1; i <= structurePartDetailDTO.getPrintCount(); i++) {
                        boolean result = p.print(zpl);
                        if (result == false) {
                            throw new BusinessError("print error");
                        }
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    throw new BusinessError("print error");
                }
            }
        } else {
            throw new BusinessError("print error");
        }
    }

}
