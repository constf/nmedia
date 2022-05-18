package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun getAll(): Flow<List<PostEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostEntity)


    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Long)


    @Query("""UPDATE posts SET
                numLikes = numLikes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE  id = :id""")
    fun likeById(id: Long)


    @Query("UPDATE posts SET numShares = numShares + 1 WHERE  id = :id")
    fun shareById(id: Long)


    @Query("UPDATE posts SET numViews = numViews + 1 WHERE id = :id")
    fun viewById(id: Long)


    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    fun getById(id: Long): PostEntity
}