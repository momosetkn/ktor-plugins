package momosetkn.ktor.usecase


import kotlin.math.min

object KomapperStatisticsUsecase {
    val statisticManagerMap = mutableMapOf<String, org.komapper.core.Statistics>()

//    init {
//        statisticManagerMap["default"] =
//    }

    fun fetch(
        komapperConfigId: String,
        sort: KomapperStatisticsSortType,
        sortDirection: SortDirection,
        limit: Int,
        page: Int,
    ): List<SqlStatistics> {
        val statisticManager = statisticManagerMap[komapperConfigId]
        requireNotNull(statisticManager)
        val statistics = statisticManager.getAllSqlStatistics().map { it.toMyDataClass() }

        val sortFun: (SqlStatistics) -> Comparable<*> = when (sort) {
            KomapperStatisticsSortType.EXECUTION_COUNT -> SqlStatistics::executionCount
            KomapperStatisticsSortType.EXECUTION_MAX_TIME -> SqlStatistics::executionMaxTime
            KomapperStatisticsSortType.EXECUTION_MIN_TIME -> SqlStatistics::executionMinTime
            KomapperStatisticsSortType.EXECUTION_TOTAL_TIME -> SqlStatistics::executionTotalTime
            KomapperStatisticsSortType.EXECUTION_AVG_TIME -> SqlStatistics::executionAvgTime
        }
        val offset = (page - 1) * page
        val limit = page * limit
        val result = statistics
            .sortedWith(compareBy(sortFun))
            .let {
                when (sortDirection) {
                    SortDirection.ASC -> it
                    SortDirection.DESC -> it.reversed()
                }
            }
        val res = result
            .slice(offset until min(limit, result.size))
        return res
    }

    fun clear(komapperConfigId: String) {
        val statisticManager = statisticManagerMap[komapperConfigId]
        requireNotNull(statisticManager)

        statisticManager.clear()
    }
}

data class SqlStatistics(
    val sql: String,
    val executionCount: Long,
    val executionMaxTime: Long,
    val executionMinTime: Long,
    val executionTotalTime: Long,
    val executionAvgTime: Double,
)

private fun Map.Entry<String, org.komapper.core.SqlStatistics>.toMyDataClass() = SqlStatistics(
    sql = this.key,
    executionCount = this.value.executionCount,
    executionMaxTime = this.value.executionMaxTime,
    executionMinTime = this.value.executionMinTime,
    executionTotalTime = this.value.executionTotalTime,
    executionAvgTime = this.value.executionAvgTime,
)

enum class KomapperStatisticsSortType {
    EXECUTION_COUNT,
    EXECUTION_MAX_TIME,
    EXECUTION_MIN_TIME,
    EXECUTION_TOTAL_TIME,
    EXECUTION_AVG_TIME,
}

enum class SortDirection {
    ASC,
    DESC,
}
