package com.paladin.organization.service;

import com.mongodb.client.result.UpdateResult;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.convert.SimpleBeanCopyUtil;
import com.paladin.organization.model.App;
import com.paladin.organization.model.AppRole;
import com.paladin.organization.model.AppRoleModel;
import com.paladin.organization.service.constant.MongoCollection;
import com.paladin.organization.service.dto.AppRoleModelSave;
import com.paladin.organization.service.dto.AppRoleSave;
import com.paladin.organization.service.dto.AppRoleUpdate;
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
 * @since 2020/1/15
 */
@Service
public class AppRoleService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AppService appService;

    /**
     * 创建角色
     *
     * @param roleSave
     */
    public AppRole createRole(AppRoleSave roleSave) {
        AppRole appRole = SimpleBeanCopyUtil.simpleCopy(roleSave, new AppRole());

        String appId = appRole.getAppId();
        String modelId = appRole.getModelId();

        App app = appService.get(appId);
        if (app == null) {
            throw new BusinessException("找不到角色对应应用");
        }

        AppRoleModel model = mongoTemplate.findById(modelId, AppRoleModel.class, MongoCollection.APP_ROLE_MODEL);
        Map<String, Object> effectiveProperties = DynamicPropertyUtil.checkEffectiveProperties(roleSave.getProperties(), model);
        appRole.setProperties(effectiveProperties);

        return mongoTemplate.save(appRole, MongoCollection.APP_ROLE);
    }


    /**
     * 更新角色属性
     *
     * @param roleUpdate
     * @return
     */
    public UpdateResult updateRoleProperties(AppRoleUpdate roleUpdate) {
        String roleId = roleUpdate.getId();
        AppRoleModel model = mongoTemplate.findById(roleUpdate.getModelId(), AppRoleModel.class, MongoCollection.APP_ROLE_MODEL);
        Map<String, Object> properties = DynamicPropertyUtil.checkEffectiveProperties(roleUpdate.getProperties(), model);
        roleUpdate.setProperties(properties);
        return MongoUtil.updateById(mongoTemplate, MongoCollection.APP_ROLE, roleUpdate);
    }

    /**
     * 授权角色资源
     *
     * @param roleId
     * @param resourceIds
     * @return
     */
    public UpdateResult grantResource(String roleId, List<String> resourceIds) {
        // 检查有效的资源ID，排除已经无效的
        //List<String> resourceList = checkEffectiveResources(roleSave.getResourceIds());

        Query query = new Query(Criteria.where(AppRole.FIELD_ID).is(roleId));
        Update update = Update.update(AppRole.FIELD_RESOURCE_IDS, resourceIds);
        return mongoTemplate.updateMulti(query, update, MongoCollection.APP_ROLE);
    }


    /**
     * 查找APP下所有资源
     *
     * @param appId
     * @return
     */
    public List<AppRole> findAppRole(String appId) {
        return mongoTemplate.find(new Query(Criteria.where(AppRole.FIELD_APP_ID).is(appId)), AppRole.class, MongoCollection.APP_ROLE);
    }

    /**
     * 创建资源模型
     *
     * @param modelSave
     * @return
     */
    public AppRoleModel createRoleModel(AppRoleModelSave modelSave) {
        AppRoleModel roleModel = SimpleBeanCopyUtil.simpleCopy(modelSave, new AppRoleModel());
        return mongoTemplate.save(roleModel, MongoCollection.APP_ROLE_MODEL);
    }


    public List<AppRoleModel> findAppRoleModels(String appId) {
        return mongoTemplate.find(new Query(Criteria.where(AppRoleModel.FIELD_APP_ID).is(appId)), AppRoleModel.class, MongoCollection.APP_ROLE_MODEL);
    }

}
