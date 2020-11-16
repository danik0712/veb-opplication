package controller

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.thymeleaf.*
import model.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

fun main() {

    val server = embeddedServer(Netty, port = 9090) {

        val question1: String = "What is tag use for new line with space?"
        val question2: String = "Whats tags block?"

        var person = Person(name = null, age = null)
        var result = "0%"
        val answers: MutableList<Answer> = mutableListOf()

        routing {

            install(Thymeleaf) {
                setTemplateResolver(
                        ClassLoaderTemplateResolver().apply {
                            prefix = "pages/"
                            suffix = ".html"
                            characterEncoding = "utf-8"
                        }
                )
            }

            get("/") { call.respondFile(File("./src/main/resources/pages/main.html")) }

            post("/main") {
                val parameters = call.receiveParameters()
                val name = parameters["name"].toString()
                val age = parameters["age"].toString()
                person = Person(name = name, age = age)
                call.respond(ThymeleafContent("question1", mapOf("question" to question1)))
            }

            post("/answer1") {

                val parameters = call.receiveParameters()
                val answer = parameters["answer"].toString()

                answers.add(
                        Answer(
                                numberQuestion = "1",
                                answerCurrent = answer,
                                correctAnswer = "p",
                                question = question1)
                )

                call.respond(ThymeleafContent("question2", mapOf("question" to question2)))
            }

            post("/answer2") {

                val parameters = call.receiveParameters()
                val answer = parameters["answer"].toString()

                answers.add(
                        Answer(
                                numberQuestion = "2",
                                answerCurrent = answer,
                                correctAnswer = "div, p, ul, ol",
                                question = question2
                        )

                )

                call.respond(
                        ThymeleafContent("end",
                                mapOf("user" to person, "answer" to answers, "result" to getResult(answers)))
                )

                answers.clear()

            }

            post("/sign") {
                val parameters = call.receiveParameters()

                val password = parameters["password"].toString()
                val login = parameters["login"].toString()

                val user = setPlayer(login = login, password = password)

                val foundedPlayer = users.single { it.role.name == user.role }

                call.respond(ThymeleafContent("user", mapOf("user" to foundedPlayer)))

            }

            //get("/reg") { call.respondFile(File("./src/main/resources/pages/registr.html")) }

            post("/data") {

                val parameters = call.receiveParameters()

                val name = parameters["name"].toString()
                val password = parameters["password"].toString()
                val login = parameters["login"].toString()
                val city = parameters["city"].toString()
                val birthday = parameters["birthday"].toString()
                val age = parameters["age"].toString()
                val role = parameters["role"].toString()

                val user = User(
                        name = name,
                        password = password,
                        age = age,
                        city = city,
                        birthday = birthday,
                        login = login,
                        role = Role.valueOf(role)
                )

                call.respond(ThymeleafContent("user", mapOf("user" to user)))
            }


        }
    }
    server.start(wait = true)
}

private fun getResult(answers: MutableList<Answer>): String {
    val size = answers.size
    var countTrue = 0.0f;

    answers.forEach { if (it.answerCurrent == it.correctAnswer) countTrue++ }

    val temp: Double = if (countTrue != 0.0f) (countTrue / size).toDouble() * 100 else 0.0

    return "$temp%"
}