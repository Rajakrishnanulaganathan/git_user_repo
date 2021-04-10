package com.task.github.data.retrofit

import com.task.github.data.RepoUser
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users/geerlingguy/repos")
    suspend fun getUsers(
            @Query("page") page: Int,
            @Query("perPage") perPage: Int): List<RepoUser>
}