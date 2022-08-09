package com.steam_distillery.web;

import com.steam_distillery.model.Category;
import com.steam_distillery.repository.CategoryRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CategoryResource {

    private final CategoryRepository repository;

    public CategoryResource(CategoryRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    Set<String> categories() {
        return repository.findAll().stream().map(Category::getDescription).collect(Collectors.toSet());
    }
}
