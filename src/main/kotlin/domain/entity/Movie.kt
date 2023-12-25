package domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    var title: String,
    var director: String,
    var duration: Int
) {
    var id: Int = 0
}
