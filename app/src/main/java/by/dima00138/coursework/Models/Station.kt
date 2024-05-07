package by.dima00138.coursework.Models

data class Station(
    val id: String = "",
    val name: String = ""
) : IModel {
    override fun toFirebase(): Any {
        return StationFirebase(
            id, name
        )
    }
}

data class StationFirebase(
    val id: String = "",
    val name: String = ""
)