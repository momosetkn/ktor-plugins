import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.locks.LockSupport
import kotlin.time.Duration.Companion.minutes

suspend fun main(args: Array<String>) {
    val recordSqlLoopJob =
        CoroutineScope(Dispatchers.IO).launch {
            recordSqlLoop()
        }

    val server =
        embeddedServer(
            Netty,
            configure = {
                connector {
                    port = 8080
                }
            },
            module = {
                configureContentNegotiation()
                configureRouting()
            }
        ).start(wait = false)

    waitForShutdown{
        server.stop(
            gracePeriodMillis = 50,
            timeoutMillis = 5.minutes.inWholeMilliseconds,
        )
    }
}

private fun waitForShutdown(block: () -> Unit) {
    val currentThread = Thread.currentThread()
    Runtime.getRuntime().addShutdownHook(
        Thread {
            shutdown = true
            block()
            LockSupport.unpark(currentThread)
        },
    )
    LockSupport.park()
}

@Volatile
private var shutdown = false

private tailrec suspend fun recordSqlLoop() {
    domaRecordSql()
    komapperRecordSql()
    delay((Math.random() * 100).toLong())
    if (!shutdown) recordSqlLoop()
}
