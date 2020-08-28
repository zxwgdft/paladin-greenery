package com.paladin.organization.service;

import com.paladin.framework.service.ServiceSupport;
import com.paladin.organization.model.Personnel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class PersonnelService extends ServiceSupport<Personnel> {


    /**
     * 功能描述: <根据用户ID删除人员基本信息>
     *
     * @param recordId
     */
    @Transactional
    public void deletePersonnel(String recordId) {
        // TODO 需要删除人员相关信息
    }
}
