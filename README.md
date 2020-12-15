# Spring Data JDBC Springfield demo

## Prerequisites
- Java 11
- Docker

### Start dependencies
```bash
$ docker-compose up -d
```

## Overview
Source: https://github.com/spring-projects/spring-data-jdbc

Overview: https://spring.io/projects/spring-data-jdbc#overview

Reference documentation: https://docs.spring.io/spring-data/jdbc/docs/current/reference/html

Summary
- Inspired by Domain Driven Design - lots of references to DDD concepts
  (Aggregate, Aggregate root etc.)
- Loading an entity is always done via SQL - no lazy loading, no caching
- Saving an entity requires an explicit operation:
  - [X] repository.save(entity)
  - [ ] ~~entity.setName(...)~~
- A repository's default implementation is located [here](https://docs.spring.io/spring-data/jdbc/docs/current/api/org/springframework/data/jdbc/repository/support/SimpleJdbcRepository.html)
- A repository's methods are transactional by default
  - for reading operations, the `readOnly` flag is set to `true`
- `@Column`, `@Table`, `@Id` - but not from the `javax.persistence` package
  - `@org.springframework.data.relational.core.mapping.Column`
  - `@org.springframework.data.relational.core.mapping.Table`
  - `@org.springframework.data.annotation.Id`
- one-to-many relationships are managed by `@org.springframework.data.relational.core.mapping.MappedCollection`
  - `@org.springframework.data.relational.core.mapping.IdColumn` is best used
    for mapping the opposite end (foreign key) of the relationship
- `@org.springframework.data.annotation.PersistenceConstructor` is a way to tell
Spring Data JDBC to use the annotated constructor when initializing entities

Pros:
- Offers some CRUD functionality out-of-the-box - saves the developer some
  initial time
- Very transparent - most often, what you see is what you get:
  - loading an entity is always done via SQL - no lazy loading, no caching
  - saving
- Enforces good design in some way:
  - discourages from using two-sided relationships
  - the preferred way to manage entities is through an aggregate root -
    e.g. a `task` can be added only through a `project`

Cons:
- Often, a hands-on approach is required, especially when fetching entities
  by their parent
  - if retrieving an aggregate root that has some 'child' entities
    in its domain, the default behavior is [the N+1 select](https://stackoverflow.com/questions/97197/what-is-the-n1-selects-problem-in-orm-object-relational-mapping)
    - an example of this can be showcased in the
      `com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository#findAll()`
      and
      `com.github.dhodja92.springdatajpademo.domain.project.ProjectRepository#findAll(Pageable pageable)`
      repository methods
  - when updating an aggregate root that has some 'child' entities
    in its domain, the default behavior is a `DELETE FROM` SQL statement
    on all the child entities, followed by an `INSERT` for each entity
    - an example of this can be showcased in the
      `com.github.dhodja92.springdatajpademo.domain.project.ProjectRepositoryTests#save_existingProject_shouldUpdateProject`
      test
- You need to be careful how you design your entities
- Many edge cases can pop up during the implementation of the domain
  (model, repository) layer - most of those issues probably require some form
  of manual handling  
- It's still a young library, probably not suited to larger projects
