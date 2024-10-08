package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/")
    public String home() {
        logger.info("Accessing home page");
        return "redirect:/issues";
    }

    @GetMapping("/issues")
    public String listIssues(Model model, @PageableDefault(size = 10) Pageable pageable) {
        logger.info("Accessing list issues page");
        Page<Issue> issuePage = issueService.getAllIssues(pageable);
        model.addAttribute("issuePage", issuePage);
        return "issues/list";
    }

    @GetMapping("/issues/new")
    public String newIssueForm(Model model) {
        logger.info("Accessing new issue form");
        model.addAttribute("issue", new Issue());
        return "issues/form";
    }

    @PostMapping("/issues")
    public String createIssue(@ModelAttribute Issue issue) {
        logger.info("Creating new issue");
        issueService.createIssue(issue);
        return "redirect:/issues";
    }

    @GetMapping("/issues/{id}")
    public String viewIssue(@PathVariable Long id, Model model) {
        logger.info("Viewing issue with id: {}", id);
        issueService.getIssueById(id).ifPresent(issue -> model.addAttribute("issue", issue));
        return "issues/view";
    }

    @GetMapping("/issues/{id}/edit")
    public String editIssueForm(@PathVariable Long id, Model model) {
        logger.info("Editing issue with id: {}", id);
        issueService.getIssueById(id).ifPresent(issue -> model.addAttribute("issue", issue));
        return "issues/form";
    }

    @PostMapping("/issues/{id}")
    public String updateIssue(@PathVariable Long id, @ModelAttribute Issue issue) {
        logger.info("Updating issue with id: {}", id);
        issue.setId(id);
        issueService.updateIssue(issue);
        return "redirect:/issues";
    }

    @PostMapping("/issues/{id}/delete")
    public String deleteIssue(@PathVariable Long id) {
        logger.info("Deleting issue with id: {}", id);
        issueService.deleteIssue(id);
        return "redirect:/issues";
    }

    @GetMapping("/issues/search")
    public String searchIssues(@RequestParam String keyword, Model model, @PageableDefault(size = 10) Pageable pageable) {
        logger.info("Searching issues with keyword: {}", keyword);
        Page<Issue> issuePage = issueService.searchIssues(keyword, pageable);
        model.addAttribute("issuePage", issuePage);
        model.addAttribute("keyword", keyword);
        return "issues/search-results";
    }
}