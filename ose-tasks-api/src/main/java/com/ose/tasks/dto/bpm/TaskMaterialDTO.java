package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 临时文件信息。
 */
public class TaskMaterialDTO extends BaseDTO {

    private static final long serialVersionUID = -4059288973826028452L;

    @Schema(description = "材料1")
    private String material1;

    @Schema(description = "材料2")
    private String material2;

    @Schema(description = "材料代码1")
    private String materialCode1;

    @Schema(description = "材料代码2")
    private String materialCode2;

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public String getMaterialCode1() {
        return materialCode1;
    }

    public void setMaterialCode1(String materialCode1) {
        this.materialCode1 = materialCode1;
    }

    public String getMaterialCode2() {
        return materialCode2;
    }

    public void setMaterialCode2(String materialCode2) {
        this.materialCode2 = materialCode2;
    }

}
