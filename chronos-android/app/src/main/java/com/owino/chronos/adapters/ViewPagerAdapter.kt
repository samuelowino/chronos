package com.owino.chronos.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.owino.chronos.R
import com.owino.chronos.ui.fragments.HomeFragment
import com.owino.chronos.ui.fragments.TrendsFragment

class ViewPagerAdapter(var context: Context, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    val PAGE_SIZE: Int = 2

    override fun getCount(): Int {
        return PAGE_SIZE
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> TrendsFragment()
            else -> HomeFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> context.resources.getString(R.string.general_home)
            1 -> context.resources.getString(R.string.general_trends)
            else -> context.resources.getString(R.string.general_home)
        }
    }

}