package com.example.mynewsapp.mvvm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.model.Article
import com.example.mynewsapp.model.NewsResponse
import com.example.mynewsapp.repository.NewsRepository
import com.example.mynewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val newsRepo: NewsRepository): AndroidViewModel(app) {

    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null





    //database repo fonksiyonları
    fun addToFav(article: Article) = viewModelScope.launch {
        newsRepo.insertNews(article)
    }

    fun getFavNews() = newsRepo.getFavNews()

    fun deleteArticle(article:Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }




    init {
        getHeadlines("us")
    }

    // api fonksiyonları

    fun getHeadlines(countryCode:String)=viewModelScope.launch {
        checkInternetForGetNews(countryCode,"headlines")
    }


    fun getSearchNews(searchQuery:String)=viewModelScope.launch {
        checkInternetForGetNews("us","searchNews",searchQuery)
    }




    private suspend fun checkInternetForGetNews(countryCode: String, key: String, searchQuery: String? = null) {
        // Loading state
        when (key) {
            "headlines" -> headlines.postValue(Resource.Loading())
            "searchNews" -> searchNews.postValue(Resource.Loading())
        }

        // Fetching data from internet
        try {
            if (internetConnection(getApplication())) {
                val response = when (key) {
                    "headlines" -> newsRepo.getHeadlinesNews(countryCode, headlinesPage)
                    "searchNews" -> newsRepo.searchNews(searchQuery!!, searchNewsPage)
                    else -> null
                }

                // Handling response
                when (key) {
                    "headlines" -> headlines.postValue(response?.let { handleHeadlinesResponse(it)})
                    "searchNews" -> searchNews.postValue(response?.let { handleSearchNewsResponse(it) })
                }
            } else {
                // No internet connection
                when (key) {
                    "headlines" -> headlines.postValue(Resource.Error("No Internet Connection"))
                    "searchNews" -> searchNews.postValue(Resource.Error("No Internet Connection"))
                }
            }
        } catch (t: Throwable) {
            // Network error
            when (t) {
                is IOException -> {
                    when (key) {
                        "headlines" -> headlines.postValue(Resource.Error("Unable to connect"))
                        "searchNews" -> searchNews.postValue(Resource.Error("Unable to connect"))
                    }
                }
                else -> {
                    // Other errors
                    when (key) {
                        "headlines" -> headlines.postValue(Resource.Error("No signal"))
                        "searchNews" -> searchNews.postValue(Resource.Error("No signal"))
                    }
                }
            }
        }
    }




    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    //  !!!!!!!! burası videodaki gibi değil kontrol et !!!!!!!!
                    return Resource.Success(resultResponse)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++

                    //  !!!!!!!! burası videodaki gibi değil kontrol et !!!!!!!!
                    return Resource.Success(resultResponse)

                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }







//Checks The Internet Connection
    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply{
            return  getNetworkCapabilities(activeNetwork)?.run{

                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                    else->false
                }
            } ?:false
        }
    }





}