package com.project.kaamwaala.fragment.admin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.kaamwaala.R
import com.project.kaamwaala.dialog.admin.CreateStallDialog


class SetUpFragment: Fragment() {
    var list: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View=inflater.inflate(R.layout.fragment_setup, container, false)

        return  view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)






    }


    private fun createstallDialog() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val newFragment = CreateStallDialog()
        newFragment.show(ft, "dialog")
    }

}


