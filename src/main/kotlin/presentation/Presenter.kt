package presentation
import data.MovieDaoImpl
import data.SessionDaoImpl
import domain.CinemaValidatorImpl
import domain.entity.Session
import kotlinx.datetime.Month
import readTime

interface Presenter {
    fun showSchedule()
    fun showScheduleOnDate(day : Int , month: Int)
    fun showCinemaHall(session: Session)
    fun showCinemaHallOnSession()
    fun showMovies()
}

class PresenterImpl(private val sessionDaoImpl: SessionDaoImpl,
                    private val movieDaoImpl : MovieDaoImpl) : Presenter {

    //private var sessions = sessionDaoImpl.getAllSessions()
    //private val movies = movieDaoImpl.getAllMovies()
    override fun showSchedule() {
        val sessions = sessionDaoImpl.getAllSessions()
        val sortedSchedule = sessions.sortedWith(compareBy { it.time })
        for (session in sortedSchedule) {
            println("Дата сеанса: ${session.time.date} | Время сеанса: ${session.time.time} |" +
                    " Фильм: ${session.movie.title} | " +
                    "Количество свободных мест: ${session.countOfFreeSeats}")
            for (ticket in session.allTickets) {
                println(ticket)
            }
        }
    }

    override fun showScheduleOnDate(day : Int , month: Int) {
        val sessions = sessionDaoImpl.getAllSessions()
        val sortedSchedule = sessions.sortedWith(compareBy { it.time })
        for (session in sortedSchedule) {
            if (session.time.dayOfMonth == day && session.time.month == Month.of(month)) {
                println("Дата сеанса: ${session.time.date} | Время сеанса: ${session.time.time} | " +
                        "Фильм: ${session.movie.title} | " +
                        "Количество свободных мест: ${session.countOfFreeSeats}")
               // println(session)
            }
        }
    }

    override fun showCinemaHall(session: Session) {
        val hall : MutableList<MutableList<Char>> = mutableListOf(mutableListOf('-', '-','-','-','-','-','-','-'),
            mutableListOf('-', '-','-','-','-','-','-','-'), mutableListOf('-', '-','-','-','-','-','-','-'),
            mutableListOf('-', '-','-','-','-','-','-','-',))
        for (ticket in session.allTickets) {
            hall[ticket.seatRow - 1][ticket.seatNum - 1] = '+'
        }
        for (row in hall) {
            println(row.joinToString(separator = " "))
        }
    }

    override fun showCinemaHallOnSession() {
        val sessions = sessionDaoImpl.getAllSessions()
        val validator = CinemaValidatorImpl()
        val time = readTime(sessions, validator)

        val session = sessions.find {it.time == time }
        if (session == null) {
            println("")
            return
        }

        println("Зал во время выбранного сеанса:")
        showCinemaHall(session)
    }

    override fun showMovies() {
        val movies = movieDaoImpl.getAllMovies()
        for (movie in movies) {
            println("Название: ${movie.title} | Режиссер: ${movie.director} | Длительность: ${movie.duration}")
            //println(movie)
        }
    }

}