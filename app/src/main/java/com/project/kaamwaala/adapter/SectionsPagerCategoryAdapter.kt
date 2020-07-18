package com.project.kaamwaala.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.project.kaamwaala.model.customer.customer_category.StoreCategoryItem

class SectionsPagerCategoryAdapter(manager: FragmentManager) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: StoreCategoryItem, position: Int) {
        mFragmentList.add(position, fragment)
        mFragmentTitleList.add(position, title.StoreCategoryName)
    }

    fun removeFragment(fragment: Fragment?, position: Int) {
        mFragmentList.removeAt(position)
        mFragmentTitleList.removeAt(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}