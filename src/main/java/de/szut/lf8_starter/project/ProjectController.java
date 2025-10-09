package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.CreateProjectDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
public class ProjectController {


    @PostMapping
    public ProjectEntity createProject(@RequestBody CreateProjectDto project) {
        return new ProjectEntity();
    }
}
