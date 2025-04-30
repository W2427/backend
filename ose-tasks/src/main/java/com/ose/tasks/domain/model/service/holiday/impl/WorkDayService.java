package com.ose.tasks.domain.model.service.holiday.impl;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.holiday.WorkDayRepository;
import com.ose.tasks.domain.model.service.holiday.WorkDayInterface;
import com.ose.tasks.dto.holiday.WorkDayCreateDTO;
import com.ose.tasks.dto.holiday.WorkDaySearchDTO;
import com.ose.tasks.entity.holiday.WorkDayData;
import com.ose.vo.EntityStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class WorkDayService implements WorkDayInterface {

    private final WorkDayRepository workDayRepository;
    @Autowired
    public WorkDayService(WorkDayRepository workDayRepository) {
        this.workDayRepository = workDayRepository;
    }

    @Override
    public Page<WorkDayData> search(
        WorkDaySearchDTO dto
    ) {
        return workDayRepository.search(
            dto
        );
    }

    @Override
    public WorkDayData create(
        WorkDayCreateDTO workDayCreateDTO,
        ContextDTO contextDTO
    ) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String workDate = formatter.format(workDayCreateDTO.getWorkDate());

        WorkDayData workDayDataFind = workDayRepository.findByWorkDateAndCountryAndDeletedIsFalse(
            workDate,
            workDayCreateDTO.getCountry()
        );
        if (workDayDataFind != null) {
            throw new BusinessError("work day already exists");
        }

        WorkDayData workDayData = new WorkDayData();
        BeanUtils.copyProperties(workDayCreateDTO, workDayData);
        workDayData.setWorkDate(workDate);
        workDayData.setStatus(EntityStatus.ACTIVE);
        workDayData.setCreatedAt(new Date());
        workDayData.setCreatedBy(contextDTO.getOperator().getId());
        workDayData.setDeleted(false);
        workDayData.setLastModifiedAt(new Date());
        workDayData.setLastModifiedBy(contextDTO.getOperator().getId());

        return workDayRepository.save(workDayData);
    }

//    /**
//     * 更新节假日。
//     *
//     * @param id
//     * @param holidayCreateDTO
//     * @param contextDTO
//     * @return
//     */
//    @Override
//    public HolidayData update(
//        Long id,
//        HolidayCreateDTO holidayCreateDTO,
//        ContextDTO contextDTO
//    ) {
//        Optional<HolidayData> holidayDataOptional = holidayDateRepository.findById(id);
//        if (!holidayDataOptional.isPresent()) {
//            throw new BusinessError("holiday does not exist");
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String holidayDate = formatter.format(holidayCreateDTO.getHolidayDate());
//
//        HolidayData holidayDataFind = holidayDateRepository.findByHolidayDateAndCountryAndDeletedIsFalse(
//            holidayDate,
//            holidayCreateDTO.getCountry()
//        );
//        if (holidayDataFind != null && !holidayCreateDTO.getHolidayName().equals(holidayDataFind.getHolidayName())) {
//            throw new BusinessError("holiday already exists");
//        }
//
//        HolidayData holidayData = holidayDataOptional.get();
//        BeanUtils.copyProperties(holidayCreateDTO, holidayData);
//        holidayData.setHolidayDate(holidayDate);
//        holidayData.setLastModifiedBy(contextDTO.getOperator().getId());
//        holidayData.setLastModifiedAt(new Date());
//        return holidayDateRepository.save(holidayData);
//    }

    /**
     * 删除节假日。
     *
     * @param id
     * @param contextDTO
     */
    @Override
    public void delete(
        Long id,
        ContextDTO contextDTO
    ) {
        Optional<WorkDayData> optionalWorkDayData = workDayRepository.findById(id);
        if (optionalWorkDayData.isPresent()) {
            WorkDayData workDayData = optionalWorkDayData.get();
            workDayData.setDeleted(true);
            workDayData.setStatus(EntityStatus.DELETED);
            workDayData.setDeletedAt(new Date());
            workDayData.setDeletedBy(contextDTO.getOperator().getId());
            workDayData.setLastModifiedAt(new Date());
            workDayData.setLastModifiedBy(contextDTO.getOperator().getId());
            workDayRepository.save(workDayData);
        } else {
            throw new BusinessError("work day does not exist");
        }

    }

//    @Override
//    public HolidayData detail(Long id) {
//        Optional<HolidayData> holidayDataFind = holidayDateRepository.findById(id);
//        if (!holidayDataFind.isPresent()) {
//            throw new BusinessError("holiday does not exist");
//        }
//        return holidayDataFind.get();
//    }
}
