package com.ose.util;

import com.spire.pdf.barcode.PdfCode128BBarcode;
import com.spire.pdf.barcode.TextLocation;
import com.spire.pdf.graphics.PdfRGBColor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BarCodeUtils {

    public static void main(String[] args) {
        try {
            // PdfCode128BBarcode 绘制Codebar条码
            PdfCode128BBarcode codebar = new PdfCode128BBarcode("DnXkerHrT");
            PdfRGBColor blue = new PdfRGBColor(Color.blue);
            codebar.setBarcodeToTextGapHeight(1f);
            codebar.setBarHeight(50f);
            codebar.setTextDisplayLocation(TextLocation.Bottom);
            codebar.setTextColor(blue);
            BufferedImage barImage = codebar.toImage();
//            条形码图片临时文件
            String barCodeFileName = "/Users/lishengyang/Desktop/" + CryptoUtils.uniqueId() + ".png";
            ImageIO.write(barImage, "png", new File(barCodeFileName));
//            X轴
            int coverPositionX = 100;
//            Y轴
            int coverPositionY = 50;

            int coverScaleToFitX = 100;
            int coverScaleToFitY = 50;
            String inputFilePath = "/Users/lishengyang/Desktop/1.pdf";
            String outPutFilePath = "/Users/lishengyang/Desktop/1.pdf";
            String temporaryDir = "/Users/lishengyang/Desktop/";
//            setImageToPdf(
//                outPutFilePath,
//                inputFilePath,
//                barImage,
//                barCodeFileName,
//                coverPositionX,
//                coverPositionY,
//                coverScaleToFitX,
//                coverScaleToFitY,
//                temporaryDir
//            );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }
    }

    public static void generateBarCodeToPdf(
        String content,
        int x,
        int y,
        int scaleToFitX,
        int scaleToFitY,
        String temporaryDir,
        String filePath,
        String drawingFilePath,
        String barCode
    ) {
        try {
            //绘制Codebar条码
            PdfCode128BBarcode codebar = new PdfCode128BBarcode(content);

            // 设置不显示文本
            codebar.setTextDisplayLocation(TextLocation.None);

//            PdfRGBColor blue = new PdfRGBColor(Color.blue);
//            codebar.setBarcodeToTextGapHeight(1f);
//            codebar.setBarHeight(50f);
//            codebar.setTextDisplayLocation(TextLocation.Bottom);
//            codebar.setTextColor(blue);
            BufferedImage barImage = codebar.toImage();
            String barCodeFileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
            ImageIO.write(barImage, "png", new File(barCodeFileName));

            setImageToPdf(filePath, drawingFilePath, barImage, barCodeFileName, x, y, scaleToFitX, scaleToFitY, temporaryDir, barCode);
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
    public static void setImageToPdf(
        String output,
        String input,
        BufferedImage image,
        String imagePath,
        int positionX,
        int positionY,
        int scaleToFitX,
        int scaleToFitY,
        String temporaryDir,
        String barCode
    ) throws IOException {
        //Loading an existing document
        File file = new File(input);
        PDDocument doc = PDDocument.load(file);
        int total = doc.getNumberOfPages();
        if ( total > 0) {
            //Retrieving the page
            PDPage page = doc.getPage(0);
            page.getRotation();
            //Creating PDImageXObject object
            // 如果存在版则特殊处理
            PDImageXObject pdImage = null;
            float width = scaleToFitX * 1f;
            float height = scaleToFitY * 1f;


            if (barCode.equals("BARCODE_VERTICAL")){
                if (page.getRotation() == 270 || page.getRotation() == 90) {
                    pdImage = PDImageXObject.createFromFile(imagePath, doc);
                } else {
                    String newImageFileName = rotateImage(image, 90, temporaryDir);
                    pdImage = PDImageXObject.createFromFile(newImageFileName, doc);
                    width = scaleToFitY * 1f;
                    height = scaleToFitX * 1f;
                }
            }else {
                if (page.getRotation() == 270) {
                    String newImageFileName = rotateImage(image, 90, temporaryDir);
                    pdImage = PDImageXObject.createFromFile(newImageFileName, doc);
                    width = scaleToFitY * 1f;
                    height = scaleToFitX * 1f;
                } else if (page.getRotation() == 90) {
                    String newImageFileName = rotateImage(image, 270, temporaryDir);
                    pdImage = PDImageXObject.createFromFile(newImageFileName, doc);
                    width = scaleToFitY * 1f;
                    height = scaleToFitX * 1f;
                } else {
                    pdImage = PDImageXObject.createFromFile(imagePath, doc);
                }
            }

            float adjustedPositionX = positionX;
            float adjustedPositionY = positionY;

            switch (page.getRotation()) {
                case 90:
                    adjustedPositionX = page.getMediaBox().getHeight() - positionY - height/2;
                    adjustedPositionY = positionX - width/2;
                    break;
                case 180:
                    adjustedPositionX = page.getMediaBox().getWidth() - positionX - width;
                    adjustedPositionY = page.getMediaBox().getHeight() - positionY;
                    break;
                case 270:
                    adjustedPositionX = positionX - width/2;
                    adjustedPositionY = page.getMediaBox().getHeight() - positionY - height/2;
                    break;
                default:
                    adjustedPositionY = positionY - height;
                    break;
            }

            //creating the PDPageContentStream object
            PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //Drawing the image in the PDF document
            contents.drawImage(pdImage, adjustedPositionX, adjustedPositionY, width, height);
            System.out.println("Image inserted");
            //Closing the PDPageContentStream object
            contents.close();
        }
        //Saving the document
        doc.save(output);
        doc.close();
    }

    //图片旋转制定角度
    public static String rotateImage(BufferedImage originalImage, double degree, String temporaryDir) {
        try {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // 计算旋转后的新宽度和高度
            double sin = Math.abs(Math.sin(Math.toRadians(degree))),
                cos = Math.abs(Math.cos(Math.toRadians(degree)));
            int newWidth = (int) Math.floor(width * cos + height * sin),
                newHeight = (int) Math.floor(height * cos + width * sin);

            // 创建一个新的空白图片用于将原始图片绘制上去
            BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

            // 创建一个变换对象并设置变换参数
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - width) / 2, (newHeight - height) / 2);
            at.rotate(Math.toRadians(degree), width / 2, height / 2);

            // 创建一个变换操作对象并执行变换
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
            rotatedImage = op.filter(originalImage, rotatedImage);

            String barCodeFileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
            ImageIO.write(rotatedImage, "png", new File(barCodeFileName));
            return barCodeFileName;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return null;
        }

    }
}
