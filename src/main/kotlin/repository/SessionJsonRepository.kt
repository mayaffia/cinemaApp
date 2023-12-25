package repository

import domain.entity.Session
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SessionJsonRepository : JsonRepository<Session>() {
    private val json = Json { prettyPrint = true }
    override fun serialize(data: List<Session>) : String {
        return json.encodeToString(data)
    }

    override fun deserialize(data: String) : List<Session>{
        return json.decodeFromString(data)
    }
}