package co.cobli.newbeetle.respository

import co.cobli.newbeetle.model.Vehicle
import org.springframework.data.repository.CrudRepository
import java.util.*

interface VehicleRepository : CrudRepository<Vehicle, UUID> {

    fun findByFleetIdAndIdIn(fleetId: UUID, id: List<UUID>): List<Vehicle>

}