package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.employee.skill.EmployeeSkillDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectGetDto {
    private long id;

    private String description;

    private Date startDate;

    private Date endDate;

    private Date realEndDate;

    private String comment;

    private Long customerId;

    private Long responsibleEmployeeId;

    private List<EmployeeSkillDto> assignedEmployees;

}
