package data

import domain.entity.Cinema
import domain.entity.Movie
import domain.entity.Session

interface CinemaDao {
    fun getSchedule() : MutableList<Session>
    fun getMovies() : MutableList<Movie>
    fun changeSchedule()
}

class RuntimeCinemaDao() : CinemaDao {
    private val schedule : MutableList<Session> = mutableListOf()
    private val movies : MutableList<Movie> = mutableListOf()

    override fun getSchedule() : MutableList<Session> {
        return schedule
    }

    override fun getMovies() : MutableList<Movie>{
        return movies
    }

    override fun changeSchedule() {
        TODO("Not yet implemented")
    }

}
