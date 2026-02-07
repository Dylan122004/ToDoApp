package com.dylan.todoapp.controller;

import com.dylan.todoapp.model.Task;
import com.dylan.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Value("${test.status}")
    private String status;

    @GetMapping("/")
    public String home(Model model) {
        var x = new Task();
        x.setDescription("Task 1");
        taskRepository.save(x);

        model.addAttribute("appName", "Railway Demo App");
        model.addAttribute("status", status);
        return "index";
    }

    @Autowired
    private TaskRepository taskRepository;

}
