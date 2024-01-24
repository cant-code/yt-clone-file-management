package com.cantcode.yt.filemanagement.webapp.repository;

import com.cantcode.yt.filemanagement.webapp.repository.entities.EncodedVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncodedVideosRepository extends JpaRepository<EncodedVideo, Long> {
}
