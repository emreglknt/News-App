package com.example.mynewsapp.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynewsapp.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class ArticleDatabase:RoomDatabase() {


    abstract val newsDao:ArticleDao

   companion object {

       @Volatile
       private var INSTANCE: ArticleDatabase? = null

       fun createDatabase(context: Context): ArticleDatabase {
           // senkronize bir şekilde sırasıyla thredlerde işlem görmesini sağlar
           synchronized(this) {
               var instance = INSTANCE
               if (instance == null) { //eğer instance yoksa  yani nullsa
                   //create database
                   instance = Room.databaseBuilder( // yeni bir database oluştur
                       context.applicationContext,
                       ArticleDatabase::class.java,
                       "article_database"
                   ).build()

                   INSTANCE = instance
               }
               return instance // eğer instance varsa instance yı döndür
           }
       }


   }





}