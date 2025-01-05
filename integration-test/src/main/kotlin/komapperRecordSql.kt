import kotlinx.coroutines.delay
import momosetkn.ktor.usecase.KomapperStatisticsUsecase
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

suspend fun komapperRecordSql() {
    val defaultStatisticManager = KomapperStatisticsUsecase.statisticManagerMap.getOrPut("default") {
        org.komapper.core.DefaultStatistics().apply {
            setEnabled(true)
        }
    }
    val sql = "SELECT * FROM table${(Math.random() * 10).toLong()}"
    val startTime = System.nanoTime()
    val endTime = startTime + (Math.random() * 100_000_000).toLong()
    defaultStatisticManager.add(sql, startTime, endTime)
    delay(100L)
}
