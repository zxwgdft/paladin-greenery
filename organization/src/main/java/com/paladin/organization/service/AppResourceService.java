package com.paladin.organization.service;

import com.mongodb.client.result.UpdateResult;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import com.paladin.organization.model.App;
import com.paladin.organization.model.AppResource;
import com.paladin.organization.model.AppResourceModel;
import com.paladin.organization.service.constant.MongoCollection;
import com.paladin.organization.service.dto.AppResourceModelSave;
import com.paladin.organization.service.dto.AppResourceSave;
import com.paladin.organization.service.dto.AppResourceUpdate;
import com.paladin.organization.service.util.DynamicPropertyUtil;
import com.paladin.organization.service.util.MongoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/1/8
 */
@Service
public class AppResourceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AppService appService;

    /**
     * 创建资源
     *
     * @param resourceSave
     */
    public AppResource createResource(AppResourceSave resourceSave) {
        AppResource appResource = SimpleBeanCopyUtil.simpleCopy(resourceSave, new AppResource());

        String appId = appResource.getAppId();
        String modelId = appResource.getModelId();

        App app = appService.get(appId);
        if (app == null) {
            throw new BusinessException("找不到资源对应应用");
        }

        AppResourceModel model = mongoTemplate.findById(modelId, AppResourceModel.class, MongoCollection.APP_RESOURCE_MODEL);
        Map<String, Object> effectiveProperties = DynamicPropertyUtil.checkEffectiveProperties(resourceSave.getProperties(), model);
        appResource.setProperties(effectiveProperties);

        String parentId = appResource.getParent();
        if (StringUtil.isNotEmpty(parentId)) {
            AppResource parent = mongoTemplate.findById(parentId, AppResource.class, MongoCollection.APP_RESOURCE);
            if (parent == null) {
                throw new BusinessException("无法创建应用资源，因为找不到上级资源[ID:" + parentId + "]");
            }
        }

        return mongoTemplate.save(appResource, MongoCollection.APP_RESOURCE);
    }

    /**
     * 更新资源属性
     *
     * @param resourceUpdate
     * @return
     */
    public UpdateResult updateResourceProperties(AppResourceUpdate resourceUpdate) {
        AppResourceModel model = mongoTemplate.findById(resourceUpdate.getModelId(), AppResourceModel.class, MongoCollection.APP_RESOURCE_MODEL);
        Map<String, Object> properties = DynamicPropertyUtil.checkEffectiveProperties(resourceUpdate.getProperties(), model);
        resourceUpdate.setProperties(properties);
        return MongoUtil.updateById(mongoTemplate, MongoCollection.APP_RESOURCE, resourceUpdate);
    }

    /**
     * 更新资源父节点
     *
     * @param resourceId
     * @param parentId
     */
    public UpdateResult updateResourceParent(String resourceId, String parentId) {
        Query query = new Query(Criteria.where(AppResource.FIELD_ID).is(resourceId));
        Update update = Update.update(AppResource.FIELD_PARENT, parentId);
        return mongoTemplate.updateMulti(query, update, MongoCollection.APP_RESOURCE);
    }


    /**
     * 查找APP下所有资源
     *
     * @param appId
     * @return
     */
    public List<AppResource> findAppResource(String appId) {
        return mongoTemplate.find(new Query(Criteria.where(AppResource.FIELD_APP_ID).is(appId)), AppResource.class, MongoCollection.APP_RESOURCE);
    }

    /**
     * 创建资源模型
     *
     * @param modelSave
     * @return
     */
    public AppResourceModel createResourceModel(AppResourceModelSave modelSave) {
        AppResourceModel resourceModel = SimpleBeanCopyUtil.simpleCopy(modelSave, new AppResourceModel());
        return mongoTemplate.save(resourceModel, MongoCollection.APP_RESOURCE_MODEL);
    }


    public List<AppResourceModel> findAppResourceModels(String appId) {
        return mongoTemplate.find(new Query(Criteria.where(AppResourceModel.FIELD_APP_ID).is(appId)), AppResourceModel.class, MongoCollection.APP_RESOURCE_MODEL);
    }


}
