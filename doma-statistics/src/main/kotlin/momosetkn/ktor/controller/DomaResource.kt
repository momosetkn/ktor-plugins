package momosetkn.ktor.controller

import io.ktor.resources.Resource
import momosetkn.ktor.usecase.DomaStatisticsSortType
import momosetkn.ktor.usecase.SortDirection

class DomaResource {
    @Resource("doma/statistics/{doma_config_id}")
    class DomaStatistics(
        val doma_config_id: String,
        val sort: DomaStatisticsSortType = DomaStatisticsSortType.EXEC_MAX_TIME,
        val sort_direction: SortDirection = SortDirection.DESC,
        val limit: Int = 100,
        val page: Int = 1,
    )

    @Resource("doma/statistics/{doma_config_id}/clear")
    class DomaStatisticsClear(
        val doma_config_id: String,
    )
}
