package com.cantcode.yt.filemanagement.webapp.repository;

import com.cantcode.yt.filemanagement.webapp.repository.entities.EncodedVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EncodedVideosRepository extends JpaRepository<EncodedVideo, Long> {

    Optional<EncodedVideo> findByVideoIdAndQuality(Long id, String quality);
}
