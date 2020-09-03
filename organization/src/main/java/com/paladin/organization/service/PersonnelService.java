package com.paladin.organization.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import com.paladin.organization.model.Personnel;
import com.paladin.organization.service.dto.PersonnelSave;
import com.paladin.organization.service.dto.PersonnelUpdate;
import com.paladin.organization.service.dto.UploadFileBase64;
import com.paladin.organization.service.vo.OpenPersonnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class PersonnelService extends ServiceSupport<Personnel> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UploadFileService uploadFileService;


    public OpenPersonnel getPersonnel(String id) {
        OpenPersonnel personnel = get(id, OpenPersonnel.class);
        personnel.setProfilePhotoFile(uploadFileService.getAttachmentResource(personnel.getProfilePhoto()));
        return personnel;
    }

    @Transactional
    public void savePersonnel(PersonnelSave save) {
        String id = UUIDUtil.createUUID();
        String account = save.getAccount();
        sysUserService.createPersonnelAccount(account, id);

        Personnel personnel = SimpleBeanCopyUtil.simpleCopy(save, new Personnel());
        personnel.setId(id);

        MultipartFile file = save.getProfilePhotoFile();
        if (file != null) {
            List<UploadFileBase64> uploadFiles = uploadFileService.convertFile(true, file);
            String fileId = uploadFileService.uploadAttachment(Personnel.class, id, uploadFiles, 1);
            personnel.setProfilePhoto(fileId);
        }

        save(personnel);
    }

    @Transactional
    public void updatePersonnel(PersonnelUpdate update) {
        String id = update.getId();
        Personnel origin = get(id);
        if (origin == null) {
            throw new BusinessException("找不到需要修改的人员数据");
        }

        String originAccount = origin.getAccount();
        String currentAccount = update.getAccount();

        if (originAccount == null || !originAccount.equals(currentAccount)) {
            sysUserService.updatePersonnelAccount(id, currentAccount);
        }

        MultipartFile file = update.getProfilePhotoFile();
        List<UploadFileBase64> uploadFiles = file == null ? null : uploadFileService.convertFile(true, file);
        String fileId = uploadFileService.uploadAttachment(Personnel.class, id, uploadFiles, origin.getProfilePhoto(), update.getProfilePhoto(), 1);

        update.setProfilePhoto(fileId);
        SimpleBeanCopyUtil.simpleCopy(update, origin);
        update(origin);
    }

    @Transactional
    public void deletePersonnel(String id) {
        Personnel origin = get(id);
        if (origin == null) {
            throw new BusinessException("找不到需要修改的人员数据");
        }
        // 删除附件
        uploadFileService.uploadAttachment(Personnel.class, id, null, origin.getProfilePhoto(), null, 100);
        sysUserService.removePersonnelAccount(id);
        removeByPrimaryKey(id);
    }

}
