package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private final IssueService issueService;

    @Autowired
    public IssueRestController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public Page<Issue> getAllIssues(@PageableDefault(size = 10) Pageable pageable) {
        return issueService.getAllIssues(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Issue createIssue(@Valid @RequestBody Issue issue) {
        return issueService.createIssue(issue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id, @Valid @RequestBody Issue issue) {
        if (!issueService.getIssueById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        issue.setId(id);
        return ResponseEntity.ok(issueService.updateIssue(issue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        if (!issueService.getIssueById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        issueService.deleteIssue(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public Page<Issue> searchIssues(@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return issueService.searchIssues(keyword, pageable);
    }
}