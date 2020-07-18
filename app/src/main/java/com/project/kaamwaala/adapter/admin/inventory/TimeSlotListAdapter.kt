package com.project.kaamwaala.adapter.admin.TimeSlot


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.kaamwaala.R
import com.project.kaamwaala.model.admin_stall_detail.TimeSlotItem
import com.project.kaamwaala.OnItemDeleteTime

import kotlinx.android.synthetic.main.inflate_min_order_value.view.*


class TimeSlotListAdapter(
    private var modelList: List<TimeSlotItem>,
    private var OnItemDeleteTime: OnItemDeleteTime
) :
    RecyclerView.Adapter<TimeSlotViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        return TimeSlotViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.inflate_min_order_value
                ,parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {

        holder.itemView.tvData.setText(modelList.get(position).svalue)
        holder.itemView.ivClose.setOnClickListener { OnItemDeleteTime.onClickTime(modelList.get(position).Code,position) }
    }

    override fun getItemCount(): Int =modelList.size
}

class TimeSlotViewHolder(vieww: View) :
    RecyclerView.ViewHolder(vieww) {
}

