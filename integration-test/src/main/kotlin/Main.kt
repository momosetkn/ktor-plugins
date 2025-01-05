import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

suspend fun main(args: Array<String>) {
    val server =
        embeddedServer(
            Netty,
            configure = {
                connector {
                    port = 8080
                }
            },
            module = {
                configureRouting()
            }
        ).start()
}
