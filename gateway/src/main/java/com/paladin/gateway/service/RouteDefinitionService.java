package com.paladin.gateway.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.service.ServiceSupport;
import com.paladin.framework.utils.StringUtil;
import com.paladin.framework.utils.UUIDUtil;
import com.paladin.gateway.dao.mapper.RouteMapper;
import com.paladin.gateway.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tk.mybatis.mapper.entity.Example;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TontoZhou
 * @since 2020/9/7
 */
@Slf4j
@Service
public class RouteDefinitionService extends ServiceSupport<Route> implements RouteDefinitionRepository {

    private boolean initialized = false;
    private volatile Map<String, RouteDefinition> routes = new LinkedHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RouteMapper routeMapper;


    private void initialize() {
        // 读取数据库初始化路由
        Example example = new Example(Route.class);


        List<Route> routeList = routeMapper.selectByExample();
        // searchAll(new Condition(Route.FIELD_ENABLED, QueryType.EQUAL, true));

        Map<String, RouteDefinition> routes = new LinkedHashMap<>();

        for (Route route : routeList) {
            String id = route.getId();
            try {
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.setId(id);
                routeDefinition.setUri(new URI(route.getUri()));
                routeDefinition.setOrder(route.getOrders());

                String predicates = route.getPredicates();
                String filters = route.getFilters();

                routeDefinition.setPredicates(objectMapper.readValue(predicates, new TypeReference<List<PredicateDefinition>>() {
                }));

                if (StringUtil.isNotEmpty(filters)) {
                    routeDefinition.setFilters(objectMapper.readValue(filters, new TypeReference<List<FilterDefinition>>() {
                    }));
                }

                routes.put(id, routeDefinition);
            } catch (Exception e) {
                throw new BusinessException("创建RouteDefinition[ID:" + route.getId() + "]异常", e);
            }
        }

        this.routes = Collections.unmodifiableMap(routes);
    }


    public void refresh() {
        initialize();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        if (!initialized) {
            synchronized (RouteDefinitionService.class) {
                if (!initialized) {
                    initialize();
                    initialized = true;
                }
            }
        }
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> routeDefinition) {

        return routeDefinition.flatMap(r -> {

            try {
                String id = r.getId();
                if (StringUtil.isEmpty(id)) {
                    id = UUIDUtil.createUUID();
                }

                Route route = new Route();

                route.setId(id);
                route.setUri(r.getUri().toString());
                route.setOrders(r.getOrder());
                route.setEnabled(true);

                List<PredicateDefinition> ps = r.getPredicates();
                route.setPredicates(objectMapper.writeValueAsString(ps));

                List<FilterDefinition> fs = r.getFilters();
                if (fs != null && fs.size() > 0) {
                    route.setFilters(objectMapper.writeValueAsString(fs));
                }

                save(route);
                refresh();

                return Mono.empty();
            } catch (Exception e) {
                throw new BusinessException("创建路由失败");
            }
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        if (removeByPrimaryKey(routeId)) {
            refresh();
            return Mono.empty();
        }
        return Mono.defer(() -> Mono.error(
                new NotFoundException("RouteDefinition not found: " + routeId)));
    }


}
