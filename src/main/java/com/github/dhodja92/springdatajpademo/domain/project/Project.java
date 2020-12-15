package com.github.dhodja92.springdatajpademo.domain.project;

import com.github.dhodja92.springdatajpademo.domain.task.Task;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Table("project")
public class Project {

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("color")
    private String color;

    @MappedCollection(idColumn = "project_id")
    private Set<Task> tasks;

    public Project(String name, String color, Set<Task> tasks) {
        this.name = name;
        this.color = color;
        this.tasks = tasks;
    }

    @PersistenceConstructor
    public Project(UUID id, String name, String color, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.tasks = tasks;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Set<Task> getTasks() {
        return Collections.unmodifiableSet(this.tasks);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || this.getClass() != o.getClass()) { return false; }
        Project project = (Project) o;
        return Objects.equals(this.id, project.id);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", color=" + this.color +
                ", tasks=" + this.tasks +
                '}';
    }
}
