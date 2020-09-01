package com.paladin.organization.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.security.UserSession;
import com.paladin.framework.security.WebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 使用Ribbon方式上传MultipartFile参数格式文件（默认Feign不支持该格式）
 *
 * @author TontoZhou
 * @since 2020/9/1
 */
@Service
public class UploadMultipartFileService {

    @Autowired
    private RestTemplate restTemplate;

    public String uploadFile(MultipartFile file) {
        try {
            ByteArrayResource resource = new MultipartFileResource(file);

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);

            HttpHeaders headers = new HttpHeaders();

            ArrayList<MediaType> acceptMediaTypes = new ArrayList<>();
            acceptMediaTypes.add(MediaType.APPLICATION_JSON);
            headers.setAccept(acceptMediaTypes);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            UserSession userSession = WebSecurityManager.getCurrentUserSession();
            headers.set(WebSecurityManager.HEADER_USER_ID, userSession.getUserId());
            headers.set(WebSecurityManager.HEADER_USER_TYPE, userSession.getUserType());


            HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.postForEntity("http://greenery-upload/upload/attachment/upload/file",
                    requestEntity, String.class).getBody();
        } catch (Exception e) {
            throw new BusinessException("上传文件失败", e);
        }
    }


    public static class MultipartFileResource extends ByteArrayResource {

        private String filename;

        public MultipartFileResource(MultipartFile multipartFile) throws IOException {
            super(multipartFile.getBytes());
            this.filename = multipartFile.getOriginalFilename();
        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }

}
