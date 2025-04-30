package com.ose.tasks.util;

import com.ose.util.*;
import com.ose.tasks.entity.drawing.DrawingMarkup;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
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

//import static org.opencv.core.Core.NATIVE_LIBRARY_NAME;

public class PdfParseSysUtil {

    static final float INCH = 72;

    private static float height = 0;

    private static float width = 0;

    public static void main(String[] args) {
//        try {
//            remain("/var/www/01/temp/");
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//
//
//        URL url = ClassLoader.getSystemResource("lib/libopencv_core.4.0.1.dylib");
//        System.load("/var/www/saint-whale/backend/ose-tasks/src/main/resources/lib/libopencv_core.4.0.1.dylib");


//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        System.loadLibrary(NATIVE_LIBRARY_NAME);


        try {

            tidyAnn("/var/www/abcde.pdf");
            cloneAnn("/Users/CS/FPU/PROCEDURE/Topside_ISO/", "/Users/CS/FPU/PROCEDURE/Topside_ISO/cover.pdf");
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

    public static void treatDwgs(String dwgPath) throws IOException {
        String[] files = FileUtils.getFileNames(dwgPath);

        for (String file : files) {
            System.out.print(file);
            if(!file.endsWith(".pdf")) continue;
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
                        String annStr =  ann.getCOSObject().getNameAsString("T");
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
        sheet = wb.getSheet("pdf_page_infos");
        int i = 1;
        Row row = sheet.getRow(1);
        final Pattern SPOOL_PATTERN = Pattern.compile(
            "^(\\d[\\d|\\.|\\s|\"|/]+\\-[\\w|\\d]+\\-([C]+\\-){0,1}[\\w|\\d]+\\-HP\\d+(\\-[C|H|P|S]){0,1})(\\-[\\w|\\d]{2,10})((\\-X){0,1}\\-[\\w|\\d]+)$");

        while(WorkbookUtils.getCell(row, 1) != null) {
            String entityNo = WorkbookUtils.getCell(row, 2).getStringCellValue();
            Matcher matcher = SPOOL_PATTERN.matcher(entityNo);
            String spoolNo = "";
            String isoNo = "";
            String lineNo = "";
//            if(entityNo.equalsIgnoreCase("1\"-CO2-C-005-HP371-ENG")){
//                System.out.println("abc");
//            }
            if(matcher.find()){
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
        if(projectId == null) projectId = 0L;

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
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");
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
                    annStr = annStr.replaceAll("--","-").replaceAll("'","\"").
                        replaceAll("“","\"").replaceAll("”","\"").
                        replaceAll("1\\.1/2\"","1 1/2\"");
                    ((PDAnnotationMarkup) ann).setTitlePopup(annStr);
                    cbStr = cbStr.replaceAll("--","-").replaceAll("''","\"").
                        replaceAll("\"\"","\"").
                        replaceAll("'","\"").
                        replaceAll("“","\"").replaceAll("”","\"");
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
                        String entityNo = displayedEntityNo.replaceAll("1 1/2", "1\\.1/2").replaceAll("2 1/2","2\\.1/2");
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

    static private PDAnnotationLine createLine(PDRectangle position, PDColor newColor, PDBorderStyleDictionary bs){
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

    static private PDAnnotationMarkup cpPolyLine(PDAnnotationMarkup oldPLine, PDColor newColor, PDBorderStyleDictionary bs){
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

    static private PDAnnotationMarkup createText(String content, PDRectangle position, PDColor color, String daStr){
        PDAnnotationMarkup freeTextAnnotation = new PDAnnotationMarkup();
        freeTextAnnotation.getCOSObject().setName(COSName.SUBTYPE, PDAnnotationMarkup.IT_FREE_TEXT);

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
                                                      PDRectangle rect) throws IOException  {
        // Now add the markup annotation, a highlight to PDFBox text
        PDAnnotationTextMarkup txtMark = new PDAnnotationTextMarkup(
            PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
        PDColor blue = new PDColor(new float[] { 0, 0, 1 }, PDDeviceRGB.INSTANCE);

        txtMark.setColor(blue);

        // remove line below if PDF/A-2b (and possibly other PDF-A flavours)
        // also add txtMark.setPrinted(true)
        txtMark.setConstantOpacity((float) 0.2); // 20% transparent

        // Set the rectangle containing the markup
//        PDFont font = PDType1Font.HELVETICA_BOLD;

        float stringWidth = font.getStringWidth( content )*fontSize/1000f;
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
        quads[1] = position.getUpperRightY()-2; // y1
        quads[2] = position.getUpperRightX(); // x2
        quads[3] = quads[1]; // y2
        quads[4] = quads[0];  // x3
        quads[5] = position.getLowerLeftY()-2; // y3
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


        float stringWidth = font.getStringWidth( content )*fontSize/1000f;
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
        contentStream.setFont( font, fontSize );
        // set text color to red
        contentStream.setNonStrokingColor(Color.red);
        if (rotation == 90)
        {
            // rotate the text according to the page rotation
            contentStream.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, centerX, centerY));
        } else if (rotation == 270)
        {
            // rotate the text according to the page rotation
            contentStream.setTextMatrix(Matrix.getRotateInstance(3*Math.PI / 2, centerX, centerY));
        }
        else
        {
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

        for(int i =0; i< total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            for (PDAnnotation ann : page1.getAnnotations()) {
                if (ann instanceof PDAnnotationMarkup) {
                    String annStr = ann.getContents();
                    if(StringUtils.isEmpty(annStr)) {
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

                    float w = rux -ldx;
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

    public static String extractCp(String filename, List<Integer> pageIs, String outputDir, Set<String> cables, Set<String> cps) throws IOException {
        String extractedFile = PdfUtils.extractPages(filename, pageIs, outputDir);

        File file1 = new File(extractedFile);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        List<PDAnnotation> removedAns = new ArrayList<>();


        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();

            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");

                    annStr = annStr.replaceAll(" ","");

                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    cbStr = cbStr.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    for (String cb : cbs) {
                        if(!cables.contains(cb.replaceAll(" ","")) &&
                            !cps.contains(annStr)) {
                            removedAns.add(ann);
                        }
                        break;

                    }
                }
            }

            annotations.removeAll(removedAns);

            page1.setAnnotations(annotations);



        }
        doc.save(extractedFile);
        doc.close();

        return extractedFile;
    }

    public static String extractTp(String filename,
                                   List<Integer> pageIs,
                                   String outputDir,
                                   Set<String> spools,
                                   Set<String> isos,
                                   Set<String> lines,
                                   Set<String> tps) throws IOException {
        String extractedFile = PdfUtils.extractPages(filename, pageIs, outputDir);

        File file1 = null;
        try {
            file1 = new File(extractedFile);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        List<PDAnnotation> removedAns = new ArrayList<>();


        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();

            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");

                    annStr = StringUtils.trim(annStr);//.replaceAll(" ","");

                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    cbStr = StringUtils.trim(cbStr);//.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    for (String cb : cbs) {
                        cb = StringUtils.trim(cb);
//                        cb = cb.replaceAll("1\\.1/2\"","1 1/2\"");
                        cb = cb.replaceAll("1 1/2\"","1\\.1/2\"");
                        cb = cb.replaceAll("2 1/2\"","2\\.1/2\"");
                        if(!spools.contains(cb) &&
                            !isos.contains(cb) &&
                            !lines.contains(cb) &&
                            !tps.contains(annStr)) {
                            removedAns.add(ann);
                        }
                        break;

                    }
                }
            }

            annotations.removeAll(removedAns);

            page1.setAnnotations(annotations);



        }
        doc.save(extractedFile);
        doc.close();

        return extractedFile;
    }


    public static String extractPid(String filename,
                                   List<Integer> pageIs,
                                   String outputDir,
                                   Set<String> subSysNos) throws IOException {
        String extractedFile = PdfUtils.extractPages(filename, pageIs, outputDir);

        File file1 = null;
        try {
            file1 = new File(extractedFile);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        List<PDAnnotation> removedAns = new ArrayList<>();


        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();

            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");

                    annStr = StringUtils.trim(annStr);//.replaceAll(" ","");

                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    annStr = StringUtils.trim(annStr);//.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    if("".equals(annStr) || !subSysNos.contains(annStr)) {

                        removedAns.add(ann);
                    }

                }
            }

            annotations.removeAll(removedAns);

            page1.setAnnotations(annotations);



        }
        doc.save(extractedFile);
        doc.close();

        return extractedFile;
    }


    public static String extractPidByPkg(String filename,
                                        List<Integer> pageIs,
                                        String outputDir,
                                        Set<String> pkgNos) throws IOException {
        String extractedFile = PdfUtils.extractPages(filename, pageIs, outputDir);

        File file1 = null;
        try {
            file1 = new File(extractedFile);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        List<PDAnnotation> removedAns = new ArrayList<>();


        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();

            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");
//                    String annStr = ((PDAnnotationMarkup) ann).getSubject();


                    annStr = StringUtils.trim(annStr);//.replaceAll(" ","");

                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    annStr = StringUtils.trim(annStr);//.replaceAll(" ", "");
                    cbStr = StringUtils.trim(cbStr);
                    String[] cbs = cbStr.split("\\|\\|");
                    if(!pkgNos.contains(annStr) && !pkgNos.contains(cbStr)) {

                        removedAns.add(ann);
                    }

                }
            }

            annotations.removeAll(removedAns);

            page1.setAnnotations(annotations);



        }
        doc.save(extractedFile);
        doc.close();

        return extractedFile;
    }

    public static void remain(String dwgPath) throws IOException {
        String str = "1/2\"-WO-C-027-HP374||PDP4AB-4\"-FW-B-079-U361||1/2\"-WO-C-061-HP374||2\"-VS-C-026-HP380/10\"-VS-C-024-HP380||2\"-VS-C-028-HP380/10\"-VS-C-027-HP380||2\"-VS-C-032-HP380/10\"-VS-C-031-HP380||2\"-VS-C-036-HP380/10\"-VS-C-035-HP380||2\"-VS-C-040-HP380/10\"-VS-C-039-HP380||2\"-VS-C-047-HP380/4\"-VS-C-048-HP380||4\"-VS-C-023-HP380/4\"-VS-C-023-HP380||8\"-WS-C-037-HP375-L9-004||2\"-WS-005-HP375||1 1/2-WO-C-023-HP374||1 1/2\"-WB-C-020-HP374||6\"-WB-C-009-HP374||8\"-WB-C-012-HP374||8\"-WB-C-013-HP374||8\"-WB-C-036-HP376||8\"-WB-C-024-HP374||8\"-WB-C-025-HP374||8\"-WB-C-028-HP374||1/2\"DO-C-028-HP371||1/2\"DO-C-029-HP371||6\"-EE-C-702-HP379||12\"-EE-C-705-HP379||14\"-EE-C-705-HP379||2\"-FW-C-081-HP375||1\"-CO2-C-007-HP371||1/2\"-CO2-C-008-HP371||1\"-OP-C-085-HP371||1\"-OP-C-086-HP371||1\"-OP-C-087-HP371||1\"-OP-C-088-HP371||1\"-OP-C-089-HP371||1\"-OP-C-090-HP371||1\"-OP-C-091-HP371||1\"-OP-C-092-HP371||1\"-OP-C-093-HP371||3/4\"-OD-C-012-HP377||6\"-SP-C-043-HP376||6\"-SP-C-044-HP376||1 1/2\"-OD-C-258-HP375||2\"-ST-C-167-HP377||2\"-ST-C-250-HP377||1/2\"-PW-C-221-HP374||10\"-FW-B-063-U361||10\"-FW-B-064-U361||2\"-DF-B-030-HP301||2\"-FW-B-104-U361||2\"-FW-B-113-U341||2\"-FW-B-119-U361||2\"-FW-B-127-U361||2\"-FW-B-134-U361||2\"-IA-B-001-U361||2\"-PW-B-101-U361||2\"-UA-B-001-U361||2\"-UW-B-101-U361||3\"-FW-B-109-U361||3\"-FW-B-123-U361||3\"-OD-B-011-HP301||4\"-FW-B-107-U361||4\"-FW-B-122-U361||4\"-OD-B-065-HP301";
        String[] arr = str.split("\\|\\|");
        List<String> ar = Arrays.asList(arr);

        String[] files = FileUtils.getFileNames(dwgPath);

        for (String file : files) {
            System.out.print(file);
            if(!file.endsWith(".pdf")) continue;
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
                        if (StringUtils.isEmpty(cbStr)) {
                            continue;
                        }
                        cbStr = StringUtils.trim(cbStr);
                        ///////
                        annStr = annStr.replaceAll("--", "-").replaceAll("'", "\"").
                            replaceAll("“", "\"").replaceAll("”", "\"");
                        ((PDAnnotationMarkup) ann).setTitlePopup(annStr);
                        cbStr = cbStr.replaceAll("--", "-").replaceAll("''", "\"").
                            replaceAll("\"\"", "\"").
                            replaceAll("'", "\"").
                            replaceAll("“", "\"").replaceAll("”", "\"");
                        ((PDAnnotationMarkup) ann).setSubject(cbStr);
                        if (ar.contains(cbStr)) newAnns.add(ann);
                    }
                }
                page1.setAnnotations(newAnns);
            }
            doc.save(dwgPath + file);
            doc.close();
        }

    }

    public static String extractEp(String filename, List<Integer> pageIs, String outputDir, Set<String> entityNos, Set<String> eps) throws IOException {

        String extractedFile = PdfUtils.extractPages(filename, pageIs, outputDir);
        if(extractedFile == null) return null;
        File file1 = new File(extractedFile);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = doc.getNumberOfPages();
        List<PDAnnotation> removedAns = new ArrayList<>();


        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();

            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");

                    annStr = StringUtils.trim(annStr);//.replaceAll(" ","");

                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();
                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    cbStr = StringUtils.trim(cbStr);//.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    for (String cb : cbs) {
                        cb = StringUtils.trim(cb);
                        if(!entityNos.contains(cb) &&
                            !eps.contains(annStr)) {
                            removedAns.add(ann);
                        }
                        break;

                    }
                }
            }

            annotations.removeAll(removedAns);

            page1.setAnnotations(annotations);



        }
        doc.save(extractedFile);
        doc.close();

        return extractedFile;
    }

    public static void replaceTagName(String dwgPath) throws IOException {

        String[] files = FileUtils.getFileNames(dwgPath);

        for (String file : files) {
            System.out.print(file);
            if(!file.endsWith(".pdf")) continue;
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
                        if (StringUtils.isEmpty(annStr)) {
                            continue;
                        }
                        cbStr = StringUtils.trim(cbStr);
                        ///////
                        annStr = annStr.replaceAll("RPC-", "RCP-");
                        ((PDAnnotationMarkup) ann).setTitlePopup(annStr);
                        cbStr = cbStr.replaceAll("RPC-", "RCP-");
                        ((PDAnnotationMarkup) ann).setSubject(cbStr);
                        newAnns.add(ann);
                    }
                }
                page1.setAnnotations(newAnns);
            }
            doc.save(dwgPath + file+"1");
            doc.close();
        }

    }

    public static void mergerEqToCable(String dwgPath, String desDwgPath) throws IOException {

        String[] files = FileUtils.getFileNames(dwgPath);
        Map<String, Map<Integer, List<PDAnnotation>>> oldAnnotationMap = new HashMap<>();
        List<PDDocument> fromDocs = new ArrayList<>();
        for (String file : files) {
//            System.out.print(file);
            if(!file.endsWith(".pdf")) continue;
            File file1 = new File(dwgPath + file);
            PDDocument doc = null;
            try {
                doc = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc.getNumberOfPages();
            Map<Integer, List<PDAnnotation>> oAnns = new HashMap<>();
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc.getPage(i);
                List<PDAnnotation> annotations = page1.getAnnotations();

                oAnns.put(i, annotations);
            }
            oldAnnotationMap.put(file, oAnns);
            fromDocs.add(doc);
//            doc.close();
        }

        for (String file : files) {
            if(!file.endsWith(".pdf")) continue;
            File file1 = new File(desDwgPath + file);
            PDDocument doc1 = null;
            try {
                doc1 = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc1.getNumberOfPages();
            Map<Integer, List<PDAnnotation>> nAnnsMap = oldAnnotationMap.get(file);
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc1.getPage(i);
                List<PDAnnotation> annotations = page1.getAnnotations();
//                System.out.println(file);
                annotations.addAll(nAnnsMap.get(i));
                page1.setAnnotations(annotations);
            }
            doc1.save(dwgPath + file+1);
            doc1.close();
        }

        fromDocs.forEach(d -> {
            try {
                d.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public static void rotate(String dwgPath, String desDwgPath) throws IOException {

        String[] files = FileUtils.getFileNames(dwgPath);
        int sourceRotation = 0;
        Map<String, Map<Integer, List<PDAnnotation>>> oldAnnotationMap = new HashMap<>();
        Map<String, Map<Integer, Integer>> oldPageRotationMap = new HashMap<>();
        List<PDDocument> fromDocs = new ArrayList<>();
        for (String file : files) {
//            System.out.print(file);
            if(!file.endsWith(".pdf")) continue;
            File file1 = new File(dwgPath + file);
            PDDocument doc = null;
            try {
                doc = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc.getNumberOfPages();
            Map<Integer, List<PDAnnotation>> oAnns = new HashMap<>();
            Map<Integer, Integer> oPageRotateMap = new HashMap<>();
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc.getPage(i);
                width = page1.getMediaBox().getWidth();
                height = page1.getMediaBox().getHeight();
                List<PDAnnotation> annotations = page1.getAnnotations();
                List<PDAnnotation> newAnns = new ArrayList<>();

                oPageRotateMap.put(i, page1.getRotation());
                oAnns.put(i, annotations);

                PDFont font = PDType1Font.HELVETICA;

                float fontSize = 12.0f;
                PDRectangle rect = new PDRectangle();// ann.getRectangle();
                rect.setLowerLeftX(10);
                rect.setLowerLeftY(10);
                rect.setUpperRightY(10);
                rect.setUpperRightX(10);


                wrText(doc, page1, font, fontSize, "270", 100, height - 100);

            }
            oldAnnotationMap.put(file, oAnns);
            oldPageRotationMap.put(file, oPageRotateMap);
            fromDocs.add(doc);
//            doc.save(dwgPath+file + "1");
//            doc.close();
        }

        for (String file : files) {
            if(!file.endsWith(".pdf")) continue;
            File file1 = new File(desDwgPath + file);
            PDDocument doc1 = null;
            try {
                doc1 = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc1.getNumberOfPages();
            int curentPageRotation = 0;
            Map<Integer, List<PDAnnotation>> nAnnsMap = oldAnnotationMap.get(file);
            Map<Integer, Integer> oPageRotaMap = oldPageRotationMap.get(file);
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc1.getPage(i);
                curentPageRotation = page1.getRotation();
                int oRotation = oPageRotaMap.get(i);
                List<PDAnnotation> annotations = page1.getAnnotations();
                if(curentPageRotation == 0 && oRotation == 270) {
                    annotations.addAll(transformAnn(nAnnsMap.get(i), doc1, page1));

                } else {
                    annotations.addAll(nAnnsMap.get(i));
                }
                page1.setAnnotations(annotations);

            }
            doc1.save(desDwgPath + 1+file);
            doc1.close();
        }

        fromDocs.forEach(d -> {
            try {
                d.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public static String replaceFile(String oldFile, String newFile, String newFileName) throws IOException {
        //new File and old file is in Full Name

//        int sourceRotation = 0;
//        Map<Integer, List<PDAnnotation>> oldAnnotationMap = new HashMap<>();
//        Map<Integer, Integer> oldPageRotationMap = new HashMap<>();
        List<PDDocument> fromDocs = new ArrayList<>();
//            if(!oldFile.endsWith(".pdf")) return null;
            File file0 = new File(oldFile);
            PDDocument doc = null;
            try {
                doc = PDDocument.load(file0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = doc.getNumberOfPages();
            Map<Integer, List<PDAnnotation>> oAnns = new HashMap<>();
            Map<Integer, Integer> oPageRotateMap = new HashMap<>();
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc.getPage(i);
                width = page1.getMediaBox().getWidth();
                height = page1.getMediaBox().getHeight();
                List<PDAnnotation> annotations = page1.getAnnotations();
                List<PDAnnotation> newAnns = new ArrayList<>();

                oPageRotateMap.put(i, page1.getRotation());
                oAnns.put(i, annotations);

                PDFont font = PDType1Font.HELVETICA;

            }
            fromDocs.add(doc);

//            if(!newFile.endsWith(".pdf")) return null;
            File file1 = new File(newFile);
            PDDocument doc1 = null;
            try {
                doc1 = PDDocument.load(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            total = doc1.getNumberOfPages();
            int curentPageRotation = 0;
            for (int i = 0; i < total; i++) {
                PDPage page1 = doc1.getPage(i);
                curentPageRotation = page1.getRotation();
                int oRotation = oPageRotateMap.get(i);
                List<PDAnnotation> annotations = page1.getAnnotations();
                if((curentPageRotation == 0 && oRotation == 270) ||
                    (curentPageRotation == 270 && oRotation == 0) ||
                    (curentPageRotation == 90 && oRotation == 0)) {
                    annotations.addAll(transformAnn(oAnns.get(i), doc1, page1));

                } else {
                    annotations.addAll(oAnns.get(i));
                }
                page1.setAnnotations(annotations);

            }
            doc1.save(newFileName);
            doc1.close();

        fromDocs.forEach(d -> {
            try {
                d.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return newFileName;

    }

    public static Integer getPageCount(String s) {
        int total = 0;
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(s));
            total = doc.getNumberOfPages();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;


    }

    static private List<PDAnnotation> transformAnn(List<PDAnnotation> annotations, PDDocument doc1, PDPage page1) {
        if(annotations == null) return new ArrayList<>();
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
            if( ann instanceof PDAnnotationLine) {
                pdr.setUpperRightY((urx+llx)/2);
                pdr.setLowerLeftY((urx+llx)/2);
                float[] linepos = new float[4];
                linepos[0] = pdr.getLowerLeftX();  // x2 = lhs of square
                linepos[1] = pdr.getLowerLeftY(); // y2 halfway down square
                linepos[2] = pdr.getUpperRightX();  // x1 = rhs of circle
                linepos[3] = pdr.getUpperRightY(); // y1 halfway down circle

                PDAnnotation nl = cpLine((PDAnnotationLine)ann, ann.getColor(), ((PDAnnotationLine) ann).getBorderStyle());
                ((PDAnnotationLine)nl).setLine(linepos);
                ((PDAnnotationLine) nl).setSubject(((PDAnnotationLine) ann).getSubject());
                ((PDAnnotationLine) nl).setTitlePopup(((PDAnnotationLine) ann).getTitlePopup());
                anns.add(nl);

            }  else if(ann instanceof PDAnnotationSquareCircle) {
                PDAnnotationSquareCircle pdsc = (PDAnnotationSquareCircle)ann;
                PDRectangle rect = pdsc.getRectangle();
                if(rect == null) continue;
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
                ((PDAnnotationSquareCircle)pda).setSubject(((PDAnnotationSquareCircle) ann).getSubject());
                ((PDAnnotationSquareCircle)pda).setTitlePopup(((PDAnnotationSquareCircle) ann).getTitlePopup());
                anns.add(pda);
            }
            else if(ann instanceof PDAnnotationMarkup) {
                float[] vts = ((PDAnnotationMarkup) ann).getVertices();
                if(vts == null) continue;
                float[] nvts = new float[vts.length];

                PDFont font = PDType1Font.HELVETICA;
                float fontSize = 12.0f;



                for(int i = 0; i < vts.length -1; i = i+2) {

                    nvts[i] = height - vts[i+1];
                    nvts[i+1] = vts[i];
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
                if(rect == null) continue;
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
                PDAnnotation npl = cpPolyLine((PDAnnotationMarkup)ann, ann.getColor(), ((PDAnnotationMarkup) ann).getBorderStyle());
                ((PDAnnotationMarkup) npl).setVertices(nvts);
                npl.setRectangle(nrect);
//                ((PDAnnotationMarkup) npl).setDefaultAppearance(((PDAnnotationMarkup) ann).getDefaultAppearance());
                ((PDAnnotationMarkup) npl).setTitlePopup(((PDAnnotationMarkup) ann).getTitlePopup());
                ((PDAnnotationMarkup) npl).setSubject(((PDAnnotationMarkup) ann).getSubject());
                npl.getCOSObject().setName("Rotate","270");
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
            }
            else if( ann instanceof PDAnnotationPopup ) {
                PDRectangle rect = ann.getRectangle();

                if(rect == null) continue;
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
                if(pdad == null) continue;
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

    static private PDAppearanceDictionary getApps(PDDocument doc, PDRectangle rect){
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
            if(!file.endsWith(".pdf")) continue;
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

                        for(Map.Entry<String, String> entry : replaceMap.entrySet()) {
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

    public static String markupSubSysPdf(String filePath, Map<String, String> subSystemMap,
                                         Map<String, String> tagSubSysMap,
                                         Map<String, String> pkgSubSysMap,
                                         String temporaryDir) throws IOException {
        // Init Colors
        PDColorSpace cs = PDDeviceRGB.INSTANCE;
        float[] cl1 = new float[]{0.0f,1.0f,0.0f};
        PDColor c1 = new PDColor(cl1, cs);
        List<PDColor> pdcolors = new ArrayList<>();
        pdcolors.add(c1);
        float[] cl2 = new float[]{1.0f,0.0f,0.0f};
        PDColor c2 = new PDColor(cl2, cs);
        pdcolors.add(c2);

        float[] cl3 = new float[]{0.0f,0.0f,1.0f};
        PDColor c3 = new PDColor(cl3, cs);
        pdcolors.add(c3);

        float[] cl4 = new float[]{1.0f,1.0f,0.0f};
        PDColor c4 = new PDColor(cl4, cs);
        pdcolors.add(c4);

        float[] cl5 = new float[]{1.0f,0.0f,1.0f};
        PDColor c5 = new PDColor(cl5, cs);
        pdcolors.add(c5);

        float[] cl6 = new float[]{0.0f,1.0f,1.0f};
        PDColor c6 = new PDColor(cl6, cs);
        pdcolors.add(c6);

        float[] cl7 = new float[]{0.0f,0.5f,0.5f};
        PDColor c7 = new PDColor(cl7, cs);
        pdcolors.add(c7);

        float[] cl8 = new float[]{0.5f,0.5f,0.0f};
        PDColor c8 = new PDColor(cl8, cs);
        pdcolors.add(c8);

        float[] cl9 = new float[]{0.5f,0.0f,0.5f};
        PDColor c9 = new PDColor(cl9, cs);
        pdcolors.add(c9);

        float[] cl10 = new float[]{0.5f,0.0f,0.0f};
        PDColor c10 = new PDColor(cl10, cs);
        pdcolors.add(c10);

        float[] cl11 = new float[]{0.0f,0.5f,0.0f};
        PDColor c11 = new PDColor(cl11, cs);
        pdcolors.add(c11);

        float[] cl12 = new float[]{0.0f,0.0f,0.5f};
        PDColor c12 = new PDColor(cl12, cs);
        pdcolors.add(c12);


        float[] cl13 = new float[]{0.75f,0.0f,0.0f};
        PDColor c13 = new PDColor(cl13, cs);
        pdcolors.add(c13);


        float[] cl14 = new float[]{0.0f,0.75f,0.0f};
        PDColor c14 = new PDColor(cl14, cs);
        pdcolors.add(c14);

        float[] cl15 = new float[]{0.0f,0.0f,0.75f};
        PDColor c15 = new PDColor(cl15, cs);
        pdcolors.add(c15);

        PDAnnotationLine pdl = new PDAnnotationLine();
        PDBorderStyleDictionary borderThick = new PDBorderStyleDictionary();

        borderThick.setWidth(INCH / 18); // 4 point
        pdl.setBorderStyle(borderThick);

//        PDAnnotationTextMarkup pdt = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_FREETEXT);

        final float INCH = 72;

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

            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            int rt = page1.getRotation();

            float width = page1.getMediaBox().getWidth();
            float height = page1.getMediaBox().getHeight();
            if(rt == 90 || rt == 270) {
                float tmp = width;
                width = height;

                height = tmp;
            }
            //font size; left top;
            List<PDAnnotation> addedAnn = new ArrayList<>();
            int cnt = 0;
            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");
                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();

                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    annStr = annStr.replaceAll(" ","");
                    if(StringUtils.isEmpty(annStr)) annStr = "";
                    String[] anns = annStr.split("\\|\\|");
                    annStr = anns[0];

                    if(StringUtils.isEmpty(cbStr)) cbStr = "";
                    cbStr = cbStr.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    cbStr = cbs[0];
                    String subSystem = "";
                    if(!StringUtils.isEmpty(annStr)) {
                        subSystem = tagSubSysMap.get(annStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(annStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(annStr)) {
                                subSystem = annStr;
                            }
                        }
                    }

                    if(StringUtils.isEmpty(subSystem) && !StringUtils.isEmpty(cbStr)) {
                        subSystem = tagSubSysMap.get(cbStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(cbStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(cbStr)) {
                                subSystem = cbStr;
                            }
                        }

                    }

                    if(!StringUtils.isEmpty(subSystem) && ss.contains(subSystem)) {
                        PDColor pdc = subSysColor.get(subSystem);
                        ann.setColor(pdc);

//                        removedAns.add(ann);
//                        if( ann instanceof  PDAnnotationLine){
//                            ann.setColor(pdc);
//                        }
//                            addedAnn.add(cpLine((PDAnnotationLine) ann, pdc, pdl.getBorderStyle()));
//                        if(ann instanceof PDAnnotationMarkup && ann.getSubtype().equalsIgnoreCase("PolyLine")) {
//                            addedAnn.add(cpPolyLine((PDAnnotationMarkup) ann, pdc, pdl.getBorderStyle()));
//                            ann.setColor(pdc);
//                        }


                    } else if(!StringUtils.isEmpty(subSystem) && !ss.contains(annStr)) {//未处理过这个子系统
                        PDColor pdc = pdcolors.get(cnt);
                        subSysColor.put(subSystem, pdc);
                        ss.add(subSystem);
                        ann.setColor(pdc);

                        PDRectangle rect = ann.getRectangle();
                        float[] l0 = null;
                        if(rt == 0) {
                            l0 = new float[]{20.0f, 255 - cnt * 20.0f, 100.0f, 255 - cnt * 20.0f};
                            rect.setLowerLeftX(20.0f);
                            rect.setLowerLeftY(250 - cnt * 20.0f);
                            rect.setUpperRightX(100.0f);
                            rect.setUpperRightY(250 - cnt * 20.0f);
                        } else if(rt == 90) {
                            l0 = new float[]{255 - cnt * 20.0f,20.0f, 255 - cnt * 20.0f,  100.0f};
                            rect.setUpperRightX(255 - cnt * 20.0f);
                            rect.setUpperRightY(100.0f);
                            rect.setLowerLeftX(255 - cnt * 20.0f);
                            rect.setLowerLeftY(20.0f);

                        } else if(rt == 270) {
                            l0 = new float[]{255 - cnt * 20.0f,width - 20.0f, 255 - cnt * 20.0f,  width - 100.0f};

                            rect.setLowerLeftX(255 - cnt * 20.0f);
                            rect.setLowerLeftY(width - 100.0f);

                            rect.setUpperRightX(255 - cnt * 20.0f);
                            rect.setUpperRightY(width - 20.0f);
                        }
//                        addedAnn.add(createLine(rect, pdc, pdl.getBorderStyle()));
//                        if( ann instanceof  PDAnnotationLine)
//                            addedAnn.add(cpLine((PDAnnotationLine) ann, pdc, pdl.getBorderStyle()));
//                        if(ann instanceof PDAnnotationMarkup && ann.getSubtype().equalsIgnoreCase("PolyLine")) {
//                            ann.setColor(pdc);
//                            ann.setBorder
//                            addedAnn.add(cpPolyLine((PDAnnotationMarkup) ann, pdc, pdl.getBorderStyle()));

//                        }
                        PDRectangle rect1 = new PDRectangle();
                        if(rt == 0) {
                            rect1.setLowerLeftX(120);
                            rect1.setLowerLeftY(260 - cnt * 20);
                            rect1.setUpperRightX(400);
                            rect1.setUpperRightY(240 - cnt * 20);

                        } else if(rt == 90) {
                            rect1.setLowerLeftX(120);
                            rect1.setLowerLeftY(170 - cnt * 20);

                            rect1.setUpperRightX(400);
                            rect1.setUpperRightY(200 - cnt * 20);

                        } else if(rt == 270) {
                            rect1.setLowerLeftX(245 - cnt * 20);
                            rect1.setLowerLeftY(width - 320);
                            rect1.setUpperRightX(265 - cnt * 20);
                            rect1.setUpperRightY(width - 120);

                        }


                        PDColor red = new PDColor(new float[] { 1, 0, 0 }, PDDeviceRGB.INSTANCE);

                        float fontSize = 10.0f;
                        PDRectangle pageSize = page1.getMediaBox();
                        PDFont font = PDType1Font.HELVETICA;
//                        PDAnnotation fs = createTextM(font, fontSize, annStr + subSystemMap.get(subSystem), rect1);
                        String daStr = pdc.getComponents()[0] + " " +pdc.getComponents()[1] + " " + pdc.getComponents()[2] + " rg /Helv 16 Tf";
                        PDAnnotation fs = createText(annStr + subSystemMap.get(subSystem), rect1, red, daStr);
                        fs.constructAppearances();
                        addedAnn.add(fs);

//                        wrText(doc, page1, font, fontSize, annStr + subSystemMap.get(subSystem), 250 - cnt*20, rect1.getUpperRightY());
                        cnt++;


                    }

                    ann.constructAppearances();
                }
            }

//            annotations.removeAll(removedAns);

//            annotations.addAll(addedAnn);
            annotations.addAll(addedAnn);


            /////////
            page1.setAnnotations(annotations);

        }
        String filename2 = temporaryDir + CryptoUtils.shortUniqueId() +".pdf";
        doc.save(filename2);
        doc.close();


        return filename2;
    }

    public static String extractSubSys(String filename,
                                       List<Integer> pageIs,
                                       String temporaryDir,
                                       Map<String,String> tagSubSysMap,
                                       Map<String,String> pkgSubSysMap,
                                       Map<String,String> subSystemMap) throws IOException {

        String extractedFile = PdfUtils.extractPages(filename, pageIs, temporaryDir);

        File file1 = new File(extractedFile);
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<PDAnnotation> removedAns = new ArrayList<>();
        int total = doc.getNumberOfPages();

        for (int i = 0; i < total; i++) {
            PDPage page1 = doc.getPage(i);
            List<PDAnnotation> annotations = page1.getAnnotations();
            for (PDAnnotation ann : annotations) {
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {
                    String annStr =  ann.getCOSObject().getNameAsString("T");
                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();

                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    annStr = annStr.replaceAll(" ","");
                    if(StringUtils.isEmpty(annStr)) annStr = "";
                    String[] anns = annStr.split("\\|\\|");
                    annStr = anns[0];

                    if(StringUtils.isEmpty(cbStr)) cbStr = "";
                    cbStr = cbStr.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    cbStr = cbs[0];
                    String subSystem = "";
                    if(!StringUtils.isEmpty(annStr)) {
                        subSystem = tagSubSysMap.get(annStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(annStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(annStr)) {
                                subSystem = annStr;
                            }
                        }
                    }

                    if(StringUtils.isEmpty(subSystem) && !StringUtils.isEmpty(cbStr)) {
                        subSystem = tagSubSysMap.get(cbStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(cbStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(cbStr)) {
                                subSystem = cbStr;
                            }
                        }

                    }

                    if(StringUtils.isEmpty(subSystem)) {

                        removedAns.add(ann);

                    }

                }
            }

            annotations.removeAll(removedAns);

            page1.setAnnotations(annotations);

        }
        String filename2 = temporaryDir + CryptoUtils.shortUniqueId() +".pdf";
        doc.save(filename2);
        doc.close();


        return filename2;
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
        if(!filePath.endsWith(".pdf")) return;
        if(filePath.endsWith("cover.pdf")) return;
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
        if(!annFile.endsWith(".pdf")) return;
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
            if(rt == 90 || rt == 270) {
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
                if(ann instanceof PDAnnotationLine || ann instanceof PDAnnotationMarkup) {

//                    removedAnn.add(ann);
                    String annStr =  ann.getCOSObject().getNameAsString("T");
                    String cbStr = ((PDAnnotationMarkup) ann).getSubject();

                    if (StringUtils.isEmpty(annStr) && StringUtils.isEmpty(cbStr)) {
                        continue;
                    }

                    annStr = annStr.replaceAll(" ","");
                    if(StringUtils.isEmpty(annStr)) annStr = "";
                    String[] anns = annStr.split("\\|\\|");
                    annStr = anns[0];

                    if(StringUtils.isEmpty(cbStr)) cbStr = "";
                    cbStr = cbStr.replaceAll(" ", "");
                    String[] cbs = cbStr.split("\\|\\|");
                    cbStr = cbs[0];
                    String subSystem = "";
                    if(!StringUtils.isEmpty(annStr)) {
                        subSystem = tagSubSysMap.get(annStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(annStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(annStr)) {
                                subSystem = annStr;
                            }
                        }
                    }

                    if(StringUtils.isEmpty(subSystem) && !StringUtils.isEmpty(cbStr)) {
                        subSystem = tagSubSysMap.get(cbStr);
                        if (StringUtils.isEmpty(subSystem)) {
                            subSystem = pkgSubSysMap.get(cbStr);
                            if (StringUtils.isEmpty(subSystem) && subSystemMap.keySet().contains(cbStr)) {
                                subSystem = cbStr;
                            }
                        }

                    }
                    if(cbStr.contains("2\"-CD-B-055-HP316")){
                        System.out.println("abcd");
                    }

                    if(!StringUtils.isEmpty(subSystem)) {

                        PDColor pdc = getPdColor(subSystemColorMap.get(subSystem));
                        PDBorderStyleDictionary bd = new PDBorderStyleDictionary();
                        if(ann instanceof PDAnnotationLine){
                            PDAnnotation nl = cpLine((PDAnnotationLine)ann,pdc,((PDAnnotationLine) ann).getBorderStyle());
                            bd = ((PDAnnotationLine) ann).getBorderStyle();
//                        ann.setColor(pdc)
                            addedAnn.add(nl);
                            isAdded = true;
                        } else if(ann instanceof  PDAnnotationMarkup) {
                            PDAnnotation npl = cpPolyLine((PDAnnotationMarkup) ann,pdc,((PDAnnotationMarkup) ann).getBorderStyle());
                            addedAnn.add(npl);
                            isAdded = true;
                            bd = ((PDAnnotationMarkup) ann).getBorderStyle();
                        }
//                        ann.constructAppearances();
                        if(!ss.contains(subSystem)){
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
                if(!isAdded) {
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
        String filename2 = temporaryDir + CryptoUtils.shortUniqueId() +".pdf";
        doc.save(filename2);
        doc.close();


        return filename2;
    }

    private static PDColor getPdColor(String colorStr){
        colorStr = colorStr.replaceAll("#","").replaceAll(" ","");
        float redf = Long.valueOf(colorStr.substring(0, 2),16)*1.0f/255.0f;
        float yellowf = Long.valueOf(colorStr.substring(2,4),16)*1.0f/255.0f;
        float bluef = Long.valueOf(colorStr.substring(4,6),16)*1.0f/255.0f;
        float[] c = new float[]{redf, yellowf, bluef};
        PDColorSpace cs = PDDeviceRGB.INSTANCE;
        return new PDColor(c,cs);
//        float[] cl13 = new float[]{0.75f,0.0f,0.0f};
//        PDColor c13 = new PDColor(cl13, cs);
//        pdcolors.add(c13);
    }

    //////////////
    private static Set<PDAnnotation> setNotation(int cnt, int rt, PDColor pdColor, String str, PDBorderStyleDictionary bd) {
        Set<PDAnnotation> ans = new HashSet<>();
        PDRectangle rect1 = new PDRectangle();
        if(rt == 0) {
            rect1.setLowerLeftX(20);
            rect1.setLowerLeftY(260 - cnt * 20);
            rect1.setUpperRightX(300);
            rect1.setUpperRightY(240 - cnt * 20);

        } else if(rt == 90) {
            rect1.setLowerLeftX(20);
            rect1.setLowerLeftY(170 - cnt * 20);

            rect1.setUpperRightX(300);
            rect1.setUpperRightY(200 - cnt * 20);

        } else if(rt == 270) {
            rect1.setLowerLeftX(245 - cnt * 20);
            rect1.setLowerLeftY(width - 220);
            rect1.setUpperRightX(265 - cnt * 20);
            rect1.setUpperRightY(width - 20);

        }


//        PDColor red = new PDColor(new float[] { 1, 0, 0 }, PDDeviceRGB.INSTANCE);

        float fontSize = 10.0f;
//        PDRectangle pageSize = page1.getMediaBox();
        PDFont font = PDType1Font.HELVETICA;
        String daStr = pdColor.getComponents()[0] + " " +pdColor.getComponents()[1] + " " + pdColor.getComponents()[2] + " rg /Helv 16 Tf";
        PDAnnotation fs = createText(str, rect1, pdColor, daStr);
        fs.constructAppearances();
        ans.add(fs);

        PDRectangle rect = new PDRectangle();
//        PDBorderStyleDictionary bd = new PDBorderStyleDictionary();
        float[] l0 = null;
        if(rt == 0) {
            l0 = new float[]{20.0f, 255 - cnt * 20.0f, 100.0f, 255 - cnt * 20.0f};
            rect.setLowerLeftX(20.0f + str.length() * 15);
            rect.setLowerLeftY(250 - cnt * 20.0f);
            rect.setUpperRightX(100.0f + str.length() * 15);
            rect.setUpperRightY(250 - cnt * 20.0f);
        } else if(rt == 90) {
            l0 = new float[]{255 - cnt * 20.0f,20.0f, 255 - cnt * 20.0f,  100.0f};
            rect.setUpperRightX(255 - cnt * 20.0f);
            rect.setUpperRightY(100.0f + str.length() * 15);
            rect.setLowerLeftX(255 - cnt * 20.0f);
            rect.setLowerLeftY(20.0f + str.length() * 15);

        } else if(rt == 270) {
            l0 = new float[]{255 - cnt * 20.0f,width - 20.0f, 255 - cnt * 20.0f,  width - 100.0f};

            rect.setLowerLeftX(255 - cnt * 20.0f);
            rect.setLowerLeftY(width - 100.0f - str.length() * 15);

            rect.setUpperRightX(255 - cnt * 20.0f);
            rect.setUpperRightY(width - 20.0f - str.length() * 15);
        }
        ans.add(createLine(rect, pdColor, bd));

        return ans;
    }

}
