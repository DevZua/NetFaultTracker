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

import org.springframework.data.domain.Sort;

@Service
public class IssueService {

    private static final Logger logger = LoggerFactory.getLogger(IssueService.class);
    private final IssueRepository issueRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    // 최신순으로 이슈 목록을 가져오는 메소드
    public Page<Issue> getAllIssues(Pageable pageable) {
        // 최신순으로 정렬된 페이징 요청 생성
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        return issueRepository.findAll(sortedByCreatedAtDesc);
    }

    public Page<Issue> searchIssues(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = ""; // 기본값 설정
        }
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        return issueRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrStatusContainingIgnoreCase(
                keyword, keyword, keyword, sortedByCreatedAtDesc);
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
            issueRepository.delete(issue);
            logger.info("이슈(ID: {})가 성공적으로 삭제되었습니다.", id);
        }, () -> {
            throw new EmptyResultDataAccessException("삭제할 이슈가 존재하지 않습니다. ID: " + id, 1);
        });
    }

    public Optional<Issue> findByTitle(String title) {
        return issueRepository.findByTitle(title);
    }
}
