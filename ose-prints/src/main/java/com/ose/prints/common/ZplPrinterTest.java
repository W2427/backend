package com.ose.prints.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ose.util.QRCodeUtils;

public class ZplPrinterTest {
    public static void main(String[] args) {
        try {
            ZplPrinter p = new ZplPrinter("OsePrintShop01", "C://var//www//ose//libs//ts24.lib");
            // ZplPrinter p = new ZplPrinter("\\\\10.10.13.224\\ZDesigner GT800 (ZPL)");
            // printBarcode(p);
            // p.resetZpl();//清除
            printPicking300DPI22(p);
            p.resetZpl();// 清除
            // printPicking203DPI(p);
            // p.resetZpl();//清除
            // printRestoking(p);
        } catch (Exception e) {

        }

    }

    private static boolean printQRcode(ZplPrinter p) {
        // 1.打印单个条码

        // 1.打印单个条码
        // String bar0 = Fnthex.getFontHex("你好", 10, 50, 80, "黑体");//条码内容
        BufferedImage image;
        try {
            image = ImageIO.read(new File("c:\\aaaa2.png"));
            p.setQRcodeImage(image, 500, 10);
            String zpl = p.getZpl();
            System.out.println(zpl);
            boolean result = p.print(zpl);// 打印
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }
        return false;
    }

    private static boolean printPicking300DPI22(ZplPrinter p) {

        BufferedImage image;
        try {
            //
            p.setQRcodeImage(QRCodeUtils.createImage("asdfasdfasdf", 150), 30, 10);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setCharR("Spec:", 10, 250, 40, 40);

        p.setCharR("HeatNo:", 50, 250, 40, 40);

        String zpl = p.getZpl();
        boolean result = p.print(zpl);
        return result;
    }

    private static boolean printBarcode(ZplPrinter p) {
        // 1.打印单个条码
        String bar0 = "131ZA010101";// 条码内容
        // String bar0Zpl = "^FO110,110^BY6,3.0,280^BCN,,Y,N,N^FD${data}^FS";//条码样式模板
        String bar0Zpl = "^FO100,50^AAN,100,110^BY6,3.0,280^BCN,,N,N,N^FD${data}^FS";// 条码样式模板
        p.setBarcode(bar0, bar0Zpl);
        p.setChar(bar0, 200, 380, 140, 110);
        p.setText("上", 880, 380, 60, 60, 30, 4, 4, 24);

        String zpl = p.getZpl();
        System.out.println(zpl);
        boolean result = p.print(zpl);// 打印
        return result;
    }

    private static boolean printPicking300DPI(ZplPrinter p) {

        BufferedImage image;
        try {
            //
            p.setQRcodeImage(QRCodeUtils.createImage("asdfasdfasdf", 250), 30, 10);
            // String zpl = p.getZpl();
            // System.out.println(zpl);
            // boolean result = p.print(zpl);//打印
            // return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }
        p.setChar("Spec:", 380, 40, 40, 40);
        p.setText("国国国国国国", 480, 40, 40, 40, 20, 2, 2, 24);
        p.setChar("HeatNo:", 380, 100, 40, 40);
        p.setText("国国国国国国", 540, 100, 40, 40, 20, 2, 2, 24);
        p.setChar("TagNubmer:", 380, 160, 40, 40);
        p.setText("xxxxx", 580, 160, 40, 40, 20, 2, 2, 24);
        p.setChar("Unit:", 380, 220, 40, 40);
        p.setText("xxxxx", 480, 220, 40, 40, 20, 2, 2, 24);
        p.setChar("QTY:", 380, 280, 40, 40);
        p.setText("xxxxx", 480, 280, 40, 40, 20, 2, 2, 24);
        p.setChar("CSS0BPKPPR", 30, 330, 40, 40);
        p.setChar("No:", 30, 420, 40, 40);
        p.setText("国国国国国国国国国国国国国国国国国国国国国", 110, 420, 40, 40, 20, 2, 2, 24);
        p.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", 110, 480, 40, 40, 20, 2, 2, 24);

        String zpl = p.getZpl();
        boolean result = p.print(zpl);
        return result;
    }

    private static boolean printPicking203DPI(ZplPrinter p) {
        // 左边的条码
        String bar1 = "07";
        p.setChar(bar1, 126, 86, 40, 40);
        String bar1Zpl = "^FO66,133^BY5,3.0,160^BCR,,N,N,N^FD${data}^FS";// 条码样式模板
        p.setBarcode(bar1, bar1Zpl);
        // 下边的条码
        String bar2 = "00000999990018822969";// 20位
        String bar2Paper = "^FO253,400^BY2,3.0,66^BCN,,Y,N,N^FD${data}^FS";// 条码样式模板
        p.setBarcode(bar2, bar2Paper);

        p.setText("国药控股湖南有限公司", 253, 26, 40, 40, 20, 1, 1, 24);
        p.setChar("CSS0BPKPPR", 253, 66, 20, 20);

        p.setText("09件", 626, 53, 40, 40, 20, 1, 1, 24);
        p.setText("补", 733, 53, 40, 40, 20, 1, 1, 24);

        p.setText("公司自配送 公路", 253, 120, 53, 53, 20, 1, 1, 24);
        p.setChar("03231151", 626, 124, 26, 26);
        p.setChar("2015-10-10", 626, 151, 20, 20);

        p.setText("湖南六谷大药房连锁有限公司", 253, 173, 40, 40, 20, 1, 1, 24);

        p.setText("长沙市开福区捞刀河镇中岭村258号", 253, 213, 30, 30, 20, 1, 1, 22);

        p.setText("多SKU", 533, 323, 40, 40, 20, 1, 1, 24);

        p.setText("库位:49", 253, 280, 37, 37, 20, 1, 1, 24);
        p.setText("品类:感冒胶囊", 253, 323, 37, 37, 20, 1, 1, 24);

        p.setText("批号:", 253, 366, 37, 37, 20, 1, 1, 24);
        p.setChar("78787878788", 333, 373, 26, 26);

        String zpl = p.getZpl();
        System.out.println(zpl);
        boolean result = p.print(zpl);
        return result;
    }

    private static boolean printRestoking(ZplPrinter p) {
        // 上方的条码
        String bar1 = "1434455567773344";
        String bar1Zpl = "^FO10,70^BY4,3.0,140^BCN,,Y,N,N^FD${data}^FS";// 条码样式模板
        p.setBarcode(bar1, bar1Zpl);
        // 源货位
        p.setText("来源:131ZA010101", 100, 320, 60, 60, 30, 2, 2, 24);
        // 目标货位
        p.setText("目的:131ZA010102", 640, 320, 60, 60, 30, 2, 2, 24);
        // 货品编号
        p.setText("货品编号:YF10001", 100, 440, 60, 60, 30, 2, 2, 24);
        // 件装数
        p.setText("补货数量:200", 640, 440, 60, 60, 30, 2, 2, 24);
        // 品名
        p.setText("复方一支黄花：", 100, 580, 60, 60, 30, 2, 2, 24);

        String zpl = p.getZpl();
        System.out.println(zpl);
        boolean result = p.print(zpl);
        return result;
    }
}
