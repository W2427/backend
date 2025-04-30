package com.ose.util;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

/**
 * 目标处理的图片类别有：png，jpg，jpeg
 *
 * 参考：
 * <ul>
 *   <li>[浅析data:image/png;base64的应用](https://www.cnblogs.com/ECJTUACM-873284962/p/9245474.html)</li>
 *   <li>[Base64](https://zh.wikipedia.org/wiki/Base64)</li>
 *   <li>[Data URLs](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs)
 * </ul>
 *
 * @author Donespeak
 * @date 2019/06/26
 */
public class ImageConvertBase64 {

    /**
     * 将图片文件转化为 byte 数组
     *
     * @param image
     *            待处理图片文件
     * @return 图片文件转化为的byte数组
     */
    public static byte[] toBytes(File image) {
        try (FileInputStream input = new FileInputStream(image)) {
            // InputStream 的 available() 返回的值是该InputStream 在不被阻塞的情况下，一次可以读取到的数据长度。
            // byte[] imageBytes = new byte[input.available()];
            // input.read(imageBytes);
            return IOUtils.toByteArray(input);
        } catch (IOException e) {
            return null;
        }
    }

    public static String toBase64(byte[] bytes) {
        return bytesEncode2Base64(bytes);
    }

    /**
     * 将图片转化为 base64 的字符串
     *
     * @param image
     *            待处理图片文件
     * @return 图片文件转化出来的 base64 字符串
     */
    public static String toBase64(File image) {
        return toBase64(image, false);
    }

    /**
     * 将图片转化为 base64 的字符串。如果<code>appendDataURLScheme</code>的值为true，则为图片的base64字符串拓展Data URL scheme。
     * @param image 图片文件的路径
     * @param appendDataURLScheme 是否拓展 Data URL scheme 前缀
     * @return 图片文件转化为的base64字符串
     */
    public static String toBase64(File image, boolean appendDataURLScheme) {
        String imageBase64 = bytesEncode2Base64(toBytes(image));
        if(appendDataURLScheme) {
            imageBase64 = ImageDataURISchemeMapper.getScheme(image) + imageBase64;
        }
        return imageBase64;
    }

    private static String bytesEncode2Base64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    private static byte[] base64Decode2Bytes(String base64) {
        try {
//            String decodedUrl = new String(Base64.getDecoder().decode(encodedUrl), StandardCharsets.UTF_8);

            return Base64.getDecoder().decode(URLDecoder.decode(base64,"UTF-8").replaceAll("\\n","").replaceAll(" ","+"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将byte数组恢复为图片文件
     *
     * @param imageBytes
     *            图片文件的 byte 数组
     * @param imagePath
     *            恢复的图片文件的保存地址
     * @return 如果生成成功，则返回生成的文件路径，此时结果为参数的<code>imagePath</code>。否则返回 null
     */
    public static File toImage(byte[] imageBytes, File imagePath) {
        if (!imagePath.getParentFile().exists()) {
            imagePath.getParentFile().mkdirs();
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath))) {
            bos.write(imageBytes);
            return imagePath;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 将base64字符串恢复为图片文件
     *
     * @param imageBase64
     *            图片文件的base64字符串
     * @param imagePath
     *            恢复的图片文件的保存地址
     * @return 如果生成成功，则返回生成的文件路径，此时结果为参数的<code>imagePath</code>。。否则返回 null
     */
    public static File toImage(String imageBase64, File imagePath) {
        // base64 字符串中没有 ","
        int firstComma = imageBase64.indexOf(",");
        if(firstComma >= 0) {
            imageBase64 = imageBase64.substring(firstComma + 1);
        }
        return toImage(base64Decode2Bytes(imageBase64), imagePath);
    }

    /**
     * 保存 imageBase64 到指定文件中。如果<code>fileName</code>含有拓展名，则直接使用<code>fileName</code>的拓展名。
     * 否则，如果 <code>imageBase64</code> 为Data URLs，则更具前缀的来判断拓展名。如果无法判断拓展名，则使用“png”作为默认拓展名。
     * @param imageBase64 图片的base64编码字符串
     * @param dir 保存图片的目录
     * @param fileName 图片的名称
     * @return 如果生成成功，则返回生成的文件路径。否则返回 null
     */
    public static File toImage(String imageBase64, File dir, String fileName) {
        File imagePath = null;
        if(fileName.indexOf(".") < 0) {
            String extension = ImageDataURISchemeMapper.getExtensionFromImageBase64(imageBase64, "png");
            imagePath = new File(dir, fileName + "." + extension);
        } else {
            imagePath = new File(dir, fileName);
        }
        return toImage(imageBase64, imagePath);
    }

    public static void main(String[] args) {


        String str = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFUAAAAUCAIAAABzk+bEAAACHUlEQVR42q1YwU0EMQx0FUhQBw3w%0Aux4oAYkCkGgMXTf3uypYKZKxZsaTZA8pQrtJ1vHM2D6HuF5/bvcbjGPSD/6ELZxefcRy90n1vFqI%0AXF43schC3cObpYUpa/8iTN0c3qHHNUnKu0Dbldf4OXWDXQpYltafP1+OwUafXi/H4IP5tR7pid4N%0AxhMU1CyIKUMDPOMf4Kf4vz/eYcj5t69LNyB1mUre0PEC+f+nv0+wRf2ZQWABPDuek44xKuA6wxgY%0AUkcEM4L1T5JUGRn4mdSBv6O2ww/RUV8Tf5W6UrCivAkBXgpJDHyc+GFU/Rk/ZwHXwqE/qM0HcRRA%0AqPugMOiiW6hhnw9gN/XnWgBuVfyVgsRfcTKqxM+R3FEvy9kGfgCf+nclECgAtyD/Qf883esvA7tT%0AWDLV1n9wqwpeAwGMQv6fwy/zH9w1+KclUEZKPoSsDZDwjH/8hZjPVz6b63/yYnTu6oLpGgxyOXT/%0AJ/Ezfx4/13+Wy+tveJFZMBV/D38X/1z/Kn6pD+PPuJA6d0XR/KR3Nd8/R9e3peZZ/zgEKn7gwlDA%0AxY+hyszvfuS5bTH6w0yYWxpQIHv4LPvcCLB/shGuOH3/O9W2SwqTI7Fy6+iuBt4Pf0vteJ9a7jaY%0AFlBWR33/NZ3joh8mUacz6zf8lTq3ki/h7+ry4BW/fR/SdWxb/9vwaGVfxxSEbyRMVBt2p6smBRYp%0AmJ7rbzS57RfxtRVgyq0vXwAAAABJRU5ErkJggg==";
        toImage(str, new File("/var/www/test.png"));
    }
}
