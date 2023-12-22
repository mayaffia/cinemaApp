import data.RuntimeCinemaDao
import data.RuntimeMovieDao
import data.RuntimeSessionDao
import domain.CinemaController
import domain.CinemaControllerImpl
import domain.CinemaValidatorImpl
import domain.Success
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

    val runMovie = RuntimeMovieDao()
    val movies = runMovie.getMovies()
    val runSession = RuntimeSessionDao(movies)
    val schedule = runSession.getSchedule()

    val cinemaController = CinemaControllerImpl(schedule, movies)

    val presenter = RuntimePresenter(schedule, movies)

    val validator = CinemaValidatorImpl()

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
                sellTicket(cinemaController, movies, schedule, validator)
            }
            "2" -> {
                returnTicket(cinemaController, validator, schedule)
            }
            "3" -> {
                presenter.showCinemaHallOnSession()
            }
            "4" -> {
                editMovieData(runMovie, movies)
            }
            "5" -> {
                editSchedule(runSession, validator, schedule, movies)
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


fun sellTicket(cinemaController : CinemaControllerImpl, movies: MutableList<Movie>,
               schedule: MutableList<Session>, validator: CinemaValidatorImpl) {
    println("На какой фильм поситетель хочет приобрести билет?")
    val movie = readMovie(movies) ?: return
    val time = readTime(schedule, validator)

    println("Желаемое место?(ряд и номер места через пробел)")
    val seat = readln().split(" ")
    val row = seat[0].toIntOrNull()
    val num = seat[1].toIntOrNull()

    if (time != null && row != null && num != null) {
        val res = cinemaController.sellTicket(movie, time, row, num)
        if (res == Success) {
            println("билет успешно продан")
        } else {
            println(res)    ////////CHECK
        }
    } else {
        println("incorrect time or seats not int")
    }
}

fun returnTicket(cinemaController : CinemaControllerImpl, validator: CinemaValidatorImpl, schedule: MutableList<Session>) {
    val time = readTime(schedule, validator)
    println("Введите ряд места:")
    val row = readln().toIntOrNull()
    println("Введите номер места:")
    val num = readln().toIntOrNull()

    if (time != null && row != null && num != null) {
        val res = cinemaController.returnTicket(time, row, num)
        if (res == Success) {
            println("Возврат билета успешно произведен")
        } else {
            println(res)    ////////CHECK
        }
    } else {
        println("incorrect time or seats not int")
    }


}
fun readMovie(movies: MutableList<Movie>): Movie? {
    val title = readln()

   // validator.validateMovieTitle(title, movies)

    val movie = movies.find { it.title == title }
    if (movie == null) {
        println("В прокате нет такого фильма")
    }
    return movie
}

fun readTime(schedule: MutableList<Session>, validator : CinemaValidatorImpl): kotlinx.datetime.LocalDateTime? {
    println("Дата сеанса?(введите в формате dd.MM)")
    val date = readln()

    var day = 0
    var month = 0
    var hour = 0
    var minute = 0

    if (validator.validateDate(date) == Success) {
        val dateSplit = date.split(".")
        day = dateSplit[0].toInt()
        month = dateSplit[1].toInt()
    }

    println("Время сеанса?(введите в формате HH:mm)")
    val timeSession = readln()

    if (validator.validateTime(timeSession) == Success) {
        val timeSplit = timeSession.split(":")
        hour = timeSplit[0].toInt()
        minute = timeSplit[1].toInt()
    }

    if (validator.validateDate(date) == Success && validator.validateTime(timeSession) == Success) {
        return kotlinx.datetime.LocalDateTime(2023, month, day, hour, minute)
    }
    return null
}

fun editSchedule(runSession: RuntimeSessionDao, validator: CinemaValidatorImpl, schedule: MutableList<Session>, movies: MutableList<Movie>) {
    println("Нажмите:")
    println("1 - чтобы добавить сеанс")
    println("2 - чтобы поменять время сеанса")
    println("3 - чтобы удалить сеанс")

    val oper = readln()

    when (oper) {
        "1" -> {
            println("Введите название фильма:")
            val movie = readMovie(movies) ?: return
            println("Введите время сеанса:")
            val time = readTime(schedule, validator)
            if (time != null) {
                val res = runSession.addNewSession(movie, time)
                if (res == Success) {
                    println("Новый сеанс был успешно добавлен")
                } else {
                    println(res)    ////////CHECK
                }
            } else {
                println("incorrect time")
            }
        }

        "2" -> {
            println("Введите название фильма:")
            val movie = readMovie(movies) ?: return
            println("Введите старое время сеанса:")
            val time = readTime(schedule, validator)
            println("Введите время сеанса, на которое хотите поменять:")
            val newTime = readTime(schedule, validator)
            if (time != null && newTime != null) {
                val res = runSession.changeSessionTime(movie, time, newTime)
                if (res == Success) {
                    println("Время сеанса было успешно изменено")
                } else {
                    println(res)    ////////CHECK
                }
            } else {
                println("incorrect time")
            }

        }

        "3" -> {
            println("Введите название фильма:")
            val movie = readMovie(movies) ?: return
            println("Введите время сеанса:")
            val time = readTime(schedule, validator)
            if (time != null) {
                val res = runSession.deleteSession(movie, time)
                if (res == Success) {
                    println("Сеанс был успешно удален")
                } else {
                    println(res)    ////////CHECK
                }
            } else {
                println("incorrect time")
            }

        }
    }
}


fun editMovieData(runMovie : RuntimeMovieDao, movies : MutableList<Movie>) {
    println("Введите название фильма, данные которого хотите поменять:")
    val title = readln()
    val movie = movies.find { it.title == title }
    if (movie == null) {
        println("")
        return
    }
    println("Нажмите:")
    println("1 - чтобы поменять название фильма")
    println("2 - чтобы поменять режиссера фильма")
    println("3 - чтобы поменять длительность фильма")


    val oper = readln()

    when (oper) {
        "1" -> {
            println("Введите новое название:")
            val newName = readln()
            runMovie.changeMovieTitle(movie.id, newName)
        }

        "2" -> {
            println("Введите нового режиссера:")
            val newDirector = readln()
            runMovie.changeMovieDirector(movie.id, newDirector)
        }

        "3" -> {
            println("Введите новую длительность:")
            val newDuration = readln().toInt()
            runMovie.changeMovieDuration(movie.id, newDuration)
        }
    }
}