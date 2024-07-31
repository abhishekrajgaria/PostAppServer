package example.com

import example.com.model.FakePostRepository
import example.com.model.Posts
import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.netty.handler.codec.DefaultHeaders
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = FakePostRepository()

    configureSerialization(repository)
    configureDatabases()

    transaction {
        SchemaUtils.create(Posts)
    }

    configureRouting()
}
