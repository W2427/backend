package com.ose.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.entity.CaptchaBase;
import com.ose.exception.AccessDeniedError;
import com.ose.exception.AccessTokenExpiredError;
import com.ose.exception.AccessTokenInvalidError;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 人机测试工具。
 */
public class CaptchaUtils {

    // 图形验证码字符集
    private static final String CHARSET = "34578acdefhkmnprtwxyzACEFGHJKLMNPRWXYZ";

    // 加密指数
    private static final int LOG_ROUNDS = 6;

    // 接受的字体文件类型
    private static final Pattern ACCEPTED_FONT_NAME = Pattern.compile(
        "\\.(ttf)$",
        Pattern.CASE_INSENSITIVE
    );

    // 字体文件路径
    private static List<String> FONTS = new ArrayList<>();

    // 图形验证码几何信息
    private static final int WIDTH = 120;
    private static final int HEIGHT = 60;
    private static final String IMAGE_SIZE = WIDTH + "x" + HEIGHT;
    private static final String[] BACK_COLOR_RANGE = {"BF", "FF"};
    private static final double TEXT_LAYER_PADDING = 0.15;
    private static final double TEXT_LAYER_CONTENT = 1 - TEXT_LAYER_PADDING * 2;
    private static final double[] TEXT_SCALE_RANGE = {1.0, 1.25};
    private static final double[] TEXT_ROTATION_RANGE = {-15.0, 15.0};
    private static final double[] TEXT_SKEW_RANGE = {-15.0, 15.0};
    private static final String[] TEXT_COLOR_RANGE = {"00", "9F"};
    private static final String[] NOISE_COLOR_RANGE = {"7F", "DF"};

