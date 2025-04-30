package com.ose.tasks.util;

import cn.hutool.core.io.resource.ClassPathResource;
import com.ose.dto.AnnotationPointDTO;
import com.ose.dto.AnnotationRawColorDTO;
import com.ose.dto.AnnotationResponseDTO;
import com.ose.util.CryptoUtils;
import com.ose.util.FileUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.tasks.entity.drawing.DrawingMarkup;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;
import org.apache.pdfbox.util.Matrix;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationMarkup.IT_FREE_TEXT;

//import static org.opencv.core.Core.NATIVE_LIBRARY_NAME;

public class PdfAnnotationUtil {

    static final float INCH = 72;

    private static float height = 0;

    private static float width = 0;

    private static final float RATIO_PDF_TO_DISPLAY = 1.0f;

    public static List<AnnotationResponseDTO> getPageAnnotation(String fileName, Integer pageNo) {
        List<AnnotationResponseDTO> annotationResponseDTOList = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(new File(fileName));
            PDPage page = document.getPage(pageNo - 1);
            List<PDAnnotation> annotations = page.getAnnotations();
            for (PDAnnotation annotation : annotations) {
                String type = annotation.getSubtype().toUpperCase();
                AnnotationResponseDTO annotationResponseDTO = new AnnotationResponseDTO();
                annotationResponseDTO.setType(type);
                if ("POPUP".equals(type)) continue;
                annotationResponseDTO.setId(annotation.getAnnotationName());
                annotationResponseDTO.setSubject(annotation.getCOSObject().getNameAsString(COSName.SUBJ));
                annotationResponseDTO.setUser(annotation.getCOSObject().getNameAsString(COSName.T));
                PDColor pdColor = annotation.getColor();
//                .getComponents();
                float[] tmpa = new float[4];//annotation.getColor().getComponents();
                if (pdColor == null) {
                    tmpa = new float[]{0.0f, 0.0f, 0.0f};
                } else {
                    tmpa = pdColor.getComponents();
                }
                List<Float> tmpb = new ArrayList<>();
                for (float a : tmpa) {
                    tmpb.add(a);
                }
                AnnotationRawColorDTO rawColor = new AnnotationRawColorDTO(tmpb);

                annotationResponseDTO.setRawColor(rawColor);
                annotationResponseDTO.setBorderStyle(annotation.getBorder().toString());
                annotationResponseDTO.setPageNo(pageNo.toString());

                List<AnnotationPointDTO> points = new ArrayList<>();
                List<AnnotationPointDTO> pathPoints = new ArrayList<>();

                switch (type) {
                    case "LINE":
                        float[] linePoints = ((PDAnnotationLine) annotation).getLine();
                        if (type.equals(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase())) {
                            annotationResponseDTO.setType(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase());
                        }
                        for (int i = 0; i < linePoints.length; i += 2) {
                            AnnotationPointDTO point = new AnnotationPointDTO();
                            point.setX(linePoints[i]);
                            point.setY(linePoints[i + 1]);
                            points.add(point);
                        }
                        break;
                    case "POLYLINE":
                    case "POLYGON":
                        float[] polyLinePoints = ((PDAnnotationMarkup) annotation).getVertices();
                        if (type.equals(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase())) {
                            annotationResponseDTO.setType(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase());
                        } else if (type.equals(PDAnnotationMarkup.SUB_TYPE_POLYGON.toUpperCase())) {
                            annotationResponseDTO.setType(PDAnnotationMarkup.SUB_TYPE_POLYGON.toUpperCase());
                        }
                        for (int i = 0; i < polyLinePoints.length; i += 2) {
                            AnnotationPointDTO point = new AnnotationPointDTO();
                            point.setX(polyLinePoints[i]);
                            point.setY(polyLinePoints[i + 1]);
                            points.add(point);
                        }
                        break;
                    case "INK":
                        float[][] pathPts = ((PDAnnotationMarkup) annotation).getInkList();
                        int size = pathPts[0].length;
                        for (int k = 0; k < size; k = k + 2) {
                            AnnotationPointDTO pathPt = new AnnotationPointDTO();
                            pathPt.setX(pathPts[0][k]);
                            pathPt.setY(pathPts[0][k + 1]);
                            pathPoints.add(pathPt);
                        }
                        break;
                    case "SQUARE":
                        PDRectangle rectPts = annotation.getRectangle();
                        AnnotationPointDTO lb = new AnnotationPointDTO();
                        AnnotationPointDTO ru = new AnnotationPointDTO();
                        lb.setX(rectPts.getLowerLeftX());
                        lb.setY(rectPts.getLowerLeftY());
                        ru.setX(rectPts.getUpperRightX());
                        ru.setY(rectPts.getUpperRightY());
                        points.add(lb);
                        points.add(ru);
                        annotationResponseDTO.setType("RECT");
                        break;
                    case "CIRCLE":
                        PDRectangle circlePts = annotation.getRectangle();
                        AnnotationPointDTO clb = new AnnotationPointDTO();
                        AnnotationPointDTO cru = new AnnotationPointDTO();
                        clb.setX(circlePts.getLowerLeftX());
                        clb.setY(circlePts.getLowerLeftY());
                        cru.setX(circlePts.getUpperRightX());
                        cru.setY(circlePts.getUpperRightY());
                        points.add(clb);
                        points.add(cru);
                        annotationResponseDTO.setType("CIRCLE");
                        break;
                    case "FREETEXT":
                        annotationResponseDTO.setContent(annotation.getContents());
                        annotationResponseDTO.setType("TEXT");
                        PDRectangle rect = annotation.getRectangle();
                        AnnotationPointDTO tlb = new AnnotationPointDTO();
                        AnnotationPointDTO tru = new AnnotationPointDTO();
                        tlb.setX(rect.getLowerLeftX());
                        tlb.setY(rect.getLowerLeftY());
                        tru.setX(rect.getUpperRightX());
                        tru.setY(rect.getUpperRightY());
                        points.add(tlb);
                        points.add(tru);
                        break;
                }

                annotationResponseDTO.setJsonPoints(points);
//                annotationResponseDTO.setJsonPath(pathPoints);
                annotationResponseDTOList.add(annotationResponseDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return annotationResponseDTOList;
    }

    public static void rotate2Zero(PDPage page, PDDocument doc) {
        int rotation = page.getRotation();
        try{
            if (rotation != 0) {
                PDPageContentStream cs = new PDPageContentStream(doc, page,
                    PDPageContentStream.AppendMode.PREPEND, true);

                // 保存当前图形状态
                cs.saveGraphicsState();

                // 计算页面中心点
                PDRectangle cropBox = page.getCropBox();
                float centerX = (cropBox.getLowerLeftX() + cropBox.getUpperRightX()) / 2;
                float centerY = (cropBox.getLowerLeftY() + cropBox.getUpperRightY()) / 2;

                // 围绕中心点旋转
                cs.transform(Matrix.getRotateInstance(Math.toRadians(rotation), centerX, centerY));

                // 恢复图形状态
                cs.restoreGraphicsState();
                cs.close();
            }
                page.setRotation(0);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void rotateAngle(PDDocument doc, PDPage page, Integer angle){

        try {


            PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, false, false);

            PDRectangle cropBox = page.getCropBox();

            float tx = (cropBox.getLowerLeftX() + cropBox.getUpperRightX()) / 2;

            float ty = (cropBox.getLowerLeftY() + cropBox.getUpperRightY()) / 2;

            cs.transform(Matrix.getTranslateInstance(tx, ty));

            cs.transform(Matrix.getRotateInstance(Math.toRadians(angle), 0, 0));

            cs.transform(Matrix.getTranslateInstance(-tx, -ty));

            cs.close();
            page.setRotation(0);
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {

        try {
            //重置角度
            PDDocument doc = PDDocument.load(new File("/var/www/test2.pdf"));
//            PDDocument doc = PDDocument.load(new File("/var/www/test-rotation.pdf"));
//            PDDocument doc = PDDocument.load(new URL("https://issues.apache.org/jira/secure/attachment/12977270/Report_Template_DE.pdf").openStream());
            PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
            PDVariableText field = (PDVariableText) acroForm.getField("search_query");
            List<PDField> fields = acroForm.getFields();
//            PDFont font = PDType0Font.load(doc, new FileInputStream("c:/windows/fonts/arialuni.ttf"), false);
            PDType0Font font = PDType0Font.load(doc, new ClassPathResource("STSong.ttf").getFile());


            PDResources res = acroForm.getDefaultResources();
            String fontName = res.add(font).getName();
            String defaultAppearanceString = "/" + fontName + " 10 Tf 0 g";
//sd
            field.setDefaultAppearance(defaultAppearanceString);
            field.setValue("李柱");

            acroForm.flatten(fields, true);
            doc.save("/var/www/test22.pdf");
            doc.close();

            PDPage page = doc.getPage(0);
            List<PDAnnotation> annos = page.getAnnotations();
            PDAnnotation pda = annos.get(0);
//            pda.setAppearance(new PDPageContentStream(doc, page));
            // 获取当前页面的MediaBox
            PDRectangle mediaBox = page.getMediaBox();


            PDRectangle cropBox = page.getCropBox();
            float tx = (cropBox.getLowerLeftX() + cropBox.getUpperRightX()) / 2;

            float ty = (cropBox.getLowerLeftY() + cropBox.getUpperRightY()) / 2;

            // 交换宽度和高度
            float width = mediaBox.getWidth();
            float height = mediaBox.getHeight();
            mediaBox.setLowerLeftX(mediaBox.getLowerLeftX());
            mediaBox.setLowerLeftY(mediaBox.getLowerLeftY());
            mediaBox.setUpperRightX(mediaBox.getLowerLeftX() + height);
            mediaBox.setUpperRightY(mediaBox.getLowerLeftY() + width);

            float widthc = cropBox.getWidth();
            float heightc = cropBox.getHeight();
            cropBox.setLowerLeftX(cropBox.getLowerLeftX());
            cropBox.setLowerLeftY(cropBox.getLowerLeftY());
            cropBox.setUpperRightX(cropBox.getLowerLeftX() + heightc);
            cropBox.setUpperRightY(cropBox.getLowerLeftY() + widthc);

            // 设置新的MediaBox
            page.setMediaBox(mediaBox);

            page.setCropBox(cropBox);

//                        page.setRotation(0);


            PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, false, false);

//            PDRectangle cropBox = page.getCropBox();



//            cs.transform(Matrix.getTranslateInstance(-1*tx, -1*ty));

            cs.transform(Matrix.getRotateInstance(Math.toRadians(-90), 0, 0));
//            cs.transform(Matrix.getRotateInstance(Math.toRadians(90), tx, ty));
//            cs.transform(Matrix.getRotateInstance(Math.toRadians(-90), ty, tx));
//                        cs.transform(Matrix.getTranslateInstance((int)-0.0*ty, (int)(-0.0*tx)));
            cs.transform(Matrix.getTranslateInstance(-tx*2, 0));


//            cs.transform(Matrix.getTranslateInstance(-(ty-tx)/2, -(ty-tx)/2));

            cs.close();
            page.setRotation(0);
        //////////
            float pw = page.getMediaBox().getWidth();
            float ph = page.getMediaBox().getHeight();
            // 获取页面上的所有注释
            List<PDAnnotation> annotations = page.getAnnotations();

            // 旋转页面上的每个注释
            for (PDAnnotation annotation : annotations) {
                if(annotation instanceof PDAnnotationLine)
                transformLineAnnotation(doc, (PDAnnotationLine) annotation, page);

            }

            /////////

            // 由于交换了宽度和高度，需要旋转页面内容90度以保持视图内容不变
//            page.setRotation((page.getRotation() + 90) % 360);
//// 获取原始页面尺寸
//            PDRectangle rect=page.getMediaBox();
////创建新的页面尺寸，这里以放大为例
//
//            PDRectangle newRect=new PDRectangle(0,0,rect.getWidth()*2,rect.getHeight()* 2);
//设置新的媒体框尺寸
//            page.setMediaBox(PDRectangle.A3);
//
//            PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.PREPEND, false, false);
//
//            PDRectangle cropBox = page.getCropBox();
//
//            float tx = (cropBox.getLowerLeftX() + cropBox.getUpperRightX()) / 2;
//
//            float ty = (cropBox.getLowerLeftY() + cropBox.getUpperRightY()) / 2;
//
//            cs.transform(Matrix.getTranslateInstance(tx, ty));
//
//            cs.transform(Matrix.getRotateInstance(Math.toRadians(90), 0, 0));
//
//            cs.transform(Matrix.getTranslateInstance(-tx, -ty));
////
////
////
//            cs.close();
//            page.setRotation(0);

            doc.save("/var/www/test-rotation1.pdf");
            doc.close();
////////
//            PDDocument document = PDDocument.load(new File("input.pdf"));
//            PDPage page = document.getPage(0);  // 第一页
//            page.setRotation(90);
//            document.save("rotated.pdf");
//            analyzePdf("/var/www/", "test-1.pdf");

//            tidyAnn("/var/www/abcde.pdf");
//            tidyAnn("/var/www/bpm-operation/src/assets/test.pdf");
//            cloneAnn("/Users/CS/FPU/PROCEDURE/Topside_ISO/", "/Users/CS/FPU/PROCEDURE/Topside_ISO/cover.pdf");
//            replaceTagName("/Users/CS/FPU/db/package/tmp/");
//            mergerEqToCable("/Users/CS/FPU/db/package/tmp-cp/", "/Users/CS/FPU/db/package/tmp-ep/");
//            Map<String, String> replaceMap = new HashMap<>();
//            replaceMap.put("IN-","IF-");
//            replaceName("/var/www/tmp2/", replaceMap);
//            rotate("/Users/CS/FPU/db/package/tmp-cp/", "/Users/CS/FPU/db/package/tmp-ep/");
//            analyzePdf("/Users/CS/FPU/db/package/tmp-ep/", "1D-102-DAT-FPU-42020_Rev.D2.pdf");
//            replaceName("/Users/CS/FPU/db/package/tmp-ep/");
//            replaceTagName("/Users/Macbook/Downloads/tmpf/");
        } catch (IOException e) {
            e.printStackTrace();
        }//        Set<String> abs = new HashSet<>();
//        abs.add("abcde ef");
//        abs.add("zsx");
//
//        List pageIs = new ArrayList();
//        pageIs.add(0);
//        pageIs.add(2);
//        PdfUtils.extractPages("/Users/CS/FPU/db/package/test2.pdf", pageIs, "/Users/CS/FPU/db/package");
//        try {
//            parseSysPage("/Users/CS/FPU/db/package/test2.pdf", 0L, "ELECTRICAL_SLD");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void transformLineAnnotation(PDDocument document, PDAnnotationLine lineAnnotation, PDPage page) {
        // 获取页面尺寸
        PDRectangle pageSize = page.getMediaBox();
        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();

        // 获取原始线段的起点和终点坐标
        float[] line = lineAnnotation.getLine();
        float[] originalStartPoint = new float[]{line[0], line[1]};
        float[] originalEndPoint = new float[]{line[2], line[3]};

        // 计算旋转后的坐标
        float[] newStartPoint = transformPoint(originalStartPoint[0], originalStartPoint[1], pageWidth, pageHeight);
        float[] newEndPoint = transformPoint(originalEndPoint[0], originalEndPoint[1], pageWidth, pageHeight);

        // 更新线段坐标
        lineAnnotation.setLine(new float[]{
            newStartPoint[0], newStartPoint[1],
            newEndPoint[0], newEndPoint[1]
        });

        // 更新注释的边界框
        PDRectangle newBoundingBox = calculateNewBoundingBox(newStartPoint, newEndPoint);
        lineAnnotation.setRectangle(newBoundingBox);
    }
    private static float[] transformPoint(float x, float y, float pageWidth, float pageHeight) {
        // 执行顺时针90度旋转的坐标变换:
        // 1. 将坐标原点移到页面中心
        // 2. 进行旋转变换
        // 3. 将坐标原点移回左下角

        // 将坐标原点移到页面中心
        float centerX = pageWidth / 2;
        float centerY = pageHeight / 2;
        float translatedX = x - centerX;
        float translatedY = y - centerY;

        // 顺时针旋转90度
        float rotatedX = translatedY;
        float rotatedY = -translatedX;

        // 移回原点并考虑新的页面尺寸
        // 注意：旋转90度后，宽高互换
        float finalX = rotatedX + pageHeight / 2;
        float finalY = rotatedY + pageWidth / 2;

        return new float[]{finalX, finalY};
    }

    private static PDRectangle calculateNewBoundingBox(float[] startPoint, float[] endPoint) {
        // 计算新的边界框
        float minX = Math.min(startPoint[0], endPoint[0]);
        float maxX = Math.max(startPoint[0], endPoint[0]);
        float minY = Math.min(startPoint[1], endPoint[1]);
        float maxY = Math.max(startPoint[1], endPoint[1]);

        // 添加一些padding以确保线段完全可见
        float padding = 2.0f;
        return new PDRectangle(
            minX - padding,
            minY - padding,
            maxX - minX + 2 * padding,
            maxY - minY + 2 * padding
        );
    }

    // 使用示例
    public void transformAllLineAnnotations(String inputPath, String outputPath) {
        try (PDDocument document = PDDocument.load(new File(inputPath))) {
            for (PDPage page : document.getPages()) {
                for (PDAnnotation annotation : page.getAnnotations()) {
                    if (annotation instanceof PDAnnotationLine) {
                        transformLineAnnotation(document, (PDAnnotationLine) annotation, page);
                    }
                }
            }
            document.save(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, PageAnnoInfo> saveAnnotation(String filename,
                                                            Map<Integer, List<AnnotationResponseDTO>> annotationDTOMap,
                                                            Map<Integer, Set<String>> pageAnnoIdMap,
                                                            Integer start,
                                                            Integer limit,
                                                            Long operatorId,
                                                            Map<String, Long> pageUserNameUserIdMap) throws IOException {

        Map<Integer, PageAnnoInfo> pageAnnoInfoMap = new HashMap<>();
        File file1 = new File(filename);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int total = doc.getNumberOfPages();
        PDType0Font font = null;
        Map<Integer, List<PDAnnotation>> pageAnnotationsMap = new HashMap<>();
        for (int i = start-1; i < start + limit-1 && i<total; i++) {
            PageAnnoInfo pageAnnoInfo = new PageAnnoInfo();

            List<AnnotationResponseDTO> annotationsAtPage = annotationDTOMap.get(i + 1);
            PDPage page1 = doc.getPage(i);
            PageInfo pi = new PageInfo();


            if (annotationsAtPage == null) {annotationsAtPage = new ArrayList<>();}//continue;
            /*-----------取得原有的annotation----*/
            List<PDAnnotation> oldAnnotations = page1.getAnnotations();
            Map<String, PDAnnotation> oldAnnotationMap = new HashMap<>();
            Map<Integer, Set<String>> pageDelAnnoIdMap = new HashMap<>();
            Set<String> pageDelAnnoIds = pageDelAnnoIdMap.computeIfAbsent(i+1, k->new HashSet<>());
            for (PDAnnotation oa : oldAnnotations) {
                oldAnnotationMap.put(oa.getAnnotationName(), oa);
                if (!pageAnnoIdMap.get(i + 1).contains(oa.getAnnotationName())) {
                    pageDelAnnoIdMap.computeIfAbsent(i + 1, k -> new HashSet<>()).add(oa.getAnnotationName());
                }
            }
            float pw = page1.getMediaBox().getUpperRightX();
            float ph = page1.getMediaBox().getUpperRightY();
            int rotation = page1.getRotation();
            pi.setPh(ph);
            pi.setPw(pw);
            pi.setPageRotation(page1.getRotation());
//            System.out.println("pw:" + pw + ",ph:" + ph);
            /*-----------如果原有的annotation包含新的annotation，则更新；如果不包含则创建；如果 deleted标记为true则删除----*/
            for (AnnotationResponseDTO annotationAtPage : annotationsAtPage) {
                if(!annotationAtPage.getPageNo().equals(String.valueOf(i+1))) continue;
                PDAnnotation oldAnnotation = oldAnnotationMap.get(annotationAtPage.getId());
                List<AnnotationPointDTO> pts = annotationAtPage.getJsonPoints();
                AnnotationRawColorDTO rawColor = annotationAtPage.getRawColor();

                if (rawColor == null) {
                    rawColor = getRawColor(annotationAtPage.getPdColor());
                }
                String borderStyle = annotationAtPage.getBorderStyle();
                String user = annotationAtPage.getUser() != null ? annotationAtPage.getUser() : "ftj";
                String subject = annotationAtPage.getSubject() != null ? annotationAtPage.getSubject() : "lill";
                String id = annotationAtPage.getId();
                Map<String, Object> metaData = annotationAtPage.getMetadata();
                if (oldAnnotation != null) {

//                    oldAnnotation.getCOSObject().setString(COSName.SUBJ, subject);
                    oldAnnotation.getCOSObject().setItem("T", new COSString(user));
                    oldAnnotation.setAnnotationName(annotationAtPage.getId());
                    Long oldPageUserId = pageUserNameUserIdMap.get(oldAnnotation.getCOSObject().getNameAsString(COSName.T));
                    if (null!= oldPageUserId && oldPageUserId.equals(operatorId)) {
                        switch (oldAnnotation.getSubtype().toUpperCase()) {
                            case "LINE":
                            case "ARROW":
                                ((PDAnnotationLine) oldAnnotation).setBorderStyle(getBorderStyle(borderStyle));
//                            ((PDAnnotationLine) oldAnnotation).setStartPointEndingStyle((null != metaData && null != metaData.get("startPointEndingStyle")) ? metaData.get("startPointEndingStyle").toString() : null);
//                            ((PDAnnotationLine) oldAnnotation).setEndPointEndingStyle(null != metaData && null != metaData.get("endPointEndingStyle") ? metaData.get("endPointEndingStyle").toString() : null);
//                            if (!pts.isEmpty() && !Float.isNaN(pts.get(0).getX()) && !Float.isNaN(pts.get(0).getY())) {
//                                ((PDAnnotationLine) oldAnnotation).setLine(new float[]{pts.get(0).getX() / RATIO_PDF_TO_DISPLAY,
//                                    pts.get(0).getY() / RATIO_PDF_TO_DISPLAY,
//                                    pts.get(1).getX() / RATIO_PDF_TO_DISPLAY,
//                                    pts.get(1).getY() / RATIO_PDF_TO_DISPLAY});
//                            }

                                float[] l = new float[pts.size() * 2];
                                int z = 0;
                                for (int j = 0; j < pts.size() * 2; j += 2) {
                                    if (null != pts.get(z) && !Float.isNaN(pts.get(z).getX()) && !Float.isNaN(pts.get(z).getY())) {
                                        if(rotation == 0) {
                                            l[j] = pts.get(z).getX();
                                            l[j + 1] = ph - pts.get(z).getY();
                                        } else if(rotation == 90) {
                                            l[j] = pts.get(z).getY();
                                            l[j+1] = pts.get(z).getX();
                                        } else if(rotation == 180) {
                                            l[j] = pw-pts.get(z).getX();
                                            l[j+1] = pts.get(z).getY();
                                        } else if(rotation == 270) {
                                            l[j] = pw-pts.get(z).getY();
                                            l[j+1] = ph - pts.get(z).getX();
                                        }
                                        z = z + 1;
                                    }
                                }
                                oldAnnotation.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));
                                oldAnnotation.setColor(getPDColor(rawColor));

                                ((PDAnnotationLine) oldAnnotation).setLine(l);

                            break;
                        case "POLYLINE":
                        case "POLYGON":
                            ((PDAnnotationMarkup) oldAnnotation).setBorderStyle(getBorderStyle(borderStyle));
                            oldAnnotation.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));
//                            PDBorderStyleDictionary borderStyles = new PDBorderStyleDictionary();
//                            borderStyle.setWidth(2.0f);  // 线宽
//                            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_SOLID);  // 线型
//                            markup.setBorderStyle(borderStyle);
//                            borderStyles = ((PDAnnotationMarkup) oldAnnotation).getBorderStyle();//.setWidth(annotationAtPage.getWidth());
//                            if(borderStyles == null) borderStyles = new PDBorderStyleDictionary();
                                ((PDAnnotationMarkup) oldAnnotation).getBorderStyle().setWidth(annotationAtPage.getBordWidth());
//                            ((PDAnnotationMarkup) oldAnnotation).setBorderStyle(borderStyles);
                                COSArray verticesArray = new COSArray();
                                if (!pts.isEmpty()) {
                                    for (AnnotationPointDTO pt : pts) {
                                        if (!Float.isNaN(pt.getX()) && !Float.isNaN(pt.getY())) {
                                            if(rotation == 0) {
                                                verticesArray.add(new COSFloat(pt.getX()));
                                                verticesArray.add(new COSFloat(ph - pt.getY()));
                                            }  else if(rotation == 90) {
                                                verticesArray.add(new COSFloat(pt.getY()));
                                                verticesArray.add(new COSFloat(pt.getX()));
                                            } else if(rotation == 180) {
                                                verticesArray.add(new COSFloat(pw-pt.getX()));
                                                verticesArray.add(new COSFloat(pt.getY()));
                                            } else if(rotation == 270) {
                                                verticesArray.add(new COSFloat(pw-pt.getY()));
                                                verticesArray.add(new COSFloat(ph - pt.getX()));
                                            }
                                        }
                                    }
                                    oldAnnotation.getCOSObject().setItem(COSName.VERTICES, verticesArray);
                                }
                                oldAnnotation.setColor(getPDColor(rawColor));

                                break;
                            case "INK":
                                ((PDAnnotationMarkup) oldAnnotation).setBorderStyle(getBorderStyle(borderStyle));
                                pts.clear();
                                if (null != annotationAtPage.getPdColor()) {
                                    oldAnnotation.setColor(annotationAtPage.getPdColor());
                                } else {
                                    oldAnnotation.setColor(getPDColor(rawColor));
                                }// border color
                                if (annotationAtPage.getInkPointsList().isEmpty()) {
                                    continue;
                                }
                                int size = 0;
                                for (List<Float> inkPoints : annotationAtPage.getInkPointsList()) {
                                    size = Math.max(size, inkPoints.size());
                                }
                                oldAnnotation.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));

                                float[][] inkList = new float[annotationAtPage.getInkPointsList().size()][size];
                                for(int ll = 0; ll<annotationAtPage.getInkPointsList().size(); ll++) {
                                    List<Float> inkPoints = annotationAtPage.getInkPointsList().get(ll);
                                    for (int k = 0; k < inkPoints.size()/2; k = k +1) {
                                        AnnotationPointDTO apd = new AnnotationPointDTO();
                                        if(rotation == 0) {
                                            inkList[ll][k * 2] = inkPoints.get(k * 2);
                                            inkList[ll][k * 2 + 1] = ph - inkPoints.get(k * 2 + 1);
                                        } else if(rotation == 90) {
                                            inkList[ll][k * 2] = inkPoints.get(k * 2+1);
                                            inkList[ll][k * 2 + 1] = inkPoints.get(k * 2 );
                                        } else if(rotation == 180) {
                                            inkList[ll][k * 2] = pw-inkPoints.get(k * 2);
                                            inkList[ll][k * 2 + 1] = inkPoints.get(k * 2 + 1);
                                        } else if(rotation == 270) {
                                            inkList[ll][k * 2] = pw-inkPoints.get(k * 2+1);
                                            inkList[ll][k * 2 + 1] = ph - inkPoints.get(k * 2);
                                        }
                                        apd.setX(inkList[ll][k*2]);
                                        apd.setY(inkList[ll][k*2+1]);
                                        pts.add(apd);
                                    }
                                    for(int mm=inkPoints.size()/2;mm<size/2;mm++){
                                        inkList[ll][mm*2] = inkPoints.get(inkPoints.size()-2) ;
                                        inkList[ll][mm*2+1] = ph-inkPoints.get(inkPoints.size()-1);
                                    }
                                }

                                ((PDAnnotationMarkup) oldAnnotation).setInkList(inkList);
                                oldAnnotation.setColor(getPDColor(rawColor));

//                            oldAnnotation.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));
                                break;
                            case "RECT":
                            case "CIRCLE":
                            case "SQUARE":

                                List<AnnotationPointDTO> npts = new ArrayList<>();

                                for (AnnotationPointDTO pt : pts) {
                                    AnnotationPointDTO npt = new AnnotationPointDTO();
                                    if (null != pt && !Float.isNaN(pt.getX()) && !Float.isNaN(pt.getY())) {
                                        if(rotation == 0) {
                                            npt.setX(pt.getX());
                                            npt.setY(ph-pt.getY());
                                        }  else if(rotation == 90) {
                                            npt.setX(pt.getY());
                                            npt.setY(pt.getX());
                                        } else if(rotation == 180) {
                                            npt.setX(pw-pt.getX());
                                            npt.setY(pt.getY());
                                        } else if(rotation == 270) {
                                            npt.setX(pw-pt.getY());
                                            npt.setY(ph-pt.getX());
                                        }
                                    }
                                    npts.add(npt);
                                }

                                PDRectangle rect = new PDRectangle();
                                rect.setLowerLeftX(npts.get(0).getX());
                                rect.setLowerLeftY(npts.get(0).getY());
                                rect.setUpperRightX(npts.get(1).getX());
                                rect.setUpperRightY(npts.get(1).getY());
                                oldAnnotation.setRectangle(rect);

//                            oldAnnotation.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));

                                ((PDAnnotationSquareCircle) oldAnnotation).setBorderStyle(getBorderStyle(borderStyle));
                                oldAnnotation.setColor(getPDColor(rawColor));

                                break;
                            case "TEXT":
                            case "FREETEXT":
                                npts = new ArrayList<>();



                                for (AnnotationPointDTO pt : pts) {
                                    AnnotationPointDTO npt = new AnnotationPointDTO();
                                    if (null != pt && !Float.isNaN(pt.getX()) && !Float.isNaN(pt.getY())) {
                                        if(rotation == 0) {
                                            npt.setX(pt.getX());
                                            npt.setY(ph-pt.getY());
                                        }  else if(rotation == 90) {
                                            npt.setX(pt.getY());
                                            npt.setY(pt.getX());
                                        } else if(rotation == 180) {
                                            npt.setX(pw-pt.getX());
                                            npt.setY(pt.getY());
                                        } else if(rotation == 270) {
                                            npt.setX(pw-pt.getY());
                                            npt.setY(ph-pt.getX());
                                        }
                                    }
                                    npts.add(npt);
                                }

                                rect = new PDRectangle();
                                rect.setLowerLeftX(npts.get(0).getX());
                                rect.setLowerLeftY(npts.get(0).getY());
                                rect.setUpperRightX(npts.get(1).getX());
                                rect.setUpperRightY(npts.get(1).getY());
                                oldAnnotation.setRectangle(rect);
                                if(!StringUtils.isEmpty(annotationAtPage.getContent()) && containsChinese(annotationAtPage.getContent())) {
                                    if(font == null) {
                                        font = PDType0Font.load(doc, new ClassPathResource("STSong.ttf").getFile());
                                    }
                                    PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
                                    PDResources res = acroForm.getDefaultResources();
                                    String fontName = res.add(font).getName();
                                    String defaultAppearanceString = "/" + fontName + " " + annotationAtPage.getFontSize() + " Tf 0 g";
                                    ((PDAnnotationMarkup)oldAnnotation).setDefaultAppearance(defaultAppearanceString);
                                }

                                oldAnnotation.setContents(annotationAtPage.getContent());
                                break;
                        }
                    }

                } else {
                    switch (annotationAtPage.getType().toUpperCase()) {
                        case "LINE":
                        case "ARROW":
                            PDAnnotationLine aLine = new PDAnnotationLine();
//            aLine.setContents("Circle->Square");
//            aLine.setCaption(true);  // Make the contents a caption on the line
                            aLine.setSubject(subject);//subject
                            aLine.getCOSObject().setString(COSName.T, user); //Author
                            aLine.getCOSObject().setString("konvaName",annotationAtPage.getName());
                            aLine.setAnnotationName(id);
                            aLine.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));
                            if (pts.isEmpty()) {
                                continue;
                            }
                            float[] l = new float[pts.size() * 2];
//                            l[0] = pts.get(0).getX() / RATIO_PDF_TO_DISPLAY;
//                            l[1] = ph - pts.get(0).getY() / RATIO_PDF_TO_DISPLAY;
//                            l[2] = pts.get(1).getX() / RATIO_PDF_TO_DISPLAY;
//                            l[3] = ph - pts.get(1).getY() / RATIO_PDF_TO_DISPLAY;
                            int z = 0;
                            for (int j = 0; j < pts.size() * 2; j += 2) {
                                if (null != pts.get(z) && !Float.isNaN(pts.get(z).getX()) && !Float.isNaN(pts.get(z).getY())) {
                                    if(rotation == 0) {
                                        l[j] = pts.get(z).getX();
                                        l[j + 1] = ph - pts.get(z).getY();
                                    } else if(rotation == 90) {
                                        l[j] = pts.get(z).getY();
                                        l[j+1] = pts.get(z).getX();
                                    } else if(rotation == 180) {
                                        l[j] = pw-pts.get(z).getX();
                                        l[j+1] = pts.get(z).getY();
                                    } else if(rotation == 270) {
                                        l[j] = pw - pts.get(z).getY();
                                        l[j+1] = ph - pts.get(z).getX();
                                    }
                                    z = z + 1;
                                }
                            }
                            aLine.setLine(l);
                            aLine.setBorderStyle(getBorderStyle(borderStyle));
                            if (null != annotationAtPage.getPdColor()) {
                                aLine.setColor(annotationAtPage.getPdColor());
                            } else {
                                aLine.setColor(getPDColor(rawColor));
                            }
                            if (annotationAtPage.getMetadata() == null) annotationAtPage.setMetadata(new HashMap<>());
                            for (Map.Entry<String, Object> entry : annotationAtPage.getMetadata().entrySet()) {
                                if (entry.getKey().equals("LE"))
                                    aLine.setEndPointEndingStyle((String) entry.getValue());

//                                aLine.setEndPointEndingStyle(PDAnnotationLine.LE_CLOSED_ARROW);
                            }
                            aLine.getCOSObject().setString("lineCap",annotationAtPage.getLineCap());
                            aLine.getCOSObject().setString("lineJoin", annotationAtPage.getLineJoin());
                            aLine.getCOSObject().setString("hitStrokeWidth", annotationAtPage.getHitStrokeWidth().toString());
//                            COSDictionary bsa = new COSDictionary();
//                            bsa.setFloat(COSName.W, annotationAtPage.getHitStrokeWidth());  // 设置宽度
//                            aLine.getCOSObject().setItem(COSName.BS, bsa);
//                            aLine.getCOSObject().setString("id", annotationAtPage.getId());
                            oldAnnotations.add(aLine);
//                            Object startPointEndingStyleObj = metaData == null ? null : metaData.get("startPointEndingStyle");
//                            String startPointEndingStyle = startPointEndingStyleObj == null ? null : startPointEndingStyleObj.toString();
//                            Object endPointEndingStyleObj = metaData == null ? null : metaData.get("endPointEndingStyle");
//                            String endPointEndingStyle = endPointEndingStyleObj == null ? null : endPointEndingStyleObj.toString();
//                            aLine.setStartPointEndingStyle(startPointEndingStyle);
//                            aLine.setEndPointEndingStyle(endPointEndingStyle);
                            break;
                        case "POLYLINE":
                        case "POLYGON":
                            PDAnnotationMarkup polyLine = new PDAnnotationMarkup();
                            if(annotationAtPage.getType().equalsIgnoreCase("POLYLINE")) polyLine.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.SUB_TYPE_POLYLINE);
                            else polyLine.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.SUB_TYPE_POLYGON);
                            polyLine.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));
                            polyLine.getCOSObject().setString("konvaName",annotationAtPage.getName());
                            polyLine.setAnnotationName(id);
                            if (null != annotationAtPage.getPdColor()) {
                                polyLine.setColor(annotationAtPage.getPdColor());
                            } else {
                                polyLine.setColor(getPDColor(rawColor));
                            }// border color
                            if (annotationAtPage.getMetadata() == null) annotationAtPage.setMetadata(new HashMap<>());
                            for (Map.Entry<String, Object> entry : annotationAtPage.getMetadata().entrySet()) {
                                if (entry.getKey().equals("LE"))
                                    polyLine.setEndPointEndingStyle((String) entry.getValue());
                                if (null != annotationAtPage.getPdColor()) {
                                    polyLine.setInteriorColor(annotationAtPage.getPdColor());
                                } else {
                                    polyLine.setInteriorColor(getPDColor(rawColor));
                                }
//                                aLine.setEndPointEndingStyle(PDAnnotationLine.LE_CLOSED_ARROW);
                            }
                            COSArray verticesArray = new COSArray();
                            if (pts.isEmpty()) {
                                continue;
                            }
                            for (AnnotationPointDTO pt : pts) {
                                if (null != pt && !Float.isNaN(pt.getX()) && !Float.isNaN(pt.getY())) {
                                    if(rotation == 0) {
                                        verticesArray.add(new COSFloat(pt.getX() ));
                                        verticesArray.add(new COSFloat(ph - pt.getY()));
                                    }  else if(rotation == 90) {
                                        verticesArray.add(new COSFloat(pt.getY()));
                                        verticesArray.add(new COSFloat(pt.getX()));
                                    } else if(rotation == 180) {
                                        verticesArray.add(new COSFloat(pw-pt.getX()));
                                        verticesArray.add(new COSFloat(pt.getY()));
                                    } else if(rotation == 270) {
                                        verticesArray.add(new COSFloat(pw-pt.getY()));
                                        verticesArray.add(new COSFloat(ph - pt.getX()));
                                    }
                                }
                            }
                            polyLine.getCOSObject().setItem(COSName.VERTICES, verticesArray);
                            PDBorderStyleDictionary bs = getBorderStyle(borderStyle);
                            bs.setWidth(annotationAtPage.getBordWidth());
                            polyLine.setBorderStyle(bs);
                            polyLine.setSubject(subject);//subject
                            polyLine.getCOSObject().setString(COSName.T, user); //Author
                            polyLine.setContents("Polygon annotation");
                            polyLine.setConstantOpacity(annotationAtPage.getOpacity());
                            String isBezier = (annotationAtPage.getBezier() == null || !annotationAtPage.getBezier())? "0":"1";
                            polyLine.getCOSObject().setString("bezier", isBezier);
                            polyLine.getCOSObject().setString("lineCap",annotationAtPage.getLineCap());
                            polyLine.getCOSObject().setString("lineJoin", annotationAtPage.getLineJoin());
                            polyLine.getCOSObject().setString("hitStrokeWidth", annotationAtPage.getHitStrokeWidth()==null?"1":annotationAtPage.getHitStrokeWidth().toString());

