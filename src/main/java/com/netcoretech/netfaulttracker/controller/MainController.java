package com.netcoretech.netfaulttracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index() {
        return "index.html";
    }

    @GetMapping(value = {"/list", "/list.html"})
    public String list() {
        return "list.html";
    }

    @GetMapping(value = {"/form", "/form.html"})
    public String form() {
        return "form.html";
    }

    @GetMapping(value = {"/view", "/view.html"})
    public String view() {
        return "view.html";
    }
}