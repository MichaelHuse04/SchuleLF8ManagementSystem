package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper mapper;

    public ProjectController(ProjectService service, ProjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ProjectGetDto create(ProjectCreateDto dto) throws Exception {
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

    @GetMapping("projects/{ProjectID}")
    public ProjectEntity getById(@PathVariable Long id) throws Exception {
        return this.service.getById(id);
    }

    @DeleteMapping("/{ProjectID}")
    public void delete(@PathVariable Long id){
        var entity = this.service.getById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("HelloEntity not found on id = " + id);
        } else {
            this.service.delete(entity.getId());
        }
    }

    @GetMapping("/projects/{ProjectID}/employees")
    public void getEmployeesByProjectID(){

    }


}
