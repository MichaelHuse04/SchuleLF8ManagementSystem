package de.szut.lf8_starter.employee.skill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeSkillDto {
    private Long id;
    private String skill;
    private Long employeeId;
}
