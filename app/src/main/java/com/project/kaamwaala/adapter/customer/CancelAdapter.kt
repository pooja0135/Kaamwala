package com.project.kaamwaala.adapter.admin.Category


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R

import com.project.kaamwaala.OnItemSelect
import com.project.kaamwaala.model.customer.order.CancelReason
import kotlinx.android.synthetic.main.inflate_inventory_list.view.*


class CancelAdapter(
    private var modelList: List<CancelReason>,
    var callback: OnItemSelect
) :
    RecyclerView.Adapter<CancelViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancelViewHolder {
        return CancelViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_category
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CancelViewHolder, position: Int) {

          holder.itemView.tvCategory.setText(modelList.get(position).Name)

         holder.itemView.tvCategory.setOnClickListener { callback.onClick(modelList.get(position).Id,modelList.get(position).Name) }
    }

    override fun getItemCount(): Int =modelList.size
}

class CancelViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

