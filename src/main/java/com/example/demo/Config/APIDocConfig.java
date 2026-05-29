package com.example.demo.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Jhatpat Services",
                description = "Jhatpat Services is an on-demand platform for booking trusted home services quickly and conveniently.",
                contact = @Contact(
                        name = "Utkarsh Srivastava",
                        url = "https://www.linkedin.com/in/utkarsh-srivastava-dev/",
                        email = "utkarsh.sri.9170@gmail.com"
                ),
                version = "1.0",
                summary = "Jhatpat Services is an on-demand home service platform designed to connect customers with trusted and verified service professionals. The platform allows users to quickly book services such as plumbing, electrical repairs, cleaning, appliance maintenance, beauty services, and other household solutions. It focuses on providing fast response times, secure bookings, reliable service management, and a seamless user experience through a modern full-stack architecture."
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }

)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",  //Authorization: Bearer <token>
        bearerFormat = "JWT"
)
public class APIDocConfig {



}
