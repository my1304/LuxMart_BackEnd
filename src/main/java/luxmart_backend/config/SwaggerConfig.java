package luxmart_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "LuxMart Service Backend",
                description = "LuxMart Backend API application for JSON web tokens",
                version = "1.0.1",
                contact = @Contact(
                        name = "Vyacheslav",
                        email = "my1304@gmail.com",
                        url = "https://github.com/my1304"
                )
        )
)
public class SwaggerConfig {
}