package com.github.dhodja92.springdatajpademo.domain.task;

import com.github.dhodja92.springdatajpademo.domain.label.Label;
import com.github.dhodja92.springdatajpademo.domain.priority.Priority;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.util.Objects;
import java.util.UUID;

@Table("task")
public class Task {

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("finished")
    private boolean finished;

    @Column("label")
    private Label label;

    @Column("priority")
    private Priority priority;

    public Task(String name, boolean finished, Label label, Priority priority) {
        this.name = name;
        this.finished = finished;
        this.label = label;
        this.priority = priority;
    }

    @PersistenceConstructor
    public Task(UUID id, String name, boolean finished, Label label, Priority priority) {
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

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || this.getClass() != o.getClass()) { return false; }
        Task task = (Task) o;
        return Objects.equals(this.id, task.id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", finished=" + this.finished +
                ", label=" + this.label +
                ", priority=" + this.priority +
                '}';
    }
}
