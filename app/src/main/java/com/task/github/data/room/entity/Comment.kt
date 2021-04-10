package com.task.github.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                   val ownerId: Int,
                   @ColumnInfo(name = "comment") val comment: String)
