package by.dima00138.coursework.Models

data class User(
    var id: String = "",
    val fullName: String = "",
    val passport: String = "",
    val birthdate: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "user"
) : IModel {
    override fun toFirebase() : UserFirebase  {
        return UserFirebase(
            id = id,
            fullName = fullName,
            passport = passport,
            birthdate = birthdate,
            email = email,
            password = password,
            role = role
            )
    }
}

data class UserFirebase(
    val id: String = "",
    val fullName: String = "",
    val passport: String = "",
    val birthdate: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "user"
)
