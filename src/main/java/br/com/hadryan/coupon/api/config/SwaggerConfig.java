package br.com.hadryan.coupon.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon API")
                        .description("API for managing discount coupons. Enables creation, retrieval, and deletion of coupons with automatic code sanitization and business rule validation.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hadryan Silva")
                                .email("hadryan.hsilva@gmail.com")
                                .url("https://github.com/hadryan/coupon-api"))
                );
    }
}
