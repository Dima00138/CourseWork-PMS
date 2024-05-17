package by.dima00138.coursework.Models

data class Ticket(
    val id: String = "",
    val train: String = "",
    val numberOfSeat: String = "",
    var free: String = "true",
) : IModel {
    override fun toFirebase(): Any {
        return TicketFirebase(
            id = id,
            train = train,
            numberOfSeat = numberOfSeat,
            free = free
        )
    }
}

data class TicketFirebase(
    val id: String = "",
    val train: String = "",
    val numberOfSeat: String = "",
    val free: String = "true",
)