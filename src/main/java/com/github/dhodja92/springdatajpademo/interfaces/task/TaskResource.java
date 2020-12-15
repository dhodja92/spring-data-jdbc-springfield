package com.github.dhodja92.springdatajpademo.interfaces.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import org.springframework.hateoas.RepresentationModel;
import java.util.UUID;

public final class TaskResource extends RepresentationModel<TaskResource> {

    private final UUID id;

    private final String name;

    private final boolean finished;

    private final Label label;

    private final Priority priority;

    public TaskResource(UUID id, String name, boolean finished, Label label, Priority priority) {
        this.id = id;
        this.name = name;
        this.finished = finished;
        this.label = label;
        this.priority = priority;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public Label getLabel() {
        return this.label;
    }

    public Priority getPriority() {
        return this.priority;
    }
}
