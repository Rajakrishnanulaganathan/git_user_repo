package com.task.github.support

import com.task.github.data.RepoUser

interface RepoItemClickListener : AdapterItemClickListener {
    fun onAddComment(comment: String, pos: Int, data: RepoUser)
}