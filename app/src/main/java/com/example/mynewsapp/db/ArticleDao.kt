package com.example.mynewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynewsapp.model.Article

@Dao
interface ArticleDao {


    // article ları insert ederken eğer aynı içerikte iki adet varsa
    // replace ederek insert ediyor. Aynı şeyi iki kere insert etmiyor.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(favArticle: Article):Long // primary key döndürüyor


    @Query("SELECT * FROM favouriteArticles")
    fun getAllNews(): LiveData<List<Article>>


    @Delete
    suspend fun deleteArticle(favArticle:Article)



}