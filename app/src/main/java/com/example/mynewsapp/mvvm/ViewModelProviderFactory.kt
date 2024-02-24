package com.example.mynewsapp.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynewsapp.repository.NewsRepository


// view model sınıfını oluşturan bir factory sınıfıdır.
//dependency injection yaparken kolaylık sağlar.
class ViewModelProviderFactory(val app:Application,val newsRepository: NewsRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository)as T
    }

}