package com.ose.util;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfAnnotationCollection;
import com.spire.pdf.annotations.PdfRubberStampAnnotationWidget;
import com.spire.pdf.attachments.PdfAttachmentCollection;
import com.spire.pdf.exporting.PdfImageInfo;
import com.spire.pdf.widget.PdfPageCollection;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfUtils {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(PdfUtils.class);

    private static final Pattern PATTERN = Pattern.compile(
        "(^\\d.*\\-.*)(sht(\\d{1,2})of(\\d{1,2}))",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PATTERN_FILE_NAME = Pattern.compile(
        "(^\\w.*)_Rev\\.(\\w\\d{0,1})",
        Pattern.CASE_INSENSITIVE
    );

    @Value("${application.files.temporary}")
    private static String temporaryDir;

    public static String pdfEncoder(String pdfPath, int pageNo, String outputDir) {

        /*
         * Read the pdf passed as an input to the method
         */

        List<Integer> pages = new ArrayList<Integer>() {{
            add(pageNo);
        }};
       String outputStr = PdfUtils.extractPages(pdfPath, pages, outputDir);

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(outputStr);

            /*
             * Get the byte array from the pdf stream
             */
            int bufLength = 2048;
            byte[] buffer = new byte[2048];
            byte[] data;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int readLength;
            while ((readLength = stream.read(buffer, 0, bufLength)) != -1) {
                out.write(buffer, 0, readLength);
            }

            data = out.toByteArray();

            /*
             * Using the java.util.Base64 getEncoder method to get the Base64 String
             */
            String base64String = Base64.getEncoder().withoutPadding().encodeToString(data);

            out.close();
            stream.close();
            return base64String;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
                File tempFile = new File(outputStr);
                tempFile.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*
         * Printing the Base64 String to console
         */

//        System.out.println("Encode PDF Result : " + base64String);
    }


    public static String pdfEncoderForNew(String pdfPath, int fromPageIndex, int endPageIndex, String outputDir) {

        /*
         * Read the pdf passed as an input to the method
         */
        List<Integer> pages = new ArrayList<Integer>();
        for (int i = fromPageIndex; i <= endPageIndex; i++) {
            pages.add(i);
        }

        String outputStr = PdfUtils.extractPages(pdfPath, pages, outputDir);

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(outputStr);



            /*
             * Get the byte array from the pdf stream
             */
            int bufLength = 2048;
            byte[] buffer = new byte[2048];
            byte[] data;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int readLength;
            while ((readLength = stream.read(buffer, 0, bufLength)) != -1) {
                out.write(buffer, 0, readLength);
            }

            data = out.toByteArray();

            /*
             * Using the java.util.Base64 getEncoder method to get the Base64 String
             */
            String base64String = Base64.getEncoder().withoutPadding().encodeToString(data);

            out.close();
            stream.close();
            return base64String;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*
         * Printing the Base64 String to console
         */

//        System.out.println("Encode PDF Result : " + base64String);
    }


    /**
     * 取得pdf文件页数. REPLACED
     *
     * @param pdfFilePath p1
     */

    public static int getPdfPageCount(String pdfFilePath) {
        int count = 0;
        try {
            File pdfFile = new File(pdfFilePath);
            if (pdfFile == null) {
                throw new Exception("文件为空,请检查!");
            }
//            if (!(pdfFile.getName().endsWith(".pdf") || pdfFile.getName().endsWith(".PDF"))) {
//                throw new Exception("文件非pdf格式,请检查!");
//            }
            PDDocument pdd = PDDocument.load(pdfFile);
//            PDPageTree pages = pdd.getDocumentCatalog().getPages();
            count = pdd.getNumberOfPages();
            pdd.close();

            return count;
        } catch (Exception e) {
            logger.error("获取Pdf文件页数异常(默认返回1页)，原因：", e);
            return count;
        }

    }


    /**
     * 合并pdf 文件. REPLACED
     *
     * @param files      p1
     * @param targetPath p2
     */
    public static void mergePdfFiles(String[] files, String targetPath) {
        // pdf合并工具类
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        List<PDDocument> documentList = new ArrayList<>();
        try {

            for (int i = 0; i < files.length; i++) {
                if (files[i] == null) continue;
                File file = new File(files[i]);
                if (file.exists() && file.isFile()) {
                    // 循环添加要合并的pdf
                    PDDocument document = PDDocument.load(file);
                    documentList.add(document);
                    mergePdf.addSource(file);
                    document.close();


                }
            }
            // 设置合并生成pdf文件名称
            mergePdf.setDestinationFileName(targetPath);
            // 合并pdf
            mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

//            for (PDDocument document : documentList) {
//                document.close();
//            }
//            return new File(targetPath);

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
//        return null;
    }

    public static String extractPages(String filePath, List<Integer> pageIs, String outPath) {
        // 将windows 分割符替换成linux
        String staticpath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        if (outPath == null) {
            outPath = staticpath;
        }
        filePath = filePath.replaceAll("\\\\", "/");
        outPath = outPath.replaceAll("\\\\", "/");
        PDDocument document = null;
        String fileOutPath = null;
        try {
            File indexFile = new File(filePath);// 这是对应文件名
            document = PDDocument.load(indexFile);

            int n = document.getNumberOfPages();

            if (!outPath.endsWith("/")) {
                outPath = outPath + "/";
            }
            String fileName = "";
            if (!filePath.endsWith(".pdf")) {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            } else {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length() - 4);
            }
            String shortUniqueId = CryptoUtils.shortUniqueId();

            Splitter splitter = new Splitter();
            splitter.setStartPage(1);
            splitter.setEndPage(n);
            File file = new File(outPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            List<PDDocument> pages = splitter.split(document);
//            ListIterator<PDDocument> iterator = pages.listIterator();
            PDDocument ePd = new PDDocument();
            int i = 0;
            for (PDDocument pd : pages) {
                if (!pageIs.contains(i++)) continue;
//                pd.getPage(0).setAnnotations(new ArrayList<>());
                pd.getPage(0).getActions();
                ePd.addPage(pd.getPage(0));

//                pd.close();

            }
            fileOutPath = outPath + fileName + "_" + shortUniqueId + ".pdf";
            File newFile = new File(fileOutPath);
            if (newFile.exists()) {
                newFile.delete();
            }

            ePd.save(fileOutPath);
            ePd.close();

            for (PDDocument pd : pages) {
                pd.close();

            }

        } catch (Exception e) {
            logger.error(e.toString());

        } finally {
            try {

                if (document != null) document.close();
            } catch (Exception ee) {
                logger.error(ee.toString());
            }
        }


        return fileOutPath;
    }

    /**
     * 将pdf 分割为每一页一个pdf. REPLACED
     *
     * @param filePath
     * @param outPath  输出文件名路径
     */
    public static List<String> partitionPdfFileToPerPage(String filePath, String outPath) {
        // 将windows 分割符替换成linux
        String staticpath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        if (outPath == null) {
            outPath = staticpath;
        }
        filePath = filePath.replaceAll("\\\\", "/");
        outPath = outPath.replaceAll("\\\\", "/");
        PDDocument document = null;
        List<String> outPaths = new ArrayList<>();
        try {
            File indexFile = new File(filePath);// 这是对应文件名
            document = PDDocument.load(indexFile);

            int n = document.getNumberOfPages();

            if (!outPath.endsWith("/")) {
                outPath = outPath + "/";
            }
            String fileName = "";
            if (!filePath.endsWith(".pdf")) {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            } else {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length() - 4);
            }
            String shortUniqueId = CryptoUtils.shortUniqueId();

            Splitter splitter = new Splitter();
            splitter.setStartPage(1);
            splitter.setEndPage(n);
            File file = new File(outPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            List<PDDocument> pages = splitter.split(document);
            ListIterator<PDDocument> iterator = pages.listIterator();
            int i = 1;
            while (iterator.hasNext()) {
                String fileOutPath = outPath + fileName + "_" + shortUniqueId + "_" + i++ + ".pdf";

                PDDocument pd = iterator.next();
                File newFile = new File(fileOutPath);
                if (newFile.exists()) {
                    newFile.delete();
                }
                pd.save(fileOutPath);
                pd.close();
                outPaths.add(fileOutPath);
            }
        } catch (Exception e) {
            logger.error(e.toString());

        } finally {
            try {
                if (document != null) document.close();
            } catch (Exception ee) {
                logger.error(ee.toString());
            }
        }
        return outPaths;
    }

    /**
     * 给pdf 指定的位置追加图片 REPLACED
     *
     * @param output      要输出的pdf
     * @param input       输入的pdf
     * @param imagePath   输入的image
     * @param positionX   pdf 输出位置
     * @param positionY   pdf 输出位置
     * @param scaleToFitX image 大小，100 表示不缩放
     * @param scaleToFitY image 大小，100 表示不缩放
     * @throws IOException
     */
    public static void setImageToPdf(String output, String input, String imagePath, int positionX,
                                     int positionY, int scaleToFitX, int scaleToFitY) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            //Retrieving the page
            PDPage page = doc.getPage(i);
            //Creating PDImageXObject object
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            float width = scaleToFitX * 1f;
            float height = scaleToFitY * 1f;
            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //Drawing the image in the PDF document
            contents.drawImage(pdImage, positionX, positionY, width, height);
            System.out.println("Image inserted");
            //Closing the PDPageContentStream object
            contents.close();
        }

        //Saving the document
        doc.save(output);
        doc.close();

    }

    /**
     * 给pdf 指定的位置指定页追加图片 REPLACED
     *
     * @param output      要输出的pdf
     * @param input       输入的pdf
     * @param imagePath   输入的image
     * @param positionX   pdf 输出位置
     * @param positionY   pdf 输出位置
     * @param scaleToFitX image 大小，100 表示不缩放
     * @param scaleToFitY image 大小，100 表示不缩放
     * @throws IOException
     */
    public static void setImageToPdfByPage(
        String output,
        String input,
        String imagePath,
        int positionX,
        int positionY,
        int scaleToFitX,
        int scaleToFitY,
        int pageNo
    ) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        //Retrieving the page
        PDPage page = doc.getPage(pageNo);
        //Creating PDImageXObject object
        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
        float width = scaleToFitX * 1f;
        float height = scaleToFitY * 1f;
        //creating the PDPageContentStream object
        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        //Drawing the image in the PDF document
        contents.drawImage(pdImage, positionX, positionY, width, height);
        System.out.println("Image inserted");
        //Closing the PDPageContentStream object
        contents.close();

        //Saving the document
        doc.save(output);
        doc.close();

    }

    /**
     * 给pdf 指定的位置追加图片 REPLACED
     *
     * @param output      要输出的pdf
     * @param input       输入的pdf
     * @param imagePath   输入的image
     * @param positionX   pdf 输出位置
     * @param positionY   pdf 输出位置
     * @param scaleToFitX image 大小，100 表示不缩放
     * @param scaleToFitY image 大小，100 表示不缩放
     * @throws IOException
     */
    public static void setImageToCoverPdf(String output, String input, String imagePath, int positionX,
                                          int positionY, int scaleToFitX, int scaleToFitY) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            //Retrieving the page
            PDPage page = doc.getPage(i);
            //Creating PDImageXObject object
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            float width = scaleToFitX * 1f;
            float height = scaleToFitY * 1f;
            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //Drawing the image in the PDF document
            if (i == 0) {
                contents.drawImage(pdImage, positionX, positionY, width, height);
            }
            System.out.println("Image inserted");
            //Closing the PDPageContentStream object
            contents.close();
        }

        //Saving the document
        doc.save(output);
        doc.close();

    }

    /**
     * 设置图片同时设置一个英文文字 REPLACED
     *
     * @param output     p1
     * @param input      p2
     * @param imagePath  p3
     * @param positionX  p4
     * @param positionY  p5
     * @param scaleToFit 缩放比例    100表示不缩放
     * @param fontString p7
     * @param llx        p8
     * @param lly        p9
     * @param urx        p10
     * @param ury        p11
     * @throws IOException
     */
    public static void setImageAndFontToPdf(String output, String input, String imagePath, int positionX,
                                            int positionY, int scaleToFit, String fontString, int llx, int lly, int urx, int ury) throws IOException {

        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            //Retrieving the page
            PDPage page = doc.getPage(i);
            //Creating PDImageXObject object
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
//            float width = scaleToFit * 1f;
//            float height = (scaleToFit * 1f) / 3;
            float width = scaleToFit * 1f;
            float height = scaleToFit * 1f;
            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //Drawing the image in the PDF document
            contents.drawImage(pdImage, positionX, positionY, width, height);
            logger.info("Image inserted");

            //Begin the Content stream
            contents.beginText();
            //Setting the font to the Content stream
            contents.setFont(PDType1Font.HELVETICA, 11);
            //Setting the position for the line
            contents.newLineAtOffset(llx, lly);
//            String text = "This is the sample document and we are adding content to it.";
            //Adding text in the form of string
            contents.showText(fontString);
            //Ending the content stream
            contents.endText();
            logger.info("Content added");

            //Closing the PDPageContentStream object
            contents.close();
        }

        //Saving the document
        doc.save(output);
        doc.close();
    }


    public static Float getPdfHeight(String subFilePath) throws IOException {
        File file = new File(subFilePath);
        PDDocument doc = PDDocument.load(file);
        Float height = doc.getPage(0).getMediaBox().getHeight();
        doc.close();
        return height;

    }


    public static Float getPdfWidth(String subFilePath) throws IOException {
        File file = new File(subFilePath);
        PDDocument doc = PDDocument.load(file);
        Float width = doc.getPage(0).getMediaBox().getWidth();
        doc.close();
        return width;
    }

    /**
     * 生成白色图片覆盖原图片并生成新二维码图片，生成白色图片覆盖字体并生成新字体，
     *
     * @param output         输出文件路径
     * @param input          输入文件路径
     * @param blankImagePath 二维码空白图片
     * @param newImagePath   二维码新图片
     * @param positionX      二维码定位X坐标
     * @param positionY      二维码定位Y坐标
     * @param scaleToFitX    二维码长度
     * @param scaleToFitY    二维码高度
     * @param fontBlankPath  字体空白图片路径
     * @param fontString     字体内容
     * @param llx            字体定位X坐标
     * @param lly            字体定位Y坐标
     * @throws IOException
     */
    public static void setImageAndFontWithBlankToPdf(String output,
                                                     String input,
                                                     String blankImagePath,
                                                     String newImagePath,
                                                     int positionX,
                                                     int positionY,
                                                     int scaleToFitX,
                                                     int scaleToFitY,
                                                     String fontBlankPath,
                                                     String fontString,
                                                     int llx,
                                                     int lly) throws IOException {

        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            PDPage page = doc.getPage(i);
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            if (blankImagePath != null && !blankImagePath.equals("")) {
                // 粘贴空白二维码图片
                PDImageXObject pdImageBlank = PDImageXObject.createFromFile(blankImagePath, doc);
                float widthBlank = scaleToFitX * 1f;
                float heightBlank = scaleToFitY * 1f;
                contents.drawImage(pdImageBlank, positionX, positionY, widthBlank, heightBlank);
            }
            if (newImagePath != null && !newImagePath.equals("")) {
                // 粘贴新图片
                PDImageXObject pdImage = PDImageXObject.createFromFile(newImagePath, doc);
                float width = scaleToFitX * 1f;
                float height = scaleToFitY * 1f;
                contents.drawImage(pdImage, positionX, positionY, width, height);
            }
            if (fontBlankPath != null && !fontBlankPath.equals("")) {
                // 粘贴字体部分空白图片
                PDImageXObject pdFontBlankImage = PDImageXObject.createFromFile(fontBlankPath, doc);
                float fontWidth = fontString.length() * 8 * 1f;
                float fontHeight = 8 * 1f;
                contents.drawImage(pdFontBlankImage, llx, lly, fontWidth, fontHeight);
            }
            if (fontString != null && !fontString.equals("")) {
                contents.beginText();
                contents.setFont(PDType1Font.HELVETICA, 11);
                contents.newLineAtOffset(llx, lly);
                contents.showText(fontString);
                contents.endText();
            }
            logger.info("Content added");
            contents.close();
        }

        //Saving the document
        doc.save(output);
        doc.close();
    }

    /**
     * 生成白色图片覆盖原图片并生成新二维码图片，生成白色图片覆盖字体并生成新字体（封面）
     *
     * @param output         输出文件路径
     * @param input          输入文件路径
     * @param blankImagePath 二维码空白图片
     * @param newImagePath   二维码新图片
     * @param positionX      二维码定位X坐标
     * @param positionY      二维码定位Y坐标
     * @param scaleToFitX    二维码长度
     * @param scaleToFitY    二维码高度
     * @param fontBlankPath  字体空白图片路径
     * @param fontString     字体内容
     * @param llx            字体定位X坐标
     * @param lly            字体定位Y坐标
     * @throws IOException
     */
    public static void setCoverImageAndFontWithBlankToPdf(String output,
                                                          String input,
                                                          String blankImagePath,
                                                          String newImagePath,
                                                          int positionX,
                                                          int positionY,
                                                          int scaleToFitX,
                                                          int scaleToFitY,
                                                          String fontBlankPath,
                                                          String fontString,
                                                          int llx,
                                                          int lly) throws IOException {

        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            if (i == 0) {
                PDPage page = doc.getPage(i);
                PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
                if (blankImagePath != null && !blankImagePath.equals("")) {
                    // 粘贴空白二维码图片
                    PDImageXObject pdImageBlank = PDImageXObject.createFromFile(blankImagePath, doc);
                    float widthBlank = scaleToFitX * 1f;
                    float heightBlank = scaleToFitY * 1f;
                    contents.drawImage(pdImageBlank, positionX, positionY, widthBlank, heightBlank);
                }
                if (newImagePath != null && !newImagePath.equals("")) {
                    // 粘贴新图片
                    PDImageXObject pdImage = PDImageXObject.createFromFile(newImagePath, doc);
                    float width = scaleToFitX * 1f;
                    float height = scaleToFitY * 1f;
                    contents.drawImage(pdImage, positionX, positionY, width, height);
                }
                if (fontBlankPath != null && !fontBlankPath.equals("")) {
                    // 粘贴字体部分空白图片
                    PDImageXObject pdFontBlankImage = PDImageXObject.createFromFile(fontBlankPath, doc);
                    float fontWidth = fontString.length() * 8 * 1f;
                    float fontHeight = 8 * 1f;
                    contents.drawImage(pdFontBlankImage, llx, lly, fontWidth, fontHeight);
                }
                if (fontString != null && !fontString.equals("")) {
                    contents.beginText();
                    contents.setFont(PDType1Font.HELVETICA, 11);
                    contents.newLineAtOffset(llx, lly);
                    contents.showText(fontString);
                    contents.endText();
                }
                logger.info("Content added");
                contents.close();
            }
        }

        //Saving the document
        doc.save(output);
        doc.close();
    }

    /**
     * 设置文字 REPLACED
     *
     * @param output     输出文件路径
     * @param input      输入文件路径
     * @param fontString 文字
     * @param llx        文字坐标x
     * @param lly        文字坐标y
     * @param urx        文字坐标 xx
     * @param ury        文字坐标yy
     * @throws IOException
     */
    public static void setFontToPdf(String output, String input, String fontString, int llx, int lly, int urx, int ury) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            //Retrieving the page
            PDPage page = doc.getPage(i);
            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //Begin the Content stream
            contents.beginText();
            //Setting the font to the Content stream
            contents.setFont(PDType1Font.HELVETICA, 11);
            //Setting the position for the line
            contents.newLineAtOffset(llx, lly);
