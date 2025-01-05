package momosetkn.ktor.controller

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.NullBody
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import momosetkn.ktor.usecase.DomaStatisticsUsecase

fun Route.domaResourceHandler() {
    get<DomaResource.DomaStatistics> { params ->
        val res = DomaStatisticsUsecase.fetch(
            domaConfigId = params.doma_config_id,
            sort = params.sort,
            sortDirection = params.sort_direction,
            limit = params.limit,
            page = params.page,
        )
        call.respond(status = HttpStatusCode.OK, message = res)
    }

    delete<DomaResource.DomaStatisticsClear> { params ->
        DomaStatisticsUsecase.clear(params.doma_config_id)
        call.respond(status = HttpStatusCode.NoContent, message = NullBody)
    }
}
