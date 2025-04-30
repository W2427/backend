package com.ose.docs.vo;

import com.ose.util.FileUtils;

import java.util.*;

/**
 * 文件分类标签。
 */
public enum FileCategory {

    FILE(1, "/assets/file-types/file.png"), // 文件
    TEXT(1, "/assets/file-types/text.png"), // 纯文本
    IMAGE(1, "/assets/file-types/image.png"), // 图像
    AUDIO(1, "/assets/file-types/audio.png"), // 音频
    VIDEO(1, "/assets/file-types/movie.png"), // 视频
    DOCUMENT(1, "/assets/file-types/file.png"), // 文稿
    WORKBOOK(1, "/assets/file-types/file.png"), // 工作表
    PRESENTATION(1, "/assets/file-types/file.png"), // 演示稿
    ARCHIVE(1, "/assets/file-types/archive.png"), // 压缩档案
    HTML(2, "/assets/file-types/html.png"), // HTML
    XML(2, "/assets/file-types/xml.png"), // XML
    JSON(2, "/assets/file-types/json.png"), // JSON
    CSV(2, "/assets/file-types/csv.png"), // CSV
    PNG(2, "/assets/file-types/image.png"), // PNG
    JPEG(2, "/assets/file-types/image.png"), // JPEG
    BMP(2, "/assets/file-types/image.png"), // BMP
    SVG(2, "/assets/file-types/image.png"), // SVG
    PDF(3, "/assets/file-types/pdf.png"), // Adobe PDF
    WORD(2, "/assets/file-types/word.png"), // Microsoft Office Word
    EXCEL(2, "/assets/file-types/excel.png"), // Microsoft Office Excel
    POWERPOINT(2, "/assets/file-types/powerpoint.png"), // Microsoft Office PowerPoint
    PAGES(2, "/assets/file-types/pages.png"), // Apple Pages
    NUMBERS(2, "/assets/file-types/numbers.png"), // Apple Numbers
    KEYNOTE(2, "/assets/file-types/keynote.png"); // Apple Keynote

    // 排序权重
    private final int weight;

    // 文件类型图标
    private final String icon;

    /**
     * 构造方法。
     */
    FileCategory(int weight, String icon) {
        this.weight = weight;
        this.icon = icon;
    }

    /**
     * 对文件分类标签列表排序。
     *
     * @param categories 文件分类标签列表
     * @return 排序后的文件分类标签列表
     */
    public static List<FileCategory> sort(List<FileCategory> categories) {
        categories.sort(Comparator.comparingInt(fc -> -1 * fc.weight));
        return categories;
    }

    /**
     * 取得文件图标。
     *
     * @param categories 文件分类标签列表
     * @return 文件图标路径
     */
    public static String icon(List<FileCategory> categories) {
        return sort(categories).get(0).icon;
    }

    private static void addCategory(
        Set<FileCategory> categories,
        String category
    ) {
        try {
            categories.add(FileCategory.valueOf(category.toUpperCase()));
        } catch (IllegalArgumentException e) {
            // nothing to do
        }
    }

    /**
     * 解析 MIME Type，设置文件分类标签列表。
     *
     * @param mimeType 文件 MIME Type
     * @param filename 文件名
     * @return 文件分类标签列表
     */
    public static List<FileCategory> resolveMimeType(
        String mimeType,
        String filename
    ) {

        filename = filename == null ? "" : filename;

        Set<FileCategory> categories = new HashSet<>();

        if (!mimeType.matches("^[^/]+/[^/]+$")) {
            return new ArrayList<>(categories);
        }

        String[] parts = mimeType.toLowerCase().split("/");
        String mainType = parts[0];
        String subType = parts[1];

        addCategory(categories, mainType);
        addCategory(categories, subType);

        if ("image".equals(mainType)) {
            switch (subType) {
                case "svg+xml":
                    categories.add(XML);
                    break;
            }
        } else if ("application".equals(mainType)) {
            switch (subType) {
                case "json":
                case "xml":
                    categories.add(TEXT);
                    break;
                case "pdf":
                    categories.add(DOCUMENT);
                    categories.add(IMAGE);
                    categories.add(PDF);
                    break;
                case "msword":
                case "vnd.openxmlformats-officedocument.wordprocessingml.document":
                    categories.add(DOCUMENT);
                    categories.add(WORD);
                    break;
                case "vnd.ms-powerpoint":
                case "vnd.openxmlformats-officedocument.presentationml.presentation":
                    categories.add(PRESENTATION);
                    categories.add(POWERPOINT);
                    break;
                case "vnd.ms-excel":
                case "vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    categories.add(WORKBOOK);
                    categories.add(EXCEL);
                    break;
                case "x-shockwave-flash":
                    categories.add(VIDEO);
                    break;
                case "zip":
                case "x-rar-compressed":
                case "x-tar":
                case "x-7z-compressed":
                    categories.add(ARCHIVE);
                    break;
                case "octet-stream":
                    switch (FileUtils.extname(filename).toLowerCase()) {
                        case ".pages":
                            categories.add(DOCUMENT);
                            categories.add(PAGES);
                            break;
                        case ".key":
                            categories.add(PRESENTATION);
                            categories.add(KEYNOTE);
                            break;
                        case ".numbers":
                            categories.add(WORKBOOK);
                            categories.add(NUMBERS);
                            break;
                    }
                    break;
            }
        }

        if (categories.size() == 0) {
            categories.add(FILE);
        }

        return sort(new ArrayList<>(categories));
    }

}
