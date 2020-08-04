package com.paladin.gateway.swagger;

import com.paladin.framework.spring.DevCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;

@Conditional(DevCondition.class)
@RestController
@RequestMapping("/swagger-resources")
public class SwaggerHandler {
    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;
    @Autowired(required = false)
    private UiConfiguration uiConfiguration;
    private final SwaggerResourcesProvider swaggerResources;

    @Autowired
    public SwaggerHandler(SwaggerResourcesProvider swaggerResources) {
        this.swaggerResources = swaggerResources;
    }


    @GetMapping("/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
        return Mono.just(new ResponseEntity<>(getSecurityConfiguration(), HttpStatus.OK));
    }

    @GetMapping("/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
        return Mono.just(new ResponseEntity<>(getUiConfiguration(), HttpStatus.OK));
    }

    @GetMapping("")
    public Mono<ResponseEntity> swaggerResources() {
        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
    }

    private UiConfiguration getUiConfiguration() {
        if (uiConfiguration == null) {
            synchronized (SwaggerHandler.class) {
                if (uiConfiguration == null) {
                    uiConfiguration = UiConfigurationBuilder.builder().build();
                }
            }
        }
        return uiConfiguration;
    }

    private SecurityConfiguration getSecurityConfiguration() {
        if (securityConfiguration == null) {
            synchronized (SwaggerHandler.class) {
                if (securityConfiguration == null) {
                    securityConfiguration = SecurityConfigurationBuilder.builder().build();
                }
            }
        }
        return securityConfiguration;
    }
}

