package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/issues")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);
    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<Issue>> getAllIssues(Pageable pageable) {
        logger.info("Received GET request to /api/issues");
        Page<Issue> issues = issueService.getAllIssues(pageable);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/list")
    public String listIssues() {
        return "list";  // list.html을 반환
    }

    @GetMapping("/new")
    public String newIssueForm() {
        return "form";
    }

    @GetMapping("/edit")
    public String editIssueForm() {
        return "form";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        logger.info("Received GET request to /api/issues/{}", id);
        return issueService.getIssueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createIssue(@RequestBody Issue issue) {
        logger.info("Received POST request to /api/issues");
        logger.debug("Request body: {}", issue);
        try {
            if (issue.getTitle() == null || issue.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
            }
            if (issue.getStatus() == null) {
                throw new IllegalArgumentException("상태는 필수 입력 항목입니다.");
            }

            Issue createdIssue = issueService.createIssue(issue);
            logger.info("Issue created successfully: {}", createdIssue);
            return ResponseEntity.ok(createdIssue);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while creating issue: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while creating issue", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이슈 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id, @RequestBody Issue issue) {
        logger.info("Received PUT request to /api/issues/{}", id);
        logger.debug("Request body: {}", issue);
        return issueService.getIssueById(id)
                .map(existingIssue -> {
                    issue.setId(id);
                    Issue updatedIssue = issueService.updateIssue(issue);
                    logger.info("Issue updated successfully: {}", updatedIssue);
                    return ResponseEntity.ok(updatedIssue);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        logger.info("Received DELETE request to /api/issues/{}", id);
        return issueService.getIssueById(id)
                .map(issue -> {
                    issueService.deleteIssue(id);
                    logger.info("Issue deleted successfully: {}", id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Page<Issue>> searchIssues(@RequestParam String keyword, Pageable pageable) {
        logger.info("Received GET request to /api/issues/search with keyword: {}", keyword);
        Page<Issue> issues = issueService.searchIssues(keyword, pageable);
        return ResponseEntity.ok(issues);
    }
}
