package com.sq.SYTreeHole.service.publishService;

import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface PublishManagementService {

    boolean insert(Publish publish, HttpServletRequest multipartHttpServletRequest);

    boolean modify(Publish publish, HttpServletRequest multipartHttpServletRequest);

    boolean delete(String publishId, String userId);

    Map<String, Object> countMyPublish(String userId);

    List<Publish> selectMyPublish(String userId, String page, String index);

    List<List<PublishImages>> selectMyPublishImage(List<Publish> publishes);

}
