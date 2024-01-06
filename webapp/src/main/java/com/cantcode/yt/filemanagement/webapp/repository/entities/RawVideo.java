package com.cantcode.yt.filemanagement.webapp.repository.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "RAW_VIDEO")
@EntityListeners(AuditingEntityListener.class)
public class RawVideo {

    @Id
    @SequenceGenerator(
            name = "RAW_VIDEO_SEQ_GENERATOR",
            sequenceName = "RAW_VIDEO_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(generator = "RAW_VIDEO_SEQ_GENERATOR", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "VID")
    private Long videoId;

    @Column(name = "LINK")
    private String link;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "CREATED_AT")
    @CreatedDate
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
