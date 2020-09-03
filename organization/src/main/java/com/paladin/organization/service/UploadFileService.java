package com.paladin.organization.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.security.UserSession;
import com.paladin.framework.security.WebSecurityManager;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.convert.Base64Util;
import com.paladin.organization.service.dto.MergeFileBase64;
import com.paladin.organization.service.dto.UploadFileBase64;
import com.paladin.organization.service.vo.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.HashSet;
import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/9/1
 */
@Service
public class UploadFileService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UploadService uploadService;

    @Value("spring.application.name")
    private String serviceName;

    /**
     * 增加上传文件
     *
     * @param businessClass 业务类
     * @param businessId    业务ID
     * @param uploadFiles   上传文件
     * @return 上传后的文件ID拼接
     */
    public String uploadAttachment(Class<?> businessClass, String businessId, List<UploadFileBase64> uploadFiles, int maxFiles) {
        return uploadAttachment(businessClass, businessId, uploadFiles, null, null, maxFiles);
    }

    /**
     * 增加上传文件到当前文件，并删除被替换文件
     *
     * @param businessClass 业务类
     * @param businessId    业务ID
     * @param uploadFiles   上传文件
     * @param originId      原文件ID
     * @param currentId     当前文件ID
     * @return 替换合并后的文件ID拼接
     */
    public String uploadAttachment(Class<?> businessClass, String businessId, List<UploadFileBase64> uploadFiles, String originId, String currentId, int maxFiles) {

        String[] originIds = StringUtil.isEmpty(originId) ? null : originId.split(",");
        String[] currentIds = StringUtil.isEmpty(currentId) ? null : currentId.split(",");

        // 是否需要请求处理
        boolean need = false;

        if (uploadFiles.size() > 0) {
            need = true;
        } else {
            // 没有新增文件，但是当前文件与原文件不一致则需要发送请求处理不一致情况
            boolean bo = originIds != null && originIds.length > 0;
            boolean bc = currentIds != null && currentIds.length > 0;

            if (bo && bc) {
                if (originIds.length != currentIds.length) {
                    // 长度不同则肯定不一致
                    need = true;
                } else {
                    // 文件个数一致，需要比对文件是否相同
                    HashSet<String> set = new HashSet<>();
                    for (String oid : originIds) {
                        set.add(oid);
                    }

                    for (String cid : currentIds) {
                        if (!set.contains(cid)) {
                            need = true;
                            break;
                        }
                    }
                }
            } else if (bo != bc) {
                need = true;
            }
        }

        if (need) {
            int count = uploadFiles == null ? 0 : uploadFiles.size();
            count += currentIds == null ? 0 : currentIds.length;
            if (count > maxFiles) {
                throw new BusinessException("附件数目超过最大数：" + maxFiles);
            }

            MergeFileBase64 mergeFile = new MergeFileBase64();
            mergeFile.setFromService(serviceName);
            mergeFile.setBusiness(businessClass.getSimpleName());
            mergeFile.setBusinessId(businessId);
            mergeFile.setUploadFiles(uploadFiles);
            mergeFile.setOriginIds(originIds);
            mergeFile.setCurrentIds(currentIds);

            String[] ids = uploadService.uploadFile(mergeFile);
            return join(ids);
        }

        return join(currentIds);
    }

    private String join(String[] ids) {
        if (ids != null && ids.length > 0) {
            StringBuilder sb = new StringBuilder(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                sb.append(",").append(ids[i]);
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * MultipartFile 转换为文件上传对象
     */
    public List<UploadFileBase64> convertFile(boolean needThumbnail, MultipartFile... files) {
        try {
            if (files.length > 0) {
                ArrayList<UploadFileBase64> uploadFiles = new ArrayList<>(files.length);
                for (MultipartFile file : files) {
                    String base64str = Base64Util.encode(file.getBytes());
                    UploadFileBase64 uploadFile = new UploadFileBase64(base64str, file.getOriginalFilename());
                    uploadFile.setNeedThumbnail(needThumbnail);
                    uploadFiles.add(uploadFile);
                }
                return uploadFiles;
            }
        } catch (IOException e) {
            throw new BusinessException("上传文件异常", e);
        }
        return null;
    }

    /**
     * 获取文件资源
     */
    public FileResource getAttachmentResource(String id) {
        if (StringUtil.isNotEmpty(id)) {
            List<FileResource> resources = uploadService.getAttachments(new String[]{id});
            if (resources != null && resources.size() > 0) {
                return resources.get(0);
            }
        }
        return null;
    }

    /**
     * 获取文件资源
     */
    public List<FileResource> getAttachmentResource(String[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return uploadService.getAttachments(ids);
    }


    // 使用Ribbon方式上传MultipartFile参数格式文件（默认Feign不支持该格式）\
    // 暂时不用该方式
    @Deprecated
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

            return restTemplate.postForEntity("http://greenery-upload/upload/attachment/file",
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
