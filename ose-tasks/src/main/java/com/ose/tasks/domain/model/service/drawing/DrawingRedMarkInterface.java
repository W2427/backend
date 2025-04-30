package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.drawing.*;

/**
 * Drawing RedMark 接口
 */
public interface DrawingRedMarkInterface {


    /**
     * 上传Redmark图纸
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param uploadFileName
     * @param context
     * @param uploadDTO
     * @return
     */
    boolean uploadRedmarkFile(Long orgId, Long projectId, String uploadFileName, ContextDTO context,
                              DrawingUploadDTO uploadDTO);

    /**
     * 上传Redmark图纸zip包
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param uploadFileName
     * @param context
     * @param uploadDTO
     * @return
     */
    boolean uploadRedmarkFileZip(Long orgId, Long projectId, String uploadFileName, ContextDTO context,
                                 DrawingUploadDTO uploadDTO);

    /**
     * 扫描文件内二维码上传redmark图纸
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param uploadFileName
     * @param context
     * @param uploadDTO
     */
    void uploadRedmarkFileQrcode(Long orgId, Long projectId, String uploadFileName, ContextDTO context,
                                 DrawingUploadDTO uploadDTO);
}
