package domain
import data.RuntimeSessionDao
import domain.entity.Movie
import domain.entity.Session
import domain.entity.Ticket
import kotlinx.datetime.LocalDateTime
import presentation.RuntimePresenter
import readTime
import domain.Error
import domain.Success
import domain.Result
import presentation.model.OutputModel

interface CinemaController {
    fun sellTicket(movie : Movie, time : LocalDateTime, row : Int, num : Int) : Result
    fun returnTicket(time : LocalDateTime, row : Int, num : Int) : Result
}

class CinemaControllerImpl(private val schedule : MutableList<Session>, private val movies : MutableList<Movie>) : CinemaController {
    private val presenter = RuntimePresenter(schedule, movies)
    private val runSession = RuntimeSessionDao(movies)

    override fun sellTicket(movie : Movie, time : LocalDateTime, row : Int, num : Int) : Result {

        val session = schedule.find { it.movie == movie && it.time == time } ?: return Error(OutputModel("Нет такого сеанса"))

        if (runSession.isSeatFree(session, row, num)) {
            val ticket = Ticket(session.id, row, num)
            runSession.addTicket(session, ticket)
            return Success
        } else {
            return Error(OutputModel("Это место занято"))
        }

    }

    override fun returnTicket(time : LocalDateTime, row : Int, num : Int) : Result {

        val session = schedule.find { it.time == time } ?: return Error(OutputModel("Нет такого сеанса"))

        val t = session.allTickets.find{ it.seatRow == row && it.seatNum == num}
            ?: return Error(OutputModel("Нет такого проданного билета"))

        session.allTickets.remove(Ticket(session.id, row, num))
        return Success
    }

}