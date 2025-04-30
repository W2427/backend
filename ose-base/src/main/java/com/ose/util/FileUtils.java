package com.ose.util;

import com.ose.exception.BusinessError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * 文件工具。
 */
public class FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    // 文件字符集
    private static final String DEFAULT_CHARSET = "UTF-8";

    // 文件扩展名格式
    private static final Pattern FILENAME_PATTERN = Pattern.compile("^(.+)?(\\.[0-9a-zA-Z]+)$");

    /**
     * 设置文件的访问许可。
     *
     * @param file 文件
     */
    private static void setAsRWX(File file) {
        file.setReadable(true, false);
        file.setWritable(true, false);
        file.setExecutable(true, false);
    }

    /**
     * 取得文件的扩展名。
     *
     * @param file 文件
     * @return 扩展名
     */
    public static String extname(File file) {
        return extname(file.getName());
    }

    /**
     * 取得文件的扩展名。
     *
     * @param filename 文件名
     * @return 扩展名
     */
    public static String extname(String filename) {
        Matcher m = FILENAME_PATTERN.matcher(filename);
        return !m.matches() ? "" : m.group(2);
    }

    /**
     * 读取文件内容。
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String read(String path) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(
                new File(path),
                DEFAULT_CHARSET
            );
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
    }

    /**
     * 将字符串写入文件。
     *
     * @param path 文件路径
     * @param data 数据
     */
    public static void write(String path, String data) {
        try {

            File file = new File(path);

            org.apache.commons.io.FileUtils.write(
                file,
                data,
                DEFAULT_CHARSET
            );

            setAsRWX(file);

        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
    }

    /**
     * 将文件复制到目标路径下。
     *
     * @param is        输入流
     * @param targetDir 目标路径
     * @param salt      用于生成文件名的 MD5 算法盐
     * @return 文件名
     */
    public static String copy(InputStream is, String targetDir, String salt) {
        OutputStream os = null;
        try {

            File target = new File(
                targetDir,
                Long.toString(
                    System.currentTimeMillis() * 1000000
                        + Math.abs(System.nanoTime()) % 1000000,
                    16
                )
            );

            os = new FileOutputStream(target);
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(is, md);

            byte[] buffer = new byte[4096];

            while (dis.read(buffer) > -1) {
                os.write(buffer);
            }

            dis.close();

            md.update(salt.getBytes());

            byte[] md5 = md.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : md5) {
                sb.append(String.format("%02X", b));
            }

            String filename = sb.toString().toLowerCase();

//            move(target, targetDir, filename);
//            return filename;

            File newFile = move(target, targetDir, filename); //用于解决无法move成功的情况，依然能够返回一个文件名
            return filename;

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new BusinessError(); // TODO 使用合适的错误
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                logger.error("fileOutputStream closed exception", e);
            }
        }

    }

    /**
     * 将通过 HTTP 请求上传的文件写入到磁盘。
     *
     * @param multipartFile 通过 HTTP 请求上传的文件
     * @param dir           目标路径
     * @return 文件实例
     */
    public static File save(MultipartFile multipartFile, String dir) {
        return save(multipartFile, dir, CryptoUtils.uniqueId());
    }

    /**
     * 将通过 HTTP 请求上传的文件写入到磁盘。
     *
     * @param multipartFile 通过 HTTP 请求上传的文件
     * @param dir           目标路径
     * @param name          文件名
     * @return 文件实例
     */
    public static File save(
        MultipartFile multipartFile,
        String dir,
        String name
    ) {

        File diskFile = new File(dir, name);

        try {

            org.apache.commons.io.FileUtils.copyInputStreamToFile(
                multipartFile.getInputStream(),
                diskFile
            );

            setAsRWX(diskFile);

        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }

        return diskFile;
    }

    /**
     * 重命名文件。
     *
     * @param source   文件
     * @param filename 文件名
     * @return 重命名后的文件
     */
    public static File rename(File source, String filename) {
        return move(source, source.getParentFile().getAbsolutePath(), filename);
    }

    /**
     * 移动文件。
     *
     * @param source 文件
     * @param dir    目标路径
     * @return 重命名后的文件
     */
    public static File move(File source, String dir) {
        return move(source, dir, source.getName());
    }

    /**
     * 移动文件。
     *
     * @param source   文件
     * @param dir      目标路径
     * @param filename 文件名
     * @return 重命名后的文件
     */
    public static File move(File source, String dir, String filename) {

        File target = new File(dir, filename);

        try {
            Files.move(source.toPath(), target.toPath(), REPLACE_EXISTING);
            setAsRWX(target);
        } catch (IOException e) {
            return source;
        }

        return target;
    }

    /**
     * 删除文件。
     *
     * @param path 文件路径
     * @return 是否删除成功
     */
    public static boolean remove(String path) {

        if (path == null) {
            return false;
        }

        return remove(new File(path));
    }

    /**
     * 删除文件。
     *
     * @param file 文件
     * @return 是否删除成功
     */
    public static boolean remove(File file) {

        if (file == null) {
            return false;
        }

        try {
            return file.delete();
        } catch (SecurityException e) {
            return false;
        }

    }

    /**
     * 读取元数据。
     *
     * @param file 所属文件
     * @return 元数据文件路径
     */
    public static <T> T readMetadata(File file, Class<T> metadataType) {
        return readMetadata(file.getAbsolutePath(), metadataType);
    }

    /**
     * 读取元数据。
     *
     * @param path 所属文件路径
     * @return 元数据文件路径
     */
    public static <T> T readMetadata(String path, Class<T> metadataType) {
        try {
            return StringUtils.fromJSON(read(path + ".json"), metadataType);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 保存元数据。
     *
     * @param file     所属文件
     * @param metadata 元数据
     * @return 元数据文件路径
     */
    public static String saveMetadata(File file, Object metadata) {
        return saveMetadata(file.getAbsolutePath(), metadata);
    }

    /**
     * 保存元数据。
     *
     * @param path     所属文件路径
     * @param metadata 元数据
     * @return 元数据文件路径
     */
    public static String saveMetadata(String path, Object metadata) {
        String filename = path + ".json";
        write(filename, StringUtils.toJSON(metadata));
        return filename;
    }

    /**
     * 取得相对路径。
     *
     * @param base 基准路径
     * @param path 文件路径
     * @return 文件相对路径
     */
    public static String relative(String base, String path) {
        return "/" + (new File(base))
            .toURI()
            .relativize((new File(path)).toURI())
            .getPath();
    }

    /**
     * 文件是否为图像。
     *
     * @param mimeType 文件的 MIME 类型
     * @return 是否为图像
     */
    public static boolean isImage(String mimeType) {
        return mimeType.toLowerCase().matches("^image/[^/]+$");
    }

    /**
     * 文件是否为音频。
     *
     * @param mimeType 文件的 MIME 类型
     * @return 是否为图像
     */
    public static boolean isAudio(String mimeType) {
        return mimeType.toLowerCase().matches("^audio/[^/]+$");
    }

    /**
     * 文件是否为视频。
     *
     * @param mimeType 文件的 MIME 类型
     * @return 是否为图像
     */
    public static boolean isVideo(String mimeType) {
        return mimeType.toLowerCase().matches("^video/[^/]+$");
    }



    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     *  Created on: 2011-8-2
     *      Author: blueeagle
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;

    }

    public static String[] getFileNames(String filePath){
        String[] fileArray = null;
        if(null!=filePath && !"".equals(filePath)) {
            File file = new File(filePath);
            //判断文件或目录是否存在
            if (!file.exists()) {
                logger.info("【" + filePath + " not exists】");
            }
            //获取该文件夹下所有的文件
            fileArray = file.list();
        }

        return fileArray;
    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static Map<String, String> parseFileName(String fileName, String regStr, int namePos, int revPos) {
        //namepos = 1, revPos = 5
        Matcher matcher;
//        Pattern FILE_REG = Pattern.compile("([0-9a-zA-Z\\\"\\-]+)(.*?Rev(\\.){0,1}(\\s){0,1})([0-9a-zA-Z]{1,2}).*");
        Pattern FILE_REG = Pattern.compile(regStr);
        Map<String, String> fileInfoMap = new HashMap<>();
        matcher = FILE_REG.matcher(fileName);
        String dwgName = "";
        String dwgRev = "";
        if (matcher.find()) {
            dwgName = matcher.group(1);
            dwgRev = matcher.group(5);
            fileInfoMap.put("no", dwgName);
            fileInfoMap.put("rev", dwgRev);

        } else {
            List<String> errors = new ArrayList<>();
            errors.add("NO SUCH DWG : " + fileName);
            logger.error("NO SUCH DWG : " + fileName);
        }
        return fileInfoMap;
    }
}
