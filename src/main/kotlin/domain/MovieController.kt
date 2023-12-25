package domain

import data.MovieDaoImpl
import data.SessionDaoImpl
import domain.entity.Movie

interface MovieController {
    fun deleteMovie(movie: Movie)
}

class MovieControllerImpl(private val sessionDaoImpl: SessionDaoImpl,
                          private val movieDaoImpl: MovieDaoImpl,
                          private val sessionController: SessionController
) : MovieController {
    override fun deleteMovie(movie : Movie) {
        val sessions = sessionDaoImpl.getAllSessions()

        movieDaoImpl.delete(movie)

        for (session in sessions) {
            if (session.movie == movie) {
                sessionController.deleteSession(session.movie, session.time)
            }
        }
    }

}