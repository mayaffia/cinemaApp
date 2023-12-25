package domain.entity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val time: LocalDateTime,
    val movie: Movie,
    var allTickets: List<Ticket>,
    var countOfFreeSeats : Int,
    var id: Int = 0
)
