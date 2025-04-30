package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.dto.CheckOutHourDTO;
import com.ose.tasks.dto.LeaveDataDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.entity.drawing.DingTalkLeaveData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public interface DingTalkLeaveDataRepositoryCustom {

List<DingTalkLeaveData> searchReview(ReviewCriteriaDTO criteriaDTO);


}
