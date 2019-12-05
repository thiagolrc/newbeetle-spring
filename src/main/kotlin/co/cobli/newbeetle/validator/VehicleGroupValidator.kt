package co.cobli.newbeetle.validator

import co.cobli.newbeetle.respository.VehicleGroupRepository
import co.cobli.newbeetle.view.VehicleGroupCreate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class VehicleGroupValidator(@Autowired val vehicleGroupRepository: VehicleGroupRepository) {

    fun validateGroup(vehicleGroupCreate: VehicleGroupCreate, fleetId: UUID): Optional<ValidationError> {
        val maybeGroup = vehicleGroupRepository.findByNameAndFleetId(vehicleGroupCreate.name, fleetId)
        if (!maybeGroup.isPresent) {
            return Optional.empty()
        }

        return Optional.of(ValidationError(mapOf("name" to "alreadyExists")))
    }

}
