package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class DrawingZipHistoryDTO extends BaseDTO {

    private static final long serialVersionUID = 4976175241547936824L;


    @Schema(description = "操作人名")
    private String operateName;

    @Schema(description = "操作人id")
    private Long operateBy;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "打包完成时间")
    private Date packageAt;

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "图纸编号")
    private String DrawingNo;

    @Schema(description = "打包状态")
    private EntityStatus packageStatus ;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "制图人")
    private String drawer;
}
