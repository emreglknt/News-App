package com.example.mynewsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.NewsAdapter
import com.example.mynewsapp.databinding.FragmentFavoriBinding
import com.example.mynewsapp.mvvm.NewsViewModel
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.util.Constants.Companion.VIEW_TYPE_NORMAL
import com.google.android.material.snackbar.Snackbar


class FavoriFragment : Fragment(R.layout.fragment_favori) {


    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter // iki farklı adapter var
    lateinit var binding: FragmentFavoriBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriBinding.bind(view)

        newsViewModel = (activity as MainActivity).newsViewModel

        setupFavRecyclerView()

        // favorilerdeki article a basınca   tıklanılan article objesini article fragment a taşır
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favoriFragment_to_articleFragment, bundle)
        }



        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view,"Removed from favourities",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        newsViewModel.addToFav(article)
                    }
                    show()
                }
            }
        }


        //recycler view a bu objeyi attach eder sabitler ve kaydırma silme fonksiyonellerini ekler.
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }


        newsViewModel.getFavNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })


    }





    private fun setupFavRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }




}