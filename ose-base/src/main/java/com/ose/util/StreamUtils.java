package com.ose.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 数据流处理工具。
 */
public class StreamUtils {

    /**
     * 关闭数据流。
     *
     * @param stream 数据流
     * @return 是否成功
     */
    public static boolean close(Closeable stream) {

        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

}
