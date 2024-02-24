package com.example.mynewsapp.util

import android.icu.util.ULocale.getCountry
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class Constants {

    companion object {

        const val BASE_URL = "https://newsapi.org"
        const val API_KEY = "8ef2d8dc1ffc409f945d61f1f1c714be"
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val PAGE_SIZE = 20
        const val VIEW_TYPE_HEADLINES = 1
        const val VIEW_TYPE_NORMAL = 2


    }

}