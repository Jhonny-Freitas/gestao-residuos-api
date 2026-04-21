package com.fiap.gestao.residuos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Resíduos e Reciclagem")
                        .version("1.0")
                        .description("API RESTful para gerenciamento de resíduos e reciclagem - Tema ESG")
                        .contact(new Contact()
                                .name("FIAP")
                                .email("contato@fiap.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
