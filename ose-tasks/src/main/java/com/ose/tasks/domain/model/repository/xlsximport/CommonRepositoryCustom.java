package com.ose.tasks.domain.model.repository.xlsximport;

import java.util.List;

public interface CommonRepositoryCustom {


    <T> List<T> getColumnInfos(String dbName, String tableName);

    <T> List<T> getXlsList(Long orgId, Long projectId, String sql);

}
