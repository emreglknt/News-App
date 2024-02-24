package com.example.mynewsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.FragmentArticleBinding
import com.example.mynewsapp.mvvm.NewsViewModel
import com.example.mynewsapp.ui.MainActivity
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentArticleBinding
    val args:ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)




        newsViewModel = (activity as MainActivity).newsViewModel
        val article = args.article



        //webwiew içerisinde tıklanılan article ın url ini alıp ekrana  yükler
        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let {url->
                loadUrl(url)
            }
        }

        binding.fab.setOnClickListener{
            newsViewModel.addToFav(article)
            Snackbar.make(view,"Added to favourites",Snackbar.LENGTH_SHORT).show()
        }




    }


}