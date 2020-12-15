package com.github.dhodja92.springdatajpademo.interfaces.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import com.github.dhodja92.springdatajpademo.domain.task.Task;
import java.beans.ConstructorProperties;
import javax.validation.constraints.NotNull;

class CreateProjectTaskRequest {

    @NotNull
    private final String name;

    @NotNull
    private final Label label;

    @NotNull
    private final Priority priority;

    @ConstructorProperties({"name", "label", "priority"})
    CreateProjectTaskRequest(String name, Label label, Priority priority) {
        this.name = name;
        this.label = label;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    Task toTask() {
        return new Task(
                this.name,
                false,
                this.label,
                this.priority
        );
    }
}
