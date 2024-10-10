package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        logger.info("이슈 목록 조회 요청을 받았습니다.");
        Page<Issue> issues = issueService.getAllIssues(pageable);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        logger.info("ID가 {}인 이슈 조회 요청을 받았습니다.", id);
        Optional<Issue> issue = issueService.getIssueById(id);
        return issue.map(ResponseEntity::ok).orElseGet(() -> {
            logger.info("ID '{}'에 해당하는 이슈를 찾을 수 없습니다.", id);
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Issue> createIssue(@RequestBody Issue newIssue) {
        logger.info("새로운 이슈 생성 요청을 받았습니다: {}", newIssue.getTitle());
        Issue createdIssue = issueService.createIssue(newIssue);
        logger.info("이슈가 성공적으로 생성되었습니다: {}", createdIssue);
        return ResponseEntity.ok(createdIssue);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Issue> updateIssueById(@PathVariable Long id, @RequestBody Issue updatedIssue) {
        logger.info("ID '{}'으로 이슈 수정 요청을 받았습니다.", id);

        Optional<Issue> existingIssue = issueService.getIssueById(id);
        if (existingIssue.isPresent()) {
            Issue issue = existingIssue.get();
            issue.setTitle(updatedIssue.getTitle());
            issue.setStatus(updatedIssue.getStatus());
            issue.setDescription(updatedIssue.getDescription());

            Issue savedIssue = issueService.updateIssue(issue);
            logger.info("이슈가 성공적으로 수정되었습니다: {}", savedIssue);
            return ResponseEntity.ok(savedIssue);
        } else {
            logger.info("ID '{}'에 해당하는 이슈를 찾을 수 없습니다.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteIssueById(@PathVariable Long id) {
        logger.info("ID '{}'인 이슈 삭제 요청을 받았습니다.", id);

        Optional<Issue> issue = issueService.getIssueById(id);
        if (issue.isPresent()) {
            issueService.deleteIssue(id);  // ID로 삭제
            logger.info("이슈가 성공적으로 삭제되었습니다: {}", id);
            return ResponseEntity.ok().build();
        } else {
            logger.info("ID '{}'에 해당하는 이슈를 찾을 수 없습니다.", id);
            return ResponseEntity.notFound().build();
        }
    }
}
