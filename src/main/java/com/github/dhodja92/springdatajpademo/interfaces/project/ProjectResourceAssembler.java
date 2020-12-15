package com.github.dhodja92.springdatajpademo.interfaces.project;

import com.github.dhodja92.springdatajpademo.domain.project.Project;
import com.github.dhodja92.springdatajpademo.interfaces.task.ProjectTaskResourceController;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectResourceAssembler extends RepresentationModelAssemblerSupport<Project, ProjectResource> {

    public ProjectResourceAssembler() {
        super(ProjectResourceController.class, ProjectResource.class);
    }

    @Override
    public ProjectResource toModel(Project entity) {
        ProjectResource resource = new ProjectResource(
                entity.getId(),
                entity.getName(),
                entity.getColor()
        );
        resource.add(linkTo(ProjectResourceController.class).withRel(IanaLinkRelations.COLLECTION));
        resource.add(linkTo(methodOn(ProjectTaskResourceController.class).getProjectTasks(entity.getId(), null, null))
                .withRel("tasks"));
        resource.add(linkTo(methodOn(ProjectResourceController.class).getProjectById(entity.getId())).withRel(IanaLinkRelations.SELF));
        return resource;
    }
}
