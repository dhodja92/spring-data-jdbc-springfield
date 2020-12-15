package com.github.dhodja92.springdatajpademo.interfaces.project;

import com.github.dhodja92.springdatajpademo.domain.project.Project;
import com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping(path = "/api/projects")
public class ProjectResourceController {

    private final ProjectRepository projectRepository;

    private final ProjectResourceAssembler projectResourceAssembler;

    public ProjectResourceController(
            ProjectRepository projectRepository,
            ProjectResourceAssembler projectResourceAssembler
    ) {
        Objects.requireNonNull(projectRepository, "projectRepository must not be null");
        Objects.requireNonNull(projectRepository, "projectResourceAssembler must not be null");
        this.projectRepository = projectRepository;
        this.projectResourceAssembler = projectResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<ProjectResource>> getAllProjects(
            PagedResourcesAssembler<Project> pagedResourcesAssembler,
            Pageable pageable
    ) {
        Page<Project> projects = this.projectRepository.findAll(pageable);
        PagedModel<ProjectResource> resources = pagedResourcesAssembler.toModel(projects, this.projectResourceAssembler);
        return ResponseEntity.ok(resources);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjectResource> getProjectById(@PathVariable UUID id) {
        Project project = this.projectRepository.findById(id);
        if (project == null) {
            throw new NullPointerException();
        }
        ProjectResource resource = this.projectResourceAssembler.toModel(project);
        return ResponseEntity.ok(resource);
    }
}
