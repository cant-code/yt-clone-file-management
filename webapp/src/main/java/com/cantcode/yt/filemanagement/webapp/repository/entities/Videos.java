package com.cantcode.yt.filemanagement.webapp.repository.entities;

import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "VIDEOS")
@EntityListeners(AuditingEntityListener.class)
public class Videos {

    @Id
    @SequenceGenerator(
            name = "VIDEOS_SEQ_GENERATOR",
            sequenceName = "VIDEOS_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(generator = "VIDEOS_SEQ_GENERATOR", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "UID")
    private UUID userId;

    @Column(name = "TITLE", length = 100)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LENGTH")
    private Long length;

    @Column(name = "STATUS", length = 20)
    @Enumerated(EnumType.STRING)
    private TranscodingStatus status;

    @Column(name = "CREATED_AT")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public TranscodingStatus getStatus() {
        return status;
    }

    public void setStatus(TranscodingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Videos videos = (Videos) o;
        return Objects.equals(id, videos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
