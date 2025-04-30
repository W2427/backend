package com.ose.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ose.util.ext.BufferedImageLuminanceSource;


public class QRCodeUtils {

    private static final String CHARSET = "UTF-8";


    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 识别图片中二维码，如果有异常返回null
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String decodeQrcodeFromImage(String filePath) throws Exception {
        String qrcode = null;
        try {
            //         String filePath = "c:\\var\\201995-120HG1030762.jpg";
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map hintss = new HashMap();
            hintss.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            MultiFormatReader formatReader = new MultiFormatReader();
            com.google.zxing.Result result = formatReader.decode(binaryBitmap, hintss);
            //System.out.println("resultFormat = "+ result.getBarcodeFormat());
            System.out.println("resultText = " + result.getText());
            qrcode = result.getText();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return qrcode;
    }

    /**
     * 生成没有白边的二维码
     *
     * @param content
     * @param width
     * @param imageType
     * @param filePath
     */
    public static void generateQRCodeNoBlank(String content, int width, String imageType, String filePath) {

        BufferedImage image = generateQRCodeNoBlank(content, width, imageType);
        try {
            // 3
            ImageIO.write(image, imageType, new File(filePath));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }
    }


    /**
     * 生成没有白边的二维码
     *
     * @param content
     * @param width
     * @param imageType
     */
    public static BufferedImage generateQRCodeNoBlank(String content, int width, String imageType) {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        BufferedImage image = null;
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix;
        try {
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, width, hints);
            // 1.1去白边
            int[] rec = bitMatrix.getEnclosingRectangle();
            int resWidth = rec[2] + 1;
            int resHeight = rec[3] + 1;
            BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
            resMatrix.clear();
            for (int i = 0; i < resWidth; i++) {
                for (int j = 0; j < resHeight; j++) {
                    if (bitMatrix.get(i + rec[0], j + rec[1])) {
                        resMatrix.set(i, j);
                    }
                }
            }

            // 2
            int widthR = resMatrix.getWidth();
            int heightR = resMatrix.getHeight();
            image = new BufferedImage(widthR, heightR, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < widthR; x++) {
                for (int y = 0; y < heightR; y++) {
                    image.setRGB(x, y, resMatrix.get(x, y) == true ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }

        return image;
    }


    /**
     * user: Rex
     * date: 2016年12月29日  上午12:31:29
     *
     * @param content 二维码内容
     * @return 返回二维码图片
     * @throws WriterException
     * @throws IOException     BufferedImage
     *                         TODO 创建二维码图片
     */
    public static BufferedImage createImage(String content, int qrwidth) throws IOException, WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrwidth, qrwidth, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    static void writeToStream(BufferedImage image, String format, OutputStream stream) throws IOException {
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * Test
     *
     * @param aaa
     */
    public static void main(String[] aaa) {
        //  generateQRCodeNoBlank(StringUtils.generateShortUuid(), 100 , "png", "c://var//aaaa.png");
        try {
            // decodeQrcodeFromImage("C:\\var\\www\\ose\\private\\upload\\20180921192843.png");
            String aa = decodeQrcodeFromImage("/var/www/doc1.png");
            System.out.println("ok=" + aa);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }
    }

    /**
     * 合成纯色背景图
     *
     * @param width     宽度
     * @param height    高度
     * @param colorCode tgb 颜色代码
     * @param outPath   输出路径
     * @return
     */
    public static boolean createBackgroundImg(Integer width, Integer height, String colorCode, String outPath) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        String[] colorArr = formatColorCode(colorCode);

        if (colorArr == null) {
            return false;
        }

        File backgroundImg = new File(outPath);

        if (!backgroundImg.exists()) {
            backgroundImg.mkdirs();
        }
        return writeImage(bufferedImage, "PNG", backgroundImg, colorArr);
    }

    /**
     * 格式化颜色参数
     *
     * @param colorCode
     * @return
     */
    private static String[] formatColorCode(String colorCode) {
        if (!colorCode.contains("rgba")) {
            return null;
        }
        colorCode = colorCode.replaceAll("rgba\\(", "").replaceAll("\\)", "");

        return colorCode.split(",");
    }

    /**
     * 通过指定参数写一个图片
     */
    private static boolean writeImage(BufferedImage bi, String picType, File file, String[] colorArr) {

        Graphics g = bi.getGraphics();

        g.setColor(new Color(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer.parseInt(colorArr[2])));

        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                g.drawLine(i, j, bi.getWidth(), bi.getHeight());
            }
        }
        g.dispose();
        boolean val = false;
        try {
            val = ImageIO.write(bi, picType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val;
    }


}
