package com.task.github.data.room

import com.task.github.data.room.entity.Comment

class DatabaseHelper(private val appDatabase: AppDatabase) {
    suspend fun getCommentsById(id: Int): List<Comment> =
            appDatabase.commentDao().getCommentsById(id)

    suspend fun postComment(comment: Comment) = appDatabase.commentDao().postComment(comment)
}