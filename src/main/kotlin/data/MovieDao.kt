package data

import domain.entity.Movie
import presentation.model.OutputModel
import repository.MovieJsonRepository

interface MovieDao {
    fun addMovie(movie: Movie)
    fun deleteMovie(movie: Movie)
    fun getAllMovies(): List<Movie>
    fun changeMovieTitle(id: Int, newTitle: String): OutputModel
    fun changeMovieDirector(id: Int, newDirector: String): OutputModel
    fun changeMovieDuration(id: Int, newDuration: Int): OutputModel
}

class MovieDaoImpl(private val path: String) : MovieDao {

    private val jsonM = MovieJsonRepository()

    companion object {
        private var counter: Int = 0;
    }

    override fun addMovie(movie: Movie) {
        val movies = jsonM.loadFromFile(path)
        val temp = movies.toMutableList()
        temp.add(movie)
        movie.id = ++counter

        jsonM.saveToFile(temp, "movies.json")
    }

    override fun deleteMovie(movie: Movie) {
        val movies = jsonM.loadFromFile(path)
        val temp = movies.toMutableList()
        temp.remove(movie)

        jsonM.saveToFile(temp, "movies.json")
    }

    override fun getAllMovies(): List<Movie> {
        return jsonM.loadFromFile(path)
    }

    override fun changeMovieTitle(id: Int, newTitle: String): OutputModel {
        val movies = jsonM.loadFromFile(path)
        val movie = movies.find { it.id == id }

        return if (movie != null) {
            movie.title = newTitle
            jsonM.saveToFile(movies, "movies.json")
            OutputModel("Название успешно поменяно")
        } else {
            OutputModel("В прокате нет такого фильма")
        }
    }

    override fun changeMovieDirector(id: Int, newDirector: String): OutputModel {
        val movies = jsonM.loadFromFile(path)
        val movie = movies.find { it.id == id }
        return if (movie != null) {
            movie.director = newDirector
            jsonM.saveToFile(movies, "movies.json")
            OutputModel("Режиссер успешно поменян")
        } else {
            OutputModel("В прокате нет такого фильма")
        }
    }

    override fun changeMovieDuration(id: Int, newDuration: Int): OutputModel {
        val movies = jsonM.loadFromFile(path)
        val movie = movies.find { it.id == id }
        return if (movie != null) {
            movie.duration = newDuration
            jsonM.saveToFile(movies, "movies.json")
            OutputModel("Длительность успешно поменяна")
        } else {
            OutputModel("В прокате нет такого фильма")
        }
    }

}