package example

import io.ktor.application.call
import io.ktor.response.respondFile
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 9090) {
        routing {
            get("/") {
                call.respondFile(File("./pages/index.html"))
            }
        }
    }
    server.start(wait = true)
}
