package com.example.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        // 1000 毫秒後跳頁
        Handler(Looper.getMainLooper()).postDelayed({
            var intent = Intent(this, LoginActivity::class.java)

            // 判斷是否登入
            if (Firebase.auth.currentUser != null) {
                intent = Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1000)
    }
}