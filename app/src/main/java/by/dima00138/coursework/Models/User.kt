package by.dima00138.coursework.Models

data class User(var uid : String = "",
                val fullName: String = "",
                val passport: String = "",
                val birthdate: String = "",
                val email: String = "",
                val password: String = "",
                val role: String = ""
) : IModel