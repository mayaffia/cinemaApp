package domain

import domain.entity.Movie
import presentation.model.OutputModel


sealed class Result

data object Success : Result()
class Error(val outputModel: OutputModel) : Result()


interface CinemaValidator {
    fun validateMovieTitle(title: String, movies: MutableList<Movie>): Result

    fun validateDate(date: String): Result
    fun validateTime(time: String): Result
}

class CinemaValidatorImpl : CinemaValidator {
    override fun validateMovieTitle(title: String, movies: MutableList<Movie>): Result {
        val movie = movies.find { it.title == title }
        return when {
            movie == null -> Error(OutputModel("В прокате нет такого фильма"))
            else -> Success
        }
    }

    override fun validateDate(date: String): Result {
        val patternDate = Regex("^([0-2]\\d|3[0-1])\\.(0\\d|1[0-2])$")
        return when {
            !patternDate.matches(date) -> Error(OutputModel("Incorrect name"))
            else -> Success
        }
    }

    override fun validateTime(time: String): Result {
        val patternTime = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
        return when {
            !patternTime.matches(time) -> Error(OutputModel("Incorrect name"))
            else -> Success
        }
    }

}