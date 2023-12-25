import data.MovieDao
import data.MovieDaoImpl
import di.DI.cinemaController
import di.DI.cinemaValidator
import di.DI.movieController
import di.DI.movieDaoImpl
import di.DI.presenter
import di.DI.sessionController
import domain.CinemaControllerImpl
import domain.CinemaValidatorImpl
import domain.SessionControllerImpl
import domain.Success
import domain.entity.Movie
import kotlinx.datetime.LocalDate
import kotlin.system.exitProcess

fun main() {
    try {
        val movieDao = movieDaoImpl
        val sessionController = sessionController
        val cinemaController = cinemaController
        val movieController = movieController
        val presenter = presenter
        val validator = cinemaValidator

        var oper: String
        while (true) {
            printMenu()
            oper = readln()
            if (oper == "11") {
                exitProcess(0)
            }
            when (oper) {
                "1" -> {
                    sellTicket(cinemaController, validator, movieDao)
                }

                "2" -> {
                    returnTicket(cinemaController, validator)
                }

                "3" -> {
                    presenter.showCinemaHallOnSession()
                }

                "4" -> {
                    editMovieData(movieDao)
                }

                "5" -> {
                    editSchedule(validator, sessionController, movieDao)
                }

                "6" -> {
                    addMovie(movieDao)
                }

                "7" -> {
                    presenter.showSchedule()
                }

                "8" -> {
                    println("На какую дату показать расписание? (введите в формате dd.MM)")
                    val date = readln()

                    if (validator.validateDate(date) == Success) {
                        val dateSplit = date.split(".")
                        val day = dateSplit[0].toInt()
                        val month = dateSplit[1].toInt()
                        try {
                            LocalDate(2023, month, day)
                            presenter.showScheduleOnDate(day, month)
                        } catch (_: IllegalArgumentException) {
                            println("Некорректая дата")
                        }
                    } else {
                        println("Некорректая дата")
                    }
                }

                "9" -> {
                    presenter.showMovies()
                }

                "10" -> {
                    println("Введите название фильма:")
                    val movie = readMovie(movieDao.getAllMovies()) ?: return
                    movieController.deleteMovie(movie)
                    println("Фильм был успешно удален")
                }
            }
        }
    } catch (_: Exception) {
        println("Ошибка")
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
    println("10 - удалить фильм из проката")
    println("11 - выход")
}

fun addMovie(movieDao: MovieDao) {
    println("Введите название фильма:")
    val title = readln()
    println("Введите режиссера:")
    val director = readln()
    println("Введите длительность:")
    val duration = readln().toIntOrNull()
    if (duration == null) {
        println("Некорректная длительность")
        return
    }
    movieDao.addMovie(Movie(title, director, duration))
}

fun sellTicket(
    cinemaController: CinemaControllerImpl, validator: CinemaValidatorImpl,
    movieDao: MovieDao
) {
    val movies = movieDao.getAllMovies()

    println("На какой фильм поситетель хочет приобрести билет?")
    val movie = readMovie(movies) ?: return
    val time = readTime(validator) ?: return

    println("Желаемое место?(ряд и номер места через пробел)")
    val seat = readln().split(" ")
    val row = seat[0].toIntOrNull()
    val num = seat[1].toIntOrNull()

    if (row != null && num != null) {
        val res = cinemaController.sellTicket(movie, time, row, num)
        println(res.message)
    } else {
        println("некоректные места")
    }
}

fun returnTicket(cinemaController: CinemaControllerImpl, validator: CinemaValidatorImpl) {

    val time = readTime(validator) ?: return
    println("Введите ряд места:")
    val row = readln().toIntOrNull()
    println("Введите номер места:")
    val num = readln().toIntOrNull()

    if (row != null && num != null) {
        val res = cinemaController.returnTicket(time, row, num)
        println(res.message)
    } else {
        println("Некорректные места")
    }

}

fun readMovie(movies: List<Movie>): Movie? {
    val title = readln()

    val movie = movies.find { it.title == title }
    if (movie == null) {
        println("В прокате нет такого фильма")
    }
    return movie
}

fun readTime(validator: CinemaValidatorImpl): kotlinx.datetime.LocalDateTime? {
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

    try {
        if (validator.validateDate(date) == Success && validator.validateTime(timeSession) == Success) {
            return kotlinx.datetime.LocalDateTime(2023, month, day, hour, minute)
        }
    } catch (_: IllegalArgumentException) {
        println("Неверная дата")
    }

    println("Неверная дата")
    return null
}

fun editSchedule(
    validator: CinemaValidatorImpl, sessionController: SessionControllerImpl,
    movieDao: MovieDao
) {
    val movies = movieDao.getAllMovies()

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
            val time = readTime(validator) ?: return

            val res = sessionController.addNewSession(movie, time)
            if (res == Success) {
                println("Новый сеанс был успешно добавлен")
            } else {
                println(res)
            }
        }

        "2" -> {
            println("Введите название фильма:")
            val movie = readMovie(movies) ?: return
            println("Введите старое время сеанса:")
            val time = readTime(validator) ?: return
            println("Введите время сеанса, на которое хотите поменять:")
            val newTime = readTime(validator) ?: return

            val res = sessionController.changeSessionTime(movie, time, newTime)
            if (res == Success) {
                println("Время сеанса было успешно изменено")
            } else {
                println(res)
            }

        }

        "3" -> {
            println("Введите название фильма:")
            val movie = readMovie(movies) ?: return
            println("Введите время сеанса:")
            val time = readTime(validator) ?: return
            val res = sessionController.deleteSession(movie, time)
            if (res == Success) {
                println("Сеанс был успешно удален")
            } else {
                println(res)
            }
        }
    }
}


fun editMovieData(movieDao: MovieDaoImpl) {
    val movies = movieDao.getAllMovies()

    println("Введите название фильма, данные которого хотите поменять:")
    val title = readln()
    val movie = movies.find { it.title == title }
    if (movie == null) {
        println("В прокате нет такого фильма")
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
            println(movieDao.changeMovieTitle(movie.id, newName).message)
        }

        "2" -> {
            println("Введите нового режиссера:")
            val newDirector = readln()
            println(movieDao.changeMovieDirector(movie.id, newDirector).message)
        }

        "3" -> {
            println("Введите новую длительность:")
            val newDuration = readln().toIntOrNull()
            if (newDuration == null) {
                println("Неккоректная длительность")
                return
            }
            println(movieDao.changeMovieDuration(movie.id, newDuration).message)
        }
    }
}