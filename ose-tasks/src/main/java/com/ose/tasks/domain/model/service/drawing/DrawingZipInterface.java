package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.entity.drawing.DrawingZipDetail;
import org.springframework.data.domain.Page;

/**
 * 打包图纸历史 service
 */

public interface DrawingZipInterface {
    /**
     * 查找打包历史表中的所有数据并返回。
     *
     * @return  返回
     */
    Page<DrawingZipDetail> search(PageDTO pageDTO);

}
