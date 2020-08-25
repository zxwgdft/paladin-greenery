package com.paladin.organization.dao.repository;

import com.paladin.organization.model.AppResource;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author TontoZhou
 * @since 2020/4/2
 */
public interface AppResourceRepository extends MongoRepository<AppResource, String> {
}
