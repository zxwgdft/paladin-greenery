package com.paladin.organization.service;

import com.mongodb.client.result.UpdateResult;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import com.paladin.organization.model.Agency;
import com.paladin.organization.model.AgencyGroup;
import com.paladin.organization.service.constant.MongoCollection;
import com.paladin.organization.service.dto.AgencyGroupSave;
import com.paladin.organization.service.dto.AgencyGroupUpdate;
import com.paladin.organization.service.util.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
@Service
public class AgencyService extends ServiceSupport<Agency> {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 添加机构分组
     *
     * @param agencyGroupSave
     * @return
     */
    public AgencyGroup createAgencyGroup(AgencyGroupSave agencyGroupSave) {
        AgencyGroup agencyGroup = SimpleBeanCopyUtil.simpleCopy(agencyGroupSave, new AgencyGroup());
        List<String> agencies = agencyGroup.getAgencies();
        // TODO 检查机构ID合法性
        return mongoTemplate.save(agencyGroup, MongoCollection.AGENCY_GROUP);
    }

    /**
     * 修改机构分组
     *
     * @param agencyGroupUpdate
     * @return
     */
    public UpdateResult updateAgencyGroup(AgencyGroupUpdate agencyGroupUpdate) {
        List<String> agencies = agencyGroupUpdate.getAgencies();
        // TODO 检查机构ID合法性
        return MongoUtil.updateById(mongoTemplate, MongoCollection.AGENCY_GROUP, agencyGroupUpdate);
    }
}
