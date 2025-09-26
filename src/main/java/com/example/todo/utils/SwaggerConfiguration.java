package com.example.todo.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Value("${todo-api.version}")
    private String versao;

    /**
     * Metodo responsavel em customizar documentação da API
     *
     * @author Hylan Silva
     * @return
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // define a cabeçalho da documentação
                .info(new Info()
                        .title("Todo API")
                        .description("Api do sistema de tarefas")
                        .version(versao)
                        .contact(new Contact()
                                .name("ABC Entreprise"))
                        .license(new License()
                                .name("License of API")
                                .url("API license URL")));
        // define a segurança do scheme para passar o token de validação
        // .components(new Components()
        // .addSecuritySchemes(ConstantsUtil.SECURITY_SWAGGER,
        // new SecurityScheme()
        // .type(SecurityScheme.Type.HTTP)
        // .scheme("bearer")
        // .bearerFormat("JWT")) )
        // passar a parte de segurança de forma global
        // .security(List.of(new
        // SecurityRequirement().addList(ConstantsUtil.SECURITY_SWAGGER)));
    }


}
