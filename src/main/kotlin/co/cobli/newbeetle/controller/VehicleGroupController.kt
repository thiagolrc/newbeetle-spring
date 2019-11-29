package co.cobli.newbeetle.controller

import co.cobli.newbeetle.model.Vehicle
import co.cobli.newbeetle.service.GroupService
import co.cobli.newbeetle.model.VehicleGroup
import co.cobli.newbeetle.respository.VehicleGroupRepository
import co.cobli.newbeetle.respository.VehicleRepository
import co.cobli.newbeetle.view.VehicleGroupCreate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.WebExchangeBindException
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.util.*
import javax.validation.Valid
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/fleets/{fleetId}/groups")
class VehicleGroupController(@Autowired val repository: VehicleGroupRepository, @Autowired val groupService: GroupService, @Autowired val vehicleRepository: VehicleRepository) {


    @GetMapping
    fun getVehicles(@PathVariable fleetId: UUID): Flux<VehicleGroup> = Flux.fromIterable(repository.findByFleetId(fleetId)).subscribeOn(Schedulers.boundedElastic());

    @PostMapping
    fun createGroup(@PathVariable fleetId: UUID, @Valid @RequestBody vehicleGroupCreate: VehicleGroupCreate): VehicleGroup {
        return repository.save(vehicleGroupCreate.toVehicleGroup(fleetId, UUID.randomUUID()))
    }

    @PutMapping("/{groupId}/vehicles")
    fun setGroupVehicles(@PathVariable fleetId: UUID, @PathVariable groupId: UUID, @RequestBody vehicleIds: List<UUID>): VehicleGroup {
        return groupService.setGroupVehicles(fleetId, groupId, vehicleIds)
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
    fun handleValidationExceptions(ex: WebExchangeBindException): HashMap<String, String> {
        val errors = HashMap<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            if (errorMessage !== null) {
                errors[fieldName] = errorMessage
            }
        }
        return errors
    }

}