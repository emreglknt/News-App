package com.example.mynewsapp.repository

import androidx.lifecycle.LiveData
import com.example.mynewsapp.api.RetrofitInstance
import com.example.mynewsapp.db.ArticleDatabase
import com.example.mynewsapp.db.EntityFavArticle
import com.example.mynewsapp.model.Article

class NewsRepository(val db :ArticleDatabase) {



    //Network call / API method işlemleri ------

    // retrofit api objesi ile newsapi daki methoda erişim sağlayan ve
    // aldığı parametreleri bu methoda aktaran bir fonksiyon
    suspend fun getHeadlinesNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getHeadlinesNews(countryCode,pageNumber)

    // retrofit api objesi ile newsapi daki searchNews methoduna erişim sağlayan ve
    // aldığı parametreleri bu methoda aktaran bir fonksiyon
    suspend fun  searchNews(searchQuery:String,pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery,pageNumber)




    //Database method işlemleri  --------

    suspend fun insertNews(favArticle: Article){
        db.newsDao.insertArticle(favArticle)
    }


    suspend fun deleteArticle(favArticle:Article){
        db.newsDao.deleteArticle(favArticle)
    }


    fun getFavNews():LiveData<List<Article>>{
        return db.newsDao.getAllNews()
    }



}