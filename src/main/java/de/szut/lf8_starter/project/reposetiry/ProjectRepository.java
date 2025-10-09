package de.szut.lf8_starter.project.reposetiry;

import de.szut.lf8_starter.project.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
