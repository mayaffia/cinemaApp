import data.RuntimeCinemaDao
import data.RuntimeMovieDao
import data.RuntimeSessionDao
import domain.SessionController
import domain.SessionControllerImpl
import domain.entity.Movie
import domain.entity.Session
import kotlinx.datetime.LocalDate
import presentation.Presenter
import presentation.RuntimePresenter
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.Month
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    val runCinema = RuntimeCinemaDao()
    val schedule = runCinema.getSchedule()
    val movies = runCinema.getMovies()

    val sessionController = SessionControllerImpl(schedule, movies)
    val runSession = RuntimeSessionDao(schedule, movies)
    val runMovie = RuntimeMovieDao(movies)

    val presenter = RuntimePresenter(schedule, movies)

    //val movies = cinema.getMovieList()

    /*movies.add(Movie("title", "dir", 10))
    sessionController.addNewSession(
        LocalDateTime.of(2023, Month.DECEMBER, 16, 18, 30),
        Movie("title", "dir", 10), schedule, movies
    )
    movies.add(Movie("title2", "dir", 10))
    sessionController.addNewSession(
        LocalDateTime.of(2023, Month.DECEMBER, 17, 18, 30),
        Movie("title2", "dir", 10), schedule, movies
    )*/

    var oper = ""
    while (true) {
        printMenu()
        oper = readln()
        if (oper == "10") {
            exitProcess(0)
        }
        when (oper) {
            "1" -> {
                sessionController.sellTicket()
            }
            "2" -> {
                sessionController.returnTicket()
            }
            "3" -> {
                presenter.showCinemaHallOnSession()
            }
            "4" -> {
                runMovie.editMovieData()
            }
            "5" -> {
                runSession.editSchedule()
            }
            "6" -> {
                println("Введите название фильма:")
                val title = readln()
                println("Введите режиссера:")
                val director = readln()
                println("Введите длительность:")
                val duration = readln().toInt()
                runMovie.addMovie(Movie(title, director, duration))
            }
            "7" -> {
                presenter.showSchedule()
            }
            "8" -> {
                println("На какую дату показать расписание? (введите в формате dd.MM)")
                val date = readln().split(".")
                val day = date[0].toInt()
                val month = date[1].toInt()
                presenter.showScheduleOnDate(day, month)
            }
            "9" -> {
                presenter.showMovies()
            }
        }
    }
}



fun printMenu() {
    println(".....................................")
    println("Выберите операцию из меню. Нажмите:")
    println("1 - посетитель хочет приобрести билет")
    println("2 - осуществить возврат билета")
    println("3 - посмотреть зал для выбранного сеанса")
    println("4 - редактировать данные о фильме")
    println("5 - редактировать расписание")
    println("6 - добавить фильм в прокат")
    println("7 - показать все расписание")
    println("8 - показать расписание на конкретную дату")
    println("9 - показать все фильмы в прокате")
    println("10 - выход")
}


fun readMovie(movies: MutableList<Movie>): Movie? {
    val title = readln()
    val movie = movies.find { it.title == title }
    if (movie == null) {
        println("В прокате нет такого фильма")
    }
    return movie
}

fun readTime(schedule: MutableList<Session>): kotlinx.datetime.LocalDateTime {

    println("Дата сеанса?(введите в формате dd.MM)")
    var date = readln()
    val patternDate = Regex("^([0-2]\\d|3[0-1])\\.(0\\d|1[0-2])$")

    while(!patternDate.matches(date)) {
        println("Введены некорректные данные. Повторите ввод")
        date = readln()
    }
    val dateSplit = date.split(".")
    val day = dateSplit[0].toInt()
    val month = dateSplit[1].toInt()


    println("Время сеанса?(введите в формате HH:mm)")
    var timeSession = readln()
    val patternTime = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
    while (!patternTime.matches(timeSession)) {
        println("Введены некорректные данные. Повторите ввод")
        timeSession = readln()
    }
    val timeSplit = timeSession.split(":")
    val hour = timeSplit[0].toInt()
    val minute = timeSplit[1].toInt()
    //readTime()
    //LocalDateTime.of(2023, Month.DECEMBER, 16, 18, 30)
    try {
        LocalDateTime.of(2023, Month.of(month), day, hour, minute)
        //return LocalDateTime.of(2023, Month.of(month), day, hour, minute)
    } catch (e: DateTimeException) {
        println("Введена некорректная дата")
    }
    //return LocalDateTime.of(2023, Month.of(month), day, hour, minute)
    return kotlinx.datetime.LocalDateTime(2023, month, day, hour, minute)
    //return LocalDateTime.of(2023, 12, day, hour, minute)
}
