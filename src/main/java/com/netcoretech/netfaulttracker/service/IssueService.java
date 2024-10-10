package com.netcoretech.netfaulttracker.service;

import com.netcoretech.netfaulttracker.entity.Issue;
import com.netcoretech.netfaulttracker.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    // 최신 순으로 페이지네이션된 이슈 목록 가져오기
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
        issueRepository.deleteById(id);
    }

    // 최신 순으로 검색된 이슈 목록 가져오기
    public Page<Issue> searchIssues(String keyword, Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        return issueRepository.searchIssues(keyword, sortedByCreatedAtDesc);
    }
}
