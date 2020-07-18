package com.project.kaamwaala.adapter.admin.Shipping


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.admin_stall_detail.ShippingChargesItem


import com.project.kaamwaala.OnItemDeleteShipping

import kotlinx.android.synthetic.main.inflate_min_order_value.view.*


class ShippingListAdapter(
    private var modelList: List<ShippingChargesItem>,
    private var OnItemDeleteShipping: OnItemDeleteShipping
) :
    RecyclerView.Adapter<ShippingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingViewHolder {
        return ShippingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_min_order_value
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShippingViewHolder, position: Int) {

        holder.itemView.tvData.setText(modelList.get(position).svalue)
        holder.itemView.ivClose.setOnClickListener { OnItemDeleteShipping.onClickShipping(modelList.get(position).Code,position) }
    }

    override fun getItemCount(): Int =modelList.size
}

class ShippingViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

