package di

import data.MovieDaoImpl
import data.SessionDaoImpl
import domain.*
import presentation.PresenterImpl

object DI {
    private const val PATH_FOR_SESSIONS = "schedule.json"
    private const val PATH_FOR_MOVIES = "movies.json"


    val movieDaoImpl: MovieDaoImpl
        get() = MovieDaoImpl(PATH_FOR_MOVIES)

    private val sessionDaoImpl: SessionDaoImpl
        get() = SessionDaoImpl(PATH_FOR_SESSIONS)

    val sessionController: SessionControllerImpl
        get() = SessionControllerImpl(sessionDaoImpl, movieDaoImpl)

    val cinemaController: CinemaControllerImpl
        get() = CinemaControllerImpl(
            sessionDaoImpl, sessionController
        )

    val movieController : MovieControllerImpl
        get() = MovieControllerImpl(sessionDaoImpl, movieDaoImpl, sessionController)

    val presenter: PresenterImpl
        get() = PresenterImpl(sessionDaoImpl, movieDaoImpl)

    val cinemaValidator: CinemaValidatorImpl
        get() = CinemaValidatorImpl()
}



