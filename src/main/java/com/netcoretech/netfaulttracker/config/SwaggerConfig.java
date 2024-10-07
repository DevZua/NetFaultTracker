package com.netcoretech.netfaulttracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NetFaultTracker API")
                        .version("1.0")
                        .description("NetFaultTracker 애플리케이션의 API 문서"));
    }
}