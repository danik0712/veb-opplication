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

            get("/") {
                call.respondFile(File("./src/main/resources/pages/sign.html"))
            }

            post("/sign") {
                val parameters = call.receiveParameters()

                val password = parameters["password"].toString()
                val login = parameters["login"].toString()

                val user = setPlayer(login = login, password = password)

                val foundedPlayer = users.single { it.role.name == user.role }

                call.respond(ThymeleafContent("user", mapOf("user" to foundedPlayer)))

            }

            get("/reg") {
                call.respondFile(File("./src/main/resources/pages/registr.html"))
            }

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

            get("/question1") {
                val question = "What tag use for string with new string"
                call.respond(ThymeleafContent("question1", mapOf("question" to question)))
            }

            val answers: HashMap<String, MutableMap<Answer, AnswerUser>> = hashMapOf ()

            post("/answer1") {
                val answer = call.receiveParameters()["answer"].toString()

                val question = "What tag use for string with new string"

                val correctAnswer = Answer("p")
                val answerUser = AnswerUser(answer)

              //  answers.put(question, hashMapOf(correctAnswer, answerUser))

                val nextQuestion = "HTML it's language programming"
                call.respond(ThymeleafContent("question2", mapOf("question" to nextQuestion)))
            }

            post("/answer2") {

                val temp = call.receiveParameters()["answer2"].toString()
                val answer = Answer(temp)

                val question = "HTML it's language programming"

                // ans[Answer("false")] = answer

                call.respondText { "$answers" }
            }

        }
    }
    server.start(wait = true)
}

