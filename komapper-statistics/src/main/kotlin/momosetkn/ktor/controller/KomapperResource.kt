package momosetkn.ktor.controller

import io.ktor.resources.Resource
import momosetkn.ktor.usecase.KomapperStatisticsSortType
import momosetkn.ktor.usecase.SortDirection

class KomapperResource {
    @Resource("komapper/statistics/{komapper_config_id}")
    class KomapperStatistics(
        val komapper_config_id: String,
        val sort: KomapperStatisticsSortType = KomapperStatisticsSortType.EXECUTION_MAX_TIME,
        val sort_direction: SortDirection = SortDirection.DESC,
        val limit: Int = 100,
        val page: Int = 1,
    )

    @Resource("komapper/statistics/{komapper_config_id}/clear")
    class KomapperStatisticsClear(
        val komapper_config_id: String,
    )
}
