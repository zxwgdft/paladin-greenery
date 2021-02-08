package com.paladin.framework.service;

/**
 * 数据容器
 * <p>
 * 用于缓存数据与内存中，例如一些经常使用，但是很少更新的数据
 *
 * @author TontoZhou
 * @since 2018年12月12日
 */
public interface DataContainer {

    /**
     * 数据容器ID，唯一
     *
     * @return
     */
    default String getId() {
        return this.getClass().getName();
    }

    /**
     * 系统启动时第一次加载数据方法
     */
    void load();

    /**
     * 当数据变化时，数据重新加载方法
     */
    default void reload() {
        load();
    }

    /**
     * 数据容器执行顺序号
     *
     * @return 排序号，升序排列执行
     */
    default int order() {
        return 0;
    }

}
