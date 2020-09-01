package com.paladin.organization.service;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.framework.utils.convert.Base64Util;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import com.paladin.organization.model.Personnel;
import com.paladin.organization.service.dto.PersonnelSave;
import com.paladin.organization.service.dto.PersonnelUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class PersonnelService extends ServiceSupport<Personnel> {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadMultipartFileService uploadMultipartFileService;


    @Transactional
    public void savePersonnel(PersonnelSave save) {
        String id = UUIDUtil.createUUID();
        String account = save.getAccount();
        sysUserService.createPersonnelAccount(account, id);

        Personnel personnel = SimpleBeanCopyUtil.simpleCopy(save, new Personnel());
        personnel.setId(id);

        MultipartFile file = save.getProfilePhotoFile();
        if (file != null) {

            try {
                String base64str = Base64Util.encode(file.getBytes());
                String fileId = uploadMultipartFileService.uploadFile(file);
                personnel.setProfilePhoto(fileId);
            } catch (Exception e) {
                throw new BusinessException("上传头像失败", e);
            }
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

        SimpleBeanCopyUtil.simpleCopy(update, origin);
        update(origin);
    }

    @Transactional
    public void deletePersonnel(String id) {
        Personnel origin = get(id);
        if (origin == null) {
            throw new BusinessException("找不到需要修改的人员数据");
        }
        sysUserService.removePersonnelAccount(id);
        removeByPrimaryKey(id);
    }
}
