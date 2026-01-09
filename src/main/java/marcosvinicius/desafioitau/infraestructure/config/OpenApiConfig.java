package marcosvinicius.desafioitau.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Desafio Itaú")
                        .version("1.0")
                        .description("API para processamento de transações e cálculo de estatísticas em tempo real.")
                        .contact(new Contact()
                                .name("Marcos Vinícius")
                                .email("marcos.vinicius.dev3@gmail.com")));
    }
}