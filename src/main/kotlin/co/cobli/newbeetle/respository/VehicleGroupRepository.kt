package co.cobli.newbeetle.respository

import co.cobli.newbeetle.model.VehicleGroup
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VehicleGroupRepository : CrudRepository<VehicleGroup, UUID> {

    fun findByFleetId(fleetId: UUID): List<VehicleGroup>

    fun findByIdAndFleetId(id: UUID, fleetId: UUID): Optional<VehicleGroup>

}