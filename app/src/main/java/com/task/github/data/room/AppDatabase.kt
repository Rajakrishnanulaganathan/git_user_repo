package com.task.github.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.github.data.room.dao.CommentDao
import com.task.github.data.room.entity.Comment


@Database(entities = [Comment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
}