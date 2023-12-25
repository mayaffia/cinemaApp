package domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val sessionId: Int,
    var seatRow: Int,
    var seatNum: Int
)
