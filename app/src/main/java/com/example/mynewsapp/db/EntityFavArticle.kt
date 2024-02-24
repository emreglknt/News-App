package com.example.mynewsapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynewsapp.model.Source
import java.io.Serializable


//database tablosunu ve sütünlardaki değişken ve tiplerini oluşturur.
@Entity("favouriteArticles")
 data class EntityFavArticle (

    @PrimaryKey(true)
    val id : Int,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source, // bu source objesi döndereceği için
   // database tarafından kabul edilebilir bir fortmata dönüştürmek gerekiyor.
    //bunun için converter sınıfını kullanacağız.
    val title: String,
    val url: String,
    val urlToImage: String

 ):Serializable
