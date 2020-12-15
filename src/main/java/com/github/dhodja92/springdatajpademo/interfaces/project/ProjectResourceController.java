package com.github.dhodja92.springdatajpademo.interfaces.project;

import com.github.dhodja92.springdatajpademo.domain.project.Project;
import com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    @PostMapping
    public ResponseEntity<Void> addProject(
            @Valid @RequestBody CreateProjectRequest createProjectRequest
    ) {
        Project project = this.projectRepository.save(createProjectRequest.toProject());
        URI location = linkTo(methodOn(ProjectResourceController.class).getProjectById(project.getId())).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjectResource> getProjectById(@PathVariable UUID id) {
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Project not found"));
        ProjectResource resource = this.projectResourceAssembler.toModel(project);
        return ResponseEntity.ok(resource);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProjectResource> updateProjectById(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequest updateProjectRequest
    ) {
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Project not found"));
        Project updatedProject = this.projectRepository.save(updateProjectRequest.toProject(project.getId()));
        ProjectResource resource = this.projectResourceAssembler.toModel(updatedProject);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProjectById(
            @PathVariable UUID id
    ) {
        Project project = this.projectRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Project not found"));
        this.projectRepository.delete(project);
        return ResponseEntity.noContent().build();
    }
}
