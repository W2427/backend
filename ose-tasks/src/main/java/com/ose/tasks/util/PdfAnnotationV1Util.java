package com.ose.tasks.util;

import com.ose.dto.AnnotationPointDTO;
import com.ose.dto.AnnotationRawColorDTO;
import com.ose.dto.AnnotationReplyDTO;
import com.ose.dto.AnnotationResponseDTOV1;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationMarkup.IT_FREE_TEXT;

//import static org.opencv.core.Core.NATIVE_LIBRARY_NAME;

public class PdfAnnotationV1Util {

    static final float INCH = 72;

    private static float height = 0;

    private static float width = 0;

    private static final float RATIO_PDF_TO_DISPLAY = 1.0f;



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


    public static String getRawColorStr(PDColor pdColor) {
        if (pdColor == null || pdColor.getComponents() == null || pdColor.getComponents().length < 3) {
//            throw new IllegalArgumentException("Invalid PDColor object");
            return String.format("#%02X%02X%02X", 255, 0, 0);
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

        // 转换为十六进制格式
        return String.format("#%02X%02X%02X", red, green, blue);
    }


    public static List<AnnotationResponseDTOV1> getPageAnnotationV1(String fileName, int pageNo, int pageNoEnd) {

        List<AnnotationResponseDTOV1> annotationResponseDTOList = new ArrayList<>();
        try {
                PDDocument document = PDDocument.load(new File(fileName));
                for(int ii = pageNo; ii <=pageNoEnd && ii <=document.getNumberOfPages(); ii++) {
                    PDPage page = document.getPage(ii - 1);
                    float pw = page.getMediaBox().getUpperRightX();
                    float ph = page.getMediaBox().getUpperRightY();
                    List<PDAnnotation> annotations = page.getAnnotations();
                    for (PDAnnotation annotation : annotations) {
                        String type = annotation.getSubtype();
                        if ("POPUP".equalsIgnoreCase(type)) continue;

                        AnnotationResponseDTOV1 annotationResponseDTO = convertAnnotation(annotation, ii, pw, ph, page.getRotation());
                        if(!CollectionUtils.isEmpty(annotationResponseDTO.getKonvaObject().getChildren()))
                            annotationResponseDTOList.add(annotationResponseDTO);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return annotationResponseDTOList;
        }

    public static AnnotationResponseDTOV1 convertAnnotation(PDAnnotation annotation, Integer pageNo, Float pw, Float ph, Integer pageRotation) {

        AnnotationResponseDTOV1 annotationResponseDTO = new AnnotationResponseDTOV1();
        String type = annotation.getSubtype();
        annotationResponseDTO.setType(type);
        //name PdfjsAnnotationExtension_shape_group
        String annoId = annotation.getAnnotationName();
        if(annoId == null) annoId = UUID.randomUUID().toString();
        annotationResponseDTO.setId(annoId);
        annotationResponseDTO.setDate(annotation.getModifiedDate());
        annotationResponseDTO.setPageNumber(pageNo);
        String rawColor = null;
        annotationResponseDTO.setReadonly(false);
        AnnotationResponseDTOV1.ContentObj contentsObj = new AnnotationResponseDTOV1.ContentObj();
        contentsObj.setText(annotation.getContents());
        String opacityStr = null;

        annotationResponseDTO.setContentsObj(contentsObj);
        annotationResponseDTO.setTitle(annotation.getCOSObject().getNameAsString(COSName.T));
//                    annotationResponseDTO.setSubject(annotation.getCOSObject().getNameAsString(COSName.SUBJ));
        annotationResponseDTO.setUser(annotation.getCOSObject().getNameAsString(COSName.T));

//                    annotationResponseDTO.setBorderStyle(annotation.getBorder().toString());

        List<AnnotationPointDTO> points = new ArrayList<>();
        List<AnnotationPointDTO> pathPoints = new ArrayList<>();
        List<Float> pts = new ArrayList<>();
        Float borderSize = 2.0f;

        String konvaAttsName = annotation.getCOSObject().getNameAsString("konvaName");
        if(konvaAttsName == null) { konvaAttsName = "PdfjsAnnotationExtension_shape_group";}
        AnnotationResponseDTOV1.KonvaObject konvaObject = new AnnotationResponseDTOV1.KonvaObject();
        AnnotationResponseDTOV1.KonvaObjectAttr konvaObjectAttr = new AnnotationResponseDTOV1.KonvaObjectAttr();
        AnnotationResponseDTOV1.KonvaObjectChildren konvaObjectChildren = new AnnotationResponseDTOV1.KonvaObjectChildren();
        AnnotationResponseDTOV1.KonvaObjectChildrenAttrs konvaObjectChildrenAttrs = new AnnotationResponseDTOV1.KonvaObjectChildrenAttrs();
        List<AnnotationResponseDTOV1.KonvaObjectChildren> konvaChildren = new ArrayList<>();
        switch (type.toUpperCase()) {
            case "LINE":

                float[] linePoints = ((PDAnnotationLine) annotation).getLine();
                if (type.equals(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase())) {
                    annotationResponseDTO.setType(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase());
                }
                for (int i = 0; i < linePoints.length; i += 2) {
                    AnnotationPointDTO point = new AnnotationPointDTO();
                    if(pageRotation == 0) {
                        point.setX(linePoints[i]);
                        point.setY(ph - linePoints[i + 1]);
                    } else if(pageRotation == 90) {
                        point.setX(linePoints[i+1]);
                        point.setY(linePoints[i]);
                    } else if(pageRotation == 180) {
                        point.setX(pw-linePoints[i]);
                        point.setY(linePoints[i+1]);
                    } else if(pageRotation == 270) {
                        point.setX(ph-linePoints[i+1]);
                        point.setY(pw-linePoints[i]);
                    }

                    points.add(point);
                    pts.add(point.getX());
                    pts.add(point.getY());
                }
                rawColor = getRawColorStr(annotation.getColor());

//                                konvaObjectChildrenAttrs.setLineCap(((PDAnnotationLine) annotation).getLineEndingStyle());
                borderSize = ((PDAnnotationLine) annotation).getBorderStyle().getWidth();
                String lineEndStr = annotation.getCOSObject().getItem(COSName.LE) == null ? null :annotation.getCOSObject().getItem(COSName.LE).toString();
                if(lineEndStr != null && lineEndStr.contains("Arrow")) {
                    konvaObjectChildren.setClassName("Arrow");
                    annotationResponseDTO.setType("12");
                    annotationResponseDTO.setPdfjsEditorType("4"); //AnnotationType.LINE
                    konvaObjectChildrenAttrs.setFill(rawColor);

                } else {
                    konvaObjectChildren.setClassName("Line");
                    annotationResponseDTO.setType("11");
                    annotationResponseDTO.setPdfjsEditorType("15"); //AnnotationType.LINE

                }
                annotationResponseDTO.setSubtype(annotation.getSubtype());
                annotationResponseDTO.setPdfjsType("4");
                annotationResponseDTO.setColor(rawColor);
                konvaObjectChildrenAttrs.setStrokeScaleEnabled(false);
                konvaObjectChildrenAttrs.setStroke(rawColor);
                konvaObjectChildrenAttrs.setLineJoin(annotation.getCOSObject().getNameAsString("lineJoin"));
                konvaObjectChildrenAttrs.setLineCap(annotation.getCOSObject().getNameAsString("lineCap"));
                konvaObjectChildrenAttrs.setHitStrokeWidth(annotation.getCOSObject().getNameAsString("hitStrokeWidth"));
                konvaObjectChildrenAttrs.setOpacity(opacityStr == null ? 1.0f : Float.valueOf(opacityStr));
                konvaObjectChildrenAttrs.setPoints(pts);
                konvaChildren.add(konvaObjectChildren);

                break;
            case "POLYLINE":
            case "POLYGON":
                float[] polyLinePoints = ((PDAnnotationMarkup) annotation).getVertices();
                if (type.equals(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase())) {
                    annotationResponseDTO.setType(PDAnnotationMarkup.SUB_TYPE_POLYLINE.toUpperCase());
                }
                for (int i = 0; i < polyLinePoints.length; i += 2) {
                    AnnotationPointDTO point = new AnnotationPointDTO();
                    if(pageRotation == 0) {
                        point.setX(polyLinePoints[i]);
                        point.setY(ph - polyLinePoints[i + 1]);
                    } else if(pageRotation == 90) {
                        point.setX(polyLinePoints[i+1]);
                        point.setY(polyLinePoints[i]);
                    } else if(pageRotation == 180) {
                        point.setX(pw-polyLinePoints[i]);
                        point.setY(polyLinePoints[i+1]);
                    } else if(pageRotation == 270) {
                        point.setX(ph-polyLinePoints[i+1]);
                        point.setY(pw-polyLinePoints[i]);
                    }
                    points.add(point);
                    pts.add(point.getX());
                    pts.add(point.getY());
                }
                konvaObjectChildrenAttrs.setLineCap(((PDAnnotationMarkup) annotation).getLineEndingStyle());
                borderSize = ((PDAnnotationMarkup) annotation).getBorderStyle() == null? 2:((PDAnnotationMarkup) annotation).getBorderStyle().getWidth();
//                                String borderSizeStr = borderSize == null? "0":String.valueOf(borderSize.intValue());
                konvaObjectChildrenAttrs.setStrokeWidth(String.valueOf(borderSize.intValue()));
                opacityStr = String.valueOf(((PDAnnotationMarkup) annotation).getConstantOpacity());
                if(((PDAnnotationMarkup) annotation).getEndPointEndingStyle().toUpperCase().contains("ARROW")) {
                    konvaObjectChildren.setClassName("Arrow");
                    annotationResponseDTO.setType("12");
                    konvaObjectChildrenAttrs.setFill(getRawColorStr(annotation.getColor()));
                } else {
                    konvaObjectChildren.setClassName("Line");
                    annotationResponseDTO.setType("8");
                }
                annotationResponseDTO.setSubtype(annotation.getSubtype());
                annotationResponseDTO.setPdfjsEditorType("15"); //AnnotationType.LINE
                annotationResponseDTO.setPdfjsType("4");
                rawColor = getRawColorStr(annotation.getColor());
                konvaObjectChildrenAttrs.setStrokeScaleEnabled(false);
                konvaObjectChildrenAttrs.setStroke(rawColor);
                konvaObjectChildrenAttrs.setBezier("1".equals(annotation.getCOSObject().getString("bezier")));
                konvaObjectChildrenAttrs.setLineJoin(annotation.getCOSObject().getNameAsString("lineJoin"));
                konvaObjectChildrenAttrs.setLineCap(annotation.getCOSObject().getNameAsString("lineCap"));
                konvaObjectChildrenAttrs.setHitStrokeWidth(annotation.getCOSObject().getNameAsString("hitStrokeWidth"));
                annotationResponseDTO.setColor(rawColor);
                konvaObjectChildrenAttrs.setOpacity(opacityStr == null ? 1.0f : Float.valueOf(opacityStr));
                konvaObjectChildrenAttrs.setPoints(pts);
                konvaChildren.add(konvaObjectChildren);

                break;
            case "INK":
                float[][] pathPts = ((PDAnnotationMarkup) annotation).getInkList();
                int size = pathPts[0].length;
                rawColor = getRawColorStr(annotation.getColor());
                borderSize = ((PDAnnotationMarkup) annotation).getBorderStyle().getWidth();

                for(int ll = 0; ll<pathPts.length; ll++){
                    List<Float> childPts = new ArrayList<>();

                    for (int k = 0; k < size/2; k = k + 1) {
                        AnnotationPointDTO pathPt = new AnnotationPointDTO();
                        if (pageRotation == 0) {
                            pathPt.setX(pathPts[ll][k * 2]);
                            pathPt.setY(ph - pathPts[ll][k * 2 + 1]);
                        }  else if(pageRotation == 90) {
                            pathPt.setX(pathPts[ll][k * 2 +1]);
                            pathPt.setY(pathPts[ll][k * 2]);
                        } else if(pageRotation == 180) {
                            pathPt.setX(pw-pathPts[ll][k * 2]);
                            pathPt.setY(pathPts[ll][k * 2+1]);
                        } else if(pageRotation == 270) {
                            pathPt.setX(ph-pathPts[ll][k * 2+1]);
                            pathPt.setY(pw-pathPts[ll][k * 2]);
                        }
                        points.add(pathPt);
                        childPts.add(pathPt.getX());
                        childPts.add(pathPt.getY());
                        pathPoints.add(pathPt);
                    }
                    AnnotationResponseDTOV1.KonvaObjectChildrenAttrs konvaObjectChildAttrs = new AnnotationResponseDTOV1.KonvaObjectChildrenAttrs();

                    konvaObjectChildAttrs.setLineCap(((PDAnnotationMarkup) annotation).getLineEndingStyle());
                    konvaObjectChildAttrs.setStrokeScaleEnabled(false);
                    konvaObjectChildAttrs.setStroke(rawColor);
                    konvaObjectChildAttrs.setHitStrokeWidth(annotation.getCOSObject().getNameAsString("hitStrokeWidth"));
                    konvaObjectChildAttrs.setLineJoin(annotation.getCOSObject().getNameAsString("lineJoin"));
                    konvaObjectChildAttrs.setLineCap(annotation.getCOSObject().getNameAsString("lineCap"));
                    annotationResponseDTO.setColor(rawColor);
                    konvaObjectChildAttrs.setOpacity(opacityStr == null ? 1.0f : Float.valueOf(opacityStr));
                    konvaObjectChildAttrs.setPoints(childPts);
                    AnnotationResponseDTOV1.KonvaObjectChildren konvaObjectChild = new AnnotationResponseDTOV1.KonvaObjectChildren();
                    konvaObjectChild.setAttrs(konvaObjectChildAttrs);
                    konvaObjectChild.setClassName("Line");
                    konvaChildren.add(konvaObjectChild);

                }
                annotationResponseDTO.setType("7");
                annotationResponseDTO.setSubtype(annotation.getSubtype());
                annotationResponseDTO.setPdfjsEditorType("15"); //AnnotationType.LINE
                annotationResponseDTO.setPdfjsType("4");




                break;
            case "SQUARE":
                PDRectangle rectPts = annotation.getRectangle();
                AnnotationPointDTO lb = new AnnotationPointDTO();
                AnnotationPointDTO ru = new AnnotationPointDTO();
                if(pageRotation == 0) {
                    lb.setX(rectPts.getLowerLeftX());
                    lb.setY(ph - rectPts.getLowerLeftY());
                    ru.setX(rectPts.getUpperRightX());
                    ru.setY(ph - rectPts.getUpperRightY());
                } else if(pageRotation == 90) {
                    lb.setX(rectPts.getLowerLeftY());
                    lb.setY(rectPts.getLowerLeftX());
                    ru.setX(rectPts.getUpperRightY());
                    ru.setY(rectPts.getUpperRightX());
                } else if(pageRotation == 180) {
                    lb.setX(pw-rectPts.getLowerLeftX());
                    lb.setY(rectPts.getLowerLeftY());
                    ru.setX(pw-rectPts.getUpperRightX());
                    ru.setY(rectPts.getUpperRightY());
                } else if(pageRotation == 270) {
                    lb.setX(ph-rectPts.getUpperRightY());
                    lb.setY(pw - rectPts.getLowerLeftX());
                    ru.setX(ph-rectPts.getLowerLeftY());
                    ru.setY(pw - rectPts.getUpperRightX());
                }
                points.add(lb);
                points.add(ru);
                Float ptX = 0.0f;
                Float ptY = 0.0f;

                if(pageRotation == 0) {
//                    pts.add(rectPts.getLowerLeftX());
//                    pts.add(ph-rectPts.getUpperRightY());
//                    pts.add(rectPts.getUpperRightX());
//                    pts.add(ph-rectPts.getLowerLeftY());
                    pts.add(rectPts.getLowerLeftX());
                    pts.add(ph-rectPts.getUpperRightY());
                    pts.add(rectPts.getUpperRightX());
                    pts.add(ph-rectPts.getLowerLeftY());
                    ptX =rectPts.getLowerLeftX();
                    ptY = ph-rectPts.getUpperRightY();
                } else if(pageRotation == 90) {
                    pts.add(rectPts.getLowerLeftY());
                    pts.add(rectPts.getLowerLeftX());
                    pts.add(rectPts.getUpperRightY());
                    pts.add(rectPts.getUpperRightX());
                    ptX = rectPts.getLowerLeftY();
                    ptY = rectPts.getLowerLeftX();
                } else if(pageRotation == 180) {
                    pts.add(pw-rectPts.getLowerLeftX());
                    pts.add(rectPts.getLowerLeftY());
                    pts.add(pw-rectPts.getUpperRightX());
                    pts.add(rectPts.getUpperRightY());
                    ptX = pw-rectPts.getUpperRightX();
                    ptY = rectPts.getLowerLeftY();
                } else if(pageRotation == 270) {
                    pts.add(ph-rectPts.getLowerLeftY());
                    pts.add(pw-rectPts.getUpperRightX());
                    pts.add(ph-rectPts.getUpperRightY());
                    pts.add(pw-rectPts.getLowerLeftX());
                    ptX = ph-rectPts.getUpperRightY();
                    ptY = pw - rectPts.getUpperRightX();

//                    rect1.setLowerLeftX(pw-pts.get(0).getY()); // 1" in from right, and width of square
//                    rect1.setLowerLeftY(ph-pts.get(0).getX()-annotationAtPage.getWidth()); // 3" down (top of circle)
//                    rect1.setUpperRightX(pw-pts.get(0).getY() + annotationAtPage.getFontSize());
//                    rect1.setUpperRightY(ph-pts.get(0).getX());
                }
                annotationResponseDTO.setType("RECT");
                borderSize = ((PDAnnotationSquareCircle) annotation).getBorderStyle().getWidth();
                konvaObjectChildren.setClassName("Rect");
                annotationResponseDTO.setType("5");
                annotationResponseDTO.setSubtype(annotation.getSubtype());
                annotationResponseDTO.setPdfjsEditorType("15"); //AnnotationType.LINE
                annotationResponseDTO.setPdfjsType("5");
                rawColor = getRawColorStr(annotation.getColor());
                annotationResponseDTO.setColor(rawColor);
                PDRectangle rect = annotation.getRectangle();
                konvaObjectChildrenAttrs.setX(ptX);
                konvaObjectChildrenAttrs.setY(ptY);
                konvaObjectChildrenAttrs.setWidth(rect.getWidth());
                konvaObjectChildrenAttrs.setHeight(rect.getHeight());
                konvaObjectChildrenAttrs.setStrokeScaleEnabled(false);
                konvaObjectChildrenAttrs.setStroke(rawColor);
                konvaObjectChildrenAttrs.setOpacity(opacityStr == null ? 1.0f : Float.valueOf(opacityStr));
                konvaObjectChildrenAttrs.setPoints(pts);
                konvaChildren.add(konvaObjectChildren);

                break;
            case "CIRCLE":
                PDRectangle circlePts = annotation.getRectangle();
                AnnotationPointDTO clb = new AnnotationPointDTO();
                AnnotationPointDTO cru = new AnnotationPointDTO();
                ptX = 0.0f;
                ptY = 0.0f;
                if(pageRotation == 0) {
                    clb.setX(circlePts.getLowerLeftX());
                    clb.setY(ph - circlePts.getLowerLeftY());
                    cru.setX(circlePts.getUpperRightX());
                    cru.setY(ph - circlePts.getUpperRightY());
                }  else if(pageRotation == 90) {
                    clb.setX(circlePts.getLowerLeftY());
                    clb.setY(circlePts.getLowerLeftX());
                    cru.setX(circlePts.getUpperRightY());
                    cru.setY(circlePts.getUpperRightX());
                } else if(pageRotation == 180) {
                    clb.setX(pw-circlePts.getLowerLeftX());
                    clb.setY(circlePts.getLowerLeftY());
                    cru.setX(pw-circlePts.getUpperRightX());
                    cru.setY(circlePts.getUpperRightY());
                } else if(pageRotation == 270) {
                    clb.setX(ph-circlePts.getUpperRightY());
                    clb.setY(pw - circlePts.getLowerLeftX());
                    cru.setX(ph-circlePts.getLowerLeftY());
                    cru.setY(pw - circlePts.getUpperRightX());
                }
                points.add(clb);
                points.add(cru);
                if(pageRotation == 0) {
                    pts.add(circlePts.getLowerLeftX());
                    pts.add(ph - circlePts.getLowerLeftY());
                    pts.add(circlePts.getUpperRightX());
                    pts.add(ph - circlePts.getUpperRightY());
                    ptX = 0.5f*(circlePts.getLowerLeftX()+circlePts.getUpperRightX());
                    ptY = ph - 0.5f*(circlePts.getLowerLeftY()+circlePts.getUpperRightY());
                } else if(pageRotation == 90) {
                    pts.add(circlePts.getLowerLeftY());
                    pts.add(circlePts.getLowerLeftX());
                    pts.add(circlePts.getUpperRightY());
                    pts.add(circlePts.getUpperRightX());
                    ptX = 0.5f*(circlePts.getLowerLeftY()+circlePts.getUpperRightY());
                    ptY = 0.5f*(circlePts.getLowerLeftX()+circlePts.getUpperRightX());

                } else if(pageRotation == 180) {
                    pts.add(pw-circlePts.getLowerLeftX());
                    pts.add(circlePts.getLowerLeftY());
                    pts.add(pw-circlePts.getUpperRightX());
                    pts.add(circlePts.getUpperRightY());
                    ptX = pw-0.5f*(circlePts.getLowerLeftX()+circlePts.getUpperRightX());
                    ptY = 0.5f*(circlePts.getLowerLeftY()+circlePts.getUpperRightY());

                } else if(pageRotation == 270) {
                    pts.add(ph-circlePts.getUpperRightY());
                    pts.add(pw-circlePts.getLowerLeftX());
                    pts.add(ph-circlePts.getLowerLeftY());
                    pts.add(pw-circlePts.getUpperRightX());
                    ptX = ph-0.5f*(circlePts.getLowerLeftY()+circlePts.getUpperRightY());
                    ptY = pw - 0.5f*(circlePts.getLowerLeftX()+circlePts.getUpperRightX());

                }
                annotationResponseDTO.setType("CIRCLE");
                borderSize = ((PDAnnotationSquareCircle) annotation).getBorderStyle().getWidth();
                konvaObjectChildren.setClassName("Ellipse");
                annotationResponseDTO.setType("6");
                annotationResponseDTO.setSubtype(annotation.getSubtype());
                annotationResponseDTO.setPdfjsEditorType("6"); //AnnotationType.LINE
                annotationResponseDTO.setPdfjsType("6");
                rawColor = getRawColorStr(annotation.getColor());
                annotationResponseDTO.setColor(rawColor);
                rect = annotation.getRectangle();
                konvaObjectChildrenAttrs.setX(ptX);
                konvaObjectChildrenAttrs.setY(ptY);
                konvaObjectChildrenAttrs.setWidth(rect.getWidth());
                konvaObjectChildrenAttrs.setHeight(rect.getHeight());
                konvaObjectChildrenAttrs.setStrokeScaleEnabled(false);
                konvaObjectChildrenAttrs.setStroke(rawColor);
                konvaObjectChildrenAttrs.setOpacity(opacityStr == null ? 1.0f : Float.valueOf(opacityStr));
                konvaObjectChildrenAttrs.setPoints(pts);
                konvaChildren.add(konvaObjectChildren);

                break;
            case "FREETEXT":
                contentsObj.setText(annotation.getContents());
                annotationResponseDTO.setContentsObj(contentsObj);
                ptX = 0.0f;
                ptY = 0.0f;
                annotationResponseDTO.setType("TEXT");
                rect = annotation.getRectangle();
                AnnotationPointDTO tlb = new AnnotationPointDTO();
                AnnotationPointDTO tru = new AnnotationPointDTO();
                if(pageRotation == 0) {
                    tlb.setX(rect.getLowerLeftX());
                    tlb.setY(ph - rect.getLowerLeftY());
                    tru.setX(rect.getUpperRightX());
                    tru.setY(ph - rect.getUpperRightY());
                } else if(pageRotation == 90) {
                    tlb.setX(rect.getLowerLeftY());
                    tlb.setY(rect.getLowerLeftX());
                    tru.setX(rect.getUpperRightY());
                    tru.setY(rect.getUpperRightX());

                } else if(pageRotation == 180) {
                    tlb.setX(pw-rect.getLowerLeftX());
                    tlb.setY(rect.getLowerLeftY());
                    tru.setX(pw-rect.getUpperRightX());
                    tru.setY(rect.getUpperRightY());


                } else if(pageRotation == 270) {
                    tlb.setX(ph-rect.getUpperRightY());
                    tlb.setY(pw - rect.getLowerLeftX());
                    tru.setX(ph-rect.getLowerLeftY());
                    tru.setY(pw - rect.getUpperRightX());

                }
                points.add(tlb);
                points.add(tru);

//                pts.add(rect.getLowerLeftX());
//                pts.add(ph-rect.getLowerLeftY());
//                pts.add(rect.getUpperRightX());
//                pts.add(ph-rect.getUpperRightY());

                if(pageRotation == 0) {
                    pts.add(rect.getLowerLeftX());
                    pts.add(ph-rect.getLowerLeftY());
                    pts.add(rect.getUpperRightX());
                    pts.add(ph-rect.getUpperRightY());
                    ptX =rect.getLowerLeftX();
                    ptY = ph-rect.getLowerLeftY();
                } else if(pageRotation == 90) {
                    pts.add(rect.getLowerLeftY());
                    pts.add(rect.getLowerLeftX());
                    pts.add(rect.getUpperRightY());
                    pts.add(rect.getUpperRightX());
                    ptX = rect.getLowerLeftY();
                    ptY = rect.getLowerLeftX();
                } else if(pageRotation == 180) {
                    pts.add(pw-rect.getLowerLeftX());
                    pts.add(rect.getLowerLeftY());
                    pts.add(pw-rect.getUpperRightX());
                    pts.add(rect.getUpperRightY());
                    ptX = pw-rect.getUpperRightX();
                    ptY = rect.getLowerLeftY();
                } else if(pageRotation == 270) {
                    pts.add(ph-rect.getUpperRightY());
                    pts.add(pw-rect.getLowerLeftX());
                    pts.add(ph-rect.getLowerLeftY());
                    pts.add(pw-rect.getUpperRightX());
                    ptX = ph-rect.getUpperRightY();
                    ptY = pw - rect.getLowerLeftX();
//                    rect1.setLowerLeftX(pw-pts.get(0).getY()); // 1" in from right, and width of square
//                    rect1.setLowerLeftY(ph-pts.get(0).getX()-annotationAtPage.getWidth()); // 3" down (top of circle)
//                    rect1.setUpperRightX(pw-pts.get(0).getY() + annotationAtPage.getFontSize());
//                    rect1.setUpperRightY(ph-pts.get(0).getX());
                }

                PDAppearanceDictionary appearanceDictionary = annotation.getAppearance();
                appearanceDictionary.getNormalAppearance().getAppearanceStream();//get font information
                borderSize = ((PDAnnotationMarkup) annotation).getBorderStyle()==null?0f:((PDAnnotationMarkup) annotation).getBorderStyle().getWidth();
                konvaObjectChildren.setClassName("Text");
                String dss = ((PDAnnotationMarkup) annotation).getDefaultStyleString();

                rawColor = "#ff0000";
                if(dss == null) {
                    String da = ((PDAnnotationMarkup) annotation).getDefaultAppearance();
                    rawColor = getRawColorStrFromDa(da);
                } else {
                    rawColor = getRawColorStrFromDs(dss);
                }
                konvaObjectChildrenAttrs.setFill(rawColor);
                konvaObjectChildrenAttrs.setText(annotation.getContents());
                annotationResponseDTO.setType("4");
                annotationResponseDTO.setSubtype(annotation.getSubtype());
                annotationResponseDTO.setPdfjsEditorType("13"); //AnnotationType.LINE
                annotationResponseDTO.setPdfjsType("3");
                konvaObjectChildrenAttrs.setWrap(annotation.getCOSObject().getNameAsString("wrap"));
                konvaObjectChildrenAttrs.setX(ptX);//(annotation.getRectangle().getLowerLeftX());
                konvaObjectChildrenAttrs.setY(ptY);//(ph - annotation.getRectangle().getLowerLeftY());
                konvaObjectChildrenAttrs.setWidth(annotation.getRectangle().getWidth());
                String ds = ((PDAnnotationMarkup) annotation).getDefaultStyleString();
                String da=((PDAnnotationMarkup) annotation).getDefaultAppearance();
                String fontSize = "12";
                if(ds != null) {
                    fontSize = getFontSizeFromDs(ds);
                } else {
                    fontSize = getFontSizeFromDa(da);

                }
                konvaObjectChildrenAttrs.setFontSize(Integer.parseInt(fontSize));
//              rawColor = getRawColorStr(annotation.getColor());
                annotationResponseDTO.setColor(rawColor);
                konvaChildren.add(konvaObjectChildren);


                break;
        }
        PDRectangle rect = getRectangle(points, ph, pw, pageRotation);
        AnnotationResponseDTOV1.KonvaClientRect konvaClientRect = new AnnotationResponseDTOV1.KonvaClientRect();
        konvaClientRect.setX(rect.getLowerLeftX());
        konvaClientRect.setY(ph-rect.getUpperRightY());
        konvaClientRect.setWidth(rect.getWidth());
        konvaClientRect.setHeight(rect.getHeight());
        annotationResponseDTO.setKonvaClientRect(konvaClientRect);


        konvaObjectAttr.setName(konvaAttsName);
        konvaObjectAttr.setId(annotation.getAnnotationName());
        konvaObject.setClassName("Group");
        konvaObject.setAttrs(konvaObjectAttr);

//                        konvaObjectChildrenAttrs.setFill("S");
//                        konvaObjectChildrenAttrs.setFontSize(12);//TODO
        konvaObjectChildrenAttrs.setHeight(rect.getHeight());
        konvaObjectChildrenAttrs.setWidth(rect.getWidth());
//                        konvaObjectChildrenAttrs.setStrokeWidth(borderSize.toString());
//                        konvaObjectChildrenAttrs.setLineJoin("Round");

        konvaObjectChildren.setAttrs(konvaObjectChildrenAttrs);

        konvaObject.setChildren(konvaChildren);

        annotationResponseDTO.setKonvastring(konvaObject);
        return annotationResponseDTO;
    }
    private static String getFontSizeFromDs(String ds) {
        // 1. 先找到 "font:" 的位置
        String fontPrefix = "font:";
        int fontIndex = ds.indexOf(fontPrefix);

        if (fontIndex == -1) {
            return "12"; // 如果没找到font:就返回-1
        }
        String regex = "font:.*?([\\d.]+)(?:pt)?;";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ds);

        if (matcher.find()) {
            String fontSizeString = matcher.group(1);
            // 提取数字部分，可以根据需要进行进一步处理
//            String[] parts = fontSizeString.split("\\s+");
//            if (parts.length > 1) {
                try {
                    Integer fontSize = (int)Float.parseFloat(fontSizeString);
//                    System.out.println("提取到的字体大小：" + fontSize);
                    return fontSize.toString();

                } catch (NumberFormatException e) {
                    System.out.println("字体大小解析错误：" + e.getMessage());
                    return "12";

                }
            }
//        else {
//                System.out.println("未找到字体大小");
//                return "12";
//
//            }
//        }
        else {
            System.out.println("未找到匹配的字体样式");
            return "12";

        }

    }

    private static String getFontSizeFromDa(String da) {
            String[] components = da.split("Tf");
            if(!da.contains(" Tf")) {
                return "12";
            }

            // 寻找 "rg" 的位置
            int tfIndex = da.indexOf(" Tf");
            if (tfIndex == -1) {
                return "12";
            }

            // 获取 "rg" 前面的内容
            String beforeRg = da.substring(0, tfIndex).trim();

            // 分割字符串获取最后三个数字
            String[] parts = beforeRg.split("\\s+");
            if (parts.length < 1) {
                return "12";
            }

            // 提取最后三个数字
            try {

                String fontSize = parts[parts.length - 1];
                return fontSize;
            } catch (NumberFormatException e) {
                return "12";
            }
        }
        private static String getRawColorStrFromDa(String da){
        if(da == null) {
            return "#ff0000";
        }
        String[] components = da.split("rg");
        if(!da.contains(" rg")) {
            return "#ff0000";
        }

            // 寻找 "rg" 的位置
            int rgIndex = da.indexOf(" rg");
            if (rgIndex == -1) {
                return "#ff0000";
            }

            // 获取 "rg" 前面的内容
            String beforeRg = da.substring(0, rgIndex).trim();

            // 分割字符串获取最后三个数字
            String[] parts = beforeRg.split("\\s+");
            if (parts.length < 3) {
                return "#ff0000";
            }

            // 提取最后三个数字
            try {
                float[] rgb = new float[3];
                rgb[0] = Float.parseFloat(parts[parts.length - 3])*255;
                rgb[1] = Float.parseFloat(parts[parts.length - 2])*255;
                rgb[2] = Float.parseFloat(parts[parts.length - 1])*255;
                            // 确保值在有效范围内
                int red = (int) Math.min(255, Math.max(0, rgb[0]));
                int green = (int) Math.min(255, Math.max(0, rgb[1]));
                int blue = (int) Math.min(255, Math.max(0, rgb[2]));
                // 转换为十六进制格式
            return String.format("#%02X%02X%02X", red, green, blue);
            } catch (NumberFormatException e) {
                return "#ff0000";
            }
        }


    private static String getRawColorStrFromDs(String da){
        if(da == null) {
            return "#ff0000";
        }
        String[] components = da.split("color:");
        if(components.length != 2) {
            return "#ff0000";
        }
        String rawColorStr = "#000000";
        if(!components[1].equalsIgnoreCase("#0")) rawColorStr = components[1];
        return rawColorStr;

    }
}

