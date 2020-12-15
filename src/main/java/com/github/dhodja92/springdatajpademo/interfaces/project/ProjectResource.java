package com.github.dhodja92.springdatajpademo.interfaces.project;

import org.springframework.hateoas.RepresentationModel;
import java.util.UUID;

public final class ProjectResource extends RepresentationModel<ProjectResource> {

    private final UUID id;

    private final String name;

    private final String color;

    public ProjectResource(UUID id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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
}