//            String text = "This is the sample document and we are adding content to it.";
            //Adding text in the form of string
            contents.showText(fontString);
            //Ending the content stream
            contents.endText();
            logger.info("Content added");

            //Closing the PDPageContentStream object
            contents.close();
        }

        //Saving the document
        doc.save(output);
        doc.close();

    }

    /**
     * 设置每一页的页码 REPLACED
     *
     * @param output
     * @param input
     * @param fontString
     * @param llx
     * @param lly
     * @param urx
     * @param ury
     * @throws IOException
     */
    public static void setPageNoToPdf(String output, String input, String fontString, int llx, int lly, int urx, int ury) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            //Retrieving the page
            PDPage page = doc.getPage(i);
            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //Begin the Content stream
            contents.beginText();
            //Setting the font to the Content stream
            contents.setFont(PDType1Font.HELVETICA, 11);
            //Setting the position for the line
            contents.newLineAtOffset(llx, lly);
//            String text = "This is the sample document and we are adding content to it.";
            //Adding text in the form of string
            contents.showText(String.format(fontString, i, total));
            //Ending the content stream
            contents.endText();
            logger.info("Content added");

            //Closing the PDPageContentStream object
            contents.close();
        }

        doc.save(output);
        doc.close();

    }

    /**
     * 提取 提取pdf的二维码
     *
     * @param pdfFile PDF文件
     * @param outPath 图片存放目录
     * @return
     */
    public static List<String> parsePdfImage(String pdfFile, String outPath) throws IOException {
        File file = new File(pdfFile);
        if (!outPath.endsWith("/")) {
            outPath = outPath + "/";
        }
        List<String> images = new ArrayList<>();

        PDDocument document = PDDocument.load(file);

        int total = document.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            PDPage page = document.getPage(i);
            PDResources pdResources = page.getResources();
            for (COSName csName : pdResources.getXObjectNames()) {
//            for (COSName csName : pdResources.getCOSObject().keySet()) {

//                System.out.println(csName);
                PDXObject pdxObject = pdResources.getXObject(csName);
                if (pdxObject instanceof PDImageXObject) {
                    PDStream pdStream = pdxObject.getStream();
                    PDImageXObject image = new PDImageXObject(pdStream, pdResources);
                    // image storage location and image name
                    String imgPath = outPath + CryptoUtils.uniqueId() + ".png";
                    File imgFile = new File(imgPath);
                    ImageIO.write(image.getImage(), "png", imgFile);
                    images.add(imgPath);
                }
            }
        }
        document.close();

        return images;
    }


    public static String mergePdfToImage(List<String> files, String outPath) {
        List<String> targetFiles = new ArrayList<String>();
        for (String file : files) {
            List<String> perPdfs = partitionPdfFileToPerPage(file, outPath);
            targetFiles.addAll(perPdfs);
        }
        List<String> outFiles = new ArrayList<String>();
        String pdfFix = ".pdf";
        String pngFix = ".png";
        for (String file : targetFiles) {
            if (file.endsWith(pdfFix)) {
                String FileTemp = file.substring(0, file.length() - 4);
                if (convertPdfByTerminal(FileTemp + pdfFix, FileTemp + pngFix)) {
                    if (convertPdfByTerminal(FileTemp + pngFix, FileTemp + pdfFix)) {
                        outFiles.add(FileTemp + pdfFix);
                    }
                }
            }
        }
        String outFile = CryptoUtils.uniqueId() + pdfFix;
        if (outFiles.size() > 0) {
            mergePdfFiles(outFiles.toArray(new String[0]), outPath + outFile);
        }
        return outPath + outFile;
    }

    public static List<String> convertPdfToImage(List<String> files, String outPath) {
        List<String> targetFiles = new ArrayList<String>();
        for (String file : files) {
            List<String> perPdfs = partitionPdfFileToPerPage(file, outPath);
            targetFiles.addAll(perPdfs);
        }
        List<String> outFiles = new ArrayList<String>();
        String pdfFix = ".pdf";
        String pngFix = ".png";
        for (String file : targetFiles) {
            if (file.endsWith(pdfFix)) {
                String FileTemp = file.substring(0, file.length() - 4);
                if (convertPdfByTerminal(FileTemp + pdfFix, FileTemp + pngFix)) {
                    outFiles.add(FileTemp + pngFix);
                }
            }
        }
        return outFiles;
    }

    public static boolean convertPdfByTerminal(String file1, String file2) {
        boolean successFlg = false;
        try {
            Process process = Runtime.getRuntime().exec("convert -density 200 -alpha remove " + file1 + " " + file2);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            try {
                ir.close();
            } catch (Exception e) {
            }
            try {
                input.close();
            } catch (Exception e) {

            }
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
        File file = new File(file2);
        if (file.exists()) {
            successFlg = true;
        }
        return successFlg;
    }

    public static boolean convertFileByTerminal(String file1, String file2, String para) {
        boolean successFlg = false;
        try {
            Process process = Runtime.getRuntime().exec("convert " + para + " " + file1 + " " + file2);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            try {
                ir.close();
            } catch (Exception e) {
            }
            try {
                input.close();
            } catch (Exception e) {

            }
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
        File file = new File(file2);
        if (file.exists()) {
            successFlg = true;
        }
        return successFlg;
    }

    public static void removeOldQrImages(String filePath, float positionX, float positionY) throws IOException {
        //创建PdfDocument对象
        PdfDocument pdf = new PdfDocument();

        //加载PDF文档
        pdf.loadFromFile(filePath);

        //获取指定页
        PdfPageBase page = pdf.getPages().get(0);

        //获取页面上的图片信息
        PdfImageInfo[] imageInfo = page.getImagesInfo();
        boolean isChanged = false;

        //遍历每一个图片
        for (int j = imageInfo.length; j > 0; j--) {

            Rectangle2D r2d = imageInfo[j - 1].getBounds();
            Double x = r2d.getX();
            Double y = r2d.getY();
            if (positionX - 85 <= x.floatValue() && x.floatValue() <= positionX + 85 &&
                positionY - 85 <= y.floatValue() && y.floatValue() <= positionY + 85) {

                //通过图片的索引删除图片
                page.deleteImage(j - 1);
                isChanged = true;
            }
        }
        //获取注释集合
        PdfAnnotationCollection annotationCollection = pdf.getPages().get(0).getAnnotationsWidget();
        //遍历注释集合
        for (Object annotation : annotationCollection) {

            //判断注释是否为PdfRubberStampAnnotationWidget类型
            if (annotation instanceof PdfRubberStampAnnotationWidget) {

                //删除注释附件
                annotationCollection.remove((PdfRubberStampAnnotationWidget) annotation);
                isChanged = true;

            }
        }

        //保存文档
        if (isChanged) {
            pdf.saveToFile(filePath, FileFormat.PDF);
            System.out.println("old qr code pic is removed");
        }
        pdf.dispose();
    }

    public static void partitionPdfAndRename(String input, String output, Integer coverPage, Map<String, String> replaceStrs) {
        // 将windows 分割符替换成linux
        if (replaceStrs == null) replaceStrs = new HashMap<>();
        replaceStrs.put("-0P", "-OP");
        replaceStrs.put("SIIH", "sht");
        replaceStrs.put(".I", "of");
        replaceStrs.put(" ", "");

        String staticpath = input.substring(0, input.lastIndexOf("/") + 1);
        if (output == null) {
            output = staticpath;
        }
        input = input.replaceAll("\\\\", "/");
        output = output.replaceAll("\\\\", "/");
        PDDocument document = null;
        String fileOutPath = null;
        try {
            File indexFile = new File(input);// 这是对应文件名
            document = PDDocument.load(indexFile);

            int n = document.getNumberOfPages();

            if (!output.endsWith("/")) {
                output = output + "/";
            }
            String fileName = "";
            if (!input.endsWith(".pdf")) {
                fileName = input.substring(input.lastIndexOf("/") + 1);
            } else {
                fileName = input.substring(input.lastIndexOf("/") + 1, input.length() - 4);
            }
            Matcher matcher = PATTERN_FILE_NAME.matcher(fileName);
            String revNo = "";
            String prefixFilename = "";
            if (matcher.matches()) {
                revNo = matcher.group(2);
                prefixFilename = matcher.group(1);
            }
//            String shortUniqueId = CryptoUtils.shortUniqueId();

            Splitter splitter = new Splitter();
            splitter.setStartPage(1);
            splitter.setEndPage(n);
            File file = new File(output);
            if (!file.exists()) {
                file.mkdirs();
            }
            List<PDDocument> pages = splitter.split(document);
//            ListIterator<PDDocument> iterator = pages.listIterator();
            PDDocument cover = new PDDocument();
            int i = 0;
            for (PDDocument pd : pages) {
                if (i++ < coverPage) {
                    cover.addPage(pd.getPage(0));
                    continue;
                }
                PDDocument ePd = new PDDocument();
                ePd.addPage(pd.getPage(0));


                fileOutPath = output + prefixFilename + "_" + i + "_" + revNo + ".pdf";
//                String newname = output + fileName + "_" + shortUniqueId + ".png";

                File newFile = new File(fileOutPath);
                if (newFile.exists()) {
                    newFile.delete();
                }

                ePd.save(fileOutPath);
                ePd.close();
//                String cropStr = String.
//                    format("convert -compress none -monochrome -density 300 %s -gravity southeast -crop 930x60+292+245  %s",
//                        fileOutPath, newname);
//                runOsCmd(cropStr);
//                String ocrStr = String.format("tesseract %s %s -l engf --psm 7 --dpi 300", newname, "/var/www/01/temp/result"+i+".txt");
//
//                runOsCmd(ocrStr);
//
//                String result = FileUtils.readFileContent("/var/www/01/result"+i+".txt");
//                for(Map.Entry<String, String> entry: replaceStrs.entrySet()){
//                    result = result.replaceAll(entry.getKey(), entry.getValue());
//                }
//                Matcher matcher = PATTERN.matcher(result);
//                String shtNo = "";
//                if (matcher.matches()) {
//                    shtNo = matcher.group(3);
//                }
//                String fileRename = "";
//                if(StringUtils.isEmpty(shtNo)) {
//                    fileRename = matcher.group(1) +"_"+shtNo+"_"+rev;
//                }

//                i++;

            }

            fileOutPath = output + prefixFilename + "_cover_" + revNo + ".pdf";
//                String newname = output + fileName + "_" + shortUniqueId + ".png";
            cover.save(fileOutPath);
            cover.close();

            for (PDDocument pd : pages) {
                pd.close();

            }

        } catch (Exception e) {
            logger.error(e.toString());

        } finally {
            try {

                if (document != null) document.close();
            } catch (Exception ee) {
                logger.error(ee.toString());
            }
        }

    }

    public static boolean runOsCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            try {
                ir.close();
            } catch (Exception e) {
            }
            try {
                input.close();
            } catch (Exception e) {

            }
        } catch (IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
        return true;
    }

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String pdfFile1 = "/var/www/test222.pdf";
            String pdfOutput = "/var/www/testabcOut.pdf";

//            removeTextWatermark(pdfFile1, pdfOutput);

            //通过文件名加载文档
            //创建PdfDocument对象
//            File file = new File(pdfFile1);
//            PdfDocument pdf1 = new PdfDocument();
            File file = new File(pdfFile1);
            PDDocument pdf1 = PDDocument.load(file);
//            pdf1.loadFromFile(pdfFile1);
//            PdfPageCollection pages = pdf1.getPages();
//            Iterator<PDPage> iter = pages.iterator();
            for(int i=0;i<pdf1.getPages().getCount(); i++) {
//                PDPage page = iter.next();
                //去除文字水印
//                replaceText(page, "Evaluation Only. Created with Aspose.Words. Copyright 2003-2021 Aspose", "");
//                replaceText(page, "Pty Ltd.", "");
//                replaceText(page, "Created with an evaluation copy of Aspose.Words. To discover the full", "");
//                replaceText(page, "versions of our APIs please visit: https://products.aspose.com/words/", "");
//                replaceText(page, "This document was truncated here because it was created in the Evaluation", "");


                //去除图片水印
                removeImage(pdf1.getPages().get(i), "KSPX");
            }
//            document.removePage(document.getNumberOfPages() - 1);
//            file.delete();
            //保存文档
            pdf1.save(pdfOutput);
            pdf1.close();

            ////////

            pdfEncoder("/var/www/einvoice.pdf", 2, null);
            String input = "/var/www/01/F-102-101S-DWG-21005_Rev.D.pdf";
            Map<String, String> replaceStrs = new HashMap<>();
            replaceStrs.put("-0P", "-OP");
            replaceStrs.put("SIIH", "sht");
            replaceStrs.put(".I", "of");
            replaceStrs.put("  ", " ");

//            List<String> imgs =
            partitionPdfAndRename(input, "/var/www/01/temp", 2, replaceStrs);

//            PdfReader reader = new PdfReader(input);
//            ByteArrayOutputStream baos=new ByteArrayOutputStream();
//
//            PdfStamper stamper = new PdfStamper(reader, baos);
//            PdfContentByte overContent = stamper.getOverContent(0);
//            overContent.
//            reader.getPageN(0).;

            //创建PdfDocument对象
            PdfDocument pdf = new PdfDocument();

            //加载PDF文档
            pdf.loadFromFile("/var/www/7a.original.pdf");

            //获取指定页
            PdfPageBase page = pdf.getPages().get(0);

            //获取页面上的图片信息
            PdfImageInfo[] imageInfo = page.getImagesInfo();

            //获取附件集合（不包含注释附件）
            PdfAttachmentCollection attachments = pdf.getAttachments();
            attachments.clear();

//            page.getAnnotationsWidget().removeAt(0);

            //获取注释集合
            PdfAnnotationCollection annotationCollection = pdf.getPages().get(0).getAnnotationsWidget();
            //遍历注释集合
            for (Object annotation : annotationCollection) {

                //判断注释是否为PdfAttachmentAnnotationWidget类型
                if (annotation instanceof PdfRubberStampAnnotationWidget) {

                    //删除注释附件
                    annotationCollection.remove((PdfRubberStampAnnotationWidget) annotation);
                }
            }

            //遍历每一个图片
            for (int j = imageInfo.length; j > 0; j--) {

                Rectangle2D r2d = imageInfo[j - 1].getBounds();
                Double x = r2d.getX();
                Double y = r2d.getY();

                //通过图片的索引删除图片
                page.deleteImage(j - 1);
            }

            //保存文档
            pdf.saveToFile("/var/www/7a.original.pdf", FileFormat.PDF);
            pdf.dispose();

            parsePdfImage("/var/www/39.original的副本3.pdf", "/var/www/tmp/");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("/var/www/aa.pdf")));
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String fileName = "/var/www/aa.png";
            File pdfFile = new File("/var/www/abc.pdf");
