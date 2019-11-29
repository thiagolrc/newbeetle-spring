package co.cobli.newbeetle.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="vehicle")
data class Vehicle (@Id
                    val id: UUID,
                    val fleetId: UUID,
                    val licensePlate: String)
