package by.dima00138.coursework.Models

data class Ticket(
    val id: String = "",
    val train: String = "",
    val isFree: String = "true",
) : IModel {
    override fun toFirebase(): Any {
        return TicketFirebase(
            id = id,
            train = train,
            isFree = isFree
        )
    }
}

data class TicketFirebase(
    val id: String = "",
    val train: String = "",
    val isFree: String = "true",
)