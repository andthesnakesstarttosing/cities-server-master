package by.bsu.cities.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public OpenAPI baseOpenAPI() {
        ApiResponse badRequestApi = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples(
                                "default",
                                new Example()
                                        .value("Error processing REST request with status code 400 and message: Bad Request")
                        ))
        );
        Components components = new Components();
        components.addResponses("badRequest", badRequestApi);

        return new OpenAPI()
                .components(components)
                .info(new Info().title("Spring Doc").version("1.0.0").description("Spring Doc"));
    }
}
