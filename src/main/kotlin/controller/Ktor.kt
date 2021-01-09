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

    fun getResult(answers: MutableList<Answer>): String {
        val size = answers.size
        var countTrue = 0.0f

        answers.forEach { if (it.answerCurrent == it.correctAnswer) countTrue++ }

        val temp = if (countTrue != 0.0f) (countTrue / size).toDouble() * 100 else 0.0

        if (temp > 60 && temp < 80) return "Not bad, but you can better $temp%"

        if (temp > 80) return "Excellent, continue in this pace $temp%"

        return "You need more hard work $temp%"
    }

    val server = embeddedServer(Netty, port = 9090) {

        val question1 = "whats tag block?"
        val question2 = "С помощью какого тега следует разделять абзацы?"
        val question3 = "С помощью какого свойства изменяется ширина таблицы?"

        var person = Person(name = null, age = null)

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

            //открывает эту страницу первую в приложении
            get("/") {
                call.respondFile(File("./src/main/resources/pages/start.html"))
            }

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
                                correctAnswer = "post",
                                question = question1
                        )
                )

                call.respond(ThymeleafContent("question2", mapOf("question" to question2)))
            }

            post("/answer2") {

                val parameters = call.receiveParameters()
                val answer = parameters["answer"].toString()

                answers.add(Answer(numberQuestion = "2", answerCurrent = answer, correctAnswer = "p", question = question2))

                call.respond(ThymeleafContent("question3", mapOf("question" to question3)))

            }


            post("/answer3") {
                val parameters = call.receiveParameters()

                val answer = parameters["answer"].toString()

                answers.add(Answer(numberQuestion = "3", answerCurrent = answer, correctAnswer = "width", question = question2))

                call.respond(
                        ThymeleafContent("end",
                                mapOf("user" to person, "answer" to answers, "result" to getResult(answers)))
                )

                answers.clear()
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