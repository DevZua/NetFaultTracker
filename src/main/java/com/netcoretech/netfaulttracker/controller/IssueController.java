package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String listIssues(Model model) {
        return "issues/list";
    }

    @GetMapping("/new")
    public String newIssueForm(Model model) {
        model.addAttribute("issue", new Issue());
        return "issues/form";  // form.html로 이동
    }

    @GetMapping("/edit")
    public String editIssueForm(@RequestParam Long id, Model model) {
        issueService.getIssueById(id).ifPresent(issue -> model.addAttribute("issue", issue));
        return "issues/form";
    }

    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<Issue>> getIssues(Pageable pageable, @RequestParam(required = false) String keyword) {
        Page<Issue> issues = (keyword != null && !keyword.isEmpty())
                ? issueService.searchIssues(keyword, pageable)
                : issueService.getAllIssues(pageable);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        return issueService.getIssueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {
        Issue createdIssue = issueService.createIssue(issue);
        return ResponseEntity.ok(createdIssue);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id, @RequestBody Issue issue) {
        return issueService.getIssueById(id)
                .map(existingIssue -> {
                    issue.setId(id);
                    Issue updatedIssue = issueService.updateIssue(issue);
                    return ResponseEntity.ok(updatedIssue);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        return issueService.getIssueById(id)
                .map(issue -> {
                    issueService.deleteIssue(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
