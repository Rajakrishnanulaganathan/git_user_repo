package com.task.github.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.task.github.data.room.entity.Comment

@Dao
interface CommentDao {
    @Insert
    suspend fun postComment(users: Comment)

    @Query("SELECT * FROM Comment where ownerId = :postId")
    suspend fun getCommentsById(postId: Int): List<Comment>
}