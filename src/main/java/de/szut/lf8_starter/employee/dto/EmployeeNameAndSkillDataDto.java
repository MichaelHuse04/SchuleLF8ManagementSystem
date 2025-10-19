package de.szut.lf8_starter.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeNameAndSkillDataDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<QualificationsPostDto> skillSet;
}
