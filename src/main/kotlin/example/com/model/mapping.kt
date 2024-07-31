package example.com.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Posts : Table(){
    val id = integer("id").autoIncrement()
    val text = varchar("text", 255)
    val timeStamp = long("timeStamp")
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Post(val id: Int, val text: String, val timeStamp: Long)