package de.szut.lf8_starter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectEmployeeGetDto {
    private Long employeeId;
    private String skill;
}
