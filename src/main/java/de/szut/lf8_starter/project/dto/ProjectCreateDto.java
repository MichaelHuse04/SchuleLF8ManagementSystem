package de.szut.lf8_starter.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProjectCreateDto {

    private String description;

    private Date startDate;

    private Date endDate;

    private String comment;

    @NotNull
    private Long customerID;

    @NotNull
    private Long responsibleEmployeeID;

    private List<Long> assignedEmployees;

    @JsonCreator
    public ProjectCreateDto(String description, Date startDate, Date endDate, String comment, Long responsibleEmployeeID, Long customerID, List<Long> assignedEmployees) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.comment = comment;
        this.customerID = customerID;
        this.responsibleEmployeeID = responsibleEmployeeID;
        this.assignedEmployees = assignedEmployees;
    }
}
