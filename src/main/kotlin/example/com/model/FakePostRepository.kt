package example.com.model

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.ZoneOffset

class FakePostRepository: PostRepository {

//    private val currentTimeStamp = LocalDateTime.now()

//    private val posts = mutableListOf(
//        Post("hello world", currentTimeStamp.toEpochSecond(ZoneOffset.UTC)),
//        Post("hello world 2", currentTimeStamp.minusYears(1).toEpochSecond(ZoneOffset.UTC))
//    )
    private fun toPosts(row: ResultRow):Post {
        return Post(
            id = row[Posts.id],
            text = row[Posts.text],
            timeStamp = row[Posts.timeStamp],
        )
    }

    override  fun allPosts(): List<Post> = transaction {
        Posts.selectAll().map { toPosts(it) }
    }

    override  fun postsBySince(time: Long): List<Post> = transaction {
        Posts.select(Posts.timeStamp greaterEq time).map { toPosts(it) }
    }

    override  fun addPost(text: String) = transaction {
        val currentTimeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val id = Posts.insert {
            it[Posts.text] = text
            it[timeStamp] = currentTimeStamp
        }
        println("New post Id: $id")
    }

    override fun getPostById(id: Int): Post? = transaction {
        Posts.select(Posts.id eq id).map { toPosts(it) }.singleOrNull()
    }

    override fun deletePostById(id: Int): Unit = transaction {
        Posts.deleteWhere { Posts.id eq id }
    }

}