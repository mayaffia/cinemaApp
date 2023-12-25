import domain.entity.Movie
import domain.entity.Session1
import domain.entity.Ticket
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    val session = Session1(
        LocalDateTime(2,2,3,4,5),
        Movie("fdg","gd",3),
        listOf(Ticket(2,1,2)),
        1,
        12
    )
    val str = Json.encodeToString(session)
    val file = File("test.json")
    file.writeText(str)
}