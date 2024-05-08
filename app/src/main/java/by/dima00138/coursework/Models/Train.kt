package by.dima00138.coursework.Models

data class Train(
    val id: String = "",
    val schedule: String = "",
    val countOfSeats: String = "0",
    val countOfVans: String = "0"
) : IModel {
    override fun toFirebase(): Any {
        return TrainFirebase(
            id = id,
            schedule = schedule,
            countOfSeats = countOfSeats,
            countOfVans = countOfVans
        )
    }
}

data class TrainFirebase(
    val id: String = "",
    val schedule: String = "",
    val countOfSeats: String = "0",
    val countOfVans: String = "0"
)