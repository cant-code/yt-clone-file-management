package com.cantcode.yt.filemanagement.webapp.repository;

import com.cantcode.yt.filemanagement.webapp.repository.entities.RawVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawVideoRepository extends JpaRepository<RawVideo, Long> {
}
