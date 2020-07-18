package com.project.kaamwaala.adapter.admin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R



class AdminStallListAdapter(
    private var modelList: List<String>
) :
    RecyclerView.Adapter<AdminStallViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminStallViewHolder {
        return AdminStallViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_admin_stall_list
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdminStallViewHolder, position: Int) {


    }

    override fun getItemCount(): Int =1
}

class AdminStallViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

