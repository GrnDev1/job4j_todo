package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskDtoService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {
    final private TaskService taskService;
    final private TaskDtoService taskDtoService;
    final private PriorityService priorityService;
    final private CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskDtoService.findAll());
        model.addAttribute("key", "All");
        return "tasks/list";
    }

    @GetMapping("/completed")
    public String getDone(Model model) {
        model.addAttribute("tasks", taskDtoService.findByDone());
        model.addAttribute("key", "Completed");
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model) {
        model.addAttribute("tasks", taskDtoService.findByNew());
        model.addAttribute("key", "New");
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession session, @RequestParam List<Integer> categoriesId) {
        task.setCategories(categoryService.findAllById(categoriesId));
        task.setUser((User) session.getAttribute("user"));
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskDtoOptional = taskDtoService.findById(id);
        if (taskDtoOptional.isEmpty()) {
            model.addAttribute("message", "Task with this id is not found");
            return "errors/404";
        }
        model.addAttribute("task", taskDtoOptional.get());
        return "tasks/one";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Task with this id is not found");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model, @RequestParam List<Integer> categoriesId) {
        task.setCategories(categoryService.findAllById(categoriesId));
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Task with this id is not found");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/complete/{id}")
    public String completeTask(Model model, @PathVariable int id) {
        var isUpdated = taskService.completeTask(id);
        if (!isUpdated) {
            model.addAttribute("message", "Task with this id is not found");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Task with this id is not found");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
