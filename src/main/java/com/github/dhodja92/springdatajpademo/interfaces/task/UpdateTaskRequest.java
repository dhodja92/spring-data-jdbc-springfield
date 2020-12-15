package com.github.dhodja92.springdatajpademo.interfaces.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import com.github.dhodja92.springdatajpademo.domain.task.Task;
import java.beans.ConstructorProperties;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

final class UpdateTaskRequest {

    @NotEmpty
    private final String name;

    @NotNull
    private final Boolean finished;

    @NotNull
    private final Label label;

    @NotNull
    private final Priority priority;

    @ConstructorProperties({"name", "finished", "label", "priority"})
    UpdateTaskRequest(String name, Boolean finished, Label label, Priority priority) {
        this.name = name;
        this.finished = finished;
        this.label = label;
        this.priority = priority;
    }

    Task toTask(UUID id) {
        return new Task(id, this.name, this.finished, this.label, this.priority);
    }
}
