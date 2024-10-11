package com.netcoretech.netfaulttracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    @Column(nullable = false)
    private String title;

    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = true) // nullable 설정 명시적으로 지정
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "severity_id", nullable = true) // nullable 설정 명시적으로 지정
    private Severity severity;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = true) // nullable 설정 명시적으로 지정
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to", nullable = true) // nullable 설정 명시적으로 지정
    private User assignedTo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;


    public enum Status {
        OPEN("접수"), IN_PROGRESS("처리중"), RESOLVED("해결됨"), CLOSED("종료");

        private final String koreanName;

        Status(String koreanName) {
            this.koreanName = koreanName;
        }

        public String getKoreanName() {
            return koreanName;
        }
    }
}
