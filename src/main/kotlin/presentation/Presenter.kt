package presentation
import data.RuntimeCinemaDao
import domain.CinemaValidatorImpl
import domain.entity.Movie
import domain.entity.Session
import kotlinx.datetime.Month
import readMovie
import readTime

interface Presenter {
    fun showSchedule()
    fun showScheduleOnDate(day : Int , month: Int)
    fun showCinemaHall(session: Session)
    fun showCinemaHallOnSession()
    fun showMovies()
}

class RuntimePresenter(private val schedule : MutableList<Session>, private val movies : MutableList<Movie>) : Presenter {
    //private val runCinema = RuntimeCinemaDao()
    //private val schedule = runCinema.getSchedule()
    //private val movies = runCinema.getMovies()

    override fun showSchedule() {
        val sortedSchedule = schedule.sortedWith(compareBy { it.time })
        for (session in schedule) {
            println(session)
        }
    }

    override fun showScheduleOnDate(day : Int , month: Int) {
        val sortedSchedule = schedule.sortedWith(compareBy { it.time })
        for (session in schedule) {
            if (session.time.dayOfMonth == day && session.time.month == Month.of(month)) {
                println(session)
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
        val validator = CinemaValidatorImpl()

        println("Какой фильм интересует?")
        val movie = readMovie(movies) ?: return

        val time = readTime(schedule, validator)
        //LocalDateTime.of(2023, Month.DECEMBER, 16, 18, 30)

        val session = schedule.find { it.movie == movie && it.time == time }
        if (session == null) {
            println("")
            return
        }

        println("Зал во время выбранного сеанса:")
        showCinemaHall(session)
    }

    override fun showMovies() {
        for (movie in movies) {
            println(movie)
        }
    }

}