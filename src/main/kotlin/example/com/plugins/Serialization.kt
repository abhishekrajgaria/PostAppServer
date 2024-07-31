package example.com.plugins

import example.com.model.PostRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZoneOffset


@Serializable
data class LocalTimeStampRequest(val LocalTimeStamp: String)


@Serializable
data class CreatePostRequest(val text: String)

fun Application.configureSerialization(repository: PostRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/posts"){
            get{
                val posts = repository.allPosts()
                call.respond(posts)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id != null) {
                    val post = repository.getPostById(id)
                    if (post != null) call.respond(post)
                    else call.respond(HttpStatusCode.NotFound)
                }
                else call.respond(HttpStatusCode.BadRequest)

            }

            post("/since"){
                val request = call.receive<LocalTimeStampRequest>()
                val localDateTime = LocalDateTime.parse(request.LocalTimeStamp)
                val epochTime = localDateTime.toEpochSecond(ZoneOffset.UTC)
                call.respond(repository.postsBySince(epochTime))
            }

            post(){
                val request = call.receive<CreatePostRequest>()
                repository.addPost(request.text)
            }

            delete ("/{id}"){
                val id = call.parameters["id"]?.toIntOrNull()

                if (id != null) {
                    repository.deletePostById(id)
                }
                else call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
