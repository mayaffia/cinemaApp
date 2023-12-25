package domain

import data.MovieDaoImpl
import data.SessionDaoImpl
import domain.entity.Movie
import domain.entity.Session
import domain.entity.Ticket
import kotlinx.datetime.LocalDateTime
import presentation.model.OutputModel
import repository.SessionJsonRepository

const val COUNT_OF_SEATS = 24

interface SessionController {
    fun deleteSession(movie: Movie, time: LocalDateTime): Result
    fun changeSessionTime(
        movie: Movie, time: LocalDateTime,
        newTime: LocalDateTime
    ): Result

    fun addTicket(session: Session, ticket: Ticket)
    fun isSeatFree(session: Session, desiredRow: Int, desiredNum: Int): Boolean
    fun addNewSession(movie: Movie, time: LocalDateTime): Result
}

class SessionControllerImpl(
    private val sessionDaoImpl: SessionDaoImpl,
    private val movieDaoImpl: MovieDaoImpl
) : SessionController {

    private val jsonS = SessionJsonRepository()


    override fun deleteSession(movie: Movie, time: LocalDateTime): Result {
        val sessions = sessionDaoImpl.getAllSessions()

        val session = sessions.find { it.time == time }

        return when {
            session == null -> Error(OutputModel("Такого сеанса нет"))
            else -> {
                sessionDaoImpl.delete(session)
                Success
            }
        }
    }

    override fun changeSessionTime(movie: Movie, time: LocalDateTime, newTime: LocalDateTime): Result {
        val sessions = sessionDaoImpl.getAllSessions()

        val session = sessions.find { it.time == time } ?: return Error(OutputModel("Такого сеанс нет"))

        sessionDaoImpl.delete(session)

        val sessionChanged = sessions.find { it.time == newTime }

        return when {
            sessionChanged != null -> Error(OutputModel("На это время уже стоит другой сеанс"))
            else -> {
                addNewSession(movie, newTime)
                Success
            }
        }
    }

    override fun addTicket(session: Session, ticket: Ticket) {
        val sessions = sessionDaoImpl.getAllSessions()

        val temp = session.allTickets.toMutableList()
        temp.add(ticket)
        session.allTickets = temp
        session.countOfFreeSeats--

        jsonS.saveToFile(sessions, "schedule.json")

        //session.allTickets.add(ticket)
        // session.countOfFreeSeats--
    }

    override fun isSeatFree(session: Session, desiredRow: Int, desiredNum: Int): Boolean {
        for (ticket in session.allTickets) {
            if (ticket.seatRow == desiredRow && ticket.seatNum == desiredNum) {
                return false
            }
        }
        return true
    }


    override fun addNewSession(movie: Movie, time: LocalDateTime): Result {
        val sessions = sessionDaoImpl.getAllSessions()
        val movies = movieDaoImpl.getAllMovies()

        val session = sessions.find { it.time == time }
        movies.find { it == movie } ?: return Error(OutputModel("В прокате нет такого фильма"))

        return when {
            session != null -> Error(OutputModel("На это время уже стоит другой сеанс"))
            else -> {
                val newSession = Session(time, movie, listOf(), COUNT_OF_SEATS)

                sessionDaoImpl.addSession(newSession)
                Success
            }
        }
    }
}