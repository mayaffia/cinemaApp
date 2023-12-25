package data

import domain.entity.Session
import repository.SessionJsonRepository


interface SessionDao {
    fun getAllSessions() : List<Session>

    fun getSession(id : Int) : Session?

    fun addSession(session: Session)

    //fun deleteSession()

}

class SessionDaoImpl(private val path : String) : SessionDao {

    private val jsonS = SessionJsonRepository()

    private val sessions: List<Session>
        get() = jsonS.loadFromFile(path)

    //private var sessions = jsonS.loadFromFile(path)


    companion object {
        private var counter : Int = 0;
    }
  //  private var counter = 0

    override fun getAllSessions() : List<Session> {
        return sessions
    }

    override fun getSession(id: Int) : Session? {
        return sessions.find { it.id == id }
    }

    override fun addSession(session: Session) {
        val temp = sessions.toMutableList()
        temp.add(session)
        session.id = ++counter

        jsonS.saveToFile(temp, "schedule.json")
    }

    /*override fun addNewSession(movie : Movie, time: kotlinx.datetime.LocalDateTime) : Result {
        val session = sessions.find { it.time == time }
        val m = movies.find { it == movie } ?: return Error(OutputModel("В прокате нет такого фильма"))

        return when {
            session != null -> Error(OutputModel("На это время уже стоит другой сеанс"))
            else -> {
                val newSession = Session(time, movie)
                newSession.id = ++counter

                val temp = sessions.toMutableList()
                temp.add(newSession)
                sessions = temp
               // schedule.add(newSession)
                Success
            }
        }
    }

    override fun deleteSession(movie : Movie, time: kotlinx.datetime.LocalDateTime) : Result {
        val session = sessions.find { it.time == time }

        return when {
            session == null -> Error(OutputModel("Такого сеанса нет"))
            else -> {

                val temp = sessions.toMutableList()
                temp.remove(session)
                sessions = temp

                //sessions.remove(session)
                Success
            }
        }
    }

    override fun changeSessionTime(movie : Movie, time: kotlinx.datetime.LocalDateTime, newTime: kotlinx.datetime.LocalDateTime) : Result{

        val session = sessions.find { it.time == time } ?: return Error(OutputModel("Такого сеанс нет"))


        val temp = sessions.toMutableList()
        temp.remove(session)
        sessions = temp

        //sessions.remove(session)
        val sessionChanged = sessions.find { it.time == newTime }

        return when {
            sessionChanged != null -> Error(OutputModel("На это время уже стоит другой сеанс"))
            else -> {
                addNewSession(movie, newTime)
                Success
            }
        }
    }


    override fun addTicket(session: Session, ticket: Ticket) {
        session.allTickets.add(ticket)
        session.countOfFreeSeats--
    }

    override fun isSeatFree(session: Session, desiredRow : Int, desiredNum : Int) : Boolean {
        for (ticket in session.allTickets) {
            if (ticket.seatRow == desiredRow && ticket.seatNum == desiredNum) {
                return false
            }
        }
        return true
    }*/

}