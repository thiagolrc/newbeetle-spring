package co.cobli.newbeetle.view

import co.cobli.newbeetle.model.VehicleGroup
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class VehicleGroupCreate(
        @field:NotBlank(message = "mustNotBeBlank")
        @field:Size(max = 100, message = "maxLength")
        val name: String) {

    fun toVehicleGroup(fleetId: UUID, creatorId: UUID): VehicleGroup {
        val now = System.currentTimeMillis()
        return VehicleGroup(UUID.randomUUID(), fleetId, name, Collections.emptyList(), now, now, creatorId)
    }

}