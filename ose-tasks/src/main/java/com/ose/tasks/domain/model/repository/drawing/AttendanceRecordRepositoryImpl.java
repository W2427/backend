package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.ManHourCriteriaDTO;
import com.ose.tasks.entity.drawing.AttendanceRecord;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

public class AttendanceRecordRepositoryImpl extends BaseRepository implements AttendanceRecordRepositoryCustom {

    @Override
    public Page<AttendanceRecord> getList(ManHourCriteriaDTO dto) {
        SQLQueryBuilder<AttendanceRecord> builder = getSQLQueryBuilder(AttendanceRecord.class).is("status",EntityStatus.ACTIVE);

        if (dto.getKeyword() != null
            && !"".equals(dto.getKeyword())) {
            builder.like("lockedDate", dto.getKeyword());
        }


        return builder.paginate(dto.toPageable())
            .exec()
            .page();
    }
}