                            oldAnnotations.add(polyLine);
                            break;
                        case "INK":
                            PDAnnotationMarkup inkLine = new PDAnnotationMarkup();
                            inkLine.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.SUB_TYPE_INK);
                            pts.clear();
                            inkLine.getCOSObject().setString("konvaName",annotationAtPage.getName());
                            inkLine.setAnnotationName(id);
                            inkLine.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));

                            if (null != annotationAtPage.getPdColor()) {
                                inkLine.setColor(annotationAtPage.getPdColor());
                            } else {
                                inkLine.setColor(getPDColor(rawColor));
                            }// border color
                            // border color
                            if (annotationAtPage.getInkPointsList().isEmpty()) {
                                continue;
                            }
//                            int size = pts.size() * 2;
                            int size = 0;
                            for (List<Float> inkPoints : annotationAtPage.getInkPointsList()) {
                                size = Math.max(size, inkPoints.size());
                            }

                            float[][] inkList = new float[annotationAtPage.getInkPointsList().size()][size];
                            for(int ll = 0; ll<annotationAtPage.getInkPointsList().size(); ll++) {
                                List<Float> inkPoints = annotationAtPage.getInkPointsList().get(ll);
//                                pts = inkElementDTO.getJsonPoints();
//                                int count=0;

                                for (int k = 0; k < inkPoints.size()/2; k = k +1) {
                                    AnnotationPointDTO apd = new AnnotationPointDTO();
//                                    if (null != inkPoints.get(2*k) && !Float.isNaN(inkPoints.get(k*2)) && !Float.isNaN(inkPoints.get(k * 2 + 1))) {
                                        if(rotation == 0) {
                                            inkList[ll][k * 2] = inkPoints.get(k * 2);
                                            inkList[ll][k * 2 + 1] = ph - inkPoints.get(k * 2 + 1);
                                        } else if(rotation == 90) {
                                            inkList[ll][k * 2] = inkPoints.get(k * 2+1);
                                            inkList[ll][k * 2 + 1] = inkPoints.get(k * 2 );
                                        } else if(rotation == 180) {
                                            inkList[ll][k * 2] = pw-inkPoints.get(k * 2);
                                            inkList[ll][k * 2 + 1] = inkPoints.get(k * 2 + 1);
                                        } else if(rotation == 270) {
                                            inkList[ll][k * 2] = pw-inkPoints.get(k * 2+1);
                                            inkList[ll][k * 2 + 1] = ph - inkPoints.get(k * 2);
                                        }
                                        apd.setX(inkList[ll][k*2]);
                                        apd.setY(inkList[ll][k*2+1]);
                                        pts.add(apd);
//                                    }
                                }
                                for(int mm=inkPoints.size()/2;mm<size/2;mm++){
                                    if(rotation == 0) {
                                        inkList[ll][mm*2] = inkPoints.get(inkPoints.size()-2) ;
                                        inkList[ll][mm*2+1] = ph-inkPoints.get(inkPoints.size()-1);
                                    } else if(rotation == 90) {
                                        inkList[ll][mm*2] = inkPoints.get(inkPoints.size()-1) ;
                                        inkList[ll][mm*2+1] = inkPoints.get(inkPoints.size()-2);
                                    } else if(rotation == 180) {
                                        inkList[ll][mm*2] = pw-inkPoints.get(inkPoints.size()-2) ;
                                        inkList[ll][mm*2+1] = inkPoints.get(inkPoints.size()-1);
                                    } else if(rotation == 270) {
                                        inkList[ll][mm*2] = pw-inkPoints.get(inkPoints.size()-1) ;
                                        inkList[ll][mm*2+1] = ph-inkPoints.get(inkPoints.size()-2);
                                    }
                                }
                            }
                            inkLine.setInkList(inkList);

                            bs = getBorderStyle(borderStyle);
                            bs.setWidth(annotationAtPage.getBordWidth());
                            inkLine.setBorderStyle(bs);
                            inkLine.setSubject(subject);//subject
                            inkLine.getCOSObject().setString(COSName.T, user); //Author
                            inkLine.setContents("Polygon annotation");
                            inkLine.setAnnotationName(id);
                            inkLine.getCOSObject().setString("hitStrokeWidth", annotationAtPage.getHitStrokeWidth().toString());
                            inkLine.setLineEndingStyle(annotationAtPage.getLineCap());
                            inkLine.getCOSObject().setString("lineJoin", annotationAtPage.getLineJoin());
                            inkLine.getCOSObject().setString("lineCap", annotationAtPage.getLineCap());
                            oldAnnotations.add(inkLine);
                            break;
                        case "SQUARE":
                            PDAnnotationSquareCircle aSquare = new PDAnnotationSquareCircle(PDAnnotationSquareCircle.SUB_TYPE_SQUARE);
                            aSquare.setContents("Square Annotation");
                            aSquare.getCOSObject().setString("konvaName",annotationAtPage.getName());
                            aSquare.setAnnotationName(id);
