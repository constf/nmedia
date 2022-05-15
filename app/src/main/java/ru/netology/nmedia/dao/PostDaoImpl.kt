package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    companion object {
        val DDL: String  = "CREATE TABLE ${PostColumns.TABLE} (" +
                "${PostColumns.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${PostColumns.COL_AUTHOR} TEXT NOT NULL," +
                "${PostColumns.COL_CONTENT} TEXT NOT NULL," +
                "${PostColumns.COL_PUBLISHED} TEXT NOT NULL," +
                "${PostColumns.COL_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT false," +
                "${PostColumns.COL_YT_VIDEO} TEXT DEFAULT NULL," +
                "${PostColumns.COL_LIKES} INTEGER NOT NULL DEFAULT 0," +
                "${PostColumns.COL_SHARES} INTEGER NOT NULL DEFAULT 0," +
                "${PostColumns.COL_VIEWS} INTEGER NOT NULL DEFAULT 0" +
                " );"
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COL_ID = "id"
        const val COL_AUTHOR = "author"
        const val COL_CONTENT = "content"
        const val COL_PUBLISHED = "published"
        const val COL_LIKED_BY_ME = "likedByMe"
        const val COL_YT_VIDEO = "ytVideo"
        const val COL_LIKES = "numLikes"
        const val COL_SHARES = "numShares"
        const val COL_VIEWS = "numViews"

        val ALL_COLUMNS = arrayOf(
            COL_ID, COL_AUTHOR, COL_CONTENT, COL_PUBLISHED,
            COL_LIKED_BY_ME, COL_YT_VIDEO, COL_LIKES, COL_SHARES, COL_VIEWS
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE, PostColumns.ALL_COLUMNS,
            null, null, null, null,
            "${PostColumns.COL_ID} ASC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }

        return posts
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COL_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COL_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COL_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COL_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COL_LIKED_BY_ME)) != 0,
                ytVideo = getString(getColumnIndexOrThrow(PostColumns.COL_YT_VIDEO)),
                numLikes = getInt(getColumnIndexOrThrow(PostColumns.COL_LIKES)),
                numShares = getInt(getColumnIndexOrThrow(PostColumns.COL_SHARES)),
                numViews = getInt(getColumnIndexOrThrow(PostColumns.COL_VIEWS)),
            )
        }
    }

    override fun save(post: Post): Post {
        val contentValues = ContentValues().apply {
            // Если это новый пост, то ID не указываем (колонку не добавляем) и тогда СУБД присвоит ID автоматически,
            // т.к. эта колонка у нас автоинкремент.
            if (post.id != 0L) {
                put(PostColumns.COL_ID, post.id)
            }
            put(PostColumns.COL_AUTHOR, post.author)
            put(PostColumns.COL_CONTENT, post.content)
            put(PostColumns.COL_PUBLISHED, post.published)
            put(PostColumns.COL_LIKED_BY_ME, post.likedByMe)
            put(PostColumns.COL_YT_VIDEO, post.ytVideo)
            put(PostColumns.COL_LIKES, post.numLikes)
            put(PostColumns.COL_SHARES, post.numShares)
            put(PostColumns.COL_VIEWS, post.numViews)
        }

        val id = db.replace(PostColumns.TABLE, null, contentValues)
        db.query(
            PostColumns.TABLE, PostColumns.ALL_COLUMNS,
            "${PostColumns.COL_ID} = ?", arrayOf(id.toString()),
            null, null, null
        ).use {
            it.moveToNext()
            return map(it)
        }

    }

    override fun removeById(id: Long) {
        db.delete(PostColumns.TABLE, "${PostColumns.COL_ID} = ?", arrayOf(id.toString()))
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                    numLikes = numLikes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                    likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE  id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET numShares = numShares + 1 WHERE  id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun viewById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET numViews = numViews + 1 WHERE  id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun getById(id: Long): Post {
        db.query(
            PostColumns.TABLE, PostColumns.ALL_COLUMNS,
            "${PostColumns.COL_ID} = ?", arrayOf(id.toString()),
            null, null, null
        ).use {
            it.moveToNext()
            return map(it)
        }
    }
}
