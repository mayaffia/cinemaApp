package domain.entity

data class Cinema(val schedule : MutableList<Session>,
                  val movies: MutableList<Movie>)
