package com.example.mynewsapp.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("favouriteArticles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?
): Serializable {
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (urlToImage?.hashCode() ?: 0)
        return result
    }
}
