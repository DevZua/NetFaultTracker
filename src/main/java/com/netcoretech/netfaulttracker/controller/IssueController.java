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

    @GetMapping("/list")
    public String listIssues(Model model) {
        return "list.html";  // 이슈 목록 페이지
    }

    @GetMapping("/new")
    public String newIssueForm(Model model) {
        return "redirect:/form.html";  // 정적 리소스로 이동
    }


    @GetMapping("/edit")
    public String editIssueForm(@RequestParam Long id, Model model) {
        issueService.getIssueById(id).ifPresent(issue -> model.addAttribute("issue", issue));
        return "form.html";  // 이슈 수정 페이지
    }

    @PostMapping("/api/issues")
    @ResponseBody
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {
        Issue createdIssue = issueService.createIssue(issue);
        return ResponseEntity.ok(createdIssue);
    }

    @PutMapping("/api/issues/{id}")
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

    @DeleteMapping("/api/issues/{id}")
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
