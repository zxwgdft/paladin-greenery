package com.paladin.framework.service;


/**
 * @author TontoZhou
 * @since 2020/10/20
 */
public interface DataVersionDAO {

    long getDataVersion(String containerId);

    void incrementVersion(String containerId);

    void setDataVersion(String id, long version);
}
