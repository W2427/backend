package com.ose.tasks.domain.model.service.setting;

import com.ose.tasks.entity.setting.Discipline;

import java.util.List;

public interface DisciplineInterface {


    List<Discipline> getList(Long projectId);
}
