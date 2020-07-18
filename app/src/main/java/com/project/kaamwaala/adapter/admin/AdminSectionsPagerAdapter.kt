package com.project.kaamwaala.adapter.admin


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.project.kaamwaala.fragment.admin.adminstall.AdminStallFragment
import com.project.kaamwaala.fragment.admin.adminorder.AdminOrderListFragment

class AdminSectionsPagerAdapter(manager: FragmentManager) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(title: String, position: Int) {
        if (position==0)
        {
            mFragmentList.add(position,
                AdminOrderListFragment()
            )
        }
        else
        {
            mFragmentList.add(position,
                AdminStallFragment()
            )
        }

        mFragmentTitleList.add(position, title)
    }

    fun removeFragment(fragment: Fragment?, position: Int) {
        mFragmentList.removeAt(position)
        mFragmentTitleList.removeAt(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}