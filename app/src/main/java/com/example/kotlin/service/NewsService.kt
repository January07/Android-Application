package com.example.kotlin.service

import retrofit2.Call
import retrofit2.http.*

interface NewsService {

    @GET("v2/top-headlines?country=tw&apiKey=fea39a01c6ce448db8a9a2ad7bcf8f0d")
    fun focusListRepos(@Query("page") page: String): Call<FocusNews>

    @FormUrlEncoded
    @POST("app/")
    fun houseListRepos(@FieldMap fieldMap: Map<String, String>) : Call<HouseNews>

    data class FocusNews (
        val status      : String,
        val totalResults: Int,
        val articles    : ArrayList<FocusNewsArticles>
    )

    data class FocusNewsArticles (
        val source     : Map<String, Any>,
        val author     : String,
        val title      : String,
        val description: String,
        val url        : String,
        val urlToImage : String?,
        val publishedAt: String,
        val content    : String
    )

    data class HouseNews (
        val success: String,
        val rows   : ArrayList<HouseNewsArticles>
    )

    data class HouseNewsArticles (
        val id         : String,
        val title      : String,
        val excerpt    : String,
        val online_time: String,
        val images_url : String
    )
}