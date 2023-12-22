package data

import domain.entity.Cinema
import domain.entity.Movie
import domain.entity.Session


//СКОРЕЕ ВСЕГО Я ЭТО НАХУЙ УДАЛЮ


interface CinemaDao {

    fun changeSchedule()
}

class RuntimeCinemaDao() : CinemaDao {


    override fun changeSchedule() {
        TODO("Not yet implemented")
    }

}
