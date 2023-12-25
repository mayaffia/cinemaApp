package data

import domain.entity.Movie
import repository.MovieJsonRepository

interface MovieDao {
    fun addMovie(movie : Movie)
    fun deleteMovie(movie : Movie)
    fun getAllMovies(): List<Movie>
    fun changeMovieTitle(id : Int, newTitle : String)
    fun changeMovieDirector(id : Int, newDirector : String)
    fun changeMovieDuration(id : Int, newDuration : Int)
}

class MovieDaoImpl(private val path : String) : MovieDao {

    private val jsonM = MovieJsonRepository()
    private val movies: List<Movie>
        get() = jsonM.loadFromFile(path)


    companion object {
        private var counter : Int = 0;
    }

    override fun addMovie(movie: Movie) {
        val temp = movies.toMutableList()
        temp.add(movie)
        movie.id = ++counter

        jsonM.saveToFile(temp, "movies.json")
    }

    override fun deleteMovie(movie: Movie) {
        TODO("Not yet implemented")   //тоже в файл сохр
    }

    override fun getAllMovies(): List<Movie> {
        return movies
    }

    override fun changeMovieTitle(id: Int, newTitle: String) {
        val movie = movies.find { it.id == id }
        if (movie != null) {
            movie.title = newTitle
        } else {
            //
        }
    }

    override fun changeMovieDirector(id: Int, newDirector: String) {
        val movie = movies.find { it.id == id }
        if (movie != null) {
            movie.director = newDirector
        } else {
            //
        }
    }

    override fun changeMovieDuration(id: Int, newDuration: Int) {
        val movie = movies.find { it.id == id }
        if (movie != null) {
            movie.duration = newDuration
        } else {
            //
        }
    }



}