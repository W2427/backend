package com.ose.tasks.vo.qc;

import java.util.*;

/**
 * 工作流业务设计到的常量 集合
 */

public class ReportTypeList {
    public static final List<String> typeList = new ArrayList<>();

    static {{
        typeList.add("PIPING_Radio_Graphic");
        typeList.add("PIPING_Ultrasonic");
        typeList.add("PIPING_Magnetic_Particle");
        typeList.add("PIPING_Penetration");
        typeList.add("STRUCTURE_FIT_UP");
        typeList.add("STRUCTURE_WELD");
        typeList.add("STRUCTURE_Radio_Graphic");
        typeList.add("STRUCTURE_Ultrasonic");
        typeList.add("STRUCTURE_Magnetic_Particle");
        typeList.add("STRUCTURE_Penetration");
        typeList.add("STRUCTURE_NT_F253_FIT_UP");
        typeList.add("STRUCTURE_NT_F253_WELD");
        typeList.add("PIPING_NT_F253_WELD");
        typeList.add("PIPING_NT_F253_FIT_UP");

        typeList.add("STRUCTURE_NT_F253_Radio_Graphic");
        typeList.add("STRUCTURE_NT_F253_Ultrasonic");
        typeList.add("STRUCTURE_NT_F253_Magnetic_Particle");
        typeList.add("STRUCTURE_NT_F253_Penetrant");

        typeList.add("PIPING_NT_F253_Magnetic_Particle");
        typeList.add("PIPING_NT_F253_Radio_Graphic");
        typeList.add("PIPING_NT_F253_Ultrasonic");
        typeList.add("PIPING_NT_F253_Penetrant");
    }}

}
