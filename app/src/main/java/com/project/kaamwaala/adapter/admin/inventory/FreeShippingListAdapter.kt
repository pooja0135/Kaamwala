package com.project.kaamwaala.adapter.admin.FreeShipping


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.admin_stall_detail.FreeShippingAmountItem

import com.project.kaamwaala.OnItemDeleteFreeShipping
import kotlinx.android.synthetic.main.inflate_min_order_value.view.*


class FreeShippingListAdapter(
    private var modelList: List<FreeShippingAmountItem>,
    private var OnItemDeleteFreeShipping: OnItemDeleteFreeShipping
) :
    RecyclerView.Adapter<FreeShippingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeShippingViewHolder {
        return FreeShippingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_min_order_value
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FreeShippingViewHolder, position: Int) {

        holder.itemView.tvData.setText(modelList.get(position).svalue)
        holder.itemView.ivClose.setOnClickListener { OnItemDeleteFreeShipping.onClickFreeShipping(modelList.get(position).Code,position) }
    }

    override fun getItemCount(): Int =modelList.size
}

class FreeShippingViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

