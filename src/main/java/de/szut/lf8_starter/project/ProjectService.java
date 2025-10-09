package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.reposetiry.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

}
