package de.szut.lf8_starter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectEmployeeGetAllDto {
    private Long projectId;
    private String projectDescription;
    private List<ProjectEmployeeGetDto> projectEmployees;
}