//                            aSquare.getCOSObject().setItem(COSName.ROTATE, new COSFloat(rotation));

                            List<AnnotationPointDTO> npts = new ArrayList<>();

                            for (AnnotationPointDTO pt : pts) {
                                AnnotationPointDTO npt = new AnnotationPointDTO();
                                if (null != pt && !Float.isNaN(pt.getX()) && !Float.isNaN(pt.getY())) {
                                    if(rotation == 0) {
                                        npt.setX(pt.getX());
                                        npt.setY(ph-pt.getY());
                                    }  else if(rotation == 90) {
                                        npt.setX(pt.getY());
                                        npt.setY(pt.getX());
                                    } else if(rotation == 180) {
                                        npt.setX(pw-pt.getX());
                                        npt.setY(pt.getY());
                                    } else if(rotation == 270) {
                                        npt.setX(pw-pt.getY());
                                        npt.setY(ph-pt.getX());
                                    }
                                }
                                npts.add(npt);
                            }

                            PDRectangle rect = new PDRectangle();
                            rect.setLowerLeftX(npts.get(0).getX());
                            rect.setLowerLeftY(npts.get(0).getY());
                            rect.setUpperRightX(npts.get(1).getX());
                            rect.setUpperRightY(npts.get(1).getY());
                            aSquare.setRectangle(rect);
