package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

/**
 *
 */
public class TodoTempPdfDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    //任务流程id
    private List<String> tempFiles;

    public List<String> getTempFiles() {
        return tempFiles;
    }

    public void setTempFiles(List<String> tempFiles) {
        this.tempFiles = tempFiles;
    }

}
