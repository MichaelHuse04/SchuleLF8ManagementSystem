package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeClient;
import de.szut.lf8_starter.customer.CustomerEntity;
import de.szut.lf8_starter.customer.CustomerRepository;
import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.employee.skill.EmployeeSkillRepository;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectEmployeeGetAllDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeeGetDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final EmployeeClient employeeClient;

    public ProjectService(ProjectRepository projectRepository, CustomerRepository customerRepository, EmployeeSkillRepository employeeSkillRepository, EmployeeClient employeeClient) {
        this.projectRepository = projectRepository;
        this.customerRepository = customerRepository;
        this.employeeClient = employeeClient;
        this.employeeSkillRepository = employeeSkillRepository;
    }

    public ProjectEntity create(ProjectEntity projectEntity) {
        //Customer
        if (projectEntity.getCustomer() == null) {
            throw new IllegalArgumentException("CustomerID cant be null");
        }
        if (!customerRepository.existsById(projectEntity.getCustomer().getId())) {
            throw new IllegalArgumentException("CustomerID " + projectEntity.getCustomer().getId() + " doesnt exists");
        }

        CustomerEntity customerEntity = customerRepository.getReferenceById(projectEntity.getCustomer().getId());
        projectEntity.setCustomer(customerEntity);

        //Responsible Employee
        if (!employeeClient.existsById(projectEntity.getResponsibleEmployeeId())) {
            throw new IllegalArgumentException("Responsible Employee not found");
        }

        for (EmployeeSkillEntity entity : projectEntity.getEmployeeSkills()) {
            Long id = entity.getEmployeeId();
            if (!employeeClient.existsById(id)) {
                throw new IllegalArgumentException("Employee not found");
            }
        }
        return projectRepository.save(projectEntity);
    }

    public List<ProjectEntity> getAll() {
        return this.projectRepository.findAll();
    }

    public ProjectEntity getById(Long id) {
        if (id == null) {
            return null;
        }
        return this.projectRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        ProjectEntity projectEntity = this.projectRepository.findById(id).orElse(null);
        this.projectRepository.delete(projectEntity);
    }

    public void deleteAssignedEmployeeFromProject(EmployeeSkillEntity employeeSkillEntity, ProjectEntity projectEntity) throws ResourceNotFoundException {
        projectEntity.getEmployeeSkills().remove(employeeSkillEntity);
        this.projectRepository.save(projectEntity);
        this.employeeSkillRepository.delete(employeeSkillEntity);
    }

    public ProjectEmployeeGetAllDto getAllAssignedEmployees(ProjectEntity projectEntity) {
        ProjectEmployeeGetAllDto projectEmployeeGetAllDto = new ProjectEmployeeGetAllDto();
        projectEmployeeGetAllDto.setProjectId(projectEntity.getId());
        projectEmployeeGetAllDto.setProjectDescription(projectEntity.getDescription());
        projectEmployeeGetAllDto.setProjectEmployees(new ArrayList<>());
        for (EmployeeSkillEntity entity : projectEntity.getEmployeeSkills()) {
            ProjectEmployeeGetDto projectEmployeeGetDto = new ProjectEmployeeGetDto();
            projectEmployeeGetDto.setEmployeeId(entity.getEmployeeId());
            projectEmployeeGetDto.setSkill(entity.getSkill());
            projectEmployeeGetAllDto.getProjectEmployees().add(projectEmployeeGetDto);
        }

        return projectEmployeeGetAllDto;
    }

    public ProjectEntity update(ProjectEntity projectEntity) {

        if (projectEntity.getCustomer() == null) {
            throw new IllegalArgumentException("CustomerID cant be null");
        }
        if (!customerRepository.existsById(projectEntity.getCustomer().getId())) {
            throw new IllegalArgumentException("CustomerID " + projectEntity.getCustomer().getId() + " doesnt exists");
        }

        if (!employeeClient.existsById(projectEntity.getResponsibleEmployeeId())) {
            throw new IllegalArgumentException("Responsible Employee not found");
        }

        for (EmployeeSkillEntity entity : projectEntity.getEmployeeSkills()) {
            Long id = entity.getEmployeeId();
            if (!employeeClient.existsById(id)) {
                throw new IllegalArgumentException("Employee not found");
            }
        }

        Optional<ProjectEntity> optionalProjectEntry = this.projectRepository.findById(projectEntity.getId());
        if (optionalProjectEntry.isEmpty()) {
            return null;
        }

        ProjectEntity updatedProject = optionalProjectEntry.get();

        updatedProject.setDescription(projectEntity.getDescription());
        updatedProject.setStartDate(projectEntity.getStartDate());
        updatedProject.setEndDate(projectEntity.getEndDate());
        updatedProject.setRealEndDate(projectEntity.getRealEndDate());
        updatedProject.setComment(projectEntity.getComment());
        updatedProject.getEmployeeSkills().clear();
        updatedProject.getEmployeeSkills().addAll(projectEntity.getEmployeeSkills());
        updatedProject.setCustomer(customerRepository.getReferenceById(projectEntity.getCustomer().getId()));

        this.projectRepository.save(updatedProject);
        return updatedProject;

    }
}
