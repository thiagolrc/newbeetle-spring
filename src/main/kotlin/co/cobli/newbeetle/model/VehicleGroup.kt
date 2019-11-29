package co.cobli.newbeetle.model

import java.util.*
import javax.persistence.*


@Entity
@Table(name="vehicle_group")
data class VehicleGroup (
        @Id
        val id: UUID,
        val fleetId: UUID,
        val name: String,
        @ManyToMany(fetch = FetchType.EAGER)
        val vehicles: List<Vehicle>,
        val createdAt: Long,
        val updatedAt: Long,
        val creatorId: UUID
)

// TODO Talvez tenha q sobrescrever equals e hascode pra ficar em conformancia com o JPA (só o ID deveria ser usado)
// https://dzone.com/articles/kotlin-data-classes-and-jpa