//            getPageCount(pdfFile);
            String[] files = new String[2];
            files[0] = "/var/www/abc.pdf";
            files[1] = "/var/www/abcd.pdf";
            mergePdfFiles(files, "/var/www/aabbcc.pdf");
            QRCodeUtils.generateQRCodeNoBlank("D" + StringUtils.generateShortUuid(), 100, "png", fileName);
//            partitionPdfFileToPerPage("/var/www/aabbcc.pdf",null);
            // 将pdf文件先加水印然后输出
            setImageToPdf("/var/www/a1212.pdf", "/var/www/a.pdf", "/var/www/qr.png", 50,
                100, 50, 50);
//            setImageAndFontToPdf("/var/www/aa11.pdf", "/var/www/1.pdf", "/var/www/aa.png", 50,
//                100, 1, "FFTTJJ", 200, 100, 300, 150);
            //                String newname = filename.substring(0, filename.lastIndexOf(".")) + ".png";
//
//                String cropStr = String.
//                    format("convert -compress none -monochrome -density 300 %s -gravity southeast -crop 430x50+392+215  %s",
//                        filename, newname);
//                runOsCmd(cropStr);
//                String ocrStr = String.format("tesseract %s %s -l engf --psm 7 --dpi 300", newname, "/var/www/01/result.txt");
//
//                runOsCmd(ocrStr);