//                            aSquare.setRectangle(getRectangle(npts, ph, pw, page1.getRotation()));
                            if (null != annotationAtPage.getPdColor()) {
                                aSquare.setColor(annotationAtPage.getPdColor());
                            } else {
                                aSquare.setColor(getPDColor(rawColor));
                            }// border color
//                            aSquare.setBorderStyle(getBorderStyle(borderStyle));
                            aSquare.setSubject(subject);//subject
                            aSquare.getCOSObject().setString(COSName.T, user); //Author
                            bs = getBorderStyle(borderStyle);
//                            if(annotationAtPage.getBordWidth() > 0)
//                                bs.setWidth(annotationAtPage.getBordWidth());
                            aSquare.setBorderStyle(bs);
                            aSquare.setSubtype("Square");
                            oldAnnotations.add(aSquare);
                            break;
                        case "CIRCLE":
                            PDAnnotationSquareCircle aCircle = new PDAnnotationSquareCircle(PDAnnotationSquareCircle.SUB_TYPE_CIRCLE);
                            aCircle.setContents("Circle Annotation");
                            aCircle.getCOSObject().setString("konvaName",annotationAtPage.getName());
                            aCircle.setAnnotationName(id);

                            npts = new ArrayList<>();

                            for (AnnotationPointDTO pt : pts) {
                                AnnotationPointDTO npt = new AnnotationPointDTO();
                                if (null != pt && !Float.isNaN(pt.getX()) && !Float.isNaN(pt.getY())) {
                                    if(rotation == 0) {
                                        npt.setX(pt.getX());
                                        npt.setY(ph-pt.getY());
                                    }  else if(rotation == 90) {
                                        npt.setX(pt.getY());
                                        npt.setY(pt.getX());
                                    } else if(rotation == 180) {
                                        npt.setX(pw-pt.getX());
                                        npt.setY(pt.getY());
                                    } else if(rotation == 270) {
                                        npt.setX(pw-pt.getY());
                                        npt.setY(ph-pt.getX());
                                    }
                                }
                                npts.add(npt);
                            }

                            rect = new PDRectangle();
                            rect.setLowerLeftX(npts.get(0).getX());
                            rect.setLowerLeftY(npts.get(0).getY());
                            rect.setUpperRightX(npts.get(1).getX());
                            rect.setUpperRightY(npts.get(1).getY());
                            aCircle.setRectangle(rect);
//                            aCircle.setRectangle(getRectangle(pts, ph, pw, page1.getRotation()));
                            if (null != annotationAtPage.getPdColor()) {
                                aCircle.setColor(annotationAtPage.getPdColor());
                            } else {
                                aCircle.setColor(getPDColor(rawColor));
                            }// border color
                            aCircle.setBorderStyle(getBorderStyle(borderStyle));
                            aCircle.setSubject(subject);//subject
                            aCircle.getCOSObject().setString(COSName.T, user); //Author
                            aCircle.setAnnotationName(id);
                            oldAnnotations.add(aCircle);
                            break;
                        case "TEXT":
                            PDAnnotationMarkup aText = new PDAnnotationMarkup();
                            // 加载字体
//                            File fontFile = new File("path/to/font.ttf");

                            // 为注释添加字体资源
//                            PDResources resources = page1.getResources();
//                            resources.add(font);
                            aText.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.SUB_TYPE_FREETEXT);
//                            if (null != annotationAtPage.getPdColor()) {
//                                aText.setColor(annotationAtPage.getPdColor());
//                            } else {
//                                aText.setColor(getPDColor(rawColor));
//                            }
                            if (pts.isEmpty()) {
                                continue;
                            }
                            float lowerLeftX = 0.0f;
                            float lowerLeftY = 0.0f;
                            float upperRightX = 0.0f;
                            float upperRightY = 0.0f;
                            aText.getCOSObject().setItem(COSName.ROTATE, new COSFloat(rotation));
                            PDRectangle rect1 = new PDRectangle();
                            if (null != pts.get(0) && !Float.isNaN(pts.get(0).getX()) && !Float.isNaN(pts.get(0).getY()) && null != annotationAtPage.getWidth()) {
                                if(rotation == 0) {
                                    rect1.setLowerLeftX(pts.get(0).getX());  // 1" in + width of circle
                                    rect1.setUpperRightX(pts.get(0).getX() + annotationAtPage.getWidth() + 30); // 1" in from right, and width of square
                                    rect1.setLowerLeftY(ph - pts.get(0).getY()); // 1" height, 3.5" down
                                    rect1.setUpperRightY(ph - pts.get(0).getY() + annotationAtPage.getFontSize() * 1f); // 3" down (top of circle)
                                } else if(rotation == 90) {
                                    rect1.setLowerLeftX(pts.get(0).getY());  // 1" in + width of circle
                                    rect1.setUpperRightX(pts.get(0).getY() + annotationAtPage.getFontSize() * 1f); // 1" in from right, and width of square
                                    rect1.setLowerLeftY(pts.get(0).getX()); // 1" height, 3.5" down
                                    rect1.setUpperRightY(pts.get(0).getX() + annotationAtPage.getWidth() + 30); // 3" down (top of circle)
                                } else if(rotation == 180) {
                                    rect1.setUpperRightX(pw-pts.get(0).getX());  // 1" in + width of circle
                                    rect1.setLowerLeftX(pw-pts.get(0).getX() - annotationAtPage.getWidth()); // 1" in from right, and width of square
                                    rect1.setUpperRightY(pts.get(0).getY() + annotationAtPage.getFontSize() * 1f); // 1" height, 3.5" down
                                    rect1.setLowerLeftY(pts.get(0).getY()); // 3" down (top of circle)
                                 } else if(rotation == 270) {
                                    rect1.setLowerLeftX(pw-pts.get(0).getY()); // 1" in from right, and width of square
                                    rect1.setLowerLeftY(ph-pts.get(0).getX()-annotationAtPage.getWidth()); // 3" down (top of circle)
                                    rect1.setUpperRightX(pw-pts.get(0).getY() + annotationAtPage.getFontSize());
                                    rect1.setUpperRightY(ph-pts.get(0).getX());
                                 }
                            }

                            aText.setRectangle(rect1);
                            aText.setSubject(subject);
                            aText.setContents(annotationAtPage.getContent());
                            String fontDa = getFontColorDa(annotationAtPage.getPdColor(),annotationAtPage.getFontSize());
                            String ds = getFontColor(annotationAtPage.getPdColor(),annotationAtPage.getFontSize());
