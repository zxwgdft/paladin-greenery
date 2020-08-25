package com.paladin.organization.dao.repository;

import com.paladin.organization.model.AppResourceModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author TontoZhou
 * @since 2020/4/2
 */
public interface AppResourceModelRepository extends MongoRepository<AppResourceModel, String> {
}
