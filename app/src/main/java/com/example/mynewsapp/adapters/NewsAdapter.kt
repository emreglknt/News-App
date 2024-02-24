package com.example.mynewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.ItemNewsBinding
import com.example.mynewsapp.model.Article
import com.example.mynewsapp.model.NewsResponse
import com.example.mynewsapp.util.Constants.Companion.VIEW_TYPE_HEADLINES
import com.example.mynewsapp.util.Constants.Companion.VIEW_TYPE_NORMAL

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


   inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


    lateinit var articleImage:ImageView
    lateinit var articleSource:TextView
    lateinit var articleTitle:TextView
    lateinit var articleDescription:TextView
    lateinit var articleDateTime:TextView



    // async bri şekilde iki listi kontrol ederek
    // recycler güncellemesi yaparken data item ve
    // içerik  kontrolüne göre güncelleyecek şekilde listeyi oluşturur ve günceller.

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url // Öğelerin benzersiz kimliklerini karşılaştırır
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem // Öğelerin içeriğini karşılaştırır
        }
    }

    val differ = AsyncListDiffer(this,differCallback)





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        )
    }




    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener:((Article)->Unit) ?= null  // bir şey döndürmüyor.(unit)


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            val article = differ.currentList[position]
            articleImage = holder.itemView.findViewById(R.id.articleImage)
            articleSource = holder.itemView.findViewById(R.id.articleSource)
            articleTitle = holder.itemView.findViewById(R.id.articleTitle)
            articleDescription = holder.itemView.findViewById(R.id.articleDescription)
            articleDateTime = holder.itemView.findViewById(R.id.articleDateTime)

            holder.itemView.apply {
                Glide.with(this).load(article.urlToImage).into(articleImage)
                articleSource.text = article.source?.name
                articleTitle.text=article.title
                articleDescription.text=article.description
                articleDateTime.text=article.publishedAt

                setOnClickListener{
                    onItemClickListener?.let {
                        it(article)
                    }
                }
            }
    }


    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener = listener
    }




}