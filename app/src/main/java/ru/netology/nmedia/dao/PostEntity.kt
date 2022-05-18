package ru.netology.nmedia.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity(tableName = "posts")
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "author") val author: String,    // @ColumnInfo - необязательная аннотация
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "published") val published: String,
    @ColumnInfo(name = "likedByMe") val likedByMe: Boolean = false,
    val ytVideo: String? = null,
    @ColumnInfo(defaultValue = "0") val numLikes: Int = 0,
    @ColumnInfo(defaultValue = "0") val numShares: Int = 0,
    @ColumnInfo(defaultValue = "0") val numViews: Int = 0
)


internal fun PostEntity.toModel(): Post {
    return Post(id, author, content, published, likedByMe, ytVideo, numLikes, numShares, numViews)
}

internal fun Post.toEntity(): PostEntity {
    return PostEntity(id, author, content, published, likedByMe, ytVideo, numLikes, numShares, numViews)
}
