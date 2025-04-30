package com.ose.tasks.util;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.mpp.MPPReader;
import net.sf.mpxj.mpx.MPXWriter;

import java.io.IOException;

public class XerUtils {

    public static void main(String[] args) {

        MPXWriter writer = new MPXWriter();
        //这个是读取文件的组件
        MPPReader mppRead = new MPPReader();
        ProjectFile projectFile = null;
        //注意，如果在这一步出现了读取异常，肯定是版本不兼容，换个版本试试
        try {
            projectFile = mppRead.read("/var/www/01/proj.mpp");
            writer.write(projectFile, "/var/www/01/proj.mpx");

        } catch (MPXJException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
