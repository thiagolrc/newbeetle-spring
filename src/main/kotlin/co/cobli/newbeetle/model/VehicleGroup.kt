package co.cobli.newbeetle.model

import org.hibernate.envers.AuditJoinTable
import org.hibernate.envers.Audited
import org.hibernate.envers.RelationTargetAuditMode
import org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*


@Entity
@Table(name="vehicle_group")
@Audited(targetAuditMode = NOT_AUDITED)
data class VehicleGroup (
        @Id
        val id: UUID,

        val fleetId: UUID,

        val name: String,

        @AuditJoinTable
        @ManyToMany(fetch = FetchType.EAGER)
        val vehicles: List<Vehicle>,

        @CreatedDate
        val createdAt: Long,

        @LastModifiedDate
        val updatedAt: Long,

        val creatorId: UUID
)

// TODO Talvez tenha q sobrescrever equals e hashcode pra ficar em conformidade com o JPA (só o ID deveria ser usado)
// https://dzone.com/articles/kotlin-data-classes-and-jpa