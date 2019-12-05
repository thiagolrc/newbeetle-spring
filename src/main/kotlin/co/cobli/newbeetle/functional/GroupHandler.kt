package co.cobli.newbeetle.functional

import co.cobli.newbeetle.respository.VehicleGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*
import org.springframework.web.reactive.function.BodyInserters.fromValue
import reactor.core.scheduler.Schedulers
import java.lang.RuntimeException

@Component
class GroupHandler(@Autowired val repository: VehicleGroupRepository) {

    fun getGroup(request: ServerRequest): Mono<ServerResponse> {
        val groupId = UUID.fromString(request.pathVariable("groupId"))
        val fleetId = UUID.fromString(request.pathVariable("fleetId"))

        val monoVehicleGroup = Mono.fromCallable {
            repository.findByIdAndFleetId(groupId, fleetId)
        }.subscribeOn(Schedulers.boundedElastic());

        return okOrNotFoundMono(monoVehicleGroup)
    }

    private val notFound = ServerResponse.notFound().build()
    private fun <T> okOrNotFoundMono(mono: Mono<T>): Mono<ServerResponse> {
        return mono.flatMap { value ->
            ServerResponse.ok().contentType(APPLICATION_JSON).body(fromValue(value))
        }.switchIfEmpty(notFound)
    }
}