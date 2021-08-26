package com.example.kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlin.R
import com.example.kotlin.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager       : ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        viewPager         = findViewById(R.id.viewpager)
        viewPagerAdapter  = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_baseline_home_24)
                1 -> tab.setIcon(R.drawable.ic_baseline_article_24)
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    // 切換頁面
                    viewPager.currentItem = tab.position

                    // 非主頁隱藏 Action Bar
                    if (tab.position != 0) {
                        supportActionBar?.hide()
                    } else {
                        supportActionBar?.show()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // 載入 menu 資源
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Menu item 同時顯示圖示及文字
    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        if (menu.javaClass.simpleName.equals("MenuBuilder", ignoreCase = true)) {
            try {
                val method: Method = menu.javaClass.getDeclaredMethod(
                    "setOptionalIconsVisible",
                    java.lang.Boolean.TYPE
                )

                method.isAccessible = true
                method.invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onMenuOpened(featureId, menu)
    }

    // 響應 menu 資源中的 id 點擊事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.logout -> logout()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.currentItem != 0) {
            viewPager.currentItem = 0
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun logout(): Boolean {
        Firebase.auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        finish()
        return true
    }
}