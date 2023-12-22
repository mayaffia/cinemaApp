package data

import domain.entity.Movie
import domain.entity.Session

interface MovieDao {
    fun getMovies() : MutableList<Movie>
    fun addMovie(movie : Movie)
    fun deleteMovie(movie : Movie)
    fun getAllMovies(): List<Movie>
    fun changeMovieTitle(id : Int, newTitle : String)
    fun changeMovieDirector(id : Int, newDirector : String)
    fun changeMovieDuration(id : Int, newDuration : Int)
}

class RuntimeMovieDao() : MovieDao {
    private var counter = 0

    private val movies : MutableList<Movie> = mutableListOf()
    override fun getMovies() : MutableList<Movie>{
        return movies
    }

    override fun addMovie(movie: Movie) {
        movie.id = ++counter
        movies.add(movie)
    }

    override fun deleteMovie(movie: Movie) {
        TODO("Not yet implemented")
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