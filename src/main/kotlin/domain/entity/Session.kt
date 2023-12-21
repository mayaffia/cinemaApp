package domain.entity

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Session(val time: LocalDateTime,
                   val movie : Movie) {
    val allTickets : MutableList<Ticket> = mutableListOf()
    var id : Int = 0
    var countOfFreeSeats = 24
}
