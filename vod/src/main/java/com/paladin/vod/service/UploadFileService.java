package com.paladin.vod.service;

import com.paladin.framework.service.Condition;
import com.paladin.framework.service.PageResult;
import com.paladin.framework.service.QueryType;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.vod.model.UploadFile;
import com.paladin.vod.service.dto.UploadFileQuery;
import com.paladin.vod.service.vo.UploadFileVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2020/8/4
 */
@Service
public class UploadFileService extends ServiceSupport<UploadFile> {

    /**
     * 查找用户上传中的文件
     *
     * @param userId
     * @return
     */
    public List<UploadFileVO> findUploadingFile(String userId) {
        return searchAll(UploadFileVO.class,
                new Condition(UploadFile.FIELD_STATUS, QueryType.EQUAL, UploadFile.STATUS_UPLOADING),
                new Condition(UploadFile.FIELD_USER_ID, QueryType.EQUAL, userId)
        );
    }

    /**
     * 查找上传完成的文件
     *
     * @param query
     * @return
     */
    public PageResult<UploadFileVO> findCompletedFile(UploadFileQuery query) {
        query.setStatus(UploadFile.STATUS_COMPLETED);
        return searchPage(query, UploadFileVO.class);
    }

}