//                            font:Helvetica 14;font-stretch:Normal;text-align:left;color:#f472b6
                            aText.getCOSObject().setString(COSName.DA, fontDa);
                            aText.getCOSObject().setString(COSName.DS, ds);
                            aText.getCOSObject().setString(COSName.T, user); //Author
                            // 组合使用多个方法
//                            aText.getBorderStyle().setWidth(0f);
                            aText.getCOSObject().setItem(COSName.BORDER, new COSArray()); //有效果


                            aText.getCOSObject().setString("konvaName",annotationAtPage.getName());
                            aText.setAnnotationName(id);
                            aText.getCOSObject().setString("wrap", annotationAtPage.getWrap());
                            aText.setConstantOpacity(1.0f);
                            aText.setAnnotationName(id);
                            if(!StringUtils.isEmpty(annotationAtPage.getContent()) && containsChinese(annotationAtPage.getContent())) {
                                if(font == null) {
                                    font = PDType0Font.load(doc, new ClassPathResource("STSong.ttf").getFile());
                                }
                                PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
                                PDResources res = acroForm.getDefaultResources();
                                String fontName = res.add(font).getName();
                                String defaultAppearanceString = "/" + fontName + " " + annotationAtPage.getFontSize() + " Tf 0 g";
                                aText.setDefaultAppearance(defaultAppearanceString);
                            }
                            oldAnnotations.add(aText);
                            break;
                    }
                }
            }
            oldAnnotations = oldAnnotations.stream()
                .filter(annotation -> !pageDelAnnoIds.contains(annotation.getAnnotationName()))
                .collect(Collectors.toList());
            for (PDAnnotation ann : oldAnnotations) {
                ann.constructAppearances(doc);
            }
            pageAnnotationsMap.computeIfAbsent(i, k->new ArrayList<>()).addAll(oldAnnotations);
            page1.setAnnotations(oldAnnotations);
            pageAnnoInfo.setPageInfo(pi);
            pageAnnoInfo.setAnnotations(oldAnnotations);
            pageAnnoInfoMap.put(i, pageAnnoInfo);
        }
        doc.save(filename);
//        doc.save(outputFile);
        doc.close();
        return pageAnnoInfoMap;
    }

    private static String getFontColorDa(PDColor pdColor, int fontSize){
        //("1 0 0 rg /Helv 12 Tf");
//        if(isContainChinese)
//            return String.format("%s %s %s rg /STSong-Light %s Tf", pdColor.getComponents()[0], pdColor.getComponents()[1], pdColor.getComponents()[2], fontSize);
//        else
            return String.format("%s %s %s rg /Helv %s Tf", pdColor.getComponents()[0], pdColor.getComponents()[1], pdColor.getComponents()[2], fontSize);
    }
    private static String getFontColor(PDColor pdColor, int fontSize){
        //("1 0 0 rg /Helv 12 Tf");
        //font:Helvetica 14;font-stretch:Normal;text-align:left;color:#f472b6
        // 获取RGB分量
        float[] components = pdColor.getComponents();

        // 将0-1范围的浮点数转换为0-255范围的整数
        int red = Math.round(components[0] * 255);
        int green = Math.round(components[1] * 255);
        int blue = Math.round(components[2] * 255);

        // 确保值在有效范围内
        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));


        // 转换为十六进制格式
//        return rawColorDTO;//String.format("#%02X%02X%02X", red, green, blue);
//        if(isContainChinese)
//            return String.format("SimSun %s;font-stretch:Normal;text-align:left;color:#%02X%02X%02X", fontSize, red,green,blue);
        return String.format("font: Helvetica %s;font-stretch:Normal;text-align:left;color:#%02X%02X%02X", fontSize, red,green,blue);
    }
    private static PDColor getPDColor(AnnotationRawColorDTO rawColor) {
        List<Float> tmpa = rawColor.getJsonRgb();
        float[] components = new float[3];
        for (int ii = 0; ii < tmpa.size(); ii++) {
            components[ii] = tmpa.get(ii);
        }
        return new PDColor(components, PDDeviceRGB.INSTANCE);
    }


    public static PDColor convertPDColor(String hexColor) {
        // 判断是否为RGB格式
        if (hexColor.indexOf("#") > -1) {
            // 移除字符串中的'#'
            hexColor = hexColor.replace("#", "");

            // 将RGB颜色转换为整数
            int red = Integer.parseInt(hexColor.substring(0, 2), 16);
            int green = Integer.parseInt(hexColor.substring(2, 4), 16);
            int blue = Integer.parseInt(hexColor.substring(4, 6), 16);

            // 创建PDColor对象
            return new PDColor(new float[]{red / 255f, green / 255f, blue / 255f}, PDDeviceRGB.INSTANCE);
        } else if (hexColor.indexOf("rgb(") > -1) {
            String[] rgbValues = hexColor.replace("rgb(", "").replace(")", "").split(",");
            // 将字符串RGB值转换为整数
            int red = Integer.parseInt(rgbValues[0].trim());
            int green = Integer.parseInt(rgbValues[1].trim());
            int blue = Integer.parseInt(rgbValues[2].trim());
            return new PDColor(new float[]{red / 255f, green / 255f, blue / 255f}, PDDeviceRGB.INSTANCE);
        } else {
            return null;
        }
    }


    private static PDRectangle getUpdateRectangle(List<AnnotationPointDTO> points, float ph, float pw, float rotation) {
        float minx = pw;
        float miny = ph;
        float maxx = 0.0f;
        float maxy = 0.0f;
        for (AnnotationPointDTO point : points) {
            if (minx > point.getX() ) minx = point.getX() ;
            if (miny > (ph-point.getY() ))
                miny = (ph-point.getY() );
            if (maxx < point.getX() ) maxx = point.getX() ;
            if (maxy < (ph-point.getY() ))
                maxy = (ph-point.getY() );
        }
        PDRectangle position = new PDRectangle();
        position.setLowerLeftX(minx);  // 1" in from right, 1" wide
        position.setLowerLeftY(miny); // 1" height, 3.5" down
        position.setUpperRightX(maxx); // 1" in from right
        position.setUpperRightY(maxy); // 3.5" down
//        BoundingBox boundingBox = new BoundingBox(minx, miny, maxx, maxy);
        return position;// new PDRectangle(boundingBox);
    }

    private static PDRectangle getRectangle(List<AnnotationPointDTO> points, float ph, float pw, float rotation) {
        float minx = pw;
        float miny = ph;
        float maxx = 0.0f;
        float maxy = 0.0f;
        for (AnnotationPointDTO point : points) {
            if (minx > point.getX() / RATIO_PDF_TO_DISPLAY) minx = point.getX() / RATIO_PDF_TO_DISPLAY;
            if (miny > (ph - point.getY() / RATIO_PDF_TO_DISPLAY))
                miny = (ph - point.getY() / RATIO_PDF_TO_DISPLAY);
            if (maxx < point.getX() / RATIO_PDF_TO_DISPLAY) maxx = point.getX() / RATIO_PDF_TO_DISPLAY;
            if (maxy < (ph - point.getY() / RATIO_PDF_TO_DISPLAY))
                maxy = (ph - point.getY() / RATIO_PDF_TO_DISPLAY);
        }
        PDRectangle position = new PDRectangle();
        position.setLowerLeftX(minx);  // 1" in from right, 1" wide
        position.setLowerLeftY(miny); // 1" height, 3.5" down
        position.setUpperRightX(maxx); // 1" in from right
        position.setUpperRightY(maxy); // 3.5" down
//        BoundingBox boundingBox = new BoundingBox(minx, miny, maxx, maxy);
        return position;// new PDRectangle(boundingBox);
    }

    static PDBorderStyleDictionary getBorderStyle(String borderStyleStr) {

        PDBorderStyleDictionary borderStyle = new PDBorderStyleDictionary();

        // 设置borderStyle的样式，例如："S", "D", "B", "I", "U"
        borderStyleStr = StringUtils.isEmpty(borderStyleStr) ? "S" : borderStyleStr; // 这是一个示例

        // 根据字符串类型的borderStyle设置borderStyle的样式
        if ("S".equals(borderStyleStr)) {
            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_SOLID);
        } else if ("D".equals(borderStyleStr)) {
            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_DASHED);
        } else if ("B".equals(borderStyleStr)) {
            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_BEVELED);
        } else if ("I".equals(borderStyleStr)) {
            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_INSET);
        } else if ("U".equals(borderStyleStr)) {
            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
        } else {
            // 如果没有匹配的样式，可以添加自定义逻辑
            // 默认为SOLID
            borderStyle.setStyle(PDBorderStyleDictionary.STYLE_SOLID);
        }
        borderStyle.setWidth(INCH / 36);  // 12th inch
        return borderStyle;
    }

    public static void treatDwgs(String dwgPath) throws IOException {
        String[] files = FileUtils.getFileNames(dwgPath);

        for (String file : files) {
            System.out.print(file);
            if (!file.endsWith(".pdf")) continue;
            String originName = FileUtils.getFileNameNoEx(file);
            File file1 = new File(dwgPath + file);
            PDDocument doc = null;
            try {
                doc = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc.getPage(i);
                List<PDAnnotation> annotations = page1.getAnnotations();
                List<PDAnnotation> newAnns = new ArrayList<>();

                for (PDAnnotation ann : annotations) {
                    String annStr = ann.getCOSObject().getNameAsString("T");
                    ann.getCOSObject().setString("T", "");
                    newAnns.add(ann);
                }
                page1.setAnnotations(newAnns);
            }
            doc.save(dwgPath + file);
            doc.close();
        }
    }

    public static void treatXls(String filename) throws IOException, InvalidFormatException {
        // 创建文件
        File file = new File(filename);

        // 创建流
        InputStream input = new FileInputStream(file);

        // 获取文件后缀名
        String fileExt = file.getName().substring(file.getName().lastIndexOf(".") + 1);

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
        sheet = wb.getSheet("pdf_page_infos");
        int i = 1;
        Row row = sheet.getRow(1);
        final Pattern SPOOL_PATTERN = Pattern.compile(
            "^(\\d[\\d|\\.|\\s|\"|/]+\\-[\\w|\\d]+\\-([C]+\\-){0,1}[\\w|\\d]+\\-HP\\d+(\\-[C|H|P|S]){0,1})(\\-[\\w|\\d]{2,10})((\\-X){0,1}\\-[\\w|\\d]+)$");

        while (WorkbookUtils.getCell(row, 1) != null) {
            String entityNo = WorkbookUtils.getCell(row, 2).getStringCellValue();
            Matcher matcher = SPOOL_PATTERN.matcher(entityNo);
            String spoolNo = "";
            String isoNo = "";
            String lineNo = "";
//            if(entityNo.equalsIgnoreCase("1\"-CO2-C-005-HP371-ENG")){
//                System.out.println("abc");
//            }
            if (matcher.find()) {
                lineNo = matcher.group(1);
                isoNo = matcher.group(1) + matcher.group(4);
                spoolNo = matcher.group(0);

            }
            WorkbookUtils.getCell(row, 6).setCellValue(lineNo);
            WorkbookUtils.getCell(row, 7).setCellValue(isoNo);
            WorkbookUtils.getCell(row, 8).setCellValue(spoolNo);
            row = sheet.getRow(i++);


        }

        WorkbookUtils.save(wb, filename);
    }


    public static List<DrawingMarkup> parseSysPage(String filename, Long projectId, String dwgName) throws IOException {
        Map<String, Set<String>> cables = new HashMap<>();
        if (projectId == null) projectId = 0L;

        File file1 = new File(filename);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        List<DrawingMarkup> cablePageInfos = new ArrayList<>();
//        String dwgName = "ELETRICAL_SLD";

        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            List<PDAnnotation> newAnns = new ArrayList<>();

            for (PDAnnotation ann : annotations) {
                if (ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr = ann.getCOSObject().getNameAsString("T");
//                    if(StringUtils.isEmpty(annStr)) continue;

                    annStr = StringUtils.trim(annStr);
//                    ann.getCOSObject().setString("T", "");


                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }
//                    if (StringUtils.isEmpty(cbStr)) {
//                        continue;
//                    }
                    cbStr = StringUtils.trim(cbStr);
                    ///////
                    annStr = annStr.replaceAll("--", "-").replaceAll("'", "\"").
                        replaceAll("“", "\"").replaceAll("”", "\"").
                        replaceAll("1\\.1/2\"", "1 1/2\"");
                    ((PDAnnotationMarkup) ann).setTitlePopup(annStr);
                    cbStr = cbStr.replaceAll("--", "-").replaceAll("''", "\"").
                        replaceAll("\"\"", "\"").
                        replaceAll("'", "\"").
                        replaceAll("“", "\"").replaceAll("”", "\"");
                    ((PDAnnotationMarkup) ann).setSubject(cbStr);
                    newAnns.add(ann);
                    String[] cbs = cbStr.split("\\|\\|");
                    /////
                    Set<String> tcbs = cables.get(annStr);
                    for (String cb : cbs) {
                        DrawingMarkup cablePageInfo = new DrawingMarkup();
                        cablePageInfo.setDwgNo(dwgName);
                        cablePageInfo.setParentNo(StringUtils.trim(annStr));
                        cablePageInfo.setDisplayedEntityNo(cb);
                        String displayedEntityNo = cb;
                        cablePageInfo.setEntityNo(displayedEntityNo);
                        String entityNo = displayedEntityNo.replaceAll("1 1/2", "1\\.1/2").replaceAll("2 1/2", "2\\.1/2");
                        cablePageInfo.setEntityNo(entityNo);

                        cablePageInfo.setPageNo(i);
                        cablePageInfo.setProjectId(projectId);
                        cablePageInfos.add(cablePageInfo);
                    }

                }
            }
            page1.setAnnotations(newAnns);
        }
        doc.save(filename);
        doc.close();

        return cablePageInfos;

    }

    static private PDAnnotationLine createLine(PDRectangle position, PDColor newColor, PDBorderStyleDictionary bs) {
        // Now we want to draw a line between the two, one end with an open arrow
        PDAnnotationLine aLine = new PDAnnotationLine();
//        aLine.setEndPointEndingStyle(PDAnnotationLine.LE_OPEN_ARROW);
//        aLine.setContents("Circle->Square");
//        aLine.setCaption(true);  // Make the contents a caption on the line
//        PDRectangle position = ann.getRectangle();

        // Set the rectangle containing the line
//        position = new PDRectangle(); // Reuse the variable, but note it's a new object!
//        position.setLowerLeftX(2 * INCH);  // 1" in + width of circle
//        position.setLowerLeftY(ph - 3.5f * INCH - INCH); // 1" height, 3.5" down
//        position.setUpperRightX(pw - INCH-INCH); // 1" in from right, and width of square
//        position.setUpperRightY(ph - 3 * INCH); // 3" down (top of circle)
        aLine.setRectangle(position);

        // Now set the line position itself
        float[] linepos = new float[4];
        linepos[0] = position.getLowerLeftX();  // x2 = lhs of square
        linepos[1] = position.getLowerLeftY(); // y2 halfway down square
        linepos[2] = position.getUpperRightX();  // x1 = rhs of circle
        linepos[3] = position.getUpperRightY(); // y1 halfway down circle

        aLine.setLine(linepos);
//
        aLine.setBorderStyle(bs);
        aLine.setColor(newColor);
        aLine.constructAppearances();

        return aLine;
    }

    static private PDAnnotation cpLine(PDAnnotationLine oldLine, PDColor newColor, PDBorderStyleDictionary bs) {
        // Now we want to draw a line between the two, one end with an open arrow
        PDAnnotationLine newLine = new PDAnnotationLine();

//        PDColor pdColor = new PDColor(new float[]{0, 0, 1}, PD);


//        aLine.setEndPointEndingStyle(PDAnnotationLine.LE_OPEN_ARROW);
//        aLine.setContents("Circle->Square");
//        aLine.setCaption(true);  // Make the contents a caption on the line
        PDRectangle position = oldLine.getRectangle();
        // Set the rectangle containing the line
        newLine.setRectangle(position);
        newLine.setLine(oldLine.getLine());

        // Now set the line position itself
        newLine.setBorderStyle(oldLine.getBorderStyle());
        newLine.setSubject(oldLine.getSubject());
        newLine.getCOSObject().setItem("T", oldLine.getCOSObject().getItem("T"));
        newLine.setColor(newColor);
        newLine.setBorderStyle(bs);

        return newLine;
    }

    static private PDAnnotationMarkup cpPolyLine(PDAnnotationMarkup oldPLine, PDColor newColor, PDBorderStyleDictionary bs) {
        PDAnnotationMarkup polyline = new PDAnnotationMarkup();
        polyline.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.SUB_TYPE_POLYLINE);

        PDRectangle position = new PDRectangle();

