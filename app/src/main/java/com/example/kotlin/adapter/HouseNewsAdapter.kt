package com.example.kotlin.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.R
import com.example.kotlin.service.NewsService
import com.squareup.picasso.Picasso

class HouseNewsAdapter(var articles: ArrayList<NewsService.HouseNewsArticles>) : RecyclerView.Adapter<HouseNewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsRowLinearLayout : LinearLayout = view.findViewById(R.id.news_row)
        val newsImageImageView  : ImageView    = view.findViewById(R.id.news_image)
        val newsTitleTextView   : TextView     = view.findViewById(R.id.news_title)
        val newsSubtitleTextView: TextView     = view.findViewById(R.id.news_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.newsRowLinearLayout.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://news.housetube.tw/" + articles[position].id))

            holder.itemView.context.startActivity(browserIntent)
        }

        Picasso.get().load(articles[position].images_url).error(R.drawable.no_image).into(holder.newsImageImageView)

        holder.newsTitleTextView.text    = articles[position].title
        holder.newsSubtitleTextView.text = articles[position].online_time
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}