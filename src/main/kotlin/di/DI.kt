package di

import data.MovieDaoImpl
import data.SessionDaoImpl
import domain.CinemaControllerImpl
import domain.CinemaValidatorImpl
import domain.SessionControllerImpl
import presentation.PresenterImpl

object DI {
    private const val PATH_FOR_SESSIONS = "schedule.json"
    private const val PATH_FOR_MOVIES = "movies.json"


    val movieDaoImpl : MovieDaoImpl
        get() = MovieDaoImpl(PATH_FOR_MOVIES)

    val sessionDaoImpl : SessionDaoImpl
        get() = SessionDaoImpl(PATH_FOR_SESSIONS)

    val sessionController : SessionControllerImpl
        get() = SessionControllerImpl(sessionDaoImpl, movieDaoImpl)

    val cinemaController : CinemaControllerImpl
        get() = CinemaControllerImpl(
            sessionDaoImpl, movieDaoImpl, sessionController
        )

    val presenter : PresenterImpl
        get() = PresenterImpl(sessionDaoImpl, movieDaoImpl)

    val cinemaValidator : CinemaValidatorImpl
        get() = CinemaValidatorImpl()
}




/*object DI {

    private val bankAccountValidator: BankAccountValidator
        get() = BankAccountValidatorImpl()

    private val bankAccountDao: BankAccountDao by lazy {
        RuntimeBankAccountDao()
    }

    val bankAccountController: BankAccountController
        get() = BankAccountControllerImpl(
            bankAccountValidator = bankAccountValidator,
            bankAccountDao = bankAccountDao,
        )
}*/