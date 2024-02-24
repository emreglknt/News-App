package com.example.mynewsapp.api

import com.example.mynewsapp.model.NewsResponse
import com.example.mynewsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


//Url e Request atılan sınıf

interface NewsApi {


    // Get all news requests with countrycode
    @GET("v2/top-headlines")
    suspend fun getHeadlinesNews(
        @Query("country")
        countrycode:String ="us",
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ): Response<NewsResponse>



    // Searching NEWS REQUESTS
    @GET("/v2/everything")
    suspend fun  searchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ): Response<NewsResponse>



}