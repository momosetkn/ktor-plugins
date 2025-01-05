package momosetkn.ktor.controller

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.NullBody
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import momosetkn.ktor.usecase.KomapperStatisticsUsecase


fun Route.komapperResourceHandler() {
    get<KomapperResource.KomapperStatistics> { params ->
        val res = KomapperStatisticsUsecase.fetch(
            komapperConfigId = params.komapper_config_id,
            sort = params.sort,
            sortDirection = params.sort_direction,
            limit = params.limit,
            page = params.page,
        )
        call.respond(status = HttpStatusCode.OK, message = res)
    }

    delete<KomapperResource.KomapperStatisticsClear> { params ->
        KomapperStatisticsUsecase.clear(params.komapper_config_id)
        call.respond(status = HttpStatusCode.NoContent, message = NullBody)
    }
}
