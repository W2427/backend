package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.entity.ExportExcel;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Future;

public interface XlsExportInterface {


    /**
     * 取得指定组织的所有导出的XLS 视图。
     *
     * @param orgId     组织 ID
     * @param projectId 项目ID
     * @param pageDTO   分页信息
     * @return 项目分页数据
     */
    Page<ExportExcel> getList(Long orgId, Long projectId, PageDTO pageDTO);

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param exportView 项目ID
     * @return 实体下载临时文件
     */
    File saveDownloadFile(Long orgId, Long projectId, String exportView);

    /**
     * 保存实体下载临时文件(WHS)。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param exportView 项目ID
     */
    File saveWhsDownloadFile(Long orgId, Long projectId, String exportView) throws FileNotFoundException;

    /**
     * 根据视图名称取得视图对应的excel下载详情。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param excelView 项目ID
     * @return 实体下载临时文件
     */
    List<ExportExcel> getViewName(Long orgId, Long projectId, String excelView);

    Future<String> saveAsyncDownloadFile(ContextDTO context, Long orgId, Long projectId, ExportExcel exportExcel);
}
