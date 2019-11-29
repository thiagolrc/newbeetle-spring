package co.cobli.newbeetle

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
@EnableTransactionManagement
@EnableSwagger2WebFlux
class NewbeetleApplication {
	@Bean
	fun api(): Docket {
		return Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors
						.basePackage("co.cobli.newbeetle"))
				.paths(PathSelectors.regex("/.*"))
				.build().apiInfo(apiEndPointsInfo())
	}

	private fun apiEndPointsInfo(): ApiInfo {
		return ApiInfoBuilder().title("Spring Boot REST API")
				.description("Employee Management REST API")
				.contact(Contact("Ramesh Fadatare", "www.javaguides.net", "ramesh24fadatare@gmail.com"))
				.license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.version("1.0.0")
				.build()
	}
}

fun main(args: Array<String>) {
	runApplication<NewbeetleApplication>(*args)
}
