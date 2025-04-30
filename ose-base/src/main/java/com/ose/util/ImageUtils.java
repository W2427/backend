package com.ose.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.constant.FileSizeValues;
import com.ose.exception.BusinessError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图像处理工具。
 */
public class ImageUtils {

    private final static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 图像彩色转黑白色
     *
     * @param srcImageFile  源图像地址
     * @param destImagefile 目标图像地址
     * @param formatType    目标图像格式 如果null,默认转为PNG
     */
    public static void toGray(
        String srcImageFile,
        String destImagefile,
        String formatType
    ) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            if (formatType == null) {
                formatType = "PNG";
            }
            ImageIO.write(
                toGray(src),
                formatType,
                new File(destImagefile)
            );
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * 图像彩色转黑白色
     *
     * @param inputImage
     * @return 转换后的BufferedImage
     */
    public static BufferedImage toGray(BufferedImage inputImage) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        return op.filter(inputImage, null);
    }

    /**
     * 取得图像文件的大小。
     *
     * @param source 图像文件
     * @return [宽度, 高度]
     */
    public static int[] getImageSize(File source) {
        return getImageSize(source.getAbsolutePath());
    }

    /**
     * 取得图像文件的大小。
     *
     * @param source 图像文件路径
     * @return [宽度, 高度]
     */
    public static int[] getImageSize(String source) {

        try {

            String[] result = StringUtils.fromInputStream(
                ShellUtils
                    .exec("identify", "-format", "%w %h", source)
                    .getInputStream()
            ).split("\\s+");

            if (result.length != 2) {
                return new int[]{0, 0};
            }

            return new int[]{
                Integer.parseInt(result[0]),
                Integer.parseInt(result[1])
            };

        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }

    }

    /**
     * 压缩图像。
     *
     * @param source   源文件
     * @param target   目标文件路径
     * @param width    宽度
     * @param height   高度
     * @param resizing 大小调整方式
     */
    public static void compress(
        File source,
        String target,
        int width,
        int height,
        Resizing resizing
    ) {
        compress(source, target, width, height, 0L, resizing);
    }

    public static void compress(
        File source,
        String target,
        ImageCompressionConfig config
    ) {
        compress(
            source,
            target,
            config.getWidth(),
            config.getHeight(),
            config.getExtentSize(),
            config.getResizing()
        );
    }

    /**
     * 压缩图像。
     *
     * @param source     源文件
     * @param target     目标文件路径
     * @param width      宽度
     * @param height     高度
     * @param extentSize 文件大小阈值
     * @param resizing   大小调整方式
     */
    public static void compress(
        File source,
        String target,
        int width,
        int height,
        long extentSize,
        Resizing resizing
    ) {

        int[] size = getImageSize(source);
        width = Math.min(size[0], width);
        height = Math.min(size[1], height);
        String resize = width + "x" + height;
        String resizeWithTags;

        switch (resizing) {
            case SCALE:
                resizeWithTags = resize + ">!";
                break;
            case CROP:
                resizeWithTags = resize + "^";
                break;
            default:
                resizeWithTags = resize + ">";
        }

        List<String> command = new ArrayList<>(Arrays.asList(
            "convert",
            source.getAbsolutePath() + "[0]",
            "-resize", resizeWithTags
        ));

        if (resizing == Resizing.CROP) {
            command.addAll(Arrays.asList(
                "-gravity", "center",
                "-extent", resize
            ));
        }

        command.addAll(Arrays.asList(
            "-flatten",
            "-interlace", "line"
        ));

        if (target.toLowerCase().matches("\\.jpe?g$") && extentSize > 0L) {
            command.addAll(Arrays.asList(
                "-define",
                "jpeg:extent=" + Math.round(extentSize / FileSizeValues.KB) + "kb"
            ));
        }

        command.add(target);

        try {

            Process process = ShellUtils.exec(command);
            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();
            int exitCode = process.waitFor();
            logger.error("执行指令" + command.toString() + " 执行Code" + exitCode);
            if (exitCode != 0) {

                ConsoleUtils.log(String.join(" ", command));
                ConsoleUtils.log(StringUtils.fromInputStream(stdout));
                ConsoleUtils.log(StringUtils.fromInputStream(stderr));

                // TODO 使用合适的错误类型
                throw new BusinessError("error.imagemagick[" + exitCode + "]");
            }

        } catch (IOException | InterruptedException e) {
            throw new BusinessError(e.getMessage());
        }

    }

    /**
     * 图像大小调整方式。
     * SCALE：拉伸；CROP：裁切；KEEP：保持横纵比。
     */
    public enum Resizing {
        SCALE, CROP, KEEP
    }

    /**
     * 图像压缩配置。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ImageCompressionConfig {

        // 最大宽度
        private int width;

        // 最大高度
        private int height;

        // 缩放方式
        private Resizing resizing = Resizing.KEEP;

        // 限定大小
        private long extentSize = 0L;

        @JsonCreator
        public ImageCompressionConfig() {
        }

        public ImageCompressionConfig setSize(int width, int height) {
            setWidth(width);
            setHeight(height);
            return this;
        }

        public int getWidth() {
            return width;
        }

        public ImageCompressionConfig setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public ImageCompressionConfig setHeight(int height) {
            this.height = height;
            return this;
        }

        public Resizing getResizing() {
            return resizing;
        }

        public ImageCompressionConfig setResizing(Resizing resizing) {
            this.resizing = resizing;
            return this;
        }

        public long getExtentSize() {
            return extentSize;
        }

        public ImageCompressionConfig setExtentSize(long extentSize) {
            this.extentSize = extentSize;
            return this;
        }

    }


}
