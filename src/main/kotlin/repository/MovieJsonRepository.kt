package repository

import domain.entity.Movie
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MovieJsonRepository : JsonRepository<Movie>() {
    private val json = Json { prettyPrint = true }
    override fun serialize(data: List<Movie>): String {
        return json.encodeToString(data)
    }

    override fun deserialize(data: String): List<Movie> {
        return json.decodeFromString(data)
    }

}