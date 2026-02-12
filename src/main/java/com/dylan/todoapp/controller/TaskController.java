package com.dylan.todoapp.controller;

import com.dylan.todoapp.model.Task;
import com.dylan.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/unprotected")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/task")
    public String viewHomePage(Model model, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<Task> taskPage = taskRepository.findAll(pageable);

        model.addAttribute("listTasks", taskPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("newTask", new Task());

        return "unprotected/task";
    }

    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute("newTask") Task task) {
        taskRepository.save(task);
        return "redirect:task";
    }

    @GetMapping("/toggleTask/{id}")
    public String toggleTask(@PathVariable(value = "id") Long id) {
        Task task = taskRepository.findById(id).get();
        task.setCompleted(!task.isCompleted());
        taskRepository.save(task);
        return "redirect:/unprotected/task";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable(value = "id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/unprotected/task";
    }

    @PostMapping("/clearTasks")
    public String clearTasks() {
        taskRepository.deleteAll();
        return "redirect:task";
    }
}