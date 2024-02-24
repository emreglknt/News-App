package com.example.mynewsapp.api

import com.example.mynewsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {


    companion object{

        // singleton olarak   ilk exist olduğunda create edilir
        // onun dışında create edilmez.
        // Buna lazy initialize denir.

        private val retrofit by lazy{
            //  ** OPSİYONEL -  to log responses of retrofit
            val logging  = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()


            Retrofit.Builder().baseUrl(BASE_URL).
            addConverterFactory(GsonConverterFactory.create()).
            client(client).build()
        }

        // we will use this to make api calls
        // retrofit objesi newsapi classını kullanarak create ediliyor.
        val api by lazy{
            retrofit.create(NewsApi::class.java)
        }


    }



}