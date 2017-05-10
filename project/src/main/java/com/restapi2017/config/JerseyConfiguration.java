package com.restapi2017.config;

import com.restapi2017.resources.UsersResource;
import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("");
        beanConfig.setResourcePackage("com.restapi2017");
        beanConfig.setScan(true);
        packages("io.swagger.jaxrs.listing");

        register(UsersResource.class);
        register(CorsFilter.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
