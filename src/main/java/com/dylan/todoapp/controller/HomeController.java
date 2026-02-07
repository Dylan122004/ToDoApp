package com.dylan.todoapp.controller;

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
        // You can pass variables to the HTML here
        model.addAttribute("appName", "Railway Demo App");
        model.addAttribute("status", status);
        return "index"; // This refers to index.html in the templates folder
    }


}
