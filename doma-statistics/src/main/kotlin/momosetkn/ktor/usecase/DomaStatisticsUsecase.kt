package momosetkn.ktor.usecase


import org.seasar.doma.jdbc.ConfigSupport
import org.seasar.doma.jdbc.statistic.Statistic
import org.seasar.doma.jdbc.statistic.StatisticManager
import kotlin.math.min

object DomaStatisticsUsecase {
    val statisticManagerMap = mutableMapOf<String, StatisticManager>()

    init {
        statisticManagerMap["default"] = ConfigSupport.defaultStatisticManager
    }

    fun fetch(
        domaConfigId: String,
        sort: DomaStatisticsSortType,
        sortDirection: SortDirection,
        limit: Int,
        page: Int,
    ): List<Statistic> {
        val statisticManager = statisticManagerMap[domaConfigId]
        requireNotNull(statisticManager)
        val statistics = statisticManager.statistics

        val sortFun: (Statistic) -> Comparable<*> = when (sort) {
            DomaStatisticsSortType.EXEC_COUNT -> Statistic::execCount
            DomaStatisticsSortType.EXEC_MAX_TIME -> Statistic::execMaxTime
            DomaStatisticsSortType.EXEC_MIN_TIME -> Statistic::execMinTime
            DomaStatisticsSortType.EXEC_TOTAL_TIME -> Statistic::execTotalTime
            DomaStatisticsSortType.EXEC_AVG_TIME -> Statistic::execAvgTime
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

    fun clear(domaConfigId: String) {
        val statisticManager = statisticManagerMap[domaConfigId]
        requireNotNull(statisticManager)

        statisticManager.clear()
    }
}

enum class DomaStatisticsSortType {
    EXEC_COUNT,
    EXEC_MAX_TIME,
    EXEC_MIN_TIME,
    EXEC_TOTAL_TIME,
    EXEC_AVG_TIME,
}

enum class SortDirection {
    ASC,
    DESC,
}
