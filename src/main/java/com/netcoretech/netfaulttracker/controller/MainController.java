package com.netcoretech.netfaulttracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index() {
        return "index";  // index.html 파일을 반환
    }

    @GetMapping(value = {"/list", "/list.html"})
    public String list() {
        return "list";  // list.html 파일을 반환
    }

    @GetMapping(value = {"/form", "/form.html"})
    public String form() {
        return "form";  // form.html 파일을 반환
    }

    @GetMapping(value = {"/view", "/view.html"})
    public String view() {
        return "view";  // view.html 파일을 반환
    }
}
