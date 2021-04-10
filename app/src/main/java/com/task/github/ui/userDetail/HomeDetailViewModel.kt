package com.task.github.ui.userDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.github.data.RepoUser
import com.task.github.data.retrofit.ApiService
import com.task.github.data.room.DatabaseHelper
import com.task.github.data.room.entity.Comment
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeDetailViewModel(val apiService: ApiService,val databaseHelper: DatabaseHelper) : ViewModel() {


     var user: RepoUser?=null
    private val userLiveData = MutableLiveData<List<Comment>>()
    fun getCommentLiveData(): LiveData<List<Comment>> {
        return userLiveData
    }


    fun fetchComment() {
        if(user==null) return
        viewModelScope.launch {
            try {

                val comments = databaseHelper.getCommentsById(user!!.id)
                userLiveData.postValue(comments)
            } catch (e: Exception) {
            }
        }
    }


}