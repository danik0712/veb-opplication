package example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.thymeleaf.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.File

fun main() {

    val server = embeddedServer(Netty, port = 9090) {

        routing {

            install(Thymeleaf) {
                setTemplateResolver(ClassLoaderTemplateResolver().apply {
                    prefix = "pages/"
                    suffix = ".html"
                    characterEncoding = "utf-8"
                })
            }

            get("/") { call.respondFile(File("./src/main/resources/pages/form.html")) }

            post("/data") {

                // val name_1 = call.response.headers["name"].toString()
                // val password_1 = call.response.headers["password"].toString()

                val name = call.parameters["name"].toString()
                val password = call.parameters["password"].toString()
                val user = User(name, password)
                call.respond(ThymeleafContent("index", mapOf("user" to user)))
            }

        }
    }
    server.start(wait = true)
}

data class User(val name: String, val password: String)
