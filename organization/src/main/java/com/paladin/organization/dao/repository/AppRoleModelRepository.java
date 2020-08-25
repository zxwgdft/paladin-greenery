package com.paladin.organization.dao.repository;

import com.paladin.organization.model.AppRoleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author TontoZhou
 * @since 2020/4/2
 */
public interface AppRoleModelRepository extends MongoRepository<AppRoleModel, String> {
}
