package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.model.PageModel;
import com.cantcode.yt.filemanagement.webapp.model.VideoListResponse;
import com.cantcode.yt.filemanagement.webapp.repository.VideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import com.cantcode.yt.filemanagement.webapp.service.mappers.VideoMapper;
import com.cantcode.yt.filemanagement.webapp.service.spi.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.cantcode.yt.filemanagement.webapp.utils.StreamingConstants.PROCESSED_STATUS;

@Service
public class VideoServiceImpl implements VideoService {

    private final VideoMapper videoMapper;
    private final VideosRepository videosRepository;

    public VideoServiceImpl(final VideoMapper videoMapper, final VideosRepository videosRepository) {
        this.videoMapper = videoMapper;
        this.videosRepository = videosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public VideoListResponse getVideoPage(final PageModel pageModel) {
        final Page<Videos> videos = videosRepository.findAllByStatusIn(PROCESSED_STATUS, getPageRequest(pageModel));
        return videoMapper.videoPageToVideoList(videos);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoListResponse getVideoPageForUser(String userId, final PageModel pageModel) {
        final Page<Videos> videos = videosRepository.findAllByUserId(UUID.fromString(userId), getPageRequest(pageModel));
        return videoMapper.videoPageToVideoList(videos);
    }

    private PageRequest getPageRequest(PageModel pageModel) {
        return PageRequest.of(pageModel.getPage(), pageModel.getSize(),
                Sort.by("createdAt").descending().and(Sort.by("id").descending()));
    }
}
