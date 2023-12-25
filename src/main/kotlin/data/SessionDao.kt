package data

import domain.entity.Session
import repository.SessionJsonRepository


interface SessionDao {
    fun getAllSessions(): List<Session>

    fun getSession(id: Int): Session?

    fun addSession(session: Session)

    fun delete(session: Session)

}

class SessionDaoImpl(private val path: String) : SessionDao {

    private val jsonS = SessionJsonRepository()

    private val sessions: List<Session>
        get() = jsonS.loadFromFile(path)

    companion object {
        private var counter: Int = 0
    }

    override fun getAllSessions(): List<Session> {
        return sessions
    }

    override fun getSession(id: Int): Session? {
        return sessions.find { it.id == id }
    }

    override fun addSession(session: Session) {
        val temp = sessions.toMutableList()
        temp.add(session)
        session.id = ++counter

        jsonS.saveToFile(temp, "schedule.json")
    }

    override fun delete(session: Session) {
        val temp = sessions.toMutableList()
        temp.remove(session)

        jsonS.saveToFile(temp, "schedule.json")
    }

}