    // 加密数据格式
    private static final Pattern ENCRYPTED_DATA_PATTERN = Pattern.compile(
        "^(.+)\\$([0-9]+)\\$([0-9a-z]+)$",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * 设置字体文件。
     *
     * @param path 字体文件路径
     */
    public static void setFonts(String path) {

        File[] fonts = (new File(path)).listFiles(
            (dir, name) -> !ACCEPTED_FONT_NAME.matcher(name).matches()
        );

        if (fonts == null || fonts.length == 0) {
            return;
        }

        FONTS.clear();

        for (File font : fonts) {
            FONTS.add(font.getAbsolutePath());
        }

    }

    /**
     * 生成默认长度随机文本的图形验证码。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param ttl        有效时长（毫秒）
     * @return 人机验证数据
     */
    public static CaptchaData generateImageOfText(
        String userAgent,
        String remoteAddr,
        long ttl
    ) {
        return generateImageOfText(userAgent, remoteAddr, ttl, 4);
    }

    /**
     * 生成指定长度随机文本的图形验证码。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param ttl        有效时长（毫秒）
     * @param length     文本长度
     * @return 人机验证数据
     */
    public static CaptchaData generateImageOfText(
        String userAgent,
        String remoteAddr,
        long ttl,
        int length
    ) {
        return generateImageOfText(
            userAgent,
            remoteAddr,
            ttl,
            RandomUtils.text(CHARSET, length)
        );
    }

    /**
     * 生成指定文本的图形验证码。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param ttl        有效时长（毫秒）
     * @param text       图形验证码中的文本
     * @return 人机验证数据
     */
    public static CaptchaData generateImageOfText(
        String userAgent,
        String remoteAddr,
        long ttl,
        String text
    ) {

        long validUntil = System.currentTimeMillis() + ttl;

        int charCount = text.length();
        double textLayerWidth = Math.round(WIDTH / (TEXT_LAYER_PADDING * (charCount + 1) + TEXT_LAYER_CONTENT * charCount));
        String textLayerSize = textLayerWidth + "x" + HEIGHT;
        long textOffset = Math.round(textLayerWidth * TEXT_LAYER_PADDING);
        long textWidth = Math.round(textLayerWidth * TEXT_LAYER_CONTENT);
        long[] textOffsetRange = new long[]{-1 * textOffset, textOffset};
        char character;
        List<String> args;

        List<List<String>> commands = new ArrayList<>();

        commands.add(Arrays.asList(
            "convert",
            "-size", IMAGE_SIZE,
            "gradient:"
                + RandomUtils.color(BACK_COLOR_RANGE)
                + "-"
                + RandomUtils.color(BACK_COLOR_RANGE),
            "png:-"
        ));

        for (int index = 0; index < text.length(); index++) {

            character = text.charAt(index);

            args = new ArrayList<>(Arrays.asList(
                "convert",
                "png:-",
                "(",
                "-size", textLayerSize,
                "canvas:none",
                "-pointsize", "" + textWidth,
                "-fill", RandomUtils.color(TEXT_COLOR_RANGE),
                "-font", RandomUtils.randomItem(FONTS, "DejaVu-Sans-Mono-Bold"),
                "-draw", "gravity Center scale "
                    + RandomUtils.between(TEXT_SCALE_RANGE)
                    + ","
                    + RandomUtils.between(TEXT_SCALE_RANGE)
                    + " rotate "
                    + RandomUtils.between(TEXT_ROTATION_RANGE)
                    + " skewX "
                    + RandomUtils.between(TEXT_SKEW_RANGE)
                    + " skewY "
                    + RandomUtils.between(TEXT_SKEW_RANGE)
                    + " text 0,0 '" + character + "'",
                ")",
                "-geometry", textLayerSize
                    + "+"
                    + ((textOffset + textWidth) * index + RandomUtils.between(textOffsetRange))
                    + "+"
                    + RandomUtils.between(textOffsetRange),
                "-composite",
                "png:-"
            ));

            commands.add(args);

            args = new ArrayList<>(Arrays.asList(
                "convert",
                "png:-",
                "(",
                "-size", IMAGE_SIZE,
                "canvas:none",
                "-stroke", RandomUtils.color(NOISE_COLOR_RANGE),
                "-strokewidth", "0.5",
                "-draw", "line "
                    + RandomUtils.between(0, WIDTH)
                    + ","
                    + RandomUtils.between(0, HEIGHT)
                    + " "
                    + RandomUtils.between(0, WIDTH)
                    + ","
                    + RandomUtils.between(0, HEIGHT),
                ")",
                "-geometry", IMAGE_SIZE + "+0+0",
                "-composite"
            ));

            if (index + 1 < text.length()) {
                args.add("png:-");
            } else {
                args.add("-quality");
                args.add("75");
                args.add("jpeg:-");
            }

            commands.add(args);
        }

        String imageData;

        try {

            InputStream inputStream
                = ShellUtils.chain(commands).getInputStream();

            imageData = "data:image/jpeg;base64,"
                + CryptoUtils.encodeBase64(inputStream);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace(System.out);
            imageData = null;
        }

        return new CaptchaData(
            userAgent,
            remoteAddr,
            text.toLowerCase(),
            validUntil,
            imageData
        );

    }

    /**
     * 校验用户识别结果。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param captchaDTO 图形验证码校验数据传输对象
     * @return 图形验证码数据实体 ID
     */
    public static String verify(
        String userAgent,
        String remoteAddr,
        CaptchaDTO captchaDTO
    ) {
        return verify(userAgent, remoteAddr, captchaDTO, true);
    }

    /**
     * 校验用户识别结果。
     *
     * @param userAgent  客户端用户代理字符串
     * @param remoteAddr 客户端远程 IP 地址
     * @param captchaDTO 图形验证码校验数据传输对象
     * @param checkId    是否检查 ID
     * @return 图形验证码数据实体 ID
     */
    public static String verify(
        String userAgent,
        String remoteAddr,
        CaptchaDTO captchaDTO,
        boolean checkId
    ) {

        if (captchaDTO == null) {
            throw new AccessDeniedError();
        }

        Matcher m = ENCRYPTED_DATA_PATTERN.matcher(
            CryptoUtils.decodeBase64(captchaDTO.getEncryptedData())
        );

        if (!m.matches()
            || (checkId && !m.group(2).equals(String.valueOf(captchaDTO.getId())))) {
            throw new AccessTokenInvalidError();
        }

        long validUntil = Long.parseLong(m.group(3), 36);

        if (validUntil < System.currentTimeMillis()) {
            throw new AccessTokenExpiredError();
        }

        if (!BCrypt.checkpw(
            captchaDTO.getText()
                + m.group(2)
                + validUntil
                + remoteAddr
                + userAgent,
            m.group(1)
        )) {
            throw new AccessTokenInvalidError();
        }

        return m.group(2);
    }

    /**
     * 图形验证码数据。
     */
    public static class CaptchaData extends CaptchaBase {

        private static final long serialVersionUID = -639857883236289793L;

        @Schema(description = "内容加密数据")
        private String encryptedData;

        @Schema(description = "图像 Base64 字符串")
        private String imageData;

        /**
         * 构造方法。
         *
         * @param userAgent  客户端用户代理字符串
         * @param remoteAddr 客户端远程 IP 地址
         * @param text       内容文本
         * @param validUntil 有效截止时间（Unix Epoch 时间，毫秒）
         * @param imageData  图像 Base64 字符串
         */
        private CaptchaData(
            String userAgent,
            String remoteAddr,
            String text,
            long validUntil,
            String imageData
        ) {
            super();

            encryptedData = CryptoUtils.encodeBase64(
                BCrypt.hashpw(
                    text + this.getId() + validUntil + remoteAddr + userAgent,
                    BCrypt.gensalt(LOG_ROUNDS)
                )
                    + "$" + this.getId()
                    + "$" + Long.toString(validUntil, 36).toUpperCase()
            );

            setValidUntil(validUntil);

            this.imageData = imageData;
        }

        /**
         * 取得加密数据。
         *
         * @return 加密数据
         */
        public String getEncryptedData() {
            return encryptedData;
        }

        /**
         * 取得图像 Base64 字符串。
         *
         * @return 图像 Base64 字符串
         */
        public String getImageData() {
            return imageData;
        }

    }

    /**
     * 图形验证码校验数据传输对象。
     */
    public static class CaptchaDTO extends BaseDTO {

        private static final long serialVersionUID = -277198828858963303L;

        // 图形验证码数据实体 ID
        private Long id;

        @Schema(description = "用户识别的文本")
        private String text;

        @Schema(description = "内容加密数据")
        private String encryptedData;

        /**
         * 取得图形验证码数据实体 ID。
         *
         * @return 图形验证码数据实体 ID
         */
        @JsonIgnore
        public Long getId() {
            return id;
        }

        /**
         * 设置图形验证码数据实体 ID。
         *
         * @param id 图形验证码数据实体 ID
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * 取得用户识别的文本。
         *
         * @return 用户识别的文本
         */
        public String getText() {
            return text;
        }

        /**
         * 设置用户识别的文本。
         *
         * @param text 用户识别的文本
         */
        public void setText(String text) {
            this.text = text == null ? "" : text.toLowerCase();
        }

        /**
         * 取得内容加密数据。
         *
         * @return 内容加密数据
         */
        public String getEncryptedData() {
            return encryptedData;
        }

        /**
         * 设置内容加密数据。
         *
         * @param encryptedData 内容加密数据
         */
        public void setEncryptedData(String encryptedData) {
            this.encryptedData = encryptedData;
        }

    }

}
