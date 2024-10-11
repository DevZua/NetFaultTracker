package com.netcoretech.netfaulttracker.repository;

import com.netcoretech.netfaulttracker.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    Optional<Issue> findByTitle(String title);

    Page<Issue> findAll(Pageable pageable);

    // JPA 메소드 쿼리를 통한 부분 검색 기능 구현
    Page<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrStatusContainingIgnoreCase(
            String title, String description, String status, Pageable pageable);
}
