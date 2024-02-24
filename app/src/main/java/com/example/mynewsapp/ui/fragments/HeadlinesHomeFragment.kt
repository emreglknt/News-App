package com.example.newsprojectretrofit.ui.fragments

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
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.FragmentHeadlinesHomeBinding
import com.example.mynewsapp.mvvm.NewsViewModel
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.util.Constants
import com.example.mynewsapp.util.Resource

import com.example.newsprojectretrofit.adapters.HeadlinesAdapter



class HeadlinesHomeFragment : Fragment(R.layout.fragment_headlines_home) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var headlinesAdapter: HeadlinesAdapter
    lateinit var retryButton:Button
    lateinit var errorText: TextView
    lateinit var itemHeadlinesError : CardView
    lateinit var binding: FragmentHeadlinesHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesHomeBinding.bind(view)

        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        val view :View = inflater.inflate(R.layout.item_error,null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as MainActivity).newsViewModel

        setupHeadlinesRecycler()


        headlinesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_headlinesHomeFragment_to_articleFragment,bundle)
        }


        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMsg()
                    response.data?.let { newsResponse ->
                        headlinesAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.PAGE_SIZE
                        isLastPage = newsViewModel.headlinesPage == totalPages
                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }


                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let{
                        Toast.makeText(activity,"SORRY Error:$it",Toast.LENGTH_LONG).show()
                        showErrorMsg(it)
                    }
                }


                is Resource.Loading<*> -> {
                    showProgressBar()
                }

            }
        })

        retryButton.setOnClickListener{
            newsViewModel.getHeadlines("us")
        }

    }



    var isError = false
    var isLoading = false
    var isLastPage=false
    var isScrolling=false



    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }



    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    private fun hideErrorMsg(){
        itemHeadlinesError.visibility=View.INVISIBLE
        isLoading=false
    }


    private fun showErrorMsg(message:String){
        itemHeadlinesError.visibility=View.VISIBLE
        errorText.text =message
        isError=true
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
                newsViewModel.getHeadlines("us")
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


    private fun setupHeadlinesRecycler(){
        headlinesAdapter = HeadlinesAdapter()
        binding.recyclerHeadlines.apply {
            adapter= headlinesAdapter
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            addOnScrollListener(this@HeadlinesHomeFragment.scrollListener)
        }
    }

}



