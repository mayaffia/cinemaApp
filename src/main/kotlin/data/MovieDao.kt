package data

import domain.entity.Movie
import domain.entity.Session

interface MovieDao {
    fun addMovie(movie : Movie)
    fun deleteMovie(movie : Movie)
    fun getAllMovies(): List<Movie>
    fun changeMovieTitle(id : Int, newTitle : String)
    fun changeMovieDirector(id : Int, newDirector : String)
    fun changeMovieDuration(id : Int, newDuration : Int)
    fun editMovieData()
}

class RuntimeMovieDao(private val movies : MutableList<Movie>) : MovieDao {
    //private val runCinema = RuntimeCinemaDao()
   // private val movies = runCinema.getMovies()
    private var counter = 0

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

    override fun editMovieData() {
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
                changeMovieTitle(movie.id, newName)
            }

            "2" -> {
                println("Введите нового режиссера:")
                val newDirector = readln()
                changeMovieDirector(movie.id, newDirector)
            }

            "3" -> {
                println("Введите новую длительность:")
                val newDuration = readln().toInt()
                changeMovieDuration(movie.id, newDuration)
            }
        }
    }


}