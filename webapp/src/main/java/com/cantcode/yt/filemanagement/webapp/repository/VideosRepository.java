package com.cantcode.yt.filemanagement.webapp.repository;

import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideosRepository extends JpaRepository<Videos, Long> {

    Page<Videos> findAllByStatusIn(List<TranscodingStatus> statuses, PageRequest pageRequest);
}
