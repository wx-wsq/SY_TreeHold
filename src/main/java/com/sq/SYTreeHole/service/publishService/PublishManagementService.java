package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.Serializable;
import java.util.List;

public interface PublishManagementService {

    boolean insert(Publish publish, MultipartHttpServletRequest multipartHttpServletRequest);

    boolean modify(Publish publish, MultipartHttpServletRequest multipartHttpServletRequest);

    boolean delete(String publishId, String userId);

    List<Publish> selectMyPublish(String userId, String page);

    List<List<PublishImages>> selectMyPublishImage(List<Publish> publishes);

}
