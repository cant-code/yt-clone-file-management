package com.cantcode.yt.filemanagement.webapp.repository.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ENCODED_VIDEOS")
@EntityListeners(AuditingEntityListener.class)
public class EncodedVideo {

    @Id
    @SequenceGenerator(
            name = "ENCODED_VIDEOS_SEQ_GENERATOR",
            sequenceName = "ENCODED_VIDEOS_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(generator = "ENCODED_VIDEOS_SEQ_GENERATOR", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "VID")
    private Long videoId;

    @Column(name = "QUALITY")
    private String quality;

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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncodedVideo that = (EncodedVideo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
