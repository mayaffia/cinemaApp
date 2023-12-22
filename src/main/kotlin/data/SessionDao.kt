package data

import domain.Error
import domain.Success
import domain.Result
import domain.entity.Cinema
import domain.entity.Movie
import domain.entity.Session
import domain.entity.Ticket
import presentation.model.OutputModel


interface SessionDao {
    fun addSession(session: Session)
    fun deleteSession(movie : Movie, time: kotlinx.datetime.LocalDateTime) : Result
    fun changeSessionTime(movie : Movie, time: kotlinx.datetime.LocalDateTime,
                          newTime: kotlinx.datetime.LocalDateTime) : Result
    fun changeSessionMovie()
    fun addTicket(session: Session, ticket: Ticket)
    fun isSeatFree(session: Session, desiredRow: Int, desiredNum: Int) : Boolean
    fun addNewSession(movie : Movie, time: kotlinx.datetime.LocalDateTime) : Result
}

class RuntimeSessionDao(private val schedule : MutableList<Session>, private val movies : MutableList<Movie>) : SessionDao {

    private val runCinema = RuntimeCinemaDao()
    private var counter = 0

    override fun addSession(session: Session) {
        if (movies.find { it == session.movie } == null) {
            println("В прокате нет такого фильма")
            return
        }
        schedule.add(session)
    }


    override fun addNewSession(movie : Movie, time: kotlinx.datetime.LocalDateTime) : Result {
        val session = schedule.find { it.time == time }

        return when {
            session != null -> Error(OutputModel("На это время уже стоит другой сеанс"))
            else -> {
                addSession(Session(time , movie))
                Success
            }
        }
    }


    override fun deleteSession(movie : Movie, time: kotlinx.datetime.LocalDateTime) : Result {
        val session = schedule.find { it.time == time }

        return when {
            session == null -> Error(OutputModel("Такого сеанса нет"))
            else -> {
                schedule.remove(session)
                Success
            }
        }
    }

    override fun changeSessionTime(movie : Movie, time: kotlinx.datetime.LocalDateTime, newTime: kotlinx.datetime.LocalDateTime) : Result{

        val session = schedule.find { it.time == time } ?: return Error(OutputModel("Такого сеанс нет"))

        schedule.remove(session)
        val sessionChanged = schedule.find { it.time == newTime }

        return when {
            sessionChanged != null -> Error(OutputModel("На это время уже стоит другой сеанс"))
            else -> {
                addSession(Session(newTime, movie))
                Success
            }
        }
    }

    override fun changeSessionMovie() {
        TODO("Not yet implemented")
    }

    override fun addTicket(session: Session, ticket: Ticket) {
        session.allTickets.add(ticket)
        session.countOfFreeSeats--
    }

    override fun isSeatFree(session: Session, desiredRow : Int, desiredNum : Int) : Boolean {
        for (ticket in session.allTickets) {
            if (ticket.seatRow == desiredRow && ticket.seatNum == desiredNum) {
                return false
            }
        }
        return true
    }

}