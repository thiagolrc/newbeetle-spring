package co.cobli.newbeetle.controller

import co.cobli.newbeetle.model.Vehicle
import co.cobli.newbeetle.service.GroupService
import co.cobli.newbeetle.model.VehicleGroup
import co.cobli.newbeetle.respository.VehicleGroupRepository
import co.cobli.newbeetle.respository.VehicleRepository
import co.cobli.newbeetle.validator.ValidationError
import co.cobli.newbeetle.validator.VehicleGroupValidator
import co.cobli.newbeetle.view.VehicleGroupCreate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.lang.RuntimeException
import java.util.*
import javax.validation.Valid
import kotlin.collections.ArrayList
import java.util.Optional.empty as empty
import java.util.Optional.of as of
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.*
import org.springframework.web.reactive.function.server.ServerResponse.*


@RestController
@RequestMapping("/fleets/{fleetId}/groups")
class VehicleGroupController(@Autowired val repository: VehicleGroupRepository,
                             @Autowired val groupService: GroupService,
                             @Autowired val vehicleRepository: VehicleRepository,
                             @Autowired val vehicleGroupValidator: VehicleGroupValidator) {

    @GetMapping
    fun getVehicles(@PathVariable fleetId: UUID): Flux<VehicleGroup> = Flux.fromIterable(repository.findByFleetId(fleetId)).subscribeOn(Schedulers.boundedElastic())

    @PostMapping
    fun createGroup(@PathVariable fleetId: UUID, @Valid @RequestBody vehicleGroupCreate: VehicleGroupCreate): ResponseEntity<Response<VehicleGroup>> {
        val maybeValidationError = vehicleGroupValidator.validateGroup(vehicleGroupCreate, fleetId)

        if(maybeValidationError.isPresent) {
            val validationError = maybeValidationError.get()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseError(validationError.errors))
        }

        val creatorId = UUID.fromString("9525c823-66ae-46ab-b6e4-65eaeb69c5a5")
        val createdGroup = groupService.createGroup(vehicleGroupCreate, fleetId, creatorId)
        return ResponseEntity.status(HttpStatus.OK).body(ResponseSuccess(createdGroup))
    }

    @GetMapping("/{groupId}")
    fun getGroup(@PathVariable fleetId: UUID, @PathVariable groupId: UUID): Mono<VehicleGroup> {
        return Mono.fromCallable {
            repository.findByIdAndFleetId(groupId, fleetId).orElseThrow { RuntimeException("Group not found :(") }
        }.subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/{groupId}/notasync")
    fun getGroupNotAsync(@PathVariable fleetId: UUID, @PathVariable groupId: UUID): ResponseEntity<VehicleGroup> {
        val maybeGroup = repository.findByIdAndFleetId(groupId, fleetId)
        return ResponseEntity.of(maybeGroup)
    }

    @PutMapping("/{groupId}/vehicles")
    fun setGroupVehicles(@PathVariable fleetId: UUID, @PathVariable groupId: UUID, @RequestBody vehicleIds: List<UUID>): Mono<VehicleGroup> {
        return Mono.fromCallable {
            groupService.setGroupVehicles(fleetId, groupId, vehicleIds)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    @GetMapping("/{groupId}/vehicles")
    fun getGroupVehicles(@PathVariable fleetId: UUID, @PathVariable groupId: UUID): Flux<Vehicle> {
        return Flux.fromIterable(
                repository.findByIdAndFleetId(groupId, fleetId).map { vehicleGroup -> vehicleGroup.vehicles }.orElseThrow { RuntimeException("Group not found :(") }
        ).subscribeOn(Schedulers.boundedElastic())
    }

    @PutMapping("/vehicles")
    fun generateVehicles(@PathVariable fleetId: UUID): List<Vehicle> {
        val vehiclesList = ArrayList<Vehicle>()
        for (i in 1..10) {
            vehiclesList.add(vehicleRepository.save(Vehicle(UUID.randomUUID(), fleetId, "Veículo: $i")))
        }
        return vehiclesList
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationExceptions(ex: WebExchangeBindException): ValidationError {
        val errors = HashMap<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            if (errorMessage !== null) {
                errors[fieldName] = errorMessage
            }
        }
        return ValidationError(errors)
    }

}