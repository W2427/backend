package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.issues.entity.Issue;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.InspectionIssueCriteriaDTO;
import com.ose.tasks.dto.bpm.InternalInspectionIssueDTO;
import com.ose.tasks.dto.bpm.IssueCreateTaskDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;

import java.util.List;

public interface InspectionIssueInterface {

    void addInternalInspectionIssue(
        Long orgId,
        Long projectId,
        InternalInspectionIssueDTO dto,
        OperatorDTO operatorDTO
    );

    List<Issue> internalInspectionIssueList(
        Long orgId,
        Long projectId,
        InspectionIssueCriteriaDTO criteriaDTO
    );

    BpmActivityInstanceState createPunchlistTask(
        ContextDTO contextDTO,
        Long orgId,
        Long projectId,
        OperatorDTO operatorDTO,
        IssueCreateTaskDTO dto
    );

    BpmActivityInstanceDTO searchPunchlistTask(
        Long orgId,
        Long projectId,
        Long id
    );

}
