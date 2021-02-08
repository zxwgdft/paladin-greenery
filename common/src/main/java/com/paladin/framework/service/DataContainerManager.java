package com.paladin.framework.service;

import com.paladin.framework.spring.SpringBeanHelper;
import com.paladin.framework.spring.SpringContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 数据容器管理器
 * <p>
 * 整机环境下，程序自身能获知数据改变，只需要在数据变更的代码处触发相应数据容器加载方法即可
 * <p>
 * 分布式环境下，可通过实现DataVersionDao来通过数据库或其他中间件方式获知数据的变更，例如redis，
 * 对于实时性要求高的数据不适合该方式
 */
@Slf4j
public class DataContainerManager implements SpringContainer, Runnable {

    @Value("${paladin.data.version.enable:false}")
    private boolean versionEnable;

    @Value("${paladin.data.version.interval:2}")
    private int versionInterval;

    @Autowired(required = false)
    private DataVersionDAO dataVersionDAO;

    private static Map<String, DataContainerWrap> idContainerMap;
    private static Map<Class, DataContainerWrap> classContainerMap;
    private static List<DataContainerWrap> containers;

    @Override
    public boolean initialize() {
        Map<String, DataContainer> versionContainerMap = SpringBeanHelper.getBeansByType(DataContainer.class);

        Map<String, DataContainerWrap> idContainerMap = new HashMap<>();
        Map<Class, DataContainerWrap> classContainerMap = new HashMap<>();
        List<DataContainerWrap> containers = new ArrayList<>();

        for (Entry<String, DataContainer> entry : versionContainerMap.entrySet()) {
            DataContainer container = entry.getValue();
            String containerId = container.getId();

            if (idContainerMap.containsKey(containerId)) {
                log.warn("存在多个DataContainer的ID为[" + containerId + "]");
            }

            DataContainerWrap containerWrap = new DataContainerWrap(containerId, container);
            idContainerMap.put(containerId, containerWrap);
            classContainerMap.put(container.getClass(), containerWrap);
            containers.add(containerWrap);
        }

        Collections.sort(containers, (DataContainerWrap o1, DataContainerWrap o2) -> o1.container.order() - o2.container.order());

        for (DataContainerWrap container : containers) {
            if (versionEnable) {
                container.version = dataVersionDAO.getDataVersion(container.id);
            }
            container.container.load();
        }

        DataContainerManager.idContainerMap = Collections.unmodifiableMap(idContainerMap);
        DataContainerManager.classContainerMap = Collections.unmodifiableMap(classContainerMap);
        DataContainerManager.containers = Collections.unmodifiableList(containers);


        if (versionEnable) {
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("checkDataVersion");
                    return thread;
                }
            });

            service.scheduleWithFixedDelay(this, versionInterval, versionInterval, TimeUnit.SECONDS);
            log.info("开启数据容器版本检测线程");
        }


        return true;
    }

    // 检查数据容器版本变化
    public void run() {
        for (DataContainerWrap container : containers) {
            try {
                long version = dataVersionDAO.getDataVersion(container.id);
                if (version > container.version) {
                    container.container.reload();
                    container.version = version;
                } else if (version < container.version) {
                    dataVersionDAO.setDataVersion(container.id, container.version);
                }
            } catch (Exception e) {
                log.error("数据容器[" + container.id + "]重读异常", e);
            }
        }

        //log.debug("进行一次数据容器版本检查");
    }

    private void reloadContainer(DataContainerWrap container) {
        if (container != null) {
            container.container.reload();
            if (versionEnable) {
                dataVersionDAO.incrementVersion(container.id);
                container.version++;
                if (log.isDebugEnabled()) {
                    log.debug("数据容器[" + container.id + "]更新版本为：" + container.version);
                }
            }
        }
    }

    /**
     * 数据变化，从新读取数据
     *
     * @param id 容器ID
     */
    public void reloadContainer(String id) {
        reloadContainer(idContainerMap.get(id));
    }

    /**
     * 数据变化，从新读取数据
     *
     * @param clazz 容器Class
     */
    public void reloadContainer(Class clazz) {
        reloadContainer(classContainerMap.get(clazz));
    }


    private class DataContainerWrap {
        private String id;
        private DataContainer container;
        private long version;

        DataContainerWrap(String id, DataContainer container) {
            this.id = id;
            this.container = container;
        }
    }

}
