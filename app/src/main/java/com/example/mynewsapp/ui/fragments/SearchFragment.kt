package com.example.mynewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.NewsAdapter
import com.example.mynewsapp.databinding.FragmentSearchBinding
import com.example.mynewsapp.mvvm.NewsViewModel
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.util.Constants
import com.example.mynewsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mynewsapp.util.Constants.Companion.VIEW_TYPE_NORMAL
import com.example.mynewsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment(R.layout.fragment_search) {


    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentSearchBinding
    lateinit var newsadapter : NewsAdapter
    lateinit var itemSearchError: CardView
    lateinit var retryButton: Button
    lateinit var errorText: TextView




    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        itemSearchError = view.findViewById(R.id.itemSearchError)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        val view :View = inflater.inflate(R.layout.item_error,null)


        newsViewModel = (activity as MainActivity).newsViewModel
        setupSearchRecycler()

        newsadapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment,bundle)
        }


        // corutine kullanarak main scope ta bir iş oluşturduk
        // edit text e yazılan yazıyı aldık
        // boş değilse query olarak viewmodel daki methodu kullanarak search news fonksiyonuna verdik.
        var job : Job?=null
        binding.searchEdit.addTextChangedListener(){
            job?.cancel() //var olan işi iptal et temizle
            job = MainScope().launch {//yeni iş oluştur
                delay(SEARCH_NEWS_TIME_DELAY.toLong())// bir süre gecikme verir
                it?.let {
                    if (it.toString().isNotEmpty()){
                        newsViewModel.getSearchNews(it.toString())
                    }
                }
            }
        }



        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer {response->

            when(response){
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMsg()
                    response.data?.let {newsResponse->
                        newsadapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.PAGE_SIZE
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding.recyclerSearch.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{
                        Toast.makeText(activity,"SORRY Error:$it", Toast.LENGTH_LONG).show()
                        showErrorMsg(it)
                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }

            }
        })


        //hata mesajındaki retry butonuna basınca yeniden yüklemeyi dener.
        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        retryButton.setOnClickListener{
            if (binding.searchEdit.text.toString().isNotEmpty()){
                newsViewModel.getSearchNews(binding.searchEdit.text.toString())
            }else{
                hideErrorMsg()
            }
        }


    }

//yardımcı visiblity fonskiyonları

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }


    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


    private fun hideErrorMsg() {
        itemSearchError.visibility = View.INVISIBLE
        isLoading = false
    }


    private fun showErrorMsg(message: String) {
        itemSearchError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }



    private fun setupSearchRecycler() {
        newsadapter = NewsAdapter()
        binding.recyclerSearch.apply {
            adapter = newsadapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }




    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)


            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isnotloadAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isnotloadAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                newsViewModel.getSearchNews(binding.searchEdit.text.toString())
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }






















}
