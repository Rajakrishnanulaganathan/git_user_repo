package com.task.github.support

import com.task.github.data.RepoUser


interface AdapterItemClickListener {
  fun  onItemClick(pos:Int,item: RepoUser)
}