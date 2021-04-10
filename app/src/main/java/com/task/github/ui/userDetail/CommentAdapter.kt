package com.task.github.ui.userDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.github.data.room.entity.Comment
import com.task.github.databinding.SupportCommentBinding

class CommentAdapter(val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(
            SupportCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = comments.size


    inner class ViewHolder(val bindView: SupportCommentBinding) :
        RecyclerView.ViewHolder(bindView.root) {


        fun bind(position: Int) {
                bindView.comment = comments[position]
            bindView.executePendingBindings()
        }


    }
}