//            extractImages("/var/www/aa11.pdf", "/var/www/");

            // setImageAndFontToPdf(bos, "c:/var/1.pdf", fileName, 758, 206, 100, "Page: 10 OF 40", 20, 15, 160, 15);
//            setPageNoToPdf("/var/www/2.pdf", "/var/www/1.pdf", "Page: %s OF %s", 20, 15, 160, 15);
            // bos.close();  Cp95_1.pdf
            System.out.println("OK");

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


    /**
     * 给pdf 指定的位置追加图片 REPLACED
     *
     * @param output      要输出的pdf
     * @param input       输入的pdf
     * @param imagePath   输入的image
     * @param positionX   pdf 输出位置
     * @param positionY   pdf 输出位置
     * @param scaleToFitX image 大小，100 表示不缩放
     * @param scaleToFitY image 大小，100 表示不缩放
     * @throws IOException
     */
    public static void setSingleImageToPdf(String output, String input, String imagePath, int positionX,
                                           int positionY, int scaleToFitX, int scaleToFitY) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        //Retrieving the page
        PDPage page = doc.getPage(0);
        //Creating PDImageXObject object
        PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
        float width = scaleToFitX * 1f;
        float height = scaleToFitY * 1f;
        //creating the PDPageContentStream object
        PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        //Drawing the image in the PDF document
        contents.drawImage(pdImage, positionX, positionY, width, height);
        System.out.println("Image inserted");
        //Closing the PDPageContentStream object    [base64Src]="base64content"
        contents.close();

        //Saving the document
        doc.save(output);
        doc.close();

    }


    public static void removeTextWatermark(String inputPdfFilePath, String outputFdfFilePath) throws Exception {
        {
            File file = new File(inputPdfFilePath);
            PDDocument pd = PDDocument.load(file);
            for (PDPage page : pd.getPages()) {
                PDFStreamParser pdfsp = new PDFStreamParser(page);
                pdfsp.parse();
                List<Object> tokens = pdfsp.getTokens();
                System.out.println(tokens);
                for (int j = 0; j < tokens.size(); j++) {
                    //创建一个object对象去接收标记
                    Object next = tokens.get(j);
                    if (next instanceof Operator) {
                        Operator cur = (Operator) next;

                        //注意：PDF中Tj和TJ标记的字符串可能不相同，我的文档里有的标点和个别字符为TJ标记
                        if ("Tj".equalsIgnoreCase(cur.getName())) {
                            final Object o = tokens.get(j - 1);
                            if (o instanceof COSString) {
                                final COSString cosString = (COSString) o;

                                //此处是根据打印出的tokens中的COSString水印部分阿来做出的选择，40也可以改为其他值，需要结合实际
                                if (cosString.getString().length() > 40) {
                                    tokens.remove(j - 1);
                                }
                                System.out.println("cosString:" + cosString.getString());
                            }
                            System.out.println("====================");
                            if (o instanceof COSArray) {
                                final COSArray cosArray = (COSArray) o;
                                System.out.println("cosArray:" + cosArray);
                            }
                        }
                    }
                }
                //输出另存
                PDStream updatedStream = new PDStream(pd);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens(tokens);
                System.out.println(tokens);
                out.close();
                page.setContents(updatedStream);
            }
            pd.save(outputFdfFilePath);
            pd.close();
        }
    }

    //移除图片水印
    public static void removeImage(PDPage page, String cosName) {
        final PDResources resources = page.getResources();
        //获取图像资源等对象的名字
        final Iterable<COSName> xObjectNames = resources.getXObjectNames();
        //获取COSObject对象
        final COSDictionary cosDictionary = resources.getCOSObject();
        System.out.println(cosDictionary);
        for (COSName cn : xObjectNames) {
            System.out.println(cn);
            //此处需要注意cosDictionary对象的格式，需要先根据COSName.XOBJECT获取器对应的dictionary，
            // 才能进一步删除
            final COSDictionary dictionary = cosDictionary.getCOSDictionary(COSName.XOBJECT);
            //根据cosname来进行删除
            if(cn.getName().startsWith(cosName))
            dictionary.removeItem(cn);
        }
        System.out.println(cosDictionary);
    }
//            pdDocument.save("pdfWithNoMark.pdf");
//    }

}
