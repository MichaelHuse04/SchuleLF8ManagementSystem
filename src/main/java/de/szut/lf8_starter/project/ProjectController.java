package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeeGetAllDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/project")
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper mapper;

    public ProjectController(ProjectService service, ProjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }


    @PostMapping
    public ProjectGetDto create(@RequestBody ProjectCreateDto dto) throws Exception {
        ProjectEntity entity = this.mapper.mapCreateDtoToEntity(dto);
        entity = this.service.create(entity);
        return this.mapper.mapToGetProjectDto(entity);
    }

    @GetMapping
    public List<ProjectGetDto> getAll() throws Exception {
        return this.service
                .getAll()
                .stream()
                .map(e -> this.mapper.mapToGetProjectDto(e))
                .collect(Collectors.toList());
    }

    @GetMapping("/{projectID}")
    public ProjectGetDto getById(@PathVariable Long projectID) throws Exception {
        var entity = this.service.getById(projectID);
        if (entity == null) {
            throw new ResourceNotFoundException("Project with ID " + projectID + " not found");
        } else {
            return this.mapper.mapToGetProjectDto(entity);
        }
    }

    @DeleteMapping("/{projectID}")
    public void delete(@PathVariable Long projectID) {
        var entity = this.service.getById(projectID);
        if (entity == null) {
            throw new ResourceNotFoundException("ProjectEntity not found on id = " + projectID);
        } else {
            this.service.delete(entity.getId());
        }
    }

    @DeleteMapping("/{projectID}/assignedEmployee/{employeeID}")
    public void deleteAssignedEmployeeFromProject(@PathVariable Long projectID, @PathVariable Long employeeID) {
        var projectEntity = this.service.getById(projectID);
        if (projectEntity == null) {
            throw new IllegalArgumentException("ProjectID not found");
        }
        EmployeeSkillEntity employeeSkillEntity = null;
        for (EmployeeSkillEntity entity : projectEntity.getEmployeeSkills()) {
            if (entity.getEmployeeId().equals(employeeID)) {
                employeeSkillEntity = entity;
            }
        }

        if (employeeSkillEntity == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        this.service.deleteAssignedEmployeeFromProject(employeeSkillEntity, projectEntity);
    }


    @GetMapping("/{projectID}/employees")
    public ProjectEmployeeGetAllDto getEmployeesByProjectID(@PathVariable Long projectID) {
        var projectEntity = this.service.getById(projectID);
        if (projectEntity == null) {
            throw new IllegalArgumentException("ProjectID not found");
        }
        return this.service.getAllAssignedEmployees(projectEntity);
    }

    @PutMapping("/{projectId}")
    public ProjectGetDto updateProjectById(@PathVariable Long projectId, @RequestBody ProjectCreateDto dto) {
        ProjectEntity projectEntity = this.service.getById(projectId);
        if (projectEntity == null) {
            throw new IllegalArgumentException("Project with id " + projectId + " not found");
        }

        ProjectEntity entity = this.mapper.mapProjectCreateDtoAndIdToProjectEntity(dto, projectId);

        this.service.update(entity);

        return this.mapper.mapToGetProjectDto(entity);
    }

}
