package data

import domain.entity.Cinema
import domain.entity.Movie
import domain.entity.Session
import domain.entity.Ticket
import readMovie
import readTime
import java.time.LocalDateTime

interface SessionDao {
    fun addSession(session: Session)
    fun deleteSession()
    fun changeSessionTime()
    fun changeSessionMovie()
    fun addTicket(session: Session, ticket: Ticket)
    fun isSeatFree(session: Session, desiredRow: Int, desiredNum: Int) : Boolean
    fun editSchedule()
    fun addNewSession()
}

class RuntimeSessionDao(private val schedule : MutableList<Session>, private val movies : MutableList<Movie>) : SessionDao {

    private val runCinema = RuntimeCinemaDao()
    //private val schedule = runCinema.getSchedule()
   // private val movies = runCinema.getMovies()
    private var counter = 0

    override fun addSession(session: Session) {
        if (movies.find { it == session.movie } == null) {
            println("В прокате нет такого фильма")
            return
        }
        schedule.add(session)
    }


    override fun addNewSession() {
        println("Введите название фильма:")
        val movie = readMovie(movies) ?: return
        println("Введите время сеанса:")
        val time = readTime(schedule)
        var session = schedule.find { it.time == time }
        if (session != null) {
            println("На это время уже стоит другой сеанс")
            return
        }

        session = Session(time, movie)
        addSession(session)

        println("Новый сеанс был успешно добавлен")

    }


    override fun deleteSession() {
        println("Введите название фильма:")
        val movie = readMovie(movies) ?: return
        println("Введите время сеанса:")
        val time = readTime(schedule)
        val session = schedule.find { it.time == time }
        if (session == null) {
            println("Такого сеанса нет")
            return
        }
        schedule.remove(session)
        println("Сеанс был успешно удален")
    }

    override fun changeSessionTime() {
        println("Введите название фильма:")
        val movie = readMovie(movies) ?: return
        println("Введите старое время сеанса:")
        val time = readTime(schedule)
        val session = schedule.find { it.time == time }
        if (session == null) {
            println("Такого сеанс нет")
        }
        schedule.remove(session)
        println("Введите время сеанса, на которое хотите поменять:")

        val newTime = readTime(schedule)
        val sessionChanged = schedule.find { it.time == newTime }
        if (sessionChanged != null) {
            println("На это время уже стоит другой сеанс")
            return
        }

        addSession(Session(newTime, movie))
        println("Время сеанса было успешно изменено")
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

    override fun editSchedule() {
        println("Нажмите:")
        println("1 - чтобы добавить сеанс")
        println("2 - чтобы поменять время сеанса")
        println("3 - чтобы удалить сеанс")

        val oper = readln()

        when (oper) {
            "1" -> {
                addNewSession()
            }

            "2" -> {
                changeSessionTime()
            }

            "3" -> {
                deleteSession()
            }
        }
    }


}