package domain.entity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class Session1 (
    val time: LocalDateTime,
    val movie: Movie,
    val allTickets: List<Ticket>,
    val id: Int,
    val countOfFreeSeats: Int
)