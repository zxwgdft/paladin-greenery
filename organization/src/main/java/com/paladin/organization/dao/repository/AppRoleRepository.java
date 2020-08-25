package com.paladin.organization.dao.repository;

import com.paladin.organization.model.AppRole;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author TontoZhou
 * @since 2020/4/2
 */
public interface AppRoleRepository extends MongoRepository<AppRole, String> {
}
