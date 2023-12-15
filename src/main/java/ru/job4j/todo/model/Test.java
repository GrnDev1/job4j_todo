package ru.job4j.todo.model;

import java.util.Optional;

public class Test {
    public static void main(String[] args) {
        Optional<String> test = Optional.of(null);
        if (test.isPresent()) {
            System.out.println(test.get());
        }
    }
}
