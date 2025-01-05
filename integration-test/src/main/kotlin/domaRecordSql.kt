import kotlinx.coroutines.delay
import org.seasar.doma.jdbc.InParameter
import org.seasar.doma.jdbc.PreparedSql
import org.seasar.doma.jdbc.SqlKind
import org.seasar.doma.jdbc.SqlLogType

suspend fun enableDomaRecordSql() {
    org.seasar.doma.jdbc.ConfigSupport.defaultStatisticManager.isEnabled = true
}

suspend fun domaRecordSql() {
    enableDomaRecordSql()
    val sql =
        PreparedSql(SqlKind.SELECT, "SELECT * FROM table${(Math.random() * 10).toLong()}", "", "", emptyList<InParameter<*>>(), SqlLogType.NONE)
    val startTime = System.nanoTime()
    val endTime = startTime + (Math.random() * 100).toLong()
    org.seasar.doma.jdbc.ConfigSupport.defaultStatisticManager.recordSqlExecution(sql, startTime, endTime)
}
