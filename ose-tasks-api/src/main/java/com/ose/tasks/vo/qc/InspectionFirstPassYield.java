package com.ose.tasks.vo.qc;

/**
 *
 */
public enum InspectionFirstPassYield {

    INIT(0),

    SUCCESS(1),

    FAILURE(2);

    private int state;

    InspectionFirstPassYield(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public static InspectionFirstPassYield getEnumByState(int state) {
        for (InspectionFirstPassYield temp : InspectionFirstPassYield.values()) {
            if (temp.getState() == state) {
                return temp;
            }
        }
        return null;
    }
}
