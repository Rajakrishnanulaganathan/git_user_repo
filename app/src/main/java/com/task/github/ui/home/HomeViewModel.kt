package com.task.github.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.github.data.ApiResult
import com.task.github.data.RepoUser
import com.task.github.data.retrofit.ApiService
import com.task.github.data.room.DatabaseHelper
import com.task.github.data.room.entity.Comment
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val apiService: ApiService, private val databaseHelper: DatabaseHelper) : ViewModel() {

    var isLoading: Boolean = false
    var pageCount = 1

    private val userLiveData = MutableLiveData<ApiResult<List<RepoUser>>>()

    fun getUsers(): MutableLiveData<ApiResult<List<RepoUser>>> {
        return userLiveData
    }

    fun fetchRepoUser() {
        viewModelScope.launch {
            try {
                val users = apiService.getUsers(pageCount, 10)
                userLiveData.postValue(ApiResult.Success(users))
            } catch (e: Exception) {
                userLiveData.postValue(ApiResult.Error(e))
            }
        }
    }

    fun addComment(comment: String, id: Int) {
        viewModelScope.launch {
            databaseHelper.postComment(Comment(ownerId = id, comment = comment))
        }
    }

    fun loadNewPage() {
        isLoading = true
        pageCount++
        fetchRepoUser()

    }
}