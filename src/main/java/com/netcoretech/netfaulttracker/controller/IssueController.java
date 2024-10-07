package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public String listIssues(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Issue> issuePage = issueService.getAllIssues(pageable);
        model.addAttribute("issuePage", issuePage);
        return "issues/list";
    }

    @GetMapping("/{id}")
    public String viewIssue(@PathVariable Long id, Model model) {
        issueService.getIssueById(id).ifPresent(issue -> model.addAttribute("issue", issue));
        return "issues/view";
    }

    @GetMapping("/new")
    public String newIssueForm(Model model) {
        model.addAttribute("issue", new Issue());
        return "issues/form";
    }

    @PostMapping
    public String createIssue(@Valid @ModelAttribute Issue issue, BindingResult result) {
        if (result.hasErrors()) {
            return "issues/form";
        }
        issueService.createIssue(issue);
        return "redirect:/issues";
    }

    @GetMapping("/{id}/edit")
    public String editIssueForm(@PathVariable Long id, Model model) {
        issueService.getIssueById(id).ifPresent(issue -> model.addAttribute("issue", issue));
        return "issues/form";
    }

    @PostMapping("/{id}")
    public String updateIssue(@PathVariable Long id, @Valid @ModelAttribute Issue issue, BindingResult result) {
        if (result.hasErrors()) {
            return "issues/form";
        }
        issue.setId(id);
        issueService.updateIssue(issue);
        return "redirect:/issues";
    }

    @PostMapping("/{id}/delete")
    public String deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return "redirect:/issues";
    }

    @GetMapping("/search")
    public String searchIssues(@RequestParam String keyword,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        Page<Issue> issues = issueService.searchIssues(keyword, pageable);
        model.addAttribute("issues", issues);
        model.addAttribute("keyword", keyword);
        return "issues/search-results";
    }
}