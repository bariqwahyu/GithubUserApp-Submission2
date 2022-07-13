package com.bariq.bariqgithub2

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    fun getSearch(
        @Query("q") username: String
    ): Call<SearchResponse>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}