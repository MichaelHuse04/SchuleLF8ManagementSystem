package de.szut.lf8_starter.employee.skill;

import org.springframework.stereotype.Service;

@Service
public class EmployeeSkillMapper {
    public EmployeeSkillMapper(){

    }

    public EmployeeSkillDto mapToEmployeeSkillDto(EmployeeSkillEntity employeeSkillEntity){
        EmployeeSkillDto employeeSkillDto = new EmployeeSkillDto();
        employeeSkillDto.setId(employeeSkillEntity.getId());
        employeeSkillDto.setSkill(employeeSkillEntity.getSkill());
        employeeSkillDto.setEmployeeId(employeeSkillEntity.getEmployeeId());
        return employeeSkillDto;
    }

}
