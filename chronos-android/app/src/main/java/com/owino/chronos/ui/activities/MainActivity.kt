package com.owino.chronos.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.owino.chronos.R
import com.owino.chronos.adapters.ViewPagerAdapter

class MainActivity: AppCompatActivity(){
    lateinit var viewPager: ViewPager
    lateinit var adapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        initializeResources()
    }

    fun initializeResources(){
        viewPager = findViewById(R.id.view_pager)
        adapter = ViewPagerAdapter(applicationContext, supportFragmentManager)
        viewPager.adapter = adapter
    }
}