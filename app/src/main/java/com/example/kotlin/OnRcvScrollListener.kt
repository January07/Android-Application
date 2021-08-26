package com.example.kotlin

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

interface OnBottomListener {

    fun onBottom()
}

open class OnRcvScrollListener : RecyclerView.OnScrollListener(), OnBottomListener {

    // LayoutManager 的類型 (枚舉)
    private var layoutManagerType       : LayoutManagerType? = null
    // 最後一個的位置
    private var lastPositions           : IntArray?          = null
    // 最後一個可見 item 的位置
    private var lastVisibleItemPosition : Int                = 0
    // 第一個可見 item 的位置
    private var firstVisibleItemPosition: Int                = 0
    // 當前滑動的狀態
    private var currentScrollState                           = 0
    private var itemCount                                    = 0
    private var firstItemCount                               = 0

    enum class LayoutManagerType {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager

        if (layoutManagerType == null) {
            layoutManagerType = when (layoutManager) {
                is LinearLayoutManager -> {
                    LayoutManagerType.LINEAR
                }
                is GridLayoutManager -> {
                    LayoutManagerType.GRID
                }
                is StaggeredGridLayoutManager -> {
                    LayoutManagerType.STAGGERED_GRID
                }
                else -> {
                    throw RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager"
                    )
                }
            }
        }

        when (layoutManagerType) {
            LayoutManagerType.LINEAR -> {
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()

                firstVisibleItemPosition = layoutManager
                    .findFirstVisibleItemPosition()
            }
            LayoutManagerType.GRID -> {
                lastVisibleItemPosition = (layoutManager as GridLayoutManager)
                    .findLastVisibleItemPosition()

                firstVisibleItemPosition = layoutManager
                    .findFirstVisibleItemPosition()
            }
            LayoutManagerType.STAGGERED_GRID -> {
                val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager

                if (lastPositions == null) {
                    lastPositions = IntArray(staggeredGridLayoutManager.spanCount)
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions)
                lastVisibleItemPosition = findMax(lastPositions!!)
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        currentScrollState = newState

        val layoutManager    = recyclerView.layoutManager
        val visibleItemCount = layoutManager?.childCount ?:0
        val totalItemCount   = layoutManager?.itemCount ?:0

        setFirstItemCount(firstVisibleItemPosition)
        setItemCount(totalItemCount)

        if (visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1) {
            onBottom()
        }
    }

    private fun setFirstItemCount(count: Int) {
        firstItemCount = count
    }

    private fun setItemCount(count: Int) {
        itemCount = count
    }

    override fun onBottom() {
//        Log.d(TAG, "is onBottom")
    }

    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]

        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }
}