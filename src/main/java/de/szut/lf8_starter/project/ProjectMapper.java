package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import de.szut.lf8_starter.employee.skill.EmployeeSkillDto;
import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectMapper {
    private final CustomerRepository customerRepository;


    public ProjectMapper(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
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

    public ProjectEntity mapProjectCreateDtoAndIdToProjectEntity(ProjectCreateDto dto, Long projectId) {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(projectId);
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setRealEndDate(dto.getRealEndDate());
        entity.setComment(dto.getComment());
        entity.setCustomer(this.customerRepository.getReferenceById(dto.getCustomerID()));
        entity.setResponsibleEmployeeId(dto.getResponsibleEmployeeID());
        List<EmployeeSkillEntity> employeeSkillEntities = new ArrayList<>();
        for (EmployeeSkillDto employeeSkillDto: dto.getAssignedEmployees()){
            employeeSkillDto.setId(employeeSkillDto.getId());
            employeeSkillDto.setEmployeeId(employeeSkillDto.getEmployeeId());
            employeeSkillDto.setSkill(employeeSkillDto.getSkill());

        }
        entity.setEmployeeSkills(employeeSkillEntities);
        return entity;
    }
}
