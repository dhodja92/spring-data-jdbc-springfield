package com.github.dhodja92.springdatajpademo.interfaces.task;

import com.github.dhodja92.springdatajpademo.domain.task.Task;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskResourceAssembler extends RepresentationModelAssemblerSupport<Task, TaskResource> {

    public TaskResourceAssembler() {
        super(TaskResourceController.class, TaskResource.class);
    }

    @Override
    public TaskResource toModel(Task entity) {
        TaskResource resource = new TaskResource(
                entity.getId(),
                entity.getName(),
                entity.isFinished(),
                entity.getLabel(),
                entity.getPriority()
        );
        resource.add(linkTo(TaskResourceController.class).withRel(IanaLinkRelations.COLLECTION));
        resource.add(linkTo(methodOn(TaskResourceController.class).getTaskById(entity.getId())).withRel(IanaLinkRelations.SELF));
        return resource;
    }
}
