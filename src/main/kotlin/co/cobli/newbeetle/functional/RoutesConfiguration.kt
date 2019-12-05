package co.cobli.newbeetle.functional

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.RouterFunctions.route



@Configuration
@ComponentScan("co.cobli.newbeetle")
class RoutesConfiguration(private val groupHandler: GroupHandler) {

	@Bean
	fun notFound(): RouterFunction<ServerResponse> {
		return route().GET("/v2/fleets/{fleetId}/groups/{groupId}", groupHandler::getGroup).build()
	}
}
