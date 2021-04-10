package com.task.github.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.github.data.retrofit.ApiService
import com.task.github.data.room.DatabaseHelper
import com.task.github.ui.home.HomeViewModel
import com.task.github.ui.userDetail.HomeDetailViewModel

class ViewModelFactory(private val apiHelper: ApiService, private val dbHelper: DatabaseHelper) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(apiHelper, dbHelper) as T
        }
        if (modelClass.isAssignableFrom(HomeDetailViewModel::class.java)) {
            return HomeDetailViewModel(apiHelper, dbHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}