//        position = new PDRectangle();
//        position.setLowerLeftX(400 - INCH); // 515
//        position.setLowerLeftY(500 - INCH); //476
//        position.setUpperRightX(400 - 2 * INCH);//637
//        position.setUpperRightY(500 - 2 * INCH);//528
//        polyline.setRectangle(position);
        polyline.setRectangle(oldPLine.getRectangle());
        polyline.setColor(newColor); // border color
//        polygon.getCOSObject().setItem(COSName.IC, green.toCOSArray()); // interior color
        COSArray verticesArray = new COSArray();
//        verticesArray.add(new COSFloat(400 - INCH));
//        verticesArray.add(new COSFloat(500 - 2 * INCH));
//        verticesArray.add(new COSFloat(400 - INCH * 1.5f));
//        verticesArray.add(new COSFloat(500 - INCH));
//        verticesArray.add(new COSFloat(400 - 2 * INCH));
//        verticesArray.add(new COSFloat(500 - 2 * INCH));
        polyline.getCOSObject().setItem(COSName.VERTICES, oldPLine.getCOSObject().getItem(COSName.VERTICES));
//        polyline.getCOSObject().setItem(COSName.VERTICES, verticesArray);
        polyline.setBorderStyle(bs);

//        PDAnnotationMarkup polygon = new PDAnnotationMarkup();
//        polygon.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.SUB_TYPE_POLYLINE);
//        PDRectangle position = new PDRectangle();
//
//        position = new PDRectangle();
//        position.setLowerLeftX(pw - INCH);
//        position.setLowerLeftY(ph - INCH);
//        position.setUpperRightX(pw - 2 * INCH);
//        position.setUpperRightY(ph - 2 * INCH);
//        polygon.setRectangle(position);
//        polygon.setColor(blue); // border color
//        polygon.getCOSObject().setItem(COSName.IC, green.toCOSArray()); // interior color
        polyline.constructAppearances();
        return polyline;
    }

    static private PDAnnotationMarkup createText(String content, PDRectangle position, PDColor color, String daStr) {
        PDAnnotationMarkup freeTextAnnotation = new PDAnnotationMarkup();
        freeTextAnnotation.getCOSObject().setName(COSName.SUBTYPE, IT_FREE_TEXT);

//        PDColor yellow = new PDColor(new float[]{1, 1, 0}, PDDeviceRGB.INSTANCE);
        // this sets background only (contradicts PDF specification)
//        freeTextAnnotation.setColor(yellow);
//        PDRectangle position =new PDRectangle();
//        position.setLowerLeftX(1*INCH);//265
//        position.setLowerLeftY(500 -5f*INCH -3*INCH);//871
//        position.setUpperRightX(400 -INCH);//245
//        position.setUpperRightY(500 -5f*INCH);//1071

//        position.setLowerLeftX(245);//265
//        position.setLowerLeftY(871);//871
//        position.setUpperRightX(265);//245
//        position.setUpperRightY(1071);//1071

        freeTextAnnotation.setRectangle(position);
//        freeTextAnnotation.setTitlePopup("Sophia Lorem");
//        freeTextAnnotation.setSubject("Lorem ipsum");
        freeTextAnnotation.setContents(content);
        freeTextAnnotation.setInteriorColor(color);

//            freeTextAnnotation.setContents("Lorem ipsum dolor sit amet, consetetur sadipscing elitr,"
//            +" sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam "
//            +"erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea "
//            +"rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum "
//            +"dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
//            +"sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam "
//            +"erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea "
//            +"rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum "
//            +"dolor sit amet.");
        // Text and border in blue RGB color, "Helv" font, 20 point
        // setDefaultAppearance() missing in 2.0
        freeTextAnnotation.getCOSObject().setString(COSName.DA, daStr);
//        freeTextAnnotation.setDefaultAppearance("1 0 0 rg /Helv 12 Tf");
//        freeTextAnnotation.setIntent("FreeTextCallout");
//        COSArray newCallout = new COSArray();
//        newCallout.setFloatArray(new float[] {0, 500 - 9 * INCH, 3 * INCH, 500 - 9 * INCH, 4 * INCH, 500 - 8 * INCH   });
        // setCallout() missing in 2.0
//        freeTextAnnotation.getCOSObject().
//
//            setItem(COSName.CL, newCallout);
//        freeTextAnnotation.getCOSObject();
        // setLineEndingStyle() missing in 2.0
//        freeTextAnnotation.getCOSObject().setName(COSName.LE, PDAnnotationLine.LE_OPEN_ARROW);
        return freeTextAnnotation;
    }

    static private PDAnnotationTextMarkup createTextM(PDFont font, float fontSize, String content,
                                                      PDRectangle rect) throws IOException {
        // Now add the markup annotation, a highlight to PDFBox text
        PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(
            PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
        PDColor blue = new PDColor(new float[]{0, 0, 1}, PDDeviceRGB.INSTANCE);

        txtMark.setColor(blue);

        // remove line below if PDF/A-2b (and possibly other PDF-A flavours)
        // also add txtMark.setPrinted(true)
        txtMark.setConstantOpacity((float) 0.2); // 20% transparent

        // Set the rectangle containing the markup
//        PDFont font = PDType1Font.HELVETICA_BOLD;

        float stringWidth = font.getStringWidth(content) * fontSize / 1000f;
        PDRectangle position = rect;
//        position.setLowerLeftX(INCH);
//        position.setLowerLeftY(500 - INCH - 18);
//        position.setUpperRightX(INCH + stringWidth);
//        position.setUpperRightY(500 - INCH);
        txtMark.setRectangle(position);

        // work out the points forming the four corners of the annotations
        // set out in anti clockwise form (Completely wraps the text)
        // OK, the below doesn't match that description.
        // It's what acrobat 7 does and displays properly!
        float[] quads = new float[8];
        quads[0] = position.getLowerLeftX();  // x1
        quads[1] = position.getUpperRightY() - 2; // y1
        quads[2] = position.getUpperRightX(); // x2
        quads[3] = quads[1]; // y2
        quads[4] = quads[0];  // x3
        quads[5] = position.getLowerLeftY() - 2; // y3
        quads[6] = quads[2]; // x4
        quads[7] = quads[5]; // y5

        txtMark.setQuadPoints(quads);
        txtMark.setContents(content);


        txtMark.constructAppearances();
        return txtMark;
    }

    static private void wrText(PDDocument doc, PDPage page, PDFont font, float fontSize, String content,
                               float leftTopX, float leftTopY) throws IOException {
//        float fontSize = 12.0f;
//        PDRectangle pageSize = page.getMediaBox();
//        PDFont font = PDType1Font.HELVETICA;
//        PDDocument doc = null;


        float stringWidth = font.getStringWidth(content) * fontSize / 1000f;
        // calculate to center of the page
        int rotation = page.getRotation();
        boolean rotate = rotation == 90 || rotation == 270;
//        float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
//        float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
//        float centerX = rotate ? pageHeight/2f : (pageWidth - stringWidth)/2f;
//        float centerY = rotate ? (pageWidth - stringWidth)/2f : pageHeight/2f;
        float centerX = leftTopX;// + stringWidth / 2f;
        float centerY = leftTopY + fontSize / 2f;
        // append the content to the existing stream
        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        contentStream.beginText();
        // set font and font size
        contentStream.setFont(font, fontSize);
        // set text color to red
        contentStream.setNonStrokingColor(Color.red);
        if (rotation == 90) {
            // rotate the text according to the page rotation
            contentStream.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, centerX, centerY));
        } else if (rotation == 270) {
            // rotate the text according to the page rotation
            contentStream.setTextMatrix(Matrix.getRotateInstance(3 * Math.PI / 2, centerX, centerY));
        } else {
            contentStream.setTextMatrix(Matrix.getTranslateInstance(centerX, centerY));
        }
        contentStream.showText(content);
        contentStream.endText();
        contentStream.close();
    }

    private static void reWriteFlange() throws IOException {
        List<List<String>> flangeInfos = new ArrayList<>();
        List<List<String>> flangeFInfos = new ArrayList<>();

        String filename = "/var/www/01/21009R.pdf";
//        List<String> fileNames = partitionPdfFileToPerPage("/var/www/01/001.pdf", "/var/www/01/02");

        File file1 = new File(filename);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();

        List<PDAnnotation> addedAnns = new ArrayList<>();
        List<PDAnnotation> removedAnns = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            for (PDAnnotation ann : page1.getAnnotations()) {
                if (ann instanceof PDAnnotationMarkup) {
                    String annStr = ann.getContents();
                    if (StringUtils.isEmpty(annStr)) {
                        continue;
                    }

                    removedAnns.add(ann);
                    PDFont font = PDType1Font.HELVETICA;
                    float fontSize = 12.0f;
                    PDRectangle rect = ann.getRectangle();
                    float rux = rect.getUpperRightX();
                    float ruy = rect.getUpperRightY();
                    float ldx = rect.getLowerLeftX();
                    float ldy = rect.getLowerLeftY();

                    float w = rux - ldx;
                    float h = ruy - ldy;

                    wrText(doc, page1, font, fontSize, ann.getContents().trim(), rux - w, ldy);
//                    annStr = annStr.replaceAll(" ","");
//
//                    if(annStr.length() > 2 && StringUtils.isNumeric(annStr.substring(2,3))) {
//                        annStr = annStr.substring(0,3);
//                    } else {
//                        annStr = annStr.substring(0,2);
//                    }
//                    ann.setContents(annStr);
                }
            }

            annotations.removeAll(removedAnns);
            page1.setAnnotations(annotations);
//            while(page1.getContentStreams().hasNext()){
//                PDStream pds = page1.getContentStreams().next();
//                COSStream css = pds.getCOSObject();
//            }
        }
        doc.save(filename);
        doc.close();

    }


    static private List<PDAnnotation> transformAnn(List<PDAnnotation> annotations, PDDocument doc1, PDPage page1) {
        if (annotations == null) return new ArrayList<>();
        List<PDAnnotation> anns = new ArrayList<>();
        for (PDAnnotation ann : annotations) {
            PDRectangle pdr = ann.getRectangle();
            float ury = pdr.getUpperRightY();
            float urx = pdr.getUpperRightX();
            float lly = pdr.getLowerLeftY();
            float llx = pdr.getLowerLeftX();
            pdr.setUpperRightX(height - ury);
            pdr.setUpperRightY(urx);
            pdr.setLowerLeftY(llx);
            pdr.setLowerLeftX(height - lly);
            if (ann instanceof PDAnnotationLine) {
                pdr.setUpperRightY((urx + llx) / 2);
                pdr.setLowerLeftY((urx + llx) / 2);
                float[] linepos = new float[4];
                linepos[0] = pdr.getLowerLeftX();  // x2 = lhs of square
                linepos[1] = pdr.getLowerLeftY(); // y2 halfway down square
                linepos[2] = pdr.getUpperRightX();  // x1 = rhs of circle
                linepos[3] = pdr.getUpperRightY(); // y1 halfway down circle

                PDAnnotation nl = cpLine((PDAnnotationLine) ann, ann.getColor(), ((PDAnnotationLine) ann).getBorderStyle());
                ((PDAnnotationLine) nl).setLine(linepos);
                ((PDAnnotationLine) nl).setSubject(((PDAnnotationLine) ann).getSubject());
                ((PDAnnotationLine) nl).setTitlePopup(((PDAnnotationLine) ann).getTitlePopup());
                anns.add(nl);

            } else if (ann instanceof PDAnnotationSquareCircle) {
                PDAnnotationSquareCircle pdsc = (PDAnnotationSquareCircle) ann;
                PDRectangle rect = pdsc.getRectangle();
                if (rect == null) continue;
                float rury = rect.getUpperRightY();
                float rurx = rect.getUpperRightX();
                float rlly = rect.getLowerLeftY();
                float rllx = rect.getLowerLeftX();
                PDRectangle nrect = new PDRectangle();
                nrect.setLowerLeftX(height - rury);
                nrect.setLowerLeftY(rllx);
                nrect.setUpperRightX(height - rlly);
                nrect.setUpperRightY(rurx);
                PDAnnotation pda = cpRectangle(nrect, pdsc.getColor(), pdsc.getBorderStyle());
                ((PDAnnotationSquareCircle) pda).setSubject(((PDAnnotationSquareCircle) ann).getSubject());
                ((PDAnnotationSquareCircle) pda).setTitlePopup(((PDAnnotationSquareCircle) ann).getTitlePopup());
                anns.add(pda);
            } else if (ann instanceof PDAnnotationMarkup) {
                float[] vts = ((PDAnnotationMarkup) ann).getVertices();
                if (vts == null) continue;
                float[] nvts = new float[vts.length];

                PDFont font = PDType1Font.HELVETICA;
                float fontSize = 12.0f;


                for (int i = 0; i < vts.length - 1; i = i + 2) {

                    nvts[i] = height - vts[i + 1];
                    nvts[i + 1] = vts[i];
//                    System.out.println(vts[i] + " " + nvts[i]);
//                    System.out.println(vts[i+1] + " " + nvts[i+1]);

//                    try {
//                        wrText(doc1, page1, font, fontSize, nvts[i] + " <"+i/2+"> " +nvts[i+1], nvts[i], nvts[i+1]);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                ////////////////

//                float rux = rect.getUpperRightX();
//                float ruy = rect.getUpperRightY();
//                float ldx = rect.getLowerLeftX();
//                float ldy = rect.getLowerLeftY();
//
//                float w = rux -ldx;
//                float h = ruy - ldy;


                ///////////


                ///
//                PDRectangle rect = ann.getRectangle();

                PDRectangle rect = ann.getRectangle();
                if (rect == null) continue;
                float rury = rect.getUpperRightY();
                float rurx = rect.getUpperRightX();
                float rlly = rect.getLowerLeftY();
                float rllx = rect.getLowerLeftX();
                PDRectangle nrect = new PDRectangle();
                nrect.setLowerLeftX(height - rury);
                nrect.setLowerLeftY(rllx);
                nrect.setUpperRightX(height - rlly);
                nrect.setUpperRightY(rurx);
                ann.setRectangle(nrect);
                ann.getCOSObject().setName("Rotate", "0");
                ann.setNoRotate(true);


                ///
                PDAnnotation npl = cpPolyLine((PDAnnotationMarkup) ann, ann.getColor(), ((PDAnnotationMarkup) ann).getBorderStyle());
                ((PDAnnotationMarkup) npl).setVertices(nvts);
                npl.setRectangle(nrect);
//                ((PDAnnotationMarkup) npl).setDefaultAppearance(((PDAnnotationMarkup) ann).getDefaultAppearance());
                ((PDAnnotationMarkup) npl).setTitlePopup(((PDAnnotationMarkup) ann).getTitlePopup());
                ((PDAnnotationMarkup) npl).setSubject(((PDAnnotationMarkup) ann).getSubject());
                npl.getCOSObject().setName("Rotate", "270");
                PDAppearanceDictionary apps = ann.getAppearance();
                COSDictionary csd = apps.getCOSObject();
                csd.setNeedToBeUpdated(true);
//                npl.setAppearance(getApps(doc1, nrect));
//                npl.setAppearance(apps);
                npl.constructAppearances(doc1);

//                anns.add(npl);
//                ((PDAnnotationMarkup) npl).setVertices(nvts);

//
                anns.add(npl);
            } else if (ann instanceof PDAnnotationPopup) {
                PDRectangle rect = ann.getRectangle();

                if (rect == null) continue;
                float rury = rect.getUpperRightY();
                float rurx = rect.getUpperRightX();
                float rlly = rect.getLowerLeftY();
                float rllx = rect.getLowerLeftX();
                PDRectangle nrect = new PDRectangle();
                nrect.setUpperRightX(height - rury);
                nrect.setUpperRightY(rurx);
                nrect.setLowerLeftX(height - rlly);
                nrect.setLowerLeftY(rllx);
                PDAnnotationPopup npp = new PDAnnotationPopup();
                npp.setRectangle(nrect);
                anns.add(npp);
//                ann.setRectangle(nrect);
            }
        }
        return anns;
    }

    static private PDAnnotation cpRectangle(PDRectangle pdr, PDColor newColor, PDBorderStyleDictionary bs) {
        // Now we want to draw a line between the two, one end with an open arrow
        PDAnnotationSquareCircle newSc = new PDAnnotationSquareCircle("Square");


//        aLine.setEndPointEndingStyle(PDAnnotationLine.LE_OPEN_ARROW);
//        aLine.setContents("Circle->Square");
//        aLine.setCaption(true);  // Make the contents a caption on the line
//        PDRectangle position = oldSc.getRectangle();
        // Set the rectangle containing the line
        newSc.setRectangle(pdr);
        newSc.setColor(newColor);
        newSc.setBorderStyle(bs);
//        newSc.setRectangle(position);
//        newSc.setVertices(oldLine.getLine());
//
//         Now set the line position itself
//        newLine.setBorderStyle(oldLine.getBorderStyle());
//        newLine.setSubject(oldLine.getSubject());
//        newLine.getCOSObject().setItem("T", oldLine.getCOSObject().getItem("T"));
//        newLine.setColor(newColor);
//        newLine.setBorderStyle(bs);
//
//        return newLine;
        return newSc;
    }

    public static void analyzePdf(String dir, String file) throws IOException {
        File file1 = new File(dir + file);
        PDDocument doc1 = null;
        try {
            doc1 = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc1.getNumberOfPages();
        int curentPageRotation = 0;
        for (int i = 0; i < total; i++) {
            PDPage page1 = doc1.getPage(i);
            for (PDAnnotation ann : page1.getAnnotations()) {
                PDAppearanceDictionary pdad = ann.getAppearance();
                if (pdad == null) continue;
                PDAppearanceEntry pdae = pdad.getNormalAppearance();
                PDRectangle pdr = pdad.getNormalAppearance().getAppearanceStream().getBBox();
                float tmp = pdr.getUpperRightX();
                pdr.setUpperRightX(pdr.getUpperRightY());
                pdr.setUpperRightY(tmp);
                pdae.getAppearanceStream().setBBox(pdr);
                pdad.setNormalAppearance(pdae);
            }
        }
    }

    static private PDAppearanceDictionary getApps(PDDocument doc, PDRectangle rect) {
        //create the Form for the appearance stream
//create the Form for the appearance stream
        PDResources holderFormResources = new PDResources();
        PDStream holderFormStream = new PDStream(doc);
        PDFormXObject holderForm = new PDFormXObject(holderFormStream);
        holderForm.setResources(holderFormResources);
        holderForm.setBBox(rect);
        holderForm.setFormType(1);
// trying to set the appreanceStream for the annotation
        PDAppearanceDictionary appearance = new PDAppearanceDictionary();
        appearance.getCOSObject().setDirect(true);
//        appearance.getCOSObject().?set(true);
        PDAppearanceStream appearanceStream = new PDAppearanceStream(holderForm.getCOSStream());
//        try {
//            holderForm.getCOSStream().createFilteredStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        appearance.setNormalAppearance(appearanceStream);

//        annotation.setAppearance(appearance);
        return appearance;
    }

    public static void replaceName(String dwgPath, Map<String, String> replaceMap) throws IOException {
//        String str = "1/2\"-WO-C-027-HP374||PDP4AB-4\"-FW-B-079-U361||1/2\"-WO-C-061-HP374||2\"-VS-C-026-HP380/10\"-VS-C-024-HP380||2\"-VS-C-028-HP380/10\"-VS-C-027-HP380||2\"-VS-C-032-HP380/10\"-VS-C-031-HP380||2\"-VS-C-036-HP380/10\"-VS-C-035-HP380||2\"-VS-C-040-HP380/10\"-VS-C-039-HP380||2\"-VS-C-047-HP380/4\"-VS-C-048-HP380||4\"-VS-C-023-HP380/4\"-VS-C-023-HP380||8\"-WS-C-037-HP375-L9-004||2\"-WS-005-HP375||1 1/2-WO-C-023-HP374||1 1/2\"-WB-C-020-HP374||6\"-WB-C-009-HP374||8\"-WB-C-012-HP374||8\"-WB-C-013-HP374||8\"-WB-C-036-HP376||8\"-WB-C-024-HP374||8\"-WB-C-025-HP374||8\"-WB-C-028-HP374||1/2\"DO-C-028-HP371||1/2\"DO-C-029-HP371||6\"-EE-C-702-HP379||12\"-EE-C-705-HP379||14\"-EE-C-705-HP379||2\"-FW-C-081-HP375||1\"-CO2-C-007-HP371||1/2\"-CO2-C-008-HP371||1\"-OP-C-085-HP371||1\"-OP-C-086-HP371||1\"-OP-C-087-HP371||1\"-OP-C-088-HP371||1\"-OP-C-089-HP371||1\"-OP-C-090-HP371||1\"-OP-C-091-HP371||1\"-OP-C-092-HP371||1\"-OP-C-093-HP371||3/4\"-OD-C-012-HP377||6\"-SP-C-043-HP376||6\"-SP-C-044-HP376||1 1/2\"-OD-C-258-HP375||2\"-ST-C-167-HP377||2\"-ST-C-250-HP377||1/2\"-PW-C-221-HP374||10\"-FW-B-063-U361||10\"-FW-B-064-U361||2\"-DF-B-030-HP301||2\"-FW-B-104-U361||2\"-FW-B-113-U341||2\"-FW-B-119-U361||2\"-FW-B-127-U361||2\"-FW-B-134-U361||2\"-IA-B-001-U361||2\"-PW-B-101-U361||2\"-UA-B-001-U361||2\"-UW-B-101-U361||3\"-FW-B-109-U361||3\"-FW-B-123-U361||3\"-OD-B-011-HP301||4\"-FW-B-107-U361||4\"-FW-B-122-U361||4\"-OD-B-065-HP301";
//        String[] arr = str.split("\\|\\|");
//        List<String> ar = Arrays.asList(arr);
        Map<String, String> replaceStrMap = new HashMap<>();
//        replaceStrMap.put()

        String[] files = FileUtils.getFileNames(dwgPath);

        for (String file : files) {
            System.out.print(file);
            if (!file.endsWith(".pdf")) continue;
            String originName = FileUtils.getFileNameNoEx(file);
            File file1 = new File(dwgPath + file);
            PDDocument doc = null;
            try {
                doc = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc.getPage(i);
                List<PDAnnotation> annotations = page1.getAnnotations();
                List<PDAnnotation> newAnns = new ArrayList<>();

                for (PDAnnotation ann : annotations) {
                    String annStr = ann.getCOSObject().getNameAsString("T");
                    if (ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                        String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                        if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                            continue;
                        }
//                        if (StringUtils.isEmpty(cbStr)) {
//                            continue;
//                        }
                        cbStr = StringUtils.trim(cbStr);
                        annStr = StringUtils.trim(annStr);
                        ///////
//                        if (annStr.length() == 11) {

                        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
                            annStr = annStr.replaceAll(entry.getKey(), entry.getValue());
                            ((PDAnnotationMarkup) ann).setTitlePopup(annStr);

                            cbStr = cbStr.replaceAll(entry.getKey(), entry.getValue());
                            ((PDAnnotationMarkup) ann).setSubject(cbStr);
//                        }
                        }
//                        newAnns.add(ann);
                    }
                }
                page1.setAnnotations(annotations);
            }
            doc.save(dwgPath + file);
            doc.close();
        }

    }

    private static List<PDAnnotation> getCoverAnns(PDDocument doc) throws IOException {
        List<PDAnnotation> annotations = new ArrayList<>();


        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            annotations = page1.getAnnotations();
            break;
        }
