package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.skill.EmployeeSkillEntity;
import de.szut.lf8_starter.exceptionHandling.ErrorDetails;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeeGetAllDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/project")
@Tag(name = "Project Management", description = "Endpoints for managing projects")
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper mapper;

    public ProjectController(ProjectService service, ProjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project created successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectGetDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping
    public ProjectGetDto create(@RequestBody ProjectCreateDto dto) throws Exception {
        ProjectEntity entity = this.mapper.mapCreateDtoToEntity(dto);
        entity = this.service.create(entity);
        return this.mapper.mapToGetProjectDto(entity);
    }

    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of all projects retrieved",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectGetDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping
    public List<ProjectGetDto> getAll() throws Exception {
        return this.service
                .getAll()
                .stream()
                .map(e -> this.mapper.mapToGetProjectDto(e))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get project by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectGetDto.class))),
        @ApiResponse(responseCode = "404", description = "Project not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping("/{projectID}")
    public ProjectGetDto getById(@PathVariable Long projectID) throws Exception {
        var entity = this.service.getById(projectID);
        if (entity == null) {
            throw new ResourceNotFoundException("Project with ID " + projectID + " not found");
        } else {
            return this.mapper.mapToGetProjectDto(entity);
        }
    }

    @Operation(summary = "Delete project by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
    @DeleteMapping("/{projectID}")
    public void delete(@PathVariable Long projectID) {
        var entity = this.service.getById(projectID);
        if (entity == null) {
            throw new ResourceNotFoundException("ProjectEntity not found on id = " + projectID);
        } else {
            this.service.delete(entity.getId());
        }
    }

    @Operation(summary = "Remove an employee from a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee removed from project"),
        @ApiResponse(responseCode = "400", description = "Invalid project or employee ID",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class))),
        @ApiResponse(responseCode = "404", description = "Project or employee not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
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

    @Operation(summary = "Get all employees assigned to a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of employees retrieved",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectEmployeeGetAllDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid project ID",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class))),
        @ApiResponse(responseCode = "404", description = "Project not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
    @GetMapping("/{projectID}/employees")
    public ProjectEmployeeGetAllDto getEmployeesByProjectID(@PathVariable Long projectID) {
        var projectEntity = this.service.getById(projectID);
        if (projectEntity == null) {
            throw new IllegalArgumentException("ProjectID not found");
        }
        return this.service.getAllAssignedEmployees(projectEntity);
    }

    @Operation(summary = "Update project by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project updated successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectGetDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class))),
        @ApiResponse(responseCode = "404", description = "Project not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
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

    @Operation(summary = "Get projects by employee ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of projects retrieved",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProjectGetDto.class)))
    })
    @GetMapping("/employee/{employeeId}")
    public List<ProjectGetDto> getProjectsByEmployeeId(@PathVariable Long employeeId) {
        return this.service
                .getProjectsByEmployeeId(employeeId)
                .stream()
                .map(this.mapper::mapToGetProjectDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Add an employee with a skill to a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee with skill added to project"),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorDetails.class)))
    })
    @PostMapping("/{projectId}/employee/{employeeId}/skill/{skill}")
    public void addEmployeeWithSkill(
            @PathVariable Long projectId,
            @PathVariable Long employeeId,
            @PathVariable String skill) {
        this.service.addEmployeeWithSkill(projectId, employeeId, skill);
    }
}
