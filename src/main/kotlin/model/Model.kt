package model

import kotlin.collections.HashMap


data class Answer(
        val answer: String
)

data class AnswerUser(
        val answer: String
)

enum class Role {
    USER,
    ADMIN,
    TUTOR,
    UNDEFINED,
    PLAYER
}

data class Player(
        val password: String,
        val login: String,
        override val role: String
) : Position

data class User(
        val name: String,
        val password: String,
        val login: String,
        val birthday: String,
        val age: String,
        val city: String,
        val role: Role

)

data class Statistic(
        val name: HashMap<Answer, Answer>
)

interface Position {
    val role: String
}

fun setPlayer(login: String, password: String) = Player(
        login = login,
        password = password,
        role = when {
            login == "user" && password == "user" -> Role.USER.name
            login == "player" && password == "player" -> Role.PLAYER.name
            login == "admin" && password == "admin" -> Role.ADMIN.name
            login == "tutor" && password == "tutor" -> Role.TUTOR.name
            else -> throw Exception("Role Undefined")
        }
)

val users = listOf(saveAdmin(), savePlayer(), saveTutor(), saveUser())

private fun saveUser() =
        User(
                name = "Vlad",
                password = "user",
                login = "user",
                age = "24",
                birthday = "11.09.1996",
                city = "Minsk",
                role = Role.USER
        )

private fun saveAdmin() =
        User(
                name = "Vladimir",
                password = "admin",
                login = "admin",
                age = "23",
                birthday = "04.12.1996",
                city = "Minsk",
                role = Role.ADMIN
        )

private fun savePlayer() =
        User(
                name = "Egor",
                password = "player",
                login = "player",
                age = "20",
                birthday = "01.07.1996",
                city = "Minsk",
                role = Role.PLAYER
        )

private fun saveTutor() =
        User(
                name = "Evgeniy",
                password = "tutor",
                login = "tutor",
                age = "21",
                birthday = "01.01.1999",
                city = "Minsk",
                role = Role.TUTOR
        )
