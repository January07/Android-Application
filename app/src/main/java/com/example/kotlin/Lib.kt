package com.example.kotlin

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Lib {

    // 建立 Retrofit 連線
    fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 使用警報對話框
    fun useAlertDialog(context: Context, title: String, msg: String): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("確定", null)
            .show()
    }

    // 隱藏虛擬鍵盤
    fun hideKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}