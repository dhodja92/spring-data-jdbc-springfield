package com.github.dhodja92.springdatajpademo.interfaces.project;

import com.github.dhodja92.springdatajpademo.domain.project.Project;
import java.beans.ConstructorProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

final class CreateProjectRequest {

    @NotBlank
    private final String name;

    @NotNull
    private final String color;

    @ConstructorProperties({"name", "color"})
    CreateProjectRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    Project toProject() {
        return new Project(this.name, this.color, null);
    }
}
