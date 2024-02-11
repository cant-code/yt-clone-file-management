package com.cantcode.yt.filemanagement.webapp.service.mappers;

import com.cantcode.yt.filemanagement.webapp.model.VideoDetail;
import com.cantcode.yt.filemanagement.webapp.model.VideoListResponse;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface VideoMapper {

    @Mapping(source = "totalElements", target = "page.total")
    @Mapping(source = "size", target = "page.size")
    @Mapping(source = "content", target = "videoDetails")
    VideoListResponse videoPageToVideoList(Page<Videos> videos);

    VideoDetail videoToVideoDetail(Videos videos);
}
