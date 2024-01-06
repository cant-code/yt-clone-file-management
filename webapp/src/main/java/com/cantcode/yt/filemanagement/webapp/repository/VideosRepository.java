package com.cantcode.yt.filemanagement.webapp.repository;

import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideosRepository extends JpaRepository<Videos, Long> {
}
