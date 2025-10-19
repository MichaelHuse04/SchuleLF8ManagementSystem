package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.employee.skill.EmployeeSkillDto;
import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectMapper {


    public ProjectMapper() {

    }

    public ProjectGetDto mapToGetProjectDto(ProjectEntity projectEntity) {
        List<EmployeeSkillDto> skillDtos = new ArrayList<>();
        for(EmployeeSkillEntity employeeSkillEntity : projectEntity.getEmployeeSkills()){
            skillDtos.add(new EmployeeSkillDto(employeeSkillEntity.getId(),employeeSkillEntity.getSkill(),employeeSkillEntity.getEmployeeId()));
        }
        return new ProjectGetDto(projectEntity.getId(), projectEntity.getDescription(), projectEntity.getStartDate(),
                projectEntity.getEndDate(), projectEntity.getRealEndDate(), projectEntity.getComment(),
                projectEntity.getCustomer().getId(), projectEntity.getResponsibleEmployeeId(),
                skillDtos);
    }

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto) throws Exception {
        var entity = new ProjectEntity();
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setComment(dto.getComment());

        if (dto.getCustomerID() != null) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(dto.getCustomerID());
            entity.setCustomer(customerEntity);
        }

        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeID());
        List<EmployeeSkillEntity> employeeSkillEntities = new ArrayList<>();
        for (EmployeeSkillDto employeeSkillDto: dto.getAssignedEmployees()){
            employeeSkillEntities.add(new EmployeeSkillEntity(employeeSkillDto.getId(),employeeSkillDto.getEmployeeId(), employeeSkillDto.getSkill()));
        }
        entity.setEmployeeSkills(employeeSkillEntities);
        return entity;
    }
}
