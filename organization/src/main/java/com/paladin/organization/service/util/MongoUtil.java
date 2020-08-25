package com.paladin.organization.service.util;

import com.mongodb.client.result.UpdateResult;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.reflect.Entity;
import com.paladin.framework.utils.reflect.EntityField;
import com.paladin.organization.model.constant.SysConstant;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Mongo数据库操作常用方法
 *
 * @author TontoZhou
 * @since 2020/4/9
 */
public class MongoUtil {

    /**
     * 根据ID更新数据，根据反射获取对象中字段全部更新
     *
     * @param template   mongo操作模板
     * @param collection mongo集合
     * @param updateObj  更新对象
     * @return 更新结果
     */
    public static UpdateResult updateById(MongoTemplate template, String collection, Object updateObj) {
        return updateById(template, collection, updateObj, null);
    }

    /**
     * 根据ID更新数据，根据反射获取对象中字段全部更新
     *
     * @param template   mongo操作模板
     * @param collection mongo集合
     * @param updateObj  更新对象
     * @param id         更新对象ID
     * @return 更新结果
     */
    public static UpdateResult updateById(MongoTemplate template, String collection, Object updateObj, String id) {
        if (updateObj == null) {
            throw new BusinessException("更新对象不能为空");
        }

        Entity entity = Entity.getEntity(updateObj.getClass());
        if (id == null) {
            EntityField field = entity.getEntityField(id);
            if (field == null) {
                throw new BusinessException("更新ID不能为空");
            }
            id = (String) field.getValue(updateObj);
        }

        if (StringUtil.isEmpty(id)) {
            throw new BusinessException("更新ID不能为空");
        }

        Query query = new Query(Criteria.where(SysConstant.FIELD_ID).is(id));
        Update update = new Update();
        for (EntityField field : entity.getEntityFields()) {
            String fieldName = field.getName();
            if (!"id".equals(fieldName)) {
                update.set(fieldName, field.getValue(updateObj));
            }
        }
        return template.updateMulti(query, update, collection);
    }

    /**
     * 反射创建update对象，排除id字段
     *
     * @param updateObj 更新对象
     * @return mongoTemplate中的Update对象
     */
    public static Update createUpdate(Object updateObj) {
        if (updateObj == null) {
            throw new BusinessException("更新对象不能为空");
        }
        Entity entity = Entity.getEntity(updateObj.getClass());
        Update update = new Update();
        for (EntityField field : entity.getEntityFields()) {
            String fieldName = field.getName();
            if (!SysConstant.FIELD_ID.equals(fieldName)) {
                update.set(fieldName, field.getValue(updateObj));
            }
        }
        return update;
    }

}