//        doc.close();


        return annotations;

    }

    private static void cloneCover(String filePath, List<PDAnnotation> anns) throws IOException {

        System.out.println(filePath);
        if (!filePath.endsWith(".pdf")) return;
        if (filePath.endsWith("cover.pdf")) return;
        File file1 = new File(filePath);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            annotations.addAll(anns);
            page1.setAnnotations(annotations);

        }

        System.out.println(filePath);
        doc.save(filePath);
        doc.close();

    }

    private static void cloneAnn(String dir, String annFile) throws IOException {
        List<PDAnnotation> anns = new ArrayList<>();
        System.out.print(annFile);
        if (!annFile.endsWith(".pdf")) return;
        File file1 = new File(annFile);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            anns = getCoverAnns(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] files = FileUtils.getFileNames(dir);

        for (String file : files) {
            System.out.println(file);
            if (!file.endsWith(".pdf")) continue;
            cloneCover(dir + file, anns);


        }
        doc.close();
    }


    private static void tidyAnn(String filePath) throws IOException {
        File file1 = new File(filePath);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();

        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();

            for (PDAnnotation ann : annotations) {

                ann.constructAppearances();

            }

            /////////
            page1.setAnnotations(annotations);

        }
        doc.save(filePath);
        doc.close();
    }

    public static String markupSubSysPdfColor(String filePath, Map<String, String> subSystemMap,
                                              Map<String, String> subSystemColorMap,
                                              Map<String, String> tagSubSysMap,
                                              Map<String, String> pkgSubSysMap,
                                              String temporaryDir) throws IOException {


//        PDAnnotationLine pdl = new PDAnnotationLine();
//        PDBorderStyleDictionary borderThick = new PDBorderStyleDictionary();

//        borderThick.setWidth(INCH / 18); // 4 point
//        pdl.setBorderStyle(borderThick);

//        PDAnnotationTextMarkup pdt = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_FREETEXT);

//        final float INCH = 72;

        File file1 = new File(filePath);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();

        for (int i = 0; i < total; i++) {
            Map<String, PDColor> subSysColor = new HashMap<>();
            Set<String> ss = new HashSet<>();
            int cnt = 0;

            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            int rt = page1.getRotation();

            float width = page1.getMediaBox().getWidth();
            float height = page1.getMediaBox().getHeight();
            if (rt == 90 || rt == 270) {
                float tmp = width;
                width = height;

                height = tmp;
            }
            //font size; left top;
            List<PDAnnotation> addedAnn = new ArrayList<>();
            List<PDAnnotation> removedAnn = new ArrayList<>();
//            int cnt = 0;
            for (PDAnnotation ann : annotations) {
                boolean isAdded = false;
                if (ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {

//                    removedAnn.add(ann);
                    String annStr = ann.getCOSObject().getNameAsString("T");
                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();

                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    annStr = annStr.replaceAll(" ", "");
                    if (StringUtils.isEmpty(annStr)) annStr = "";
                    String[] anns = annStr.split("\\|\\|");
                    annStr = anns[0];

                    if (StringUtils.isEmpty(cbStr)) cbStr = "";
                    cbStr = cbStr.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    cbStr = cbs[0];
                    String subSystem = "";
                    if (!StringUtils.isEmpty(annStr)) {
                        subSystem = tagSubSysMap.get(annStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(annStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(annStr)) {
                                subSystem = annStr;
                            }
                        }
                    }

                    if (StringUtils.isEmpty(subSystem) && !StringUtils.isEmpty(cbStr)) {
                        subSystem = tagSubSysMap.get(cbStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(cbStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(cbStr)) {
                                subSystem = cbStr;
                            }
                        }

                    }
                    if (cbStr.contains("2\"-CD-B-055-HP316")) {
                        System.out.println("abcd");
                    }

                    if (!StringUtils.isEmpty(subSystem)) {

                        PDColor pdc = getPdColor(subSystemColorMap.get(subSystem));
                        PDBorderStyleDictionary bd = new PDBorderStyleDictionary();
                        if (ann instanceof PDAnnotationLine) {
                            PDAnnotation nl = cpLine((PDAnnotationLine) ann, pdc, ((PDAnnotationLine) ann).getBorderStyle());
                            bd = ((PDAnnotationLine) ann).getBorderStyle();
//                        ann.setColor(pdc)
                            addedAnn.add(nl);
                            isAdded = true;
                        } else if (ann instanceof PDAnnotationMarkup) {
                            PDAnnotation npl = cpPolyLine((PDAnnotationMarkup) ann, pdc, ((PDAnnotationMarkup) ann).getBorderStyle());
                            addedAnn.add(npl);
                            isAdded = true;
                            bd = ((PDAnnotationMarkup) ann).getBorderStyle();
                        }
//                        ann.constructAppearances();
                        if (!ss.contains(subSystem)) {
                            ss.add(subSystem);
                            addedAnn.addAll(setNotation(cnt++, rt, pdc, subSystem, bd));
//                            createLine()

                        }

                    } else {
                        isAdded = true;

                    }
//                    else if(!StringUtils.isEmpty(subSystem) && !ss.contains(annStr)) {//未处理过这个子系统
//                        PDColor pdc = getPdColor(subSystemColorMap.get(subSystem));// pdcolors.get(cnt);
//                        subSysColor.put(subSystem, pdc);
//                        ss.add(subSystem);
////                        ann.setColor(pdc);
////                        ann.constructAppearances();
//                        addedAnn.add(ann);
//
//                    }

                }
                if (!isAdded) {
                    addedAnn.add(ann);
                }
            }
//            annotations.clear();


//            annotations.removeAll(removedAnn);
//
////            annotations.addAll(addedAnn);
//            annotations.addAll(addedAnn);
//            annotations.forEach(an ->{
//                an.constructAppearances();
//            });

            /////////
            page1.setAnnotations(addedAnn);

        }
        String filename2 = temporaryDir + CryptoUtils.shortUniqueId() + ".pdf";
        doc.save(filename2);
        doc.close();


        return filename2;
    }

    private static PDColor getPdColor(String colorStr) {
        colorStr = colorStr.replaceAll("#", "").replaceAll(" ", "");
        float redf = Long.valueOf(colorStr.substring(0, 2), 16) * 1.0f / 255.0f;
        float yellowf = Long.valueOf(colorStr.substring(2, 4), 16) * 1.0f / 255.0f;
        float bluef = Long.valueOf(colorStr.substring(4, 6), 16) * 1.0f / 255.0f;
        float[] c = new float[]{redf, yellowf, bluef};
        PDColorSpace cs = PDDeviceRGB.INSTANCE;
        return new PDColor(c, cs);
//        float[] cl13 = new float[]{0.75f,0.0f,0.0f};
//        PDColor c13 = new PDColor(cl13, cs);
//        pdcolors.add(c13);
    }

    //////////////
    private static Set<PDAnnotation> setNotation(int cnt, int rt, PDColor pdColor, String str, PDBorderStyleDictionary bd) {
        Set<PDAnnotation> ans = new HashSet<>();
        PDRectangle rect1 = new PDRectangle();
        if (rt == 0) {
            rect1.setLowerLeftX(20);
            rect1.setLowerLeftY(260 - cnt * 20);
            rect1.setUpperRightX(300);
            rect1.setUpperRightY(240 - cnt * 20);

        } else if (rt == 90) {
            rect1.setLowerLeftX(20);
            rect1.setLowerLeftY(170 - cnt * 20);

            rect1.setUpperRightX(300);
            rect1.setUpperRightY(200 - cnt * 20);

        } else if (rt == 270) {
            rect1.setLowerLeftX(245 - cnt * 20);
            rect1.setLowerLeftY(width - 220);
            rect1.setUpperRightX(265 - cnt * 20);
            rect1.setUpperRightY(width - 20);

        }


//        PDColor red = new PDColor(new float[] { 1, 0, 0 }, PDDeviceRGB.INSTANCE);

        float fontSize = 10.0f;
//        PDRectangle pageSize = page1.getMediaBox();
        PDFont font = PDType1Font.HELVETICA;
        String daStr = "";
        if(!containsChinese(str)) {
            daStr = pdColor.getComponents()[0] + " " + pdColor.getComponents()[1] + " " + pdColor.getComponents()[2] + " rg /Helv 16 Tf";

        } else {
            daStr = pdColor.getComponents()[0] + " " + pdColor.getComponents()[1] + " " + pdColor.getComponents()[2] + " rg /STHeiti 16 Tf";
        }
        PDAnnotation fs = createText(str, rect1, pdColor, daStr);
        fs.constructAppearances();
        ans.add(fs);

        PDRectangle rect = new PDRectangle();
//        PDBorderStyleDictionary bd = new PDBorderStyleDictionary();
        float[] l0 = null;
        if (rt == 0) {
            l0 = new float[]{20.0f, 255 - cnt * 20.0f, 100.0f, 255 - cnt * 20.0f};
            rect.setLowerLeftX(20.0f + str.length() * 15);
            rect.setLowerLeftY(250 - cnt * 20.0f);
            rect.setUpperRightX(100.0f + str.length() * 15);
            rect.setUpperRightY(250 - cnt * 20.0f);
        } else if (rt == 90) {
            l0 = new float[]{255 - cnt * 20.0f, 20.0f, 255 - cnt * 20.0f, 100.0f};
            rect.setUpperRightX(255 - cnt * 20.0f);
            rect.setUpperRightY(100.0f + str.length() * 15);
            rect.setLowerLeftX(255 - cnt * 20.0f);
            rect.setLowerLeftY(20.0f + str.length() * 15);

        } else if (rt == 270) {
            l0 = new float[]{255 - cnt * 20.0f, width - 20.0f, 255 - cnt * 20.0f, width - 100.0f};

            rect.setLowerLeftX(255 - cnt * 20.0f);
            rect.setLowerLeftY(width - 100.0f - str.length() * 15);

            rect.setUpperRightX(255 - cnt * 20.0f);
            rect.setUpperRightY(width - 20.0f - str.length() * 15);
        }
        ans.add(createLine(rect, pdColor, bd));

        return ans;
    }


    public static AnnotationRawColorDTO getRawColor(PDColor pdColor) {
        if (pdColor == null || pdColor.getComponents() == null || pdColor.getComponents().length < 3) {
            throw new IllegalArgumentException("Invalid PDColor object");
        }

        // 获取RGB分量
        float[] components = pdColor.getComponents();

        // 将0-1范围的浮点数转换为0-255范围的整数
        int red = Math.round(components[0] * 255);
        int green = Math.round(components[1] * 255);
        int blue = Math.round(components[2] * 255);

        // 确保值在有效范围内
        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        List<Float> rgb = new ArrayList<>();
        rgb.add(red * 1.0f / 255);
        rgb.add(green * 1.0f / 255);
        rgb.add(blue * 1.0f / 255);

        AnnotationRawColorDTO rawColorDTO = new AnnotationRawColorDTO(rgb);
        // 转换为十六进制格式
        return rawColorDTO;//String.format("#%02X%02X%02X", red, green, blue);
    }

    public static boolean containsChinese(String str) {
        return str.codePoints().anyMatch(ch -> ch >= 0x4E00 && ch <= 0x9FFF);
    }

}

