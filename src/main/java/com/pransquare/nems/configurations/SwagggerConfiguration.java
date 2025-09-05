package com.pransquare.nems.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "EMS Portal", description = "Employee Management System", summary = "Employee Management System", termsOfService = "Given below are the terms and services ", contact = @Contact(name = "Pransquare", email = "@pransquare.in", url = "http://localhost:8080/v3/api-docs"), license = @License(name = "ApacheLicense.org"), version = "Apache V0.01"), servers = {
		@Server(description = "emsportal", url = "http://localhost:8080"),

})
// security = @SecurityRequirement(name = "EMPLOYEE_SECURITY"))

// @SecurityScheme(name = "EMPLOYEE_SECURITY", in = SecuritySchemeIn.HEADER,
// type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer",
// description = "this is basic description for security")
public class SwagggerConfiguration {

}
