package domain
import data.RuntimeSessionDao
import domain.entity.Movie
import domain.entity.Session
import domain.entity.Ticket
import kotlinx.datetime.LocalDateTime
import presentation.RuntimePresenter
import readTime

interface CinemaController {
    fun sellTicket(movie : Movie, time : LocalDateTime)
    //fun returnTicket()
}

class CinemaControllerImpl(private val schedule : MutableList<Session>, private val movies : MutableList<Movie>) : CinemaController {
    //private val runCinema = RuntimeCinemaDao()
    //private val schedule = runCinema.getSchedule()
    //private val movies = runCinema.getMovies()
    private val presenter = RuntimePresenter(schedule, movies)
    private val runSession = RuntimeSessionDao(schedule, movies)

    override fun sellTicket(movie : Movie, time : LocalDateTime) {
        //println("На какой фильм поситетель хочет приобрести билет?")
        //val movie = readMovie(movies) ?: return

        //val time = readTime(schedule)
        val session = schedule.find { it.movie == movie && it.time == time }
        if (session == null) {
            println("")
            return
        }


        println("Желаемое место?(ряд и номер места через пробел)")
        var seat = readln().split(" ")
        var row = seat[0].toInt()
        var num = seat[1].toInt()


        if (runSession.isSeatFree(session, row, num)) {
            val ticket = Ticket(session.id, row, num)
            runSession.addTicket(session, ticket)
           // println("билет успешно продан")
            return
        }

        while (!runSession.isSeatFree(session, row, num)) {
            println("Это место занято. Можете посмотреть на зал и предложить ему другое место из свободных")
            presenter.showCinemaHall(session)
            println("Желаемое место?(ряд и номер места через пробел)")
            seat = readln().split(" ")
            row = seat[0].toInt()
            num = seat[1].toInt()

            if (runSession.isSeatFree(session, row, num)) {
                val ticket = Ticket(session.id, row, num)
                runSession.addTicket(session, ticket)
                println("билет успешно продан")
                return
            }
        }
    }

    /*override fun returnTicket() {
        val time = readTime(schedule)
        val session = schedule.find { it.time == time }
        if (session == null) {
            println("Нет такого сеанса")
            return
        }
        println("Введите ряд места:")
        val row = readln().toInt()
        println("Введите номер места:")
        val num = readln().toInt()

        session.allTickets.remove(Ticket(session.id, row, num))
        println("Возврат билета успешно произведен")
    }*/

}