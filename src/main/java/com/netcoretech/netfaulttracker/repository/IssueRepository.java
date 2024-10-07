package com.netcoretech.netfaulttracker.repository;

import com.netcoretech.netfaulttracker.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Page<Issue> findAll(Pageable pageable);

    @Query("SELECT i FROM Issue i WHERE " +
            "LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(i.status) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Issue> searchIssues(String search, Pageable pageable);
}