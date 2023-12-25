package domain
import data.MovieDaoImpl
import data.SessionDaoImpl
import domain.entity.Movie
import domain.entity.Ticket
import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel
import repository.SessionJsonRepository

interface CinemaController {
    fun sellTicket(movie : Movie, time : LocalDateTime, row : Int, num : Int) : Result
    fun returnTicket(time : LocalDateTime, row : Int, num : Int) : Result
}


//корректировать количсетво свободных мест
class CinemaControllerImpl(private val sessionDaoImpl: SessionDaoImpl,
                           private val movieDaoImpl : MovieDaoImpl,
    private val sessionController: SessionController) : CinemaController {

    private val jsonS = SessionJsonRepository()

    override fun sellTicket(movie : Movie, time : LocalDateTime, row : Int, num : Int) : Result {
        val sessions = sessionDaoImpl.getAllSessions()

        val session = sessions.find { it.movie == movie && it.time == time }
            ?: return Error(OutputModel("Нет такого сеанса"))

        return if (sessionController.isSeatFree(session, row, num)) {
            val ticket = Ticket(session.id, row, num)
            sessionController.addTicket(session, ticket)
            jsonS.saveToFile(sessions, "schedule.json")
            Success
        } else {
            Error(OutputModel("Это место занято"))
        }


    }

    override fun returnTicket(time : LocalDateTime, row : Int, num : Int) : Result {
        val sessions = sessionDaoImpl.getAllSessions()

        val session = sessions.find { it.time == time } ?: return Error(OutputModel("Нет такого сеанса"))

        session.allTickets.find{ it.seatRow == row && it.seatNum == num}
            ?: return Error(OutputModel("Нет такого проданного билета"))

        val temp = session.allTickets.toMutableList()
        temp.remove(Ticket(session.id, row, num))
        session.allTickets = temp

        jsonS.saveToFile(sessions, "schedule.json")

       // session.allTickets.remove(Ticket(session.id, row, num))
        return Success
    }

}