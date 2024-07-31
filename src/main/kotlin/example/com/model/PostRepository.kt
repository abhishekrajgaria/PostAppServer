package example.com.model

interface PostRepository {
     fun allPosts(): List<Post>

     fun postsBySince(time:Long): List<Post>

     fun addPost(text: String)

     fun getPostById(id:Int): Post?

     fun deletePostById(id:Int)
}