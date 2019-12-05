package co.cobli.newbeetle.service

import co.cobli.newbeetle.model.Vehicle
import co.cobli.newbeetle.model.VehicleGroup
import co.cobli.newbeetle.respository.VehicleGroupRepository
import co.cobli.newbeetle.respository.VehicleRepository
import co.cobli.newbeetle.view.VehicleGroupCreate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupService(@Autowired val vehicleGroupRepository: VehicleGroupRepository, @Autowired val vehicleRepository: VehicleRepository) {

    fun createGroup(vehicleGroupCreate: VehicleGroupCreate, fleetId: UUID, creatorId: UUID): VehicleGroup {
        val vehicleGroup = vehicleGroupCreate.toVehicleGroup(fleetId, creatorId)
        return vehicleGroupRepository.save(vehicleGroup)
    }

    fun setGroupVehicles(fleetId: UUID, groupId: UUID, vehicleIds: List<UUID>): VehicleGroup {
        val maybeGroup = vehicleGroupRepository.findByIdAndFleetId(groupId, fleetId)

        if (!maybeGroup.isPresent) {
            throw RuntimeException("Group not found :(")
        }

        val group = maybeGroup.get()

        val newVehicles: List<Vehicle> = vehicleRepository.findByFleetIdAndIdIn(fleetId, vehicleIds)

        val groupWithNewVehicles = group.copy(vehicles = newVehicles)

        return vehicleGroupRepository.save(groupWithNewVehicles)
    }

}
