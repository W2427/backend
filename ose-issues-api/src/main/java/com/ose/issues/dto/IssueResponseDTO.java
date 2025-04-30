package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import com.ose.issues.entity.Issue;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public class IssueResponseDTO extends BaseDTO {


    private static final long serialVersionUID = 5203986255527737488L;
    private Page<Issue> issues;

    private List<String> departments;

    private Map<String, String> columnHeaderMap;

    private List<String> modules;

    private List<String> systems;

    private List<String> disciplines;

    public Page<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Page<Issue> issues) {
        this.issues = issues;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    public Map<String, String> getColumnHeaderMap() {
        return columnHeaderMap;
    }

    public void setColumnHeaderMap(Map<String, String> columnHeaderMap) {
        this.columnHeaderMap = columnHeaderMap;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public List<String> getSystems() {
        return systems;
    }

    public void setSystems(List<String> systems) {
        this.systems = systems;
    }

    public List<String> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<String> disciplines) {
        this.disciplines = disciplines;
    }
}
