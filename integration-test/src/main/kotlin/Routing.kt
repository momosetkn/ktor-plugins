
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.resources.Resources
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import momosetkn.ktor.controller.domaResourceHandler
import momosetkn.ktor.controller.komapperResourceHandler

fun Application.configureRouting() {
    install(Resources)
    routing {
        route("admin") {
            domaResourceHandler()
            komapperResourceHandler()
        }
    }
}
