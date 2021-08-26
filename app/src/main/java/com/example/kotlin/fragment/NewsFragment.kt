package com.example.kotlin.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.Lib
import com.example.kotlin.OnRcvScrollListener
import com.example.kotlin.R
import com.example.kotlin.adapter.FocusNewsAdapter
import com.example.kotlin.adapter.HouseNewsAdapter
import com.example.kotlin.service.NewsService
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {

    lateinit var newsProgressbar : ProgressBar
    lateinit var newsRecyclerview: RecyclerView
    lateinit var focusNewsAdapter: FocusNewsAdapter
    lateinit var houseNewsAdapter: HouseNewsAdapter
    var focusNewsArticles        : ArrayList<NewsService.FocusNewsArticles> = arrayListOf()
    var houseNewsArticles        : ArrayList<NewsService.HouseNewsArticles> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var page       = "focus"
        var pageNumber = 1

        getFocusNews(pageNumber)

        val newsHorizontalScrollView: HorizontalScrollView = view.findViewById(R.id.news_horizontalScrollView)
        val newsFocusTextView       : TextView             = view.findViewById(R.id.news_focus)
        val newsHouseTextView       : TextView             = view.findViewById(R.id.news_house)

        newsProgressbar          = view.findViewById(R.id.news_progressbar)
        newsRecyclerview         = view.findViewById(R.id.news_recyclerview)
        focusNewsAdapter         = FocusNewsAdapter(focusNewsArticles)
        newsRecyclerview.adapter = focusNewsAdapter

        // 設定過度滾動回彈
        OverScrollDecoratorHelper.setUpOverScroll(newsHorizontalScrollView)

        newsFocusTextView.setOnClickListener {
            page       = "focus"
            pageNumber = 1

            newsFocusTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#166fe5")))
            newsFocusTextView.setBackgroundResource(R.drawable.textview_selected_background)

            newsHouseTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF000000")))
            newsHouseTextView.setBackgroundResource(0)

            newsProgressbar.visibility = View.VISIBLE

            getFocusNews(pageNumber)
        }

        newsHouseTextView.setOnClickListener {
            page       = "house"
            pageNumber = 1

            newsHouseTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#166fe5")))
            newsHouseTextView.setBackgroundResource(R.drawable.textview_selected_background)

            newsFocusTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF000000")))
            newsFocusTextView.setBackgroundResource(0)

            newsProgressbar.visibility = View.VISIBLE

            val parameters: HashMap<String, String> = HashMap()

            parameters["action"] = "news"
            parameters["option"] = "list"
            parameters["page"]   = pageNumber.toString()

            getHouseNews(parameters)
        }

        // 滾動至底
        newsRecyclerview.addOnScrollListener(object : OnRcvScrollListener() {
            override fun onBottom() {
                super.onBottom()

                pageNumber += 1

                // news api 限制頁數最多到第五頁
                if (page == "focus" && pageNumber < 6) {
                    getFocusNews(pageNumber)
                } else if (page == "house") {
                    val parameters: HashMap<String, String> = HashMap()

                    parameters["action"] = "news"
                    parameters["option"] = "list"
                    parameters["page"]   = pageNumber.toString()

                    getHouseNews(parameters)
                }
            }
        })
    }

    private fun getFocusNews(pageNumber: Int) {
        val retrofit = Lib.buildRetrofit("https://newsapi.org/")
        val service  = retrofit.create(NewsService::class.java)
        val repos    = service.focusListRepos(pageNumber.toString())

        repos.enqueue(object : Callback<NewsService.FocusNews> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call    : Call<NewsService.FocusNews>,
                response: Response<NewsService.FocusNews>
            ) {
                newsProgressbar.visibility = View.GONE

                // init RecyclerView
                if (pageNumber == 1) {
                    focusNewsArticles        = arrayListOf()
                    focusNewsAdapter         = FocusNewsAdapter(focusNewsArticles)
                    newsRecyclerview.adapter = focusNewsAdapter
                }

                // 放入 Data
                focusNewsArticles += response.body()!!.articles
                focusNewsAdapter.articles = focusNewsArticles
                focusNewsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<NewsService.FocusNews>, t: Throwable) {}
        })
    }

    private fun getHouseNews(parameters: HashMap<String, String>) {
        val retrofit = Lib.buildRetrofit("https://tainan.housetube.tw/")
        val service  = retrofit.create(NewsService::class.java)
        val repos    = service.houseListRepos(parameters)

        repos.enqueue(object : Callback<NewsService.HouseNews> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call    : Call<NewsService.HouseNews>,
                response: Response<NewsService.HouseNews>
            ) {
                newsProgressbar.visibility = View.GONE

                // init RecyclerView
                if (parameters["page"] == "1") {
                    houseNewsArticles        = arrayListOf()
                    houseNewsAdapter         = HouseNewsAdapter(houseNewsArticles)
                    newsRecyclerview.adapter = houseNewsAdapter
                }

                // 放入 Data
                houseNewsArticles += response.body()!!.rows
                houseNewsAdapter.articles = houseNewsArticles
                houseNewsAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<NewsService.HouseNews>, t: Throwable) {}
        })
    }
}