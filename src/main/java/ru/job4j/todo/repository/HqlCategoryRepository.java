package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HqlCategoryRepository implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Category> findAll() {
        return crudRepository.query("FROM Category ORDER BY id", Category.class);
    }

    public List<Category> findAllById(List<Integer> list) {
        return crudRepository.query("FROM Category WHERE id IN :list ORDER BY id", Category.class, Map.of("list", list));
    }
}
