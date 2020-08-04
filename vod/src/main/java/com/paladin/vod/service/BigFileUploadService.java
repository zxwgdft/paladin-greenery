package com.paladin.vod.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.vod.service.dto.UploadFileDTO;
import com.paladin.vod.service.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Slf4j
@Service
public class BigFileUploadService {

    // 文件上传存放文件夹
    @Value("${vod.upload.folder}")
    private String targetFolder;

    // 单位M
    @Value("${vod.upload.chuck-size:5}")
    private long chunkSize;


    @PostConstruct
    private void initialize() {
        if (!targetFolder.endsWith("/")) {
            targetFolder += "/";
        }

        // M转字节大小
        chunkSize = chunkSize * 1024 * 1024;

        // 创建目录
        Path root = Paths.get(targetFolder);
        try {
            Files.createDirectories(root);
            log.info("大文件存放目录：" + targetFolder);
        } catch (Exception e) {
            log.error("创建大文件存放目录异常[" + targetFolder + "]", e);
        }
    }

    private Map<String, BigFileUploader> bigFileUploaderMap = new HashMap<>();

    public BigFileUploader getOrCreateUploader(String id, int chunkCount, String fileName) {
        BigFileUploader uploader = bigFileUploaderMap.get(id);
        if (uploader == null) {
            synchronized (bigFileUploaderMap) {
                uploader = bigFileUploaderMap.get(id);
                if (uploader == null) {
                    uploader = new BigFileUploader(id, chunkCount, chunkSize, targetFolder, fileName);
                    bigFileUploaderMap.put(id, uploader);
                }
            }
        }

        return uploader;
    }

    public boolean checkFileChunk(String id, int chunkIndex) {
        BigFileUploader uploader = bigFileUploaderMap.get(id);
        if (uploader != null) {
            return uploader.checkFileChunk(chunkIndex);
        }
        return false;
    }

    public int uploadFileChunk(String id, int chunkIndex, byte[] data) {
        BigFileUploader uploader = bigFileUploaderMap.get(id);
        if (uploader != null) {
            return uploader.uploadFileChunk(chunkIndex, data);
        }
        return BigFileUploader.UPLOAD_REUPLOAD;
    }


    /**
     * 查找所有文件
     *
     * @return
     */
    public List<FileVO> findAllFiles() {

        Path root = Paths.get(targetFolder);
        List<FileVO> videos = new ArrayList<>();

        try {
            Files.walkFileTree(root, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    if (!file.toString().endsWith("temp")) {

                        String relativePath = file.toString();

                        relativePath = relativePath.substring(targetFolder.length() - 1);

                        FileVO video = new FileVO();
                        video.setName(relativePath);
                        video.setRelativePath(relativePath);
                        video.setLastUpdateTime(Files.getLastModifiedTime(file).toMillis());
                        video.setSize(Files.size(file));

                        videos.add(video);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new BusinessException("查找文件失败");
        }

        return videos;
    }


    /**
     * 删除文件
     *
     * @param relativePath
     * @return
     */
    public boolean deleteFile(String relativePath) {
        Path path = Paths.get(targetFolder + relativePath);
        try {
            if (Files.deleteIfExists(path)) {
                cleanUploader(60);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new BusinessException("删除文件失败");
        }
    }


    /**
     * 清理无效碎片文件
     *
     * @param minutes 过期时长
     */
    public void cleanUploader(int minutes) {
        log.info("开始清理大文件上传中无效文件碎片");

        long time = System.currentTimeMillis() - 60L * 1000 * minutes;
        Iterator<Map.Entry<String, BigFileUploader>> it = bigFileUploaderMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, BigFileUploader> entry = it.next();
            BigFileUploader uploader = entry.getValue();
            if (uploader.isCompleted() || uploader.getLastUpdateTime() < time) {
                it.remove();
            }
        }
    }


    public void beginUploadFile(UploadFileDTO uploadFile) {


    }
}
