package com.paladin.upload.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.security.WebSecurityManager;
import com.paladin.framework.service.Condition;
import com.paladin.framework.service.FileStoreService;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.convert.DateFormatUtil;
import com.paladin.upload.model.UploadAttachment;
import com.paladin.upload.service.dto.FileCreateParam;
import com.paladin.upload.service.dto.FileFrom;
import com.paladin.upload.service.dto.MergeFileBase64;
import com.paladin.upload.service.dto.UploadFileBase64;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UploadAttachmentService extends ServiceSupport<UploadAttachment> {

    // 单位M
    @Value("${attachment.max-file-size}")
    private int maxFileSize;

    // 附件删除后保留时间，默认10天，
    @Value("${attachment.delete-expire-day}")
    private int expireDay;

    private int maxFileNameSize = 100;
    private int maxFileByteSize;

    @Autowired
    private FileStoreService fileStoreService;

    @PostConstruct
    protected void initialize() {
        if (maxFileSize <= 0) {
            maxFileSize = 10;
        }
        maxFileByteSize = maxFileSize * 1024 * 1024;
    }


    /**
     * 图片限制参数,如果不想限制可以设置大点
     */
    private double max_picture_size = 2 * 1024 * 1024;
    private int max_thumbnail_width = 600;
    private int max_thumbnail_height = 600;
    private int min_thumbnail_width = 200;
    private int min_thumbnail_height = 200;

    /**
     * 创建图片与缩略图，如果开启限制图片，过大图片会被压缩，压缩策略为根据图片大小与基准大小比例作为缩放大小进行缩放
     *
     * @param param 文件创建参数
     */
    public UploadAttachment createPictureAndThumbnail(FileCreateParam param, FileFrom fileFrom) {
        long size = param.getSize();

        if (size > max_picture_size) {
            double scale = max_picture_size / size;
            scale = Math.sqrt(scale) * 0.8;
            param.setScale(scale);
        }

        Integer thumbnailHeight = param.getThumbnailHeight();
        Integer thumbnailWidth = param.getThumbnailWidth();

        if (thumbnailHeight == null) {
            thumbnailHeight = min_thumbnail_height;
        }

        if (thumbnailWidth == null) {
            thumbnailWidth = min_thumbnail_width;
        }

        param.setThumbnailHeight(Math.min(thumbnailHeight, max_thumbnail_height));
        param.setThumbnailWidth(Math.min(thumbnailWidth, max_thumbnail_width));
        param.setNeedThumbnail(true);
        return createFile(param, fileFrom);
    }

    /**
     * 创建文件附件
     */
    public UploadAttachment createFile(FileCreateParam param, FileFrom fileFrom) {
        if (param.getSize() > maxFileByteSize) {
            throw new BusinessException("上传文件不能大于" + maxFileSize + "MB");
        }
        UploadAttachment attachment = new UploadAttachment();
        attachment.setId(UUIDUtil.createUUID());
        attachment.setSize(param.getSize());
        attachment.setCreateTime(new Date());
        attachment.setDeleted(false);
        attachment.setFromService(fileFrom.getFromService());
        attachment.setBusiness(fileFrom.getBusiness());
        attachment.setBusinessId(fileFrom.getBusinessId());

        String filename = param.getFilename();
        if (filename != null && filename.length() > 0) {
            int i = filename.lastIndexOf(".");
            if (i >= 0) {
                attachment.setSuffix(filename.substring(i));
                attachment.setName(filename.substring(0, i));
            } else {
                attachment.setName(filename);
            }
        } else {
            throw new BusinessException("文件名不能为空");
        }

        filename = attachment.getName();
        if (filename.length() > maxFileNameSize) {
            attachment.setName(filename.substring(0, maxFileNameSize));
        }

        try {
            saveFile(param, attachment, null);
        } catch (IOException e) {
            throw new BusinessException("保存图片文件失败", e);
        }

        attachment.setStoreType(fileStoreService.getStoreType());

        String creator = WebSecurityManager.getCurrentUserSession().getUserId();
        attachment.setCreateBy(creator);

        save(attachment);
        return attachment;
    }

    /**
     * 保存文件附件
     */
    private UploadAttachment saveFile(FileCreateParam param, UploadAttachment attachment, String subPath) throws IOException {
        String filename = attachment.getId();
        String suffix = attachment.getSuffix();
        if (suffix != null) {
            filename += suffix;
        }

        if (subPath == null || subPath.length() == 0) {
            subPath = DateFormatUtil.getThreadSafeFormat("yyyyMMdd").format(new Date());
        }

        fileStoreService.checkAndMakeDirectory(subPath);

        String relativePath = subPath + "/" + filename;
        attachment.setRelativePath(relativePath);

        boolean created = false;
        InputStream input = param.getInput();

        // 如果是图片类型，需要查看图片相关处理参数
        if (param.isNeedThumbnail()) {
            Integer width = param.getWidth();
            Integer height = param.getHeight();
            Double scale = param.getScale();
            Double quality = param.getQuality();
            Integer thumbnailWidth = param.getThumbnailWidth();
            Integer thumbnailHeight = param.getThumbnailHeight();

            // 根据以下两个字段创建缩略图，并且缩略图是建立在原图基础上的，而不是改变过质量的原图
            // 如需要修改缩略图质量和规模，可加入参数
            if (thumbnailWidth != null && thumbnailHeight != null) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int len;
                while ((len = input.read(buffer)) > -1) {
                    output.write(buffer, 0, len);
                }

                // 创建缩略图
                input = new ByteArrayInputStream(output.toByteArray());


                output = new ByteArrayOutputStream();
                Thumbnails.of(input).size(thumbnailWidth, thumbnailHeight).toOutputStream(output);

                input = new ByteArrayInputStream(output.toByteArray());

                String thumbnailName = "t_" + filename;
                String thumbnailRelativePath = subPath + "/" + thumbnailName;

                fileStoreService.storeFile(input, subPath, thumbnailName);

                attachment.setThumbnailRelativePath(thumbnailRelativePath);
                input.reset();
            }

            // 根据以下字段修改原图
            if ((width != null && height != null) || scale != null || quality != null) {
                // 有修改图片则使用缩放图片工具
                boolean changed = false;
                Builder<? extends InputStream> builder = Thumbnails.of(input);
                if (width != null && height != null) {
                    builder.size(width, height);
                    changed = true;
                }

                if (scale != null) {
                    builder.scale(scale, scale);
                    changed = true;
                }

                if (quality != null) {
                    if (!changed) {
                        // 如果没有设置size或缩放但是要压缩质量，则缩放原大小
                        builder.scale(1f);
                    }
                    builder.outputQuality(quality);
                }


                ByteArrayOutputStream output = new ByteArrayOutputStream();
                builder.toOutputStream(output);

                input = new ByteArrayInputStream(output.toByteArray());
                fileStoreService.storeFile(input, subPath, filename);
                created = true;
            }
        }

        if (!created) {
            fileStoreService.storeFile(input, subPath, filename);
        }

        return attachment;
    }


    /**
     * 获取文件附件记录
     */
    public List<UploadAttachment> getAttachments(String... ids) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<>();
        }
        return searchAll(new Condition(UploadAttachment.FIELD_ID, QueryType.IN, Arrays.asList(ids)));
    }


    /**
     * 替换和合并附件
     * <p>
     * 会删除附件，所以需要调用该方法时需要考虑是否要添加事务
     */
    @Transactional
    public List<UploadAttachment> mergeAttachments(MergeFileBase64 mergeFile) {

        // 当前有效附件
        String[] currentIds = mergeFile.getCurrentIds();

        List<UploadAttachment> currentAttList = (currentIds != null && currentIds.length > 0) ?
                getAttachments(currentIds) : new ArrayList<>();


        // 被替换需要删除附件
        ArrayList<String> deleteIdList = new ArrayList<>();
        String[] originIds = mergeFile.getOriginIds();
        if (originIds != null && originIds.length > 0) {
            for (String oid : originIds) {
                boolean del = true;
                for (UploadAttachment att : currentAttList) {
                    if (att.getId().equals(oid)) {
                        del = false;
                        break;
                    }
                }
                if (del) {
                    deleteIdList.add(oid);
                }
            }

            if (deleteIdList.size() > 0) {
                deleteAttachments(deleteIdList.toArray(new String[deleteIdList.size()]));
            }
        }

        // 上传附件
        List<UploadFileBase64> uploadFiles = mergeFile.getUploadFiles();
        if (uploadFiles != null) {
            for (UploadFileBase64 file : uploadFiles) {
                if (file != null) {
                    FileCreateParam param = new FileCreateParam(file.getBase64str(), file.getFilename());
                    if (file.isNeedThumbnail()) {
                        param.setThumbnailWidth(file.getThumbnailWidth());
                        param.setThumbnailHeight(file.getThumbnailHeight());
                        currentAttList.add(createPictureAndThumbnail(param, mergeFile));
                    } else {
                        currentAttList.add(createFile(param, mergeFile));
                    }
                }
            }
        }

        return currentAttList;
    }

    /**
     * 删除附件
     */
    public int deleteAttachments(String... ids) {
        if (ids != null && ids.length > 0) {
            Example example = buildOrCreateExample(new Condition(UploadAttachment.FIELD_ID, QueryType.IN, Arrays.asList(ids)), modelType, false);
            UploadAttachment attachment = new UploadAttachment();
            attachment.setDeleted(true);
            attachment.setDeleteTime(new Date());
            return getSqlMapper().updateByExampleSelective(attachment, example);
        }
        return 0;
    }


    /**
     * 清理删除的附件文件
     */
    public int cleanAttachmentFile() {
        int count = 0;
        List<UploadAttachment> list = searchAll(
                new Condition[]{
                        new Condition(UploadAttachment.FIELD_DELETED, QueryType.EQUAL, true),
                        new Condition(UploadAttachment.FIELD_DELETE_TIME, QueryType.LESS_THAN, new Date(System.currentTimeMillis() - (expireDay * 60L * 60 * 24 * 1000))),
                },
                UploadAttachment.class, true);

        for (UploadAttachment att : list) {
            String id = att.getId();
            if (deleteFile(id, att.getRelativePath()) &&
                    deleteFile(id, att.getThumbnailRelativePath())) {
                getSqlMapper().deleteByPrimaryKey(id);
                count++;
            }
        }

        return count;
    }

    private boolean deleteFile(String id, String filePath) {
        try {
            if (StringUtil.isNotEmpty(filePath)) {
                int i = filePath.lastIndexOf('/');
                if (i >= 0) {
                    fileStoreService.deleteFile(filePath.substring(0, i), filePath.substring(i + 1));
                } else {
                    fileStoreService.deleteFile("", filePath);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("删除附件失败[id:" + id + ",path:" + filePath + "]");
        }
        return false;
    }

}