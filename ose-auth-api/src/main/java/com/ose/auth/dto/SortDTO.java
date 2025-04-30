package com.ose.auth.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

public class SortDTO extends BaseDTO {

    private static final long serialVersionUID = -5762243061093899212L;

    @Schema(description = "排序设置")
    private String sort;

    public Map<String, String> getSort() {

        if (sort == null) {
            return new HashMap<>();
        }

        Map<String, String> sortMap = new HashMap<>();

        for (String subSort : sort.split("&")) {
            sortMap.put(subSort.split(":")[0], subSort.split(":")[1]);
        }

        return sortMap;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
