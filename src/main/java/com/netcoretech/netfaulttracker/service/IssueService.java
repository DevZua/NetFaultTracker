package com.netcoretech.netfaulttracker.service;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.repository.IssueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IssueService {

    private static final Logger logger = LoggerFactory.getLogger(IssueService.class);
    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public Page<Issue> getAllIssues(Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        return issueRepository.findAll(sortedByCreatedAtDesc);
    }

    public Optional<Issue> getIssueById(Long id) {
        return issueRepository.findById(id);
    }

    public Issue createIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public Issue updateIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public void deleteIssue(Long id) {
        issueRepository.findById(id).ifPresentOrElse(issue -> {
            // 이슈 삭제 시 연관된 엔티티도 삭제
            issueRepository.delete(issue);
            logger.info("이슈(ID: {})가 성공적으로 삭제되었습니다.", id);
        }, () -> {
            throw new EmptyResultDataAccessException("삭제할 이슈가 존재하지 않습니다. ID: " + id, 1);
        });
    }


    public Page<Issue> searchIssues(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = ""; // 기본값 설정
        }
        Pageable sortedByCreatedAtDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        return issueRepository.searchIssues(keyword, sortedByCreatedAtDesc);
    }

    public Optional<Issue> findByTitle(String title) {
        return issueRepository.findByTitle(title);
    